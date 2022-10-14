package corona.financiero.nmda.admision.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import corona.financiero.nmda.admision.adapter.EmailAdapter;
import corona.financiero.nmda.admision.adapter.EquifaxAdapter;
import corona.financiero.nmda.admision.dto.dpa.DPAResponseDTO;
import corona.financiero.nmda.admision.dto.equifax.EquifaxRequestDTO;
import corona.financiero.nmda.admision.dto.notificacion.EmailAttachmentDTO;
import corona.financiero.nmda.admision.dto.notificacion.EmailParametersDTO;
import corona.financiero.nmda.admision.dto.notificacion.EmailPropertiesDTO;
import corona.financiero.nmda.admision.dto.notificacion.EmailToDTO;
import corona.financiero.nmda.admision.util.DPAUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class TestRapido {

    @InjectMocks
    private EquifaxAdapter equifaxAdapter;

    @InjectMocks
    private EmailAdapter emailAdapter;

    @InjectMocks
    private DPAUtil DPAUtil;

    @BeforeEach
    public void initEach() {
        //Equifax
        ReflectionTestUtils.setField(equifaxAdapter, "admisionApiKeyName", "Ocp-Apim-Subscription-Key");
        ReflectionTestUtils.setField(equifaxAdapter, "admisionApiKey", "c4550d1ad0404e389749eb64ab0c5928");
        ReflectionTestUtils.setField(equifaxAdapter, "uri", "https://api-dev.financiero.corona.site/nmda/equifax");
        ReflectionTestUtils.setField(equifaxAdapter, "pathInforme", "/informe");
        ReflectionTestUtils.setField(equifaxAdapter, "pathScore", "/score/consumer");
        ReflectionTestUtils.setField(equifaxAdapter, "maxAttempts", 1);
        ReflectionTestUtils.setField(equifaxAdapter, "delay", 2);
        //EMAIL
        ReflectionTestUtils.setField(emailAdapter, "admisionApiKeyName", "Ocp-Apim-Subscription-Key");
        ReflectionTestUtils.setField(emailAdapter, "admisionApiKey", "c4550d1ad0404e389749eb64ab0c5928");
        ReflectionTestUtils.setField(emailAdapter, "maxAttempts", 1);
        ReflectionTestUtils.setField(emailAdapter, "delay", 2);
        ReflectionTestUtils.setField(emailAdapter, "uri", "https://api-dev.financiero.corona.site/nmda/email/send-email");
        //ReflectionTestUtils.setField(emailAdapter, "uri", "http://localhost:8081/email/api/send-email");


    }

    //@Test
    void pruebaInformeEquifax() {

        EquifaxRequestDTO requestDTO = new EquifaxRequestDTO();
        requestDTO.setRut("15902664-7");

        Map<String, Object> stringObjectMap = equifaxAdapter.infoProspectoEquifax(requestDTO);
    }

    //@Test
    void pruebaScoreEquifax() {
        equifaxAdapter.infoScoreEquifax("174573824");
    }

    //@Test
    void pruebaEmail() {
        EmailPropertiesDTO emailPropertiesDTO = new EmailPropertiesDTO();
        EmailToDTO to = new EmailToDTO();
        to.setEmail("nicolas@soho.com.uy");

        List<EmailToDTO> cc = new ArrayList<>();
        emailPropertiesDTO.setCc(cc);

        List<EmailToDTO> tos = new ArrayList<>();
        tos.add(to);

        emailPropertiesDTO.setTo(tos);
        emailPropertiesDTO.setTemplateId("pep-template");
        emailPropertiesDTO.setSubject("Prueba Email");
        EmailParametersDTO param1 = new EmailParametersDTO();
        param1.setParam("nombres");
        param1.setValue("Juan Perez");
        EmailParametersDTO param2 = new EmailParametersDTO();
        param2.setParam("rut");
        param2.setValue("1-9");
        List<EmailParametersDTO> params = new ArrayList<>();
        params.add(param1);
        params.add(param2);

        List<EmailAttachmentDTO> attach = new ArrayList<>();
        emailPropertiesDTO.setAttachments(attach);
        emailPropertiesDTO.setParams(params);

        emailAdapter.notificar(emailPropertiesDTO);
    }

    //@Test
    void calcularEdadTest() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate fechaNac = LocalDate.parse("19-04-1985", fmt);
        LocalDate ahora = LocalDate.now();

        Period periodo = Period.between(fechaNac, ahora);
        System.out.printf("Tu edad es: %s años, %s meses y %s días",
                periodo.getYears(), periodo.getMonths(), periodo.getDays());
    }

    private <T> String dtoToString(T dto) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
        }
        return null;
    }

    //@Test
    void leerArchivoJson() throws IOException {
        DPAUtil.regionesDesdeArchivo();
        String codigoRegion = "01";
        List<DPAResponseDTO> dpaResponseDTOS = DPAUtil.comunasDesdeArchivo(codigoRegion);
    }

    //@Test
    void aumentaFechas() {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime localDateTime = now.plusDays(30);
        System.out.println("Ahora: " + now);

        System.out.println("+ 30 dias: " + localDateTime);

    }

   // @Test
    void leerArchivoXlsx() throws IOException {

        List<String> columnas = Arrays.asList("rut", "nombre", "apellido paterno", "apellido materno", "fecha nacimiento", "cupo asignado", "producto predeterminado", "es funcionario");

        Map<String, Integer> mapNombresColumnas = new HashMap<>();

        final int filaNombresColumnas = 0;
        File archivoExcel = new File("/Users/naranda/Downloads/cargaMasiva.xlsx");
        Workbook workbook = WorkbookFactory.create(archivoExcel);
        Sheet sheet = workbook.getSheetAt(0);

        Row filaNombresColumna = sheet.getRow(filaNombresColumnas);

        filaNombresColumna.cellIterator().forEachRemaining(cell -> {

            String valorCelda = cell.getStringCellValue().trim();
            if (!valorCelda.isEmpty()) {
                mapNombresColumnas.put(valorCelda, cell.getColumnIndex());
            }
        });
        int indiceDatos = filaNombresColumnas + 1;
        Row filaDatos = null;

        while ((filaDatos = sheet.getRow(indiceDatos++)) != null) {

            log.debug("POS 0: {}",filaDatos.getCell(0).getStringCellValue());
            log.debug("POS 1: {}",filaDatos.getCell(1).getStringCellValue());
            log.debug("POS 2: {}",filaDatos.getCell(2).getStringCellValue());
            log.debug("POS 3: {}",filaDatos.getCell(3).getStringCellValue());
            log.debug("POS 4: {}",filaDatos.getCell(4).getLocalDateTimeCellValue());
            if(filaDatos.getCell(4).getLocalDateTimeCellValue() == null){
                LocalDateTime localDateTime = filaDatos.getCell(4).getLocalDateTimeCellValue();
            }

            log.debug("POS 5: {}",filaDatos.getCell(5).getNumericCellValue());
            log.debug("POS 6: {}",filaDatos.getCell(6).getStringCellValue());
            log.debug("POS 7: {}",filaDatos.getCell(7).getBooleanCellValue());


            /*for (String col : columnas) {
                log.debug("Posicion: {}, Nombre columna: {}, valor: {}", mapNombresColumnas.get(col), col, filaDatos.getCell(mapNombresColumnas.get(col)));
            }*/
        }
    }
}

