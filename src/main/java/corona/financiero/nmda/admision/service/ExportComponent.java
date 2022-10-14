package corona.financiero.nmda.admision.service;

import com.itextpdf.html2pdf.HtmlConverter;
import corona.financiero.nmda.admision.adapter.EmailAdapter;
import corona.financiero.nmda.admision.dto.ExportSolicitudAdmisionDTO;
import corona.financiero.nmda.admision.dto.notificacion.EmailAttachmentDTO;
import corona.financiero.nmda.admision.dto.notificacion.EmailParametersDTO;
import corona.financiero.nmda.admision.dto.notificacion.EmailPropertiesDTO;
import corona.financiero.nmda.admision.dto.notificacion.EmailToDTO;
import corona.financiero.nmda.admision.entity.CampanaCoronaEntity;
import corona.financiero.nmda.admision.entity.MotivoCartaRechazoEntity;
import corona.financiero.nmda.admision.entity.ProspectoEntity;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.CampanaCoronaRepository;
import corona.financiero.nmda.admision.repository.MotivoCartaRechazoRepository;
import corona.financiero.nmda.admision.repository.SolicitudAdmisionFiltrosRepositoryImpl;
import corona.financiero.nmda.admision.util.Validaciones;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Base64.getEncoder;

@Component
@Slf4j
public class ExportComponent {

    @Autowired
    private SolicitudAdmisionFiltrosRepositoryImpl solicitudAdmisionFiltrosRepository;

    @Autowired
    private CampanaCoronaRepository campanaCoronaRepository;

    private static final String SHEET = "Solicitudes de Admision";

    private static final String[] HEADERS_SOLICITUD_ADMISION = {"Id Solicitud", "Origen", "Rut Cliente", "Nombre Cliente", " Apellido Paterno", "Apellido Materno", "Edad", "Email", "Telefono Movil", "Canal Atencion", "Estado Solicitud", "Fase Evaluacion", " Regla Negocio", "Fecha Solicitud", "Sucursal", "Zona Geografica Sucursal", "Rut Ejecutivo", "Ejecutivo"};

    private static final String[] HEADERS_CAMPANIA_CORONA = {"Rut", "Nombre", " Apellido Paterno", "Apellido Materno", "Fecha nacimiento", "Cupo asignado", "Producto predeterminado", "Es funcionario"};

    @Autowired
    private MotivoCartaRechazoRepository motivoCartaRechazoRepository;

    @Autowired
    private Validaciones validaciones;

    @Autowired
    private EmailAdapter emailAdapter;

    @Autowired
    private DocumentoService documentoService;

    public ByteArrayInputStream exportarSolicitudAdmision() {

        LocalDate hasta = LocalDate.now();
        LocalDate desde = hasta.minusDays(90);
        List<ExportSolicitudAdmisionDTO> lista = solicitudAdmisionFiltrosRepository.findAllByNativoExportar(desde, hasta);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERS_SOLICITUD_ADMISION.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERS_SOLICITUD_ADMISION[col]);
            }
            AtomicInteger rowIdx = new AtomicInteger(1);


            lista.stream().forEach(l -> {
                Row row = sheet.createRow(rowIdx.getAndIncrement());
                row.createCell(0).setCellValue(l.getSolicitudId());
                row.createCell(1).setCellValue(l.getOrigen());
                row.createCell(2).setCellValue(validaciones.formateaRutHaciaFront(l.getRutCliente()));
                row.createCell(3).setCellValue(l.getNombreCliente());
                row.createCell(4).setCellValue(l.getApellidoPaternoCliente());
                row.createCell(5).setCellValue(l.getApellidoMaternoCliente());
                if (l.getEdad() != null) row.createCell(6).setCellValue(l.getEdad());
                row.createCell(7).setCellValue(l.getEmailCliente());
                if (l.getMovil() != null) row.createCell(8).setCellValue(l.getMovil());
                row.createCell(9).setCellValue(l.getCanalAtencion());
                row.createCell(10).setCellValue(l.getEstadoSolicitud());
                row.createCell(11).setCellValue(l.getFaseEvaluacion());
                row.createCell(12).setCellValue(l.getReglaNegocio());
                LocalDate fechaSolicitud = l.getFechaSolicitud();
                String fechaFormateada = fechaSolicitud.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                row.createCell(13).setCellValue(fechaFormateada);
                row.createCell(14).setCellValue(l.getSucursal());
                row.createCell(15).setCellValue(l.getZonaGeograficaSucursal());
                row.createCell(16).setCellValue(l.getRutEjecutivo());
                row.createCell(17).setCellValue(l.getNombreEjecutivo());
            });

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new BadRequestException("Se produjo un error al generar archivo a exportar");
        }
    }

    public ByteArrayInputStream exportarRegistrosCampaniasCorona(long cabeceraId) {

        if (cabeceraId <= 0) {
            throw new BadRequestException("Id invalido");
        }

        List<CampanaCoronaEntity> campanaCoronaEntityList = campanaCoronaRepository.findAllByCabeceraCampanaEntityCabeceraCampanaId(cabeceraId);

        if (campanaCoronaEntityList == null || campanaCoronaEntityList.isEmpty()) {
            throw new NoContentException();
        }

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERS_CAMPANIA_CORONA.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERS_CAMPANIA_CORONA[col]);
            }
            AtomicInteger rowIdx = new AtomicInteger(1);


            campanaCoronaEntityList.stream().forEach(l -> {
                Row row = sheet.createRow(rowIdx.getAndIncrement());
                row.createCell(0).setCellValue(validaciones.formateaRutHaciaFront(l.getRut()));
                row.createCell(1).setCellValue(l.getNombre());
                row.createCell(2).setCellValue(l.getApellidoPaterno());
                row.createCell(3).setCellValue(l.getApellidoMaterno());
                LocalDate fechaNacimiento = l.getFechaNacimiento();
                String fechaFormateada = fechaNacimiento.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                row.createCell(4).setCellValue(fechaFormateada);
                row.createCell(5).setCellValue(l.getCupoAsignado());
                row.createCell(6).setCellValue(l.getProductoPredeterminado());
                row.createCell(7).setCellValue(l.isEsFuncionario());
            });

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new BadRequestException("Se produjo un error al generar archivo a exportar");
        }
    }

    public ByteArrayInputStream exportarCartaRechazo(String rutProspecto, long solicitudAdmisionId) throws IOException, InvalidFormatException {

        validaciones.validacionGeneralRut(rutProspecto);

        if (solicitudAdmisionId <= 0) {
            throw new BadRequestException("Id solicitud invalida");
        }

        String rutFormateado = validaciones.formateaRutHaciaBD(rutProspecto);

        Optional<MotivoCartaRechazoEntity> motivoCartaRechazoEntityOptional = motivoCartaRechazoRepository.findBySolicitudAdmisionEntitySolicitudIdAndSolicitudAdmisionEntityProspectoEntityRut(solicitudAdmisionId, rutFormateado);

        if (motivoCartaRechazoEntityOptional.isEmpty()) {
            log.debug("No se encontro carta de rechazo asociada");
            throw new BadRequestException("No se encontraron carta de rechazo asociado");
        }

        MotivoCartaRechazoEntity motivoCartaRechazoEntity = motivoCartaRechazoEntityOptional.get();
        ProspectoEntity prospectoEntity = motivoCartaRechazoEntity.getSolicitudAdmisionEntity().getProspectoEntity();


        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatterTexto = DateTimeFormatter.ofPattern("dd 'de' LLLL 'de' yyyy");
        DateTimeFormatter formatterSlash = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formatoTexto = localDate.format(formatterTexto);
        String formatoSlash = localDate.format(formatterSlash);
        String fechaSolicitud = motivoCartaRechazoEntity.getSolicitudAdmisionEntity().getFechaSolicitud().format(formatterSlash);

        String nombreCompleto = "";
        if (prospectoEntity.getNombres() != null && prospectoEntity.getApellidoPaterno() != null) {
            nombreCompleto = prospectoEntity.getNombres() + " " + prospectoEntity.getApellidoPaterno() + " " + prospectoEntity.getApellidoMaterno();
        }

        Map<String, String> map = new HashMap<>();
        map.put("@fecha_actual_texto@", formatoTexto);
        map.put("@fecha_actual_slash@", formatoSlash);
        map.put("@nombre_completo@", nombreCompleto);
        map.put("@rut@", rutProspecto);
        map.put("@fecha_solicitud@", fechaSolicitud);
        map.put("@motivo_rechazo@", motivoCartaRechazoEntity.getDescripcion());
        map.put("@logo@", validaciones.insertaLogo());

        byte[] bytes = generaPDF(map);

        if (bytes.length == 0) {
            throw new BadRequestException("Se produjo un error al generar la carta de rechazo");
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ByteArrayInputStream byteArrayInputStreamMail = new ByteArrayInputStream(bytes);
        notificar(byteArrayInputStreamMail, prospectoEntity.getEmail(), nombreCompleto);

        return byteArrayInputStream;

    }

    private byte[] generaPDF(Map<String, String> map) throws IOException {
        StringBuilder html = new StringBuilder();

        File file = new ClassPathResource("plantilla_carta_rechazo.html").getFile();

        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String linea;
            Set<String> keys = map.keySet();
            while ((linea = bufferedReader.readLine()) != null) {

                for (String k : keys) {
                    if (linea.contains(k)) {
                        linea = linea.replace(k, map.get(k));
                    }
                }
                html.append(linea);
            }


            String result = html.toString();
            log.debug(result);
            InputStream inputStream = IOUtils.toInputStream(result, StandardCharsets.UTF_8);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            HtmlConverter.convertToPdf(inputStream, outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return new byte[0];
    }

    private void notificar(ByteArrayInputStream byteArrayInputStream, String email, String nombreCompleto) {

        EmailPropertiesDTO emailRequestDTO = new EmailPropertiesDTO();
        emailRequestDTO.setTemplateId("carta-rechazo-template");
        emailRequestDTO.setSubject("Carta Rechazo");

        List<EmailToDTO> cc = new ArrayList<>();
        emailRequestDTO.setCc(cc);

        EmailToDTO to = new EmailToDTO();
        to.setEmail(email);

        emailRequestDTO.setTo(Arrays.asList(to));

        EmailParametersDTO paramContenido = new EmailParametersDTO();
        paramContenido.setParam("nombreCompleto");
        paramContenido.setValue(nombreCompleto);
        emailRequestDTO.setParams(Arrays.asList(paramContenido));

        String base64 = getEncoder().encodeToString(byteArrayInputStream.readAllBytes());
        EmailAttachmentDTO emailAttachmentDTO = new EmailAttachmentDTO();
        emailAttachmentDTO.setFileName("carta_rechazo.pdf");
        emailAttachmentDTO.setType(".pdf");
        log.debug("Base64: {}", base64);
        emailAttachmentDTO.setDocument(base64);
        emailRequestDTO.setAttachments(Arrays.asList(emailAttachmentDTO));

        log.debug(emailRequestDTO.toString());

        emailAdapter.notificar(emailRequestDTO);

    }

    public ByteArrayInputStream exportarResumen() throws IOException {
        return documentoService.exportarHojaResumen();
    }

    public ByteArrayInputStream exportarContrato() throws IOException {
        return documentoService.exportarContrato();
    }

    public ByteArrayInputStream exportarAnexo(String plantilla) throws IOException {
        return documentoService.exportarAnexo(plantilla);
    }
}
