package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.OfertaAceptadaDTO;
import corona.financiero.nmda.admision.dto.OfertaRechazadaDTO;
import corona.financiero.nmda.admision.entity.*;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.repository.*;
import corona.financiero.nmda.admision.util.Constantes;
import corona.financiero.nmda.admision.util.Validaciones;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@Slf4j
public class FaseInformacionOfertaService {

    @Autowired
    private Validaciones validaciones;

    @Autowired
    private SolicitudAdmisionRepository solicitudAdmisionRepository;

    @Autowired
    private ParMotivoRechazoRepository parMotivoRechazoRepository;

    @Autowired
    private RechazoSolicitudRepository rechazoSolicitudRepository;

    @Autowired
    private ParEstadoSolicitudRepository parEstadoSolicitudRepository;

    @Autowired
    private EvaluacionProductoRepository evaluacionProductoRepository;

    @Autowired
    private ParDiaPagoRepository parDiaPagoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TarjetaRepository tarjetaRepository;

    @Autowired
    private AdmisionFaseService admisionFaseService;

    private static final int ID_MOTIVO_RECHAZO_OTRO = 9;

    private static final String USUARIO_TEMPORAL = "USR_TMP";


    @Transactional
    public void rechazarOferta(OfertaRechazadaDTO request) {

        validarOfertaRechazadaDTORequest(request);

        String rutFormateado = validaciones.formateaRutHaciaBD(request.getRut());

        Optional<SolicitudAdmisionEntity> solicitudAdmisionEntityOptional = solicitudAdmisionRepository.recuperarSolicitudAdmisionVigenteBySolicitudIdAndRutProspecto(request.getSolicitudId(), rutFormateado);

        if (solicitudAdmisionEntityOptional.isEmpty()) {
            throw new BadRequestException("La solicitud de admisión no es válida o no está asociada al prospecto indicado");
        }

        SolicitudAdmisionEntity solicitudAdmisionEntity = solicitudAdmisionEntityOptional.get();

        Optional<ParMotivoRechazoEntity> motivoRechazoEntityOptional = parMotivoRechazoRepository.recuperarMotivoRechazoByMotivoRechazoId(request.getIdMotivoRechazo());

        if (motivoRechazoEntityOptional.isEmpty()) {
            throw new BadRequestException("El motivo de rechazo no es válido");
        }

        ParMotivoRechazoEntity parMotivoRechazoEntity = motivoRechazoEntityOptional.get();
        if (parMotivoRechazoEntity.getMotivoRechazoId() == ID_MOTIVO_RECHAZO_OTRO && (request.getOtroMotivo() == null || request.getOtroMotivo().trim().isEmpty())) {
            throw new BadRequestException("Debe especificar el motivo del rechazo");
        }

        RechazoSolicitudEntity rechazoSolicitudEntity = new RechazoSolicitudEntity();
        rechazoSolicitudEntity.setDescripcion(request.getOtroMotivo());
        rechazoSolicitudEntity.setParMotivoRechazoEntity(parMotivoRechazoEntity);
        rechazoSolicitudEntity.setSolicitudAdmisionEntity(solicitudAdmisionEntity);
        rechazoSolicitudEntity.setFechaRegistro(LocalDateTime.now());

        rechazoSolicitudRepository.save(rechazoSolicitudEntity);
        rechazoSolicitudRepository.flush();

        Optional<ParEstadoSolicitudEntity> estadoAprobadoOptional = parEstadoSolicitudRepository.findById(Constantes.ESTADO_APROBADO);
        solicitudAdmisionEntity.setParEstadoSolicitudEntity(estadoAprobadoOptional.get());
        solicitudAdmisionEntity.setFechaModificacion(LocalDateTime.now());
        solicitudAdmisionEntity.setUsuarioModificacion(USUARIO_TEMPORAL);
        solicitudAdmisionEntity.setVigencia(false);
        solicitudAdmisionRepository.save(solicitudAdmisionEntity);
        solicitudAdmisionRepository.flush();

        admisionFaseService.actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_INFORMACION_OFERTA);

    }

    @Transactional
    public void aceptarOferta(OfertaAceptadaDTO request) {

        validarOfertaAceptadaDTORequest(request);

        String rutFormateado = validaciones.formateaRutHaciaBD(request.getRut());

        Optional<ParDiaPagoEntity> parDiaPagoEntityOptional = parDiaPagoRepository.recuperarDiaPagoVigenteById(request.getDiaPagoId());

        if (parDiaPagoEntityOptional.isEmpty()) {
            throw new BadRequestException("El día de pago de la tarjeta no es válido");
        }

        Optional<EvaluacionProductoEntity> evaluacionProductoEntityOptional = evaluacionProductoRepository.recuperarEvaluacionProductoByEvaluacionProductoIdAndRut(request.getEvaluacionProductoId(), rutFormateado);

        if (evaluacionProductoEntityOptional.isEmpty()) {
            throw new BadRequestException("La oferta no está disponible para el prospecto indicado");
        }
        EvaluacionProductoEntity evaluacionProductoEntity = evaluacionProductoEntityOptional.get();

        ProspectoEntity prospectoEntity = evaluacionProductoEntity.getSolicitudAdmisionEntity().getProspectoEntity();

        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setRut(prospectoEntity.getRut());
        clienteEntity.setApellidoMaterno(prospectoEntity.getApellidoMaterno());
        clienteEntity.setApellidoPaterno(prospectoEntity.getApellidoPaterno());
        clienteEntity.setNombre(prospectoEntity.getNombres());
        clienteEntity.setMovil((int) prospectoEntity.getMovil());
        clienteEntity.setEmail(prospectoEntity.getEmail());
        clienteEntity.setProspectoEntity(prospectoEntity);
        clienteEntity.setUsuarioRegistro(USUARIO_TEMPORAL);
        clienteEntity.setFechaRegistro(LocalDateTime.now());
        clienteEntity.setVigencia(true);

        clienteRepository.save(clienteEntity);
        clienteRepository.flush();

        TarjetaEntity tarjetaEntity = new TarjetaEntity();
        tarjetaEntity.setClienteEntity(clienteEntity);
        tarjetaEntity.setCupoAprobado(evaluacionProductoEntity.getCupoAprobado());
        tarjetaEntity.setDiaPago(request.getDiaPagoId().intValue());
        tarjetaEntity.setEvaluacionProductoEntity(evaluacionProductoEntity);
        tarjetaEntity.setFechaIngreso(LocalDateTime.now());
        tarjetaEntity.setUsuarioIngreso(USUARIO_TEMPORAL);
        tarjetaEntity.setVigencia(true);

        tarjetaRepository.save(tarjetaEntity);
        tarjetaRepository.flush();

        admisionFaseService.actualizarFaseSolicitud(evaluacionProductoEntity.getSolicitudAdmisionEntity(), Constantes.FASE_INFORMACION_OFERTA);

    }

    private void validarOfertaRechazadaDTORequest(OfertaRechazadaDTO request) {

        if (request == null) {
            throw new BadRequestException();
        }

        validaciones.validacionGeneralRut(request.getRut());

        if (request.getSolicitudId() == null || request.getSolicitudId().longValue() <= 0) {
            throw new BadRequestException("El id de la solicitud de admisión es requerida");
        }

        if (request.getIdMotivoRechazo() == null || request.getIdMotivoRechazo().longValue() <= 0) {
            throw new BadRequestException("El id del motivo de rechazo es requerido");
        }
    }

    private void validarOfertaAceptadaDTORequest(OfertaAceptadaDTO request) {

        if (request == null) {
            throw new BadRequestException();
        }

        validaciones.validacionGeneralRut(request.getRut());

        if (request.getDiaPagoId() == null || request.getDiaPagoId().longValue() <= 0) {
            throw new BadRequestException("Los días de vencimiento de pago de la tarjeta no es válido");
        }

        if (request.getEvaluacionProductoId() == null || request.getEvaluacionProductoId().longValue() <= 0) {
            throw new BadRequestException("El id del producto y cupo aceptado no es válido");
        }
    }
}
