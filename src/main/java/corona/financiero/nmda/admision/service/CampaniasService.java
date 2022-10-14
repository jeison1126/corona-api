package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.*;
import corona.financiero.nmda.admision.entity.*;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.*;
import corona.financiero.nmda.admision.util.Constantes;
import corona.financiero.nmda.admision.util.EnumOrigenSolcitudAdmision;
import corona.financiero.nmda.admision.util.Validaciones;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class CampaniasService {

    @Autowired
    private CampanaCoronaRepository campanaCoronaRepository;

    @Autowired
    private PreEvaluadosScoringRepository preEvaluadosScoringRepository;

    @Autowired
    private AdmisionFaseRepository admisionFaseRepository;

    @Autowired
    private ParOrigenRepository parOrigenRepository;

    @Autowired
    private SolicitudAdmisionRepository solicitudAdmisionRepository;

    @Autowired
    private AdmisionFaseService admisionfaseService;

    @Autowired
    private ProspectoRepository prospectoRepository;

    @Autowired
    private EvaluacionProductoService evaluacionProductoService;

    @Autowired
    private CabeceraCampanaRepository cabeceraCampanaRepository;

    private static final String USUARIO_TEMPORAL = "USR_TMP";

    private static final long CUPO_MAXIMO = 500000;

    @Value("${cantidad.elementos.paginacion}")
    private int paginacion;

    @Autowired
    private Validaciones validaciones;

    private static final int POSICION_EXCEL_RUT = 0;
    private static final int POSICION_EXCEL_NOMBRE = 1;
    private static final int POSICION_EXCEL_APELLIDOP = 2;
    private static final int POSICION_EXCEL_APELLIDOM = 3;
    private static final int POSICION_EXCEL_FECHA_NACIMIENTO = 4;
    private static final int POSICION_EXCEL_CUPO_ASIGNADO = 5;
    private static final int POSICION_EXCEL_PRODUCTO_PREDETERMINADO = 6;
    private static final int POSICION_EXCEL_ES_FUNCIONARIO = 7;


    private Optional<CampanaCoronaEntity> verificaCampaniaCoronaActiva(String rut) {
        return campanaCoronaRepository.esCampanaVigente(rut);
    }

    private Optional<PreEvaluadosScoringEntity> verificaCampanaEquifaxActiva(String rut) {
        return preEvaluadosScoringRepository.esCampaniaVigente(rut);
    }

    public CampaniaResponseDTO evaluaCampania(SolicitudAdmisionEntity solicitudAdmisionEntity, String rut) {
        log.debug("Dentro de evaluar campana");
        CampaniaResponseDTO campaniaResponseDTO = new CampaniaResponseDTO();
        Optional<PreEvaluadosScoringEntity> preEvaluadosScoringEntityOptional = verificaCampanaEquifaxActiva(rut);
        if (preEvaluadosScoringEntityOptional.isPresent()) {
            PreEvaluadosScoringEntity preEvaluadosScoringEntity = preEvaluadosScoringEntityOptional.get();
            campaniaResponseDTO.setCampania(true);
            campaniaResponseDTO.setOrigen(EnumOrigenSolcitudAdmision.CAMPANIA_EQUIFAX);
            actualizaFases(solicitudAdmisionEntity, campaniaResponseDTO);
            actualizaDatosProspecto(rut, preEvaluadosScoringEntity.getNombre(), preEvaluadosScoringEntity.getApellidoPaterno(), preEvaluadosScoringEntity.getApellidoMaterno(), preEvaluadosScoringEntity.getFechaNacimiento());

            long cupo = preEvaluadosScoringEntity.getCupoAsignado();
            cupo = (cupo > CUPO_MAXIMO) ? CUPO_MAXIMO: cupo;
            evaluacionProductoService.cargaProductosPreEvaluados(solicitudAdmisionEntity, cupo, null);
            return campaniaResponseDTO;
        }

        Optional<CampanaCoronaEntity> campanaCoronaEntityOptional = verificaCampaniaCoronaActiva(rut);
        if (campanaCoronaEntityOptional.isPresent()) {
            CampanaCoronaEntity campanaCoronaEntity = campanaCoronaEntityOptional.get();
            campaniaResponseDTO.setCampania(true);
            if (campanaCoronaEntity.isEsFuncionario()) {
                campaniaResponseDTO.setOrigen(EnumOrigenSolcitudAdmision.CAMPANIA_CORONA);
            } else {
                campaniaResponseDTO.setOrigen(EnumOrigenSolcitudAdmision.REEVALUADOS);
            }

            actualizaFases(solicitudAdmisionEntity, campaniaResponseDTO);
            actualizaDatosProspecto(rut, campanaCoronaEntity.getNombre(), campanaCoronaEntity.getApellidoPaterno(), campanaCoronaEntity.getApellidoMaterno(), campanaCoronaEntity.getFechaNacimiento());
            long cupo = campanaCoronaEntity.getCupoAsignado();
            cupo = (cupo > CUPO_MAXIMO) ? CUPO_MAXIMO: cupo;
            evaluacionProductoService.cargaProductosPreEvaluados(solicitudAdmisionEntity, cupo, campanaCoronaEntity.getProductoPredeterminado());
            return campaniaResponseDTO;
        }

        campaniaResponseDTO.setCampania(false);

        return campaniaResponseDTO;

    }

    private void actualizaDatosProspecto(String rut, String nombre, String apellidoPaterno, String apellidoMaterno, LocalDate fechaNacimiento) {

        ProspectoEntity prospectoEntity = prospectoRepository.findByRutAndVigenciaIsTrue(rut);
        prospectoEntity.setNombres(nombre);
        prospectoEntity.setApellidoPaterno(apellidoPaterno);
        prospectoEntity.setApellidoMaterno(apellidoMaterno);
        prospectoEntity.setFechaNacimiento(fechaNacimiento);
        prospectoEntity.setFechaModificacion(LocalDateTime.now());
        prospectoEntity.setUsuarioModificacion(USUARIO_TEMPORAL);

        prospectoRepository.save(prospectoEntity);

    }


    private void actualizaFases(SolicitudAdmisionEntity solicitudAdmisionEntity, CampaniaResponseDTO campaniaResponseDTO) {
        admisionfaseService.actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_EVALUACION_INTERNA);
        admisionfaseService.actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_EVALUACION_SCORING);
        admisionfaseService.actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_EVALUACION_COTIZACIONES);

        Optional<ParOrigenEntity> parOrigenEntityOptional = parOrigenRepository.findById(campaniaResponseDTO.getOrigen().getCodigo());
        ParOrigenEntity parOrigenEntity = parOrigenEntityOptional.get();
        solicitudAdmisionEntity.setParOrigenEntity(parOrigenEntity);
        solicitudAdmisionEntity.setFechaModificacion(LocalDateTime.now());
        solicitudAdmisionEntity.setUsuarioModificacion(USUARIO_TEMPORAL);
        solicitudAdmisionRepository.save(solicitudAdmisionEntity);
    }

    public PaginacionCampaniaCoronaResponseDTO listarCampaniaCorona(int numPagina) {

        if (numPagina < 0) {
            throw new BadRequestException("Numero de pagina no permitida");
        }

        Pageable pageable = PageRequest.of(numPagina, paginacion, Sort.by("fechaRegistro"));

        Page<CabeceraCampanaEntity> all = cabeceraCampanaRepository.findAll(pageable);

        Stream<CabeceraCampanaEntity> cabeceraCampanaEntityStream = all.get();
        PaginacionCampaniaCoronaResponseDTO p = new PaginacionCampaniaCoronaResponseDTO();
        p.setTotalPagina(all.getTotalPages());
        p.setPagina(all.getNumber());
        p.setTotalElementos(all.getTotalElements());
        p.setCampanias(
                cabeceraCampanaEntityStream.map(c -> {
                    CampaniaCoronaResponseDTO campaniaCoronaResponseDTO = new CampaniaCoronaResponseDTO();
                    campaniaCoronaResponseDTO.setCabeceraCampaniaId(c.getCabeceraCampanaId());
                    campaniaCoronaResponseDTO.setFechaCarga(c.getFechaRegistro());
                    campaniaCoronaResponseDTO.setFechaInicio(c.getFechaInicio());
                    campaniaCoronaResponseDTO.setFechaTermino(c.getFechaTermino());
                    campaniaCoronaResponseDTO.setNombreArchivo(c.getNombreArchivo());
                    campaniaCoronaResponseDTO.setUsuario(c.getUsuarioRegistro());
                    campaniaCoronaResponseDTO.setCantidadErrores(c.getCantidadErrores());
                    campaniaCoronaResponseDTO.setCantidadRegistros(c.getCantidadRegistros());
                    campaniaCoronaResponseDTO.setRegistrosProcesados(c.getRegistrosProcesados());
                    campaniaCoronaResponseDTO.setErrores(c.getErrores());

                    return campaniaCoronaResponseDTO;
                }).collect(Collectors.toList()));
        return p;

    }

    public void eliminarRegistrosCargaMasiva(long cabeceraCampaniaId) {

        if (cabeceraCampaniaId <= 0) {
            throw new BadRequestException("Id cabecera campania invalido");
        }

        List<CampanaCoronaEntity> list = campanaCoronaRepository.findAllByCabeceraCampanaEntityCabeceraCampanaId(cabeceraCampaniaId);

        if (list == null || list.isEmpty()) {
            throw new NoContentException();
        }

        CabeceraCampanaEntity cabeceraCampanaEntity = list.get(0).getCabeceraCampanaEntity();
        log.debug("cabecera: {}", cabeceraCampanaEntity.toString());

        campanaCoronaRepository.deleteAll(list);
        campanaCoronaRepository.flush();

        cabeceraCampanaRepository.delete(cabeceraCampanaEntity);
        cabeceraCampanaRepository.flush();
    }

    public void cargaCampaniaMasiva(CampaniaCoronaRequestDTO request) throws IOException {

        if (request == null) {
            throw new BadRequestException();
        }
        if (request.getArchivoBase64() == null || request.getArchivoBase64().trim().isEmpty()) {
            throw new BadRequestException("Falta adjuntar archivo excel con la carga masiva");
        }

        if (request.getNombreArchivo() == null || request.getNombreArchivo().trim().isEmpty()) {
            throw new BadRequestException("Falta el nombre del archivo");
        }

        if (request.getExtension() == null || request.getExtension().trim().isEmpty()) {
            throw new BadRequestException("Falta el tipo del archivo");
        }

        if (!request.getExtension().equalsIgnoreCase(".xlsx")) {
            throw new BadRequestException("Archivo invalido");
        }

        if (request.getFechaInicio() == null) {
            throw new BadRequestException("Fecha inicio invalida");
        }

        if (request.getFechaTermino() == null) {
            throw new BadRequestException("Fecha termino invalida");
        }

        if (request.getFechaInicio().isAfter(request.getFechaTermino())) {
            throw new BadRequestException("Rangos de fechas invalidos");
        }

        log.debug("Extension: {}", request.getExtension());
        log.debug("Base 64:");
        log.debug(request.getArchivoBase64());
        List<CargaMasivaDTO> cargaMasivaDTOS = procesarArchivoCargaMasviva(request.getArchivoBase64());
        log.debug("carga masiva: {}", cargaMasivaDTOS.toString());

        long cantidadErrores = cargaMasivaDTOS.stream().filter(c -> c.getErrores() != null && !c.getErrores().trim().isEmpty()).count();

        CabeceraCampanaEntity cabeceraCampanaEntity = new CabeceraCampanaEntity();
        cabeceraCampanaEntity.setFechaInicio(request.getFechaInicio());
        cabeceraCampanaEntity.setFechaTermino(request.getFechaTermino());
        cabeceraCampanaEntity.setNombreArchivo(request.getNombreArchivo());
        cabeceraCampanaEntity.setCantidadErrores((int) cantidadErrores);
        cabeceraCampanaEntity.setCantidadRegistros(cargaMasivaDTOS.size());
        cabeceraCampanaEntity.setRegistrosProcesados((cargaMasivaDTOS.size() - (int) cantidadErrores));

        cabeceraCampanaEntity.setFechaRegistro(LocalDateTime.now());
        cabeceraCampanaEntity.setUsuarioRegistro(USUARIO_TEMPORAL);
        cabeceraCampanaEntity.setVigencia(true);

        CabeceraCampanaEntity save = cabeceraCampanaRepository.save(cabeceraCampanaEntity);
        cabeceraCampanaRepository.flush();

        List<CampanaCoronaEntity> campanaCoronaEntityList = cargaMasivaDTOS.stream().filter(c -> c.getErrores() == null || c.getErrores().isEmpty()).map(c -> {
            CargaMasivaDatosDTO datos = c.getDatos();

            CampanaCoronaEntity campanaCoronaEntity = new CampanaCoronaEntity();
            campanaCoronaEntity.setCabeceraCampanaEntity(save);
            campanaCoronaEntity.setRut(validaciones.formateaRutHaciaBD(datos.getRut()));
            campanaCoronaEntity.setNombre(datos.getNombre());
            campanaCoronaEntity.setApellidoPaterno(datos.getApellidoPaterno());
            campanaCoronaEntity.setApellidoMaterno(datos.getApellidoMaterno());
            campanaCoronaEntity.setFechaNacimiento(datos.getFechaNacimiento());
            campanaCoronaEntity.setCupoAsignado(datos.getCupoAprobado());
            campanaCoronaEntity.setProductoPredeterminado(datos.getProductoPredeterminado());

            campanaCoronaEntity.setEsFuncionario(datos.isEsFuncionario());
            campanaCoronaEntity.setFechaRegistro(LocalDateTime.now());
            campanaCoronaEntity.setUsuarioRegistro(USUARIO_TEMPORAL);
            campanaCoronaEntity.setVigencia(true);

            return campanaCoronaEntity;
        }).collect(Collectors.toList());

        campanaCoronaRepository.saveAll(campanaCoronaEntityList);
        campanaCoronaRepository.flush();

        if (cantidadErrores > 0) {
            StringBuilder errores = new StringBuilder();
            cargaMasivaDTOS.stream().filter(c -> c.getErrores() != null && !c.getErrores().trim().isEmpty()).forEach(c -> errores.append(c.getErrores()));

            save.setErrores(errores.toString());
            cabeceraCampanaRepository.save(save);
            cabeceraCampanaRepository.flush();
        }
    }

    @SuppressWarnings("java:S3776")
    private List<CargaMasivaDTO> procesarArchivoCargaMasviva(String base64) throws IOException {

        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        InputStream targetStream = new ByteArrayInputStream(decodedBytes);
        Workbook workbook = new XSSFWorkbook(targetStream);
        Sheet sheet = workbook.getSheetAt(0);
        List<CargaMasivaDTO> lista = new ArrayList<>();

        int pos = 1;
        Row row = null;

        int linea = 1;
        CargaMasivaDTO cargaMasivaDTO = null;
        CargaMasivaDatosDTO cargaMasivaDatosDTO = null;
        while ((row = sheet.getRow(pos++)) != null) {
            cargaMasivaDatosDTO = new CargaMasivaDatosDTO();

            if (row.getCell(POSICION_EXCEL_RUT) != null)
                cargaMasivaDatosDTO.setRut(row.getCell(POSICION_EXCEL_RUT).getStringCellValue());
            if (row.getCell(POSICION_EXCEL_NOMBRE) != null)
                cargaMasivaDatosDTO.setNombre(row.getCell(POSICION_EXCEL_NOMBRE).getStringCellValue());
            if (row.getCell(POSICION_EXCEL_APELLIDOP) != null)
                cargaMasivaDatosDTO.setApellidoPaterno(row.getCell(POSICION_EXCEL_APELLIDOP).getStringCellValue());
            if (row.getCell(POSICION_EXCEL_APELLIDOM) != null)
                cargaMasivaDatosDTO.setApellidoMaterno(row.getCell(POSICION_EXCEL_APELLIDOM).getStringCellValue());

            LocalDateTime localDateTime = null;

            String fechaTmp = row.getCell(POSICION_EXCEL_FECHA_NACIMIENTO).toString();
            if (fechaTmp != null && !fechaTmp.isEmpty()) {
                localDateTime = row.getCell(POSICION_EXCEL_FECHA_NACIMIENTO).getLocalDateTimeCellValue();
                cargaMasivaDatosDTO.setFechaNacimiento(localDateTime.toLocalDate());

            }

            if (row.getCell(POSICION_EXCEL_CUPO_ASIGNADO) != null)
                cargaMasivaDatosDTO.setCupoAprobado((long) row.getCell(POSICION_EXCEL_CUPO_ASIGNADO).getNumericCellValue());
            if (row.getCell(POSICION_EXCEL_PRODUCTO_PREDETERMINADO) != null)
                cargaMasivaDatosDTO.setProductoPredeterminado(row.getCell(POSICION_EXCEL_PRODUCTO_PREDETERMINADO).getStringCellValue());

            if (row.getCell(POSICION_EXCEL_ES_FUNCIONARIO) != null)
                cargaMasivaDatosDTO.setEsFuncionario(row.getCell(POSICION_EXCEL_ES_FUNCIONARIO).getBooleanCellValue());

            cargaMasivaDTO = new CargaMasivaDTO();
            cargaMasivaDTO.setDatos(cargaMasivaDatosDTO);
            cargaMasivaDTO.setErrores(validaLinea(cargaMasivaDatosDTO, linea));
            cargaMasivaDTO.setLinea(linea);
            lista.add(cargaMasivaDTO);
            linea++;
        }

        return lista;
    }

    @SuppressWarnings("java:S3776")
    private String validaLinea(CargaMasivaDatosDTO cargaMasivaDatosDTO, int linea) {

        StringBuilder retorno = new StringBuilder();
        if (cargaMasivaDatosDTO == null) {
            retorno.append("No hay datos en la linea " + linea + "\n");
            return retorno.toString();
        }

        if (cargaMasivaDatosDTO.getRut() == null || cargaMasivaDatosDTO.getRut().trim().isEmpty()) {
            retorno.append("No se ha registrado rut en la linea " + linea + "\n");
        }

        if (cargaMasivaDatosDTO.getNombre() == null || cargaMasivaDatosDTO.getNombre().trim().isEmpty()) {
            retorno.append("Falta ingresar nombre en la linea " + linea + "\n");
        }

        if (cargaMasivaDatosDTO.getApellidoPaterno() == null || cargaMasivaDatosDTO.getApellidoPaterno().trim().isEmpty()) {
            retorno.append("Falta ingresar el apellido paterno en la linea " + linea + "\n");
        }

        if (cargaMasivaDatosDTO.getApellidoMaterno() == null || cargaMasivaDatosDTO.getApellidoMaterno().trim().isEmpty()) {
            retorno.append("Falta ingresar el apellido materno en la linea " + linea + "\n");
        }

        if (cargaMasivaDatosDTO.getCupoAprobado() == null) {
            retorno.append("Falta ingresar el cupo aprobado en linea " + linea + "\n");
        }

        if (cargaMasivaDatosDTO.getCupoAprobado() != null && cargaMasivaDatosDTO.getCupoAprobado() <= 0) {
            retorno.append("Falta ingresar un cupo aprobado valido en linea " + linea + "\n");
        }

        if (cargaMasivaDatosDTO.getFechaNacimiento() == null) {
            retorno.append("Falta ingresar fecha de nacimiento en linea " + linea + "\n");
        }

        if (cargaMasivaDatosDTO.getProductoPredeterminado() == null || cargaMasivaDatosDTO.getProductoPredeterminado().trim().isEmpty()) {
            retorno.append("Falta indicar un producto predeterminado en linea " + linea + "\n");
        }

        log.debug("Validaciones de linea: {}", retorno.toString());

        return retorno.toString();
    }
}
