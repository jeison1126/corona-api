package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.*;
import corona.financiero.nmda.admision.entity.*;
import corona.financiero.nmda.admision.enumeration.DireccionOrdenaEnum;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.*;
import corona.financiero.nmda.admision.util.Validaciones;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SolicitudAdmisionService {

    @Autowired
    private SolicitudAdmisionRepository solicitudAdmisionRepository;
    @Autowired
    private AdmisionFaseRepository admisionFaseRepository;

    @Autowired
    private FuncionariosRepository funcionariosRepository;

    @Autowired
    private SucursalesRepository sucursalesRepository;

    @Autowired
    private AdmisionReglaNegocioRepository admisionReglaNegocioRepository;

    @Autowired
    private Validaciones validaciones;

    @Autowired
    private SolicitudAdmisionFiltrosRepositoryImpl solicitudAdmisionFiltrosRepository;

    @Value("${cantidad.elementos.paginacion}")
    private int paginacion;

    private static final int RANGO_DIAS = 90;

    private static final int RANGO_MESES = 3;

    private static final int COLUMNA_FECHA_REGISTRO = 9;

    private static final int COLUMNA_INICIAL = 1;

    public SolicitudAdmisionResponseDTO obtenerEstadoSolicitudAdmision(long prospectoId) {

        if (prospectoId <= 0) {
            log.debug("Prospecto invalido");
            throw new BadRequestException("Prospecto invalido");
        }

        SolicitudAdmisionEntity solicitudAdmisionEntity = solicitudAdmisionRepository.findByProspectoEntityProspectoIdAndVigenciaIsTrue(prospectoId);

        if (solicitudAdmisionEntity == null) {
            log.debug("No se encontro solicitud de admision");
            throw new NoContentException();
        }

        List<AdmisionFaseEntity> fasesAdmision = admisionFaseRepository.findAllBySolicitudAdmisionEntityOrderByFechaRegistroAsc(solicitudAdmisionEntity);

        if (fasesAdmision.isEmpty()) {
            log.debug("No se encontraron fases de la solicitud de admision");
            throw new NoContentException();
        }


        SolicitudAdmisionResponseDTO solicitudAdmisionResponseDTO = new SolicitudAdmisionResponseDTO();
        solicitudAdmisionResponseDTO.setSolicitudId(solicitudAdmisionEntity.getSolicitudId());
        solicitudAdmisionResponseDTO.setEstado(solicitudAdmisionEntity.getParEstadoSolicitudEntity().getDescripcion());
        solicitudAdmisionResponseDTO.setFechaRegistroSolicitud(solicitudAdmisionEntity.getFechaRegistro());

        solicitudAdmisionResponseDTO.setFases(fasesAdmision.stream().map(f -> {
            AdmisionFaseResponseDTO admisionFaseResponseDTO = new AdmisionFaseResponseDTO();
            admisionFaseResponseDTO.setAdmisionFaseId(f.getAdmisionFaseId());
            admisionFaseResponseDTO.setDescripcionFase(f.getParFaseEntity().getDescripcion());
            admisionFaseResponseDTO.setFechaRegistroFase(f.getFechaRegistro());
            admisionFaseResponseDTO.setFaseId(f.getParFaseEntity().getFaseId());
            List<AdmisionReglaNegocioEntity> reglaNegocioEntities = admisionReglaNegocioRepository.findAllBySolicitudAdmisionEntityAndReglaNegocioEntityParFaseEntity(f.getSolicitudAdmisionEntity(), f.getParFaseEntity());
            reglaNegocioEntities.stream().forEach(r -> {
                ParMensajeRechazoEntity parMensajeRechazoEntity = r.getReglaNegocioEntity().getParMensajeRechazoEntity();
                admisionFaseResponseDTO.setError(true);
                admisionFaseResponseDTO.setMensajeEjecutivo(parMensajeRechazoEntity.getMensajeFuncional());
                admisionFaseResponseDTO.setMensajeInterno(parMensajeRechazoEntity.getDescripcion());
            });


            return admisionFaseResponseDTO;
        }).collect(Collectors.toList()));

        return solicitudAdmisionResponseDTO;
    }

    public DetalleSolicitudAdmisionResponseDTO detalleSolicitudAdmision(long solicitudAdmisionId, String rutProspecto) {

        log.debug("Obtener detalle solicitud para solicitud admision {} y prospecto {} ", solicitudAdmisionId, rutProspecto);

        if (solicitudAdmisionId <= 0) {
            throw new BadRequestException("Debe ingresar un id valido");
        }

        if (rutProspecto == null || rutProspecto.trim().isEmpty()) {
            throw new BadRequestException("Debe ingresar un rut");
        }
        if (!validaciones.validaRut(rutProspecto)) {
            throw new BadRequestException("El rut ingresado no es valido");
        }

        String rutFormateado = validaciones.formateaRutHaciaBD(rutProspecto);

        Optional<SolicitudAdmisionEntity> solicitudAdmisionEntityOptional = solicitudAdmisionRepository.detalleSolicitudAdmision(solicitudAdmisionId, rutFormateado);

        if (solicitudAdmisionEntityOptional.isEmpty()) {
            log.debug("No existe solicitud admision para el id {} y rut {}", solicitudAdmisionId, rutProspecto);
            throw new NoContentException();
        }
        SolicitudAdmisionEntity solicitudAdmisionEntity = solicitudAdmisionEntityOptional.get();

        DetalleSolicitudAdmisionResponseDTO detalleSolicitudAdmisionResponseDTO = new DetalleSolicitudAdmisionResponseDTO();

        detalleSolicitudAdmisionResponseDTO.setEstadoSolicitud(solicitudAdmisionEntity.getParEstadoSolicitudEntity().getDescripcion());
        detalleSolicitudAdmisionResponseDTO.setSolicitudId(solicitudAdmisionEntity.getSolicitudId());
        detalleSolicitudAdmisionResponseDTO.setFechaSolicitud(solicitudAdmisionEntity.getFechaSolicitud());

        ProspectoEntity prospectoEntity = solicitudAdmisionEntity.getProspectoEntity();

        detalleSolicitudAdmisionResponseDTO.setEmail(prospectoEntity.getEmail());
        detalleSolicitudAdmisionResponseDTO.setRut(validaciones.formateaRutHaciaFront(prospectoEntity.getRut()));
        detalleSolicitudAdmisionResponseDTO.setApellidoMaterno(prospectoEntity.getApellidoMaterno());
        detalleSolicitudAdmisionResponseDTO.setApellidoPaterno(prospectoEntity.getApellidoPaterno());
        detalleSolicitudAdmisionResponseDTO.setNombres(prospectoEntity.getNombres());
        if (prospectoEntity.getFechaNacimiento() != null) {
            detalleSolicitudAdmisionResponseDTO.setEdad(validaciones.calcularEdad(prospectoEntity.getFechaNacimiento()));
        }

        detalleSolicitudAdmisionResponseDTO.setCanal(solicitudAdmisionEntity.getParCanalEntity().getDescripcion());
        detalleSolicitudAdmisionResponseDTO.setMovil(prospectoEntity.getMovil());


        Optional<FuncionariosEntity> funcionariosEntityOptional = funcionariosRepository.findByNombreUsuario(solicitudAdmisionEntity.getUsuarioRegistro());

        if (funcionariosEntityOptional.isPresent()) {
            FuncionarioDTO funcionarioDTO = new FuncionarioDTO();
            FuncionariosEntity funcionariosEntity = funcionariosEntityOptional.get();
            funcionarioDTO.setNombres(funcionariosEntity.getNombreCompleto());
            funcionarioDTO.setRut(funcionariosEntity.getRut());
            detalleSolicitudAdmisionResponseDTO.setEjecutivo(funcionarioDTO);
        }

        Optional<SucursalesEntity> sucursalesEntityOptional = sucursalesRepository.findById(solicitudAdmisionEntity.getSucursalId());

        if (sucursalesEntityOptional.isPresent()) {
            SucursalesEntity sucursalesEntity = sucursalesEntityOptional.get();
            SucursalDTO sucursalDTO = new SucursalDTO();
            sucursalDTO.setCodigoSucursal(sucursalesEntity.getCodigoSucursal());
            sucursalDTO.setNombre(sucursalesEntity.getDescripcionSucursal());
            sucursalDTO.setZonaGeografica(sucursalesEntity.getDescripcionZonaGeografica());
            detalleSolicitudAdmisionResponseDTO.setSucursal(sucursalDTO);
        }

        //se busca si existe detalle evaluacion comercial global (en caso de que solicitud haya aplicado alguna regla de negocio)
        List<DetalleEvaluacionComercialResponsDTO> detalleEvaluacionComercialResponsDTOS = evaluacionComercialSolicitudAdmision(solicitudAdmisionEntity);
        detalleSolicitudAdmisionResponseDTO.setEvaluacionComercial(detalleEvaluacionComercialResponsDTOS);

        return detalleSolicitudAdmisionResponseDTO;
    }

    private List<DetalleEvaluacionComercialResponsDTO> evaluacionComercialSolicitudAdmision(SolicitudAdmisionEntity solicitudAdmisionEntity) {

        List<AdmisionFaseEntity> admisionFaseEntities = admisionFaseRepository.findAllBySolicitudAdmisionEntityOrderByFechaRegistroAsc(solicitudAdmisionEntity);

        return admisionFaseEntities.stream().map(a -> {
            DetalleEvaluacionComercialResponsDTO detalleEvaluacionComercialResponsDTO = new DetalleEvaluacionComercialResponsDTO();
            ParFaseEntity parFaseEntity = a.getParFaseEntity();
            detalleEvaluacionComercialResponsDTO.setFaseId(parFaseEntity.getFaseId());
            detalleEvaluacionComercialResponsDTO.setFase(parFaseEntity.getDescripcion());

            List<AdmisionReglaNegocioEntity> admisionReglaNegocioEntityList = admisionReglaNegocioRepository.findAllBySolicitudAdmisionEntityAndReglaNegocioEntityParFaseEntity(solicitudAdmisionEntity, parFaseEntity);

            detalleEvaluacionComercialResponsDTO.setReglaNegocio(admisionReglaNegocioEntityList.stream().map(r -> r.getReglaNegocioEntity().getDescripcion()

            ).collect(Collectors.toList()));

            return detalleEvaluacionComercialResponsDTO;
        }).collect(Collectors.toList());
    }

    private void validaFechas(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            log.error("Revise que haya ingresado los rangos de fecha");
            throw new BadRequestException("Revise que haya ingresado los rangos de fecha");
        }

        LocalDate fechaActual = LocalDate.now();

        if (desde.isAfter(fechaActual)) {
            log.error("Fecha desde no puede ser mayor a fecha actual");
            throw new BadRequestException("Fecha desde no puede ser mayor a fecha actual");
        }

        if (hasta.isAfter(fechaActual)) {
            log.error("Fecha hasta no puede ser mayor a fecha actual");
            throw new BadRequestException("Fecha hasta no puede ser mayor a fecha actual");
        }

        if (desde.isAfter(hasta)) {
            log.error("Fecha desde no puede ser mayor a fecha hasta");
            throw new BadRequestException("Fecha desde no puede ser mayor a fecha hasta");
        }

        Period period = Period.between(desde, hasta);

        if (period.getMonths() >= RANGO_MESES && period.getDays() > 0) {
            log.error("Rangos de fechas no pueden ser superior a 90 dias");
            throw new BadRequestException("Rangos de fechas no pueden ser superior a 90 dias");
        }

    }


    public PaginacionSolicitudAdmisionDTO listaSolicitudAdmision(FiltroSolicitudAdmisionDTO filtros) {
        if (filtros.getPagina() < 0) {
            throw new BadRequestException("Numero de pagina no permitida");
        }

        String rutFormateado = null;
        if (filtros.getRut() != null) {
            if (!validaciones.validaRut(filtros.getRut())) {
                throw new BadRequestException("El rut ingresado no es valido");
            }
            rutFormateado = validaciones.formateaRutHaciaBD(filtros.getRut());
        }

        LocalDate desde = null;
        LocalDate hasta = null;

        if (filtros.getFechaDesde() == null && filtros.getFechaHasta() == null) {
            LocalDate fechaActual = LocalDate.now();
            hasta = fechaActual;
            desde = hasta.minusDays(RANGO_DIAS - 1l);
        }

        if (filtros.getFechaDesde() != null) {
            desde = filtros.getFechaDesde().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        if (filtros.getFechaHasta() != null) {
            hasta = filtros.getFechaHasta().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        int columndaOrden = COLUMNA_FECHA_REGISTRO;
        if (filtros.getColumnaOrden() >= COLUMNA_INICIAL && filtros.getColumnaOrden() < COLUMNA_FECHA_REGISTRO) {
            columndaOrden = filtros.getColumnaOrden();
        }

        String direccionOrden = DireccionOrdenaEnum.DESC.name();
        if (filtros.getOrden() != null) {
            direccionOrden = filtros.getOrden().name();
        }

        validaFechas(desde, hasta);

        Pageable pageable = PageRequest.of(filtros.getPagina(), paginacion);

        Page<ListaSolicitudAdmisionDTO> all = solicitudAdmisionFiltrosRepository.findAllByNativo(desde, hasta, rutFormateado, filtros.getCanalAtencion(), filtros.getSucursal(), filtros.getEstadoEvaluacion(), filtros.getFaseEvaluacion(), filtros.getZonaGeografica(), columndaOrden, direccionOrden, pageable);

        PaginacionSolicitudAdmisionDTO paginacionSolicitudAdmisionDTO = new PaginacionSolicitudAdmisionDTO();
        paginacionSolicitudAdmisionDTO.setPagina(all.getNumber());
        paginacionSolicitudAdmisionDTO.setTotalElementos(all.getTotalElements());
        paginacionSolicitudAdmisionDTO.setTotalPagina(all.getTotalPages());


        paginacionSolicitudAdmisionDTO.setSolicitudes(all.toList());

        return paginacionSolicitudAdmisionDTO;
    }
}
