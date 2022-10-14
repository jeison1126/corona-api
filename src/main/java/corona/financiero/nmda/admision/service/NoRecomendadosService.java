package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.NoRecomendadosDTO;
import corona.financiero.nmda.admision.dto.NoRecomendadosRequestDTO;
import corona.financiero.nmda.admision.dto.PaginacionNoRecomendadosDTO;
import corona.financiero.nmda.admision.dto.SucursalDTO;
import corona.financiero.nmda.admision.entity.NoRecomendadosEntity;
import corona.financiero.nmda.admision.entity.SucursalesEntity;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.ex.NoRecomendadosNotFoundException;
import corona.financiero.nmda.admision.repository.NoRecomendadosRepository;
import corona.financiero.nmda.admision.repository.SucursalesRepository;
import corona.financiero.nmda.admision.util.Validaciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoRecomendadosService {

    @Autowired
    private NoRecomendadosRepository noRecomendadosRepository;

    @Autowired
    private SucursalesRepository sucursalesRepository;

    @Value("${cantidad.elementos.paginacion}")
    private int paginacion;

    @Autowired
    private Validaciones validaciones;

    private static final String USUARIO_TEMPORAL = "USR_TMP";

    public PaginacionNoRecomendadosDTO listarNoRecomendados(int numPagina, String filtro) {

        if (numPagina < 0) {
            throw new BadRequestException("Numero de pagina no permitida");
        }

        Pageable pageable = PageRequest.of(numPagina, paginacion, Sort.by("fechaRegistro"));

        Page<NoRecomendadosEntity> all = null;

        if (filtro == null || filtro.trim().isEmpty()) {
            all = noRecomendadosRepository.findAll(pageable);
        } else {
            filtro = filtro.toLowerCase(Locale.ROOT);
            all = noRecomendadosRepository.listarNoRecomendados(filtro, pageable);
        }

        List<SucursalesEntity> sucursales = sucursalesRepository.findAllByVigenciaIsTrueOrderByDescripcionSucursalAsc();


        PaginacionNoRecomendadosDTO paginacionNoRecomendadosDTO = new PaginacionNoRecomendadosDTO();
        paginacionNoRecomendadosDTO.setTotalElementos(all.getTotalElements());
        paginacionNoRecomendadosDTO.setTotalPagina(all.getTotalPages());
        paginacionNoRecomendadosDTO.setPagina(all.getNumber());
        paginacionNoRecomendadosDTO.setNoRecomendados(all.stream().map(n -> {
            NoRecomendadosDTO noRecomendadosDTO = new NoRecomendadosDTO();
            noRecomendadosDTO.setRut(validaciones.formateaRutHaciaFront(n.getRut()));
            noRecomendadosDTO.setNombre(n.getNombre());
            if (n.getSucursalOrigen() != null) {
                SucursalDTO sucursalDTO = new SucursalDTO();
                sucursales.stream().filter(s -> s.getCodigoSucursal() == n.getSucursalOrigen()).forEach(s -> {
                    sucursalDTO.setNombre(s.getDescripcionSucursal());
                    sucursalDTO.setCodigoSucursal(s.getCodigoSucursal());

                });
                noRecomendadosDTO.setSucursalOrigen(sucursalDTO);
            }

            noRecomendadosDTO.setMotivo(n.getMotivoNoRecomendado());
            noRecomendadosDTO.setApellidoPaterno(n.getApellidoPaterno());
            noRecomendadosDTO.setApellidoMaterno(n.getApellidoMaterno());
            return noRecomendadosDTO;
        }).collect(Collectors.toList()));

        return paginacionNoRecomendadosDTO;
    }


    public void registrarNoRecomendado(NoRecomendadosRequestDTO request) {

        validarDatosNoRecomendados(request);
        String rutFormateado = validaciones.formateaRutHaciaBD(request.getRut());

        Optional<NoRecomendadosEntity> byId = noRecomendadosRepository.findById(rutFormateado);
        if (byId.isPresent()) {
            throw new BadRequestException("El rut " + request.getRut() + " ya se encuentra registrado como no recomendado");
        }


        NoRecomendadosEntity noRecomendadosEntity = new NoRecomendadosEntity();
        noRecomendadosEntity.setFechaRegistro(LocalDateTime.now());
        noRecomendadosEntity.setUsuarioRegistro(USUARIO_TEMPORAL);
        noRecomendadosEntity.setRut(rutFormateado);
        noRecomendadosEntity.setNombre(request.getNombre());
        noRecomendadosEntity.setApellidoPaterno(request.getApellidoPaterno());
        noRecomendadosEntity.setApellidoMaterno(request.getApellidoMaterno());
        noRecomendadosEntity.setSucursalOrigen(request.getSucursalOrigen());
        noRecomendadosEntity.setMotivoNoRecomendado(request.getMotivo());

        noRecomendadosRepository.save(noRecomendadosEntity);
    }

    private void validarDatosNoRecomendados(NoRecomendadosRequestDTO request) {
        if (request == null) {
            throw new BadRequestException();
        }

        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            throw new BadRequestException("Falta ingresar nombre");
        }
        if (request.getApellidoPaterno() == null || request.getApellidoPaterno().trim().isEmpty())
            throw new BadRequestException("Falta ingresar apellido paterno");

        validarRut(request.getRut());
    }

    private void validarRut(String rutCliente) {
        if (rutCliente == null || rutCliente.trim().isEmpty()) {
            throw new BadRequestException("Debe ingresar un rut");
        }
        if (!validaciones.validaRut(rutCliente)) {
            throw new BadRequestException("El rut ingresado no es valido");
        }
    }

    public void eliminarNoRecomendado(String rut) {
        validarRut(rut);
        String rutFormateado = validaciones.formateaRutHaciaBD(rut);
        NoRecomendadosEntity noRecomendadosEntity = noRecomendadosRepository.findById(rutFormateado).orElseThrow(NoRecomendadosNotFoundException::new);
        noRecomendadosRepository.delete(noRecomendadosEntity);
    }

    public NoRecomendadosDTO obtenerNoRecomendable(String rut) {

        validarRut(rut);
        String rutFormateado = validaciones.formateaRutHaciaBD(rut);
        NoRecomendadosEntity noRecomendadosEntity = noRecomendadosRepository.findById(rutFormateado).orElseThrow(NoContentException::new);

        NoRecomendadosDTO noRecomendadosDTO = new NoRecomendadosDTO();
        noRecomendadosDTO.setRut(validaciones.formateaRutHaciaFront(noRecomendadosEntity.getRut()));
        noRecomendadosDTO.setNombre(noRecomendadosEntity.getNombre());

        Optional<SucursalesEntity> sucursalesEntityOptional = sucursalesRepository.findById(noRecomendadosEntity.getSucursalOrigen());

        if (sucursalesEntityOptional.isPresent()) {
            SucursalesEntity sucursalesEntity = sucursalesEntityOptional.get();
            SucursalDTO sucursalDTO = new SucursalDTO();
            sucursalDTO.setCodigoSucursal(sucursalesEntity.getCodigoSucursal());
            sucursalDTO.setNombre(sucursalesEntity.getDescripcionSucursal());
            noRecomendadosDTO.setSucursalOrigen(sucursalDTO);
        }


        noRecomendadosDTO.setMotivo(noRecomendadosEntity.getMotivoNoRecomendado());
        noRecomendadosDTO.setApellidoPaterno(noRecomendadosEntity.getApellidoPaterno());
        noRecomendadosDTO.setApellidoMaterno(noRecomendadosEntity.getApellidoMaterno());
        return noRecomendadosDTO;
    }

    public void actualizarNoRecomendado(NoRecomendadosRequestDTO request) {

        validarDatosNoRecomendados(request);

        String rutFormateado = validaciones.formateaRutHaciaBD(request.getRut());

        NoRecomendadosEntity noRecomendadosEntity = noRecomendadosRepository.findById(rutFormateado).orElseThrow(NoContentException::new);

        if (request.getMotivo() != null) {
            noRecomendadosEntity.setMotivoNoRecomendado(request.getMotivo());
        }


        if (request.getSucursalOrigen() != null) {
            noRecomendadosEntity.setSucursalOrigen(request.getSucursalOrigen());
        }

        if (request.getApellidoMaterno() != null) {
            noRecomendadosEntity.setApellidoMaterno(request.getApellidoMaterno());
        }

        noRecomendadosEntity.setNombre(request.getNombre());
        noRecomendadosEntity.setApellidoPaterno(request.getApellidoPaterno());
        noRecomendadosEntity.setFechaModificacion(LocalDateTime.now());
        noRecomendadosEntity.setUsuarioModificacion(USUARIO_TEMPORAL);

        noRecomendadosRepository.save(noRecomendadosEntity);
        noRecomendadosRepository.flush();
    }
}
