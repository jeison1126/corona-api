package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.adapter.EcertAdapter;
import corona.financiero.nmda.admision.dto.*;
import corona.financiero.nmda.admision.dto.ecert.EcertPreinscripcionRequestDTO;
import corona.financiero.nmda.admision.dto.ecert.EcertPreinscripcionResponseDTO;
import corona.financiero.nmda.admision.dto.ecert.EcertSubirDocumentoRequestDTO;
import corona.financiero.nmda.admision.dto.ecert.WebHookRequestDTO;
import corona.financiero.nmda.admision.entity.*;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.repository.*;
import corona.financiero.nmda.admision.util.Constantes;
import corona.financiero.nmda.admision.util.Validaciones;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FirmaDocumentoService {

    @Autowired
    private EcertAdapter ecertAdapter;

    @Autowired
    private Validaciones validaciones;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AdmisionFaseRepository admisionFaseRepository;

    @Autowired
    private DocumentoService documentoService;

    @Value("${ecert.url.webhook.contrato}")
    private String webhookContrato;

    @Value("${ecert.url.webhook.seguros}")
    private String webhookSeguros;

    @Value("${ecert.rut.empresa}")
    private String rutEmpresa;

    @Value("${ecert.url.callback}")
    private String callback;

    @Value("${ecert.tipo.firma}")
    private int tipoFirma;

    @Value("${ecert.producto.combinado.id}")
    private String productoCombinado;

    @Autowired
    private ParTipoDocumentoRepository parTipoDocumentoRepository;

    @Autowired
    private ParTasasDocumentoRepository parTasasDocumentoRepository;

    @Autowired
    private FirmaDocumentoRepository firmaDocumentoRepository;

    private static final String USR_TMP = "USR_TMP";

    @Autowired
    private SolicitudAdmisionRepository solicitudAdmisionRepository;

    @Autowired
    private AdmisionFaseService admisionFaseService;

    private void validacionesRequest(FirmaDocumentoRequestDTO request) {
        if (request == null) {
            throw new BadRequestException();
        }

        validaciones.validacionGeneralRut(request.getRut());

        if (request.getSolicitudId() < 1) {
            throw new BadRequestException("Id solicitud no valido");
        }
    }

    public FirmaDocumentoResponseDTO firmaContrato(FirmaContratoRequestDTO request) {

        validacionesRequest(request);

        String rutFormateado = validaciones.formateaRutHaciaBD(request.getRut());

        Optional<ClienteEntity> clienteEntityOptional = clienteRepository.validarClienteProspecto(rutFormateado);

        if (clienteEntityOptional.isEmpty()) {
            throw new BadRequestException("No existe cliente");
        }

        Optional<AdmisionFaseEntity> admisionFaseEntityOptional = admisionFaseRepository.validaSolicitudAdmisionYFaseActiva(rutFormateado, request.getSolicitudId(), Constantes.FASE_DATOS_CLIENTE);

        if (admisionFaseEntityOptional.isEmpty()) {
            throw new BadRequestException("No existe solicitud admision");
        }

        ClienteEntity clienteEntity = clienteEntityOptional.get();

        EcertPreinscripcionRequestDTO ecertPreinscripcionRequestDTO = new EcertPreinscripcionRequestDTO();
        ecertPreinscripcionRequestDTO.setNombre(clienteEntity.getNombre());
        ecertPreinscripcionRequestDTO.setRutUsuario(request.getRut());
        ecertPreinscripcionRequestDTO.setEmail(clienteEntity.getEmail());
        ecertPreinscripcionRequestDTO.setCorreoEndioDocumentoFirmado(clienteEntity.getEmail());
        ecertPreinscripcionRequestDTO.setApellidoMaterno(clienteEntity.getApellidoMaterno());
        ecertPreinscripcionRequestDTO.setApellidoPaterno(clienteEntity.getApellidoPaterno());
        ecertPreinscripcionRequestDTO.setCantidadDocumentos(2);
        ecertPreinscripcionRequestDTO.setUrlWebhook(webhookContrato);
        ecertPreinscripcionRequestDTO.setTipoFirma(tipoFirma);
        ecertPreinscripcionRequestDTO.setRutEmpresa(rutEmpresa);
        ecertPreinscripcionRequestDTO.setProductoCombinado(productoCombinado);
        ecertPreinscripcionRequestDTO.setUrlCallback(callback);

        EcertPreinscripcionResponseDTO preinscripcion = ecertAdapter.preinscripcion(ecertPreinscripcionRequestDTO);

        if (preinscripcion == null) {
            throw new BadRequestException("Se produjo un problema al solicitar firmar documentosRequest... ver respuesta final para activar excepcion");
        }

        log.debug("Preparando documentosRequest a firmar...");
        String contratoB64 = null;
        String hojaResumenB64 = null;
        try {
            contratoB64 = documentoService.contratoBase64();
            hojaResumenB64 = documentoService.hojaResumenBase64();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException("Se produjo un error al generar los documentos para firmar");
        }

        EcertSubirDocumentoRequestDTO contratoRequest = new EcertSubirDocumentoRequestDTO();
        contratoRequest.setRequiereCustodia(true);
        contratoRequest.setDocumentoBase64(contratoB64);
        contratoRequest.setNombreDocumento(request.getRut() + "_contrato.pdf");
        contratoRequest.setRutUsuario(request.getRut());
        contratoRequest.setIdUsuarioEcert(preinscripcion.getIdUsuarioEcert());

        TipoDocumentoParaFirmaDTO tipoDocumentoContrato = new TipoDocumentoParaFirmaDTO();
        tipoDocumentoContrato.setTipoDocumento(Constantes.TIPO_DOCUMENTO_CONTRATO);
        tipoDocumentoContrato.setEcertSubirDocumentoRequestDTO(contratoRequest);


        EcertSubirDocumentoRequestDTO hojaResumenRequest = new EcertSubirDocumentoRequestDTO();
        hojaResumenRequest.setRequiereCustodia(true);
        hojaResumenRequest.setDocumentoBase64(hojaResumenB64);
        hojaResumenRequest.setNombreDocumento(request.getRut() + "_hoja_resumen.pdf");
        hojaResumenRequest.setRutUsuario(request.getRut());
        hojaResumenRequest.setIdUsuarioEcert(preinscripcion.getIdUsuarioEcert());


        TipoDocumentoParaFirmaDTO tipoDocumentoResumen = new TipoDocumentoParaFirmaDTO();
        tipoDocumentoResumen.setTipoDocumento(Constantes.TIPO_DOCUMENTO_RESUMEN);
        tipoDocumentoResumen.setEcertSubirDocumentoRequestDTO(hojaResumenRequest);


        List<TipoDocumentoParaFirmaDTO> documentosRequest = Arrays.asList(tipoDocumentoContrato, tipoDocumentoResumen);

        List<TipoDocumentoParaFirmaDTO> resultadoDocumentos = documentosRequest.parallelStream().map(d -> {
            d.setEcertSubirDocumentoResponseDTO(ecertAdapter.subirDocumento(d.getEcertSubirDocumentoRequestDTO()));
            return d;
        }).collect(Collectors.toList());


        if (resultadoDocumentos == null || resultadoDocumentos.get(0) == null || resultadoDocumentos.get(1) == null) {
            throw new BadRequestException("Se produjo un error al subir los documentos para firmar");
        }

        SolicitudAdmisionEntity solicitudAdmisionEntity = admisionFaseEntityOptional.get().getSolicitudAdmisionEntity();

        Optional<ParTasasDocumentoEntity> tasasDocumentoEntityOptional = parTasasDocumentoRepository.findTopByVigenciaIsTrueOrderByFechaRegistroAsc();

        if (tasasDocumentoEntityOptional.isEmpty()) {
            log.error("No existen tasas de interes cargadas");
            throw new BadRequestException("No existen tasas de interes cargadas");
        }

        ParTasasDocumentoEntity parTasasDocumentoEntity = tasasDocumentoEntityOptional.get();


        List<Integer> documentosEcertId = new ArrayList<>();
        List<FirmaDocumentoEntity> firmaDocumentoEntities = resultadoDocumentos.stream().map(r -> {
            FirmaDocumentoEntity firmaDocumentoEntity = new FirmaDocumentoEntity();
            firmaDocumentoEntity.setSolicitudAdmisionEntity(solicitudAdmisionEntity);
            firmaDocumentoEntity.setClienteEntity(clienteEntity);
            Optional<ParTipoDocumentoEntity> tipoDocumentoEntityOptional = parTipoDocumentoRepository.findById(r.getTipoDocumento());
            if (tipoDocumentoEntityOptional.isEmpty()) {
                throw new BadRequestException("No existe el tipo de documento cargado");
            }
            firmaDocumentoEntity.setParTipoDocumentoEntity(tipoDocumentoEntityOptional.get());
            firmaDocumentoEntity.setParTasasDocumentoEntity(parTasasDocumentoEntity);
            firmaDocumentoEntity.setUsuarioEcertTmpId(preinscripcion.getIdUsuarioEcert());
            firmaDocumentoEntity.setUrlLoginEcertTmp(preinscripcion.getUrlLoginEcert());
            firmaDocumentoEntity.setFirmado(false);
            firmaDocumentoEntity.setDocumentoEcertId(r.getEcertSubirDocumentoResponseDTO().getDocumentoId());
            firmaDocumentoEntity.setFechaRegistro(LocalDateTime.now());
            firmaDocumentoEntity.setUsuarioRegistro(USR_TMP);
            firmaDocumentoEntity.setNombreDocumento(r.getEcertSubirDocumentoRequestDTO().getNombreDocumento());
            firmaDocumentoEntity.setDocumento(r.getEcertSubirDocumentoRequestDTO().getDocumentoBase64());
            firmaDocumentoEntity.setVigencia(true);

            documentosEcertId.add(r.getEcertSubirDocumentoResponseDTO().getDocumentoId());

            return firmaDocumentoEntity;
        }).collect(Collectors.toList());


        firmaDocumentoRepository.saveAll(firmaDocumentoEntities);
        firmaDocumentoRepository.flush();
        log.debug("Registro de documentos a firmar guardados...");

        FirmaDocumentoResponseDTO responseDTO = new FirmaDocumentoResponseDTO();
        responseDTO.setUsuarioEcertId(preinscripcion.getIdUsuarioEcert());
        responseDTO.setUrlLoginEcert(preinscripcion.getUrlLoginEcert());

        solicitudAdmisionEntity.setPep(request.isPep());
        solicitudAdmisionRepository.save(solicitudAdmisionEntity);
        solicitudAdmisionRepository.flush();

        return responseDTO;

    }

    public void webhookFirmaDocumento(WebHookRequestDTO request, long fase) {

        log.debug("Webhook firma documento por Ecert con resultado de firma... {}", request.toString());

        Optional<FirmaDocumentoEntity> firmaDocumentoEntityOptional = firmaDocumentoRepository.findByDocumentoEcertId(request.getDoctoId());

        if (firmaDocumentoEntityOptional.isPresent()) {
            FirmaDocumentoEntity firmaDocumentoEntity = firmaDocumentoEntityOptional.get();
            firmaDocumentoEntity.setFirmado(request.isFirmado());
            firmaDocumentoEntity.setRazonRechazo(request.getRazonRechazo());
            firmaDocumentoEntity.setFechaModificacion(LocalDateTime.now());
            firmaDocumentoEntity.setUsuarioModificacion(USR_TMP);
            firmaDocumentoEntity.setDocumentoFirmado(request.getDoctoBase64());

            firmaDocumentoRepository.save(firmaDocumentoEntity);
            firmaDocumentoRepository.flush();


            List<FirmaDocumentoEntity> firmaDocumentoEntities = firmaDocumentoRepository.findAllByClienteEntityRutAndUsuarioEcertTmpId(firmaDocumentoEntity.getClienteEntity().getRut(), firmaDocumentoEntity.getUsuarioEcertTmpId());
            int totalDocumentos = firmaDocumentoEntities.size();

            long documentosFirmados = firmaDocumentoEntities.stream().filter(f -> f.getFechaModificacion() != null).count();

            if (totalDocumentos == documentosFirmados) {
                SolicitudAdmisionEntity solicitudAdmisionEntity = firmaDocumentoEntity.getSolicitudAdmisionEntity();
                if (solicitudAdmisionEntity != null) {
                    admisionFaseService.actualizarFaseSolicitud(firmaDocumentoEntity.getSolicitudAdmisionEntity(), fase);
                }
            }
        }
    }

    public void firmaSeguros(FirmaDocumentoRequestDTO request) {
        log.debug("Se debe implementar firma de seguro, basarse en firma de de contratos, con plantillas referente a los anexos");

    }

    public ValidarFirmaDocumentoResponseDTO validarFirmaDocumento(ValidarFirmaDocumentoRequestDTO request) {
        if (request == null) {
            throw new BadRequestException();
        }

        validaciones.validacionGeneralRut(request.getRut());

        if (request.getUsuarioEcertId() < 1) {
            throw new BadRequestException("Usuario Ecert no valido");
        }

        if (request.getSolicitudId() < 1) {
            throw new BadRequestException("Solicitud no valida");
        }

        String rutFormateado = validaciones.formateaRutHaciaBD(request.getRut());

        List<FirmaDocumentoEntity> documentos = firmaDocumentoRepository.findAllByClienteEntityRutAndUsuarioEcertTmpId(rutFormateado, request.getUsuarioEcertId());

        if (documentos == null || documentos.isEmpty()) {
            throw new BadRequestException("No existen documentos para el cliente y solicitud de admision");
        }

        AtomicReference<String> motivoRechazo = new AtomicReference<>();
        AtomicBoolean activarExcepcion = new AtomicBoolean(false);
        AtomicInteger sumaFirma = new AtomicInteger(0);

        List<DocumentoDTO> documentosFirmados = new ArrayList<>();
        documentos.stream().forEach(d -> {

            sumaFirma.set(sumaFirma.incrementAndGet());

            if (!d.getFirmado()) {
                if (d.getRazonRechazo() != null) {
                    log.debug("Se produjo un error al firmar el documento: {}", d.getRazonRechazo());
                    motivoRechazo.set(d.getRazonRechazo());
                    activarExcepcion.set(true);
                }
                sumaFirma.set(sumaFirma.decrementAndGet());
            }

            if (d.getFirmado() && d.getDocumentoFirmado() != null) {
                DocumentoDTO documentoDTO = new DocumentoDTO();
                documentoDTO.setUriDocumento("/firma-documento/descarga");
                documentoDTO.setNombreDocumento(d.getNombreDocumento());
                documentoDTO.setTextoBoton("Descargar " + d.getParTipoDocumentoEntity().getDescripcion());
                documentoDTO.setDocumentoId(d.getDocumentoEcertId());

                documentosFirmados.add(documentoDTO);
            }

        });

        ValidarFirmaDocumentoResponseDTO response = new ValidarFirmaDocumentoResponseDTO();
        response.setFirmado(sumaFirma.get() == documentos.size() ? true : false);

        if (sumaFirma.get() == documentos.size()) {
            response.setDocumentosFirmados(documentosFirmados);
        }

        response.setMensajeError(motivoRechazo.get());
        response.setActivarExcepcion(activarExcepcion.get());


        return response;
    }


    public DescargaDocumentoResponseDTO descargaDocumentoFirmado(String rutCliente, int documentoId) {

        validaciones.validacionGeneralRut(rutCliente);

        if (documentoId < 1) {
            throw new BadRequestException("Id documento no valido");
        }

        String rutFormateado = validaciones.formateaRutHaciaBD(rutCliente);

        Optional<FirmaDocumentoEntity> firmaDocumentoEntityOptional = firmaDocumentoRepository.findByClienteEntityRutAndDocumentoEcertIdAndDocumentoFirmadoIsNotNull(rutFormateado, documentoId);

        if (firmaDocumentoEntityOptional.isEmpty()) {
            throw new BadRequestException("No existe documento para cliente");
        }

        FirmaDocumentoEntity firmaDocumentoEntity = firmaDocumentoEntityOptional.get();

        log.debug("Documento firmado");
        log.debug("ID documento Ecert: {}", firmaDocumentoEntity.getDocumentoEcertId());
        log.debug("Nombre documento: {}", firmaDocumentoEntity.getNombreDocumento());
        log.debug("Documento B64: {}", firmaDocumentoEntity.getDocumentoFirmado());


        return mapeaDescargaDocumento(firmaDocumentoEntity.getDocumentoFirmado(), firmaDocumentoEntity.getNombreDocumento());
    }

    private DescargaDocumentoResponseDTO mapeaDescargaDocumento(String documento, String nombreDocumento) {
        DescargaDocumentoResponseDTO descargaDocumentoResponseDTO = new DescargaDocumentoResponseDTO();

        byte[] bytes = Base64.getDecoder().decode(documento.getBytes());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        descargaDocumentoResponseDTO.setDocumento(byteArrayInputStream);
        descargaDocumentoResponseDTO.setNombreDocumento(nombreDocumento);
        return descargaDocumentoResponseDTO;
    }

    public DescargaDocumentoResponseDTO descargaDocumentoFirmaManual(String rutCliente, long solicitudId, int documentoId) throws IOException {
        log.debug("Descarga para firma manual de documentos");
        validaciones.validacionGeneralRut(rutCliente);

        if (documentoId < 1) {
            throw new BadRequestException("Id documento no valido");
        }

        String rutFormateado = validaciones.formateaRutHaciaBD(rutCliente);

        Optional<FirmaDocumentoEntity> firmaDocumentoEntityOptional = firmaDocumentoRepository.findByClienteEntityRutAndDocumentoEcertIdAndDocumentoFirmadoIsNull(rutFormateado, documentoId);

        if (firmaDocumentoEntityOptional.isEmpty()) {
            throw new BadRequestException("No existe documento para cliente");
        }


        FirmaDocumentoEntity firmaDocumentoEntity = firmaDocumentoEntityOptional.get();

        if (firmaDocumentoEntity.getDocumento() == null) {
            log.error("No existe documento previo a la firma");
            throw new BadRequestException("Se produjo un error al generar el documento a descargar");
        }

        return mapeaDescargaDocumento(firmaDocumentoEntity.getDocumento(), firmaDocumentoEntity.getNombreDocumento());

    }

    public List<DocumentoDTO> preparaDocumentosManuales(String rutCliente, long solicitudId, int usuarioEcertId) {

        validaciones.validacionGeneralRut(rutCliente);

        if (usuarioEcertId < 1) {
            throw new BadRequestException("Usuario Ecert no valido");
        }

        if (solicitudId < 1) {
            throw new BadRequestException("Solicitud no valida");
        }

        String rutFormateado = validaciones.formateaRutHaciaBD(rutCliente);
        List<FirmaDocumentoEntity> documentosPendientes = firmaDocumentoRepository.findAllByClienteEntityRutAndUsuarioEcertTmpIdAndDocumentoFirmadoIsNull(rutFormateado, usuarioEcertId);

        if (documentosPendientes == null || documentosPendientes.isEmpty()) {
            throw new BadRequestException("No existen documentos para cliente");
        }

        return documentosPendientes.stream().map(d -> {
            DocumentoDTO documentoDTO = new DocumentoDTO();
            documentoDTO.setUriDocumento("/firma-documento/descarga/firma-manual");
            documentoDTO.setNombreDocumento(d.getNombreDocumento());
            documentoDTO.setTextoBoton("Descargar " + d.getParTipoDocumentoEntity().getDescripcion());
            documentoDTO.setDocumentoId(d.getDocumentoEcertId());

            return documentoDTO;
        }).collect(Collectors.toList());

    }
}
