package corona.financiero.nmda.admision.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import corona.financiero.nmda.admision.adapter.CotizacionAdapter;
import corona.financiero.nmda.admision.dto.cotizacion.CotizacionDetalleResponseDTO;
import corona.financiero.nmda.admision.dto.cotizacion.CotizacionRequestDTO;
import corona.financiero.nmda.admision.dto.cotizacion.CotizacionesRequestDTO;
import corona.financiero.nmda.admision.entity.CotizacionPrevisionalDetalleEntity;
import corona.financiero.nmda.admision.entity.CotizacionPrevisionalEntity;
import corona.financiero.nmda.admision.entity.ParRespuestaCotizacionPrevisionalEntity;
import corona.financiero.nmda.admision.entity.ProspectoEntity;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.repository.CotizacionPrevisionalDetalleRepository;
import corona.financiero.nmda.admision.repository.CotizacionPrevisionalRepository;
import corona.financiero.nmda.admision.repository.ParRespuestaCotizacionPrevisionalRepository;
import corona.financiero.nmda.admision.repository.ProspectoRepository;
import corona.financiero.nmda.admision.util.Validaciones;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CotizacionService {

    @Autowired
    private CotizacionAdapter cotizacionAdapter;

    @Autowired
    private ProspectoRepository prospectoRepository;

    @Autowired
    private Validaciones validaciones;

    @Autowired
    private ParRespuestaCotizacionPrevisionalRepository parRespuestaCotizacionPrevisionalRepository;

    @Autowired
    private CotizacionPrevisionalRepository cotizacionPrevisionalRepository;

    @Autowired
    private CotizacionPrevisionalDetalleRepository cotizacionPrevisionalDetalleRepository;

    private static final String USUARIO_TEMPORAL = "USR_TMP";

    private static final String CODIGO_CONTROL_ERROR = "Codigo de control no registrado";

    private static final String ATRIBUTO_CONTROL = "control";
    private static final String ATRIBUTO_CODIGO = "@codigo";


    //@Transactional
    public Map<String, Object> obtenerCotizaciones(CotizacionesRequestDTO request, String transaccionId) {
        log.debug("Cotizaciones request: {}", request.toString());

        String rutFormateado = validaciones.formateaRutHaciaBD(request.getRut());

        ProspectoEntity prospectoEntity = prospectoRepository.findByRutAndVigenciaIsTrue(rutFormateado);

        if (prospectoEntity == null) {
            throw new BadRequestException("No existe prospecto");
        }

        CotizacionRequestDTO cotizacionRequestDTO = new CotizacionRequestDTO();
        cotizacionRequestDTO.setRut(request.getRut());

        cotizacionRequestDTO.setCodAutoriza(transaccionId);
        cotizacionRequestDTO.setTipoServicio(String.valueOf(request.getCantidad()));

        Map<String, Object> map = cotizacionAdapter.consultarCotizaciones(cotizacionRequestDTO);
        if (map == null) {
            log.debug("No se tiene resultado desde previred... se procede a otorgar producto de cortesia y se debe dejar observacion de solicitud con problemas de servicio previred");
            return null;
        }

        Map<String, Object> respuesta = (HashMap) map.get("respuesta");
        Map<String, Object> control = (HashMap) respuesta.get(ATRIBUTO_CONTROL);
        List<Map<String, Object>> respuestServicio = (ArrayList) respuesta.get("respuestaservicio");
        Map<String, Object> tipoAUT = respuestServicio.get(0);
        Map<String, Object> respuestaaut = (HashMap) tipoAUT.get("respuestaaut");
        Map<String, Object> tipoCCX = respuestServicio.get(1);
        Map<String, Object> respuestaccx = (HashMap) tipoCCX.get("respuestaccx");

        Map<String, Object> legal = (HashMap) respuestaccx.get("respuestaccx_legal");

        CotizacionPrevisionalEntity cotizacionPrevisionalEntity = new CotizacionPrevisionalEntity();
        cotizacionPrevisionalEntity.setFechaRegistro(LocalDateTime.now());
        cotizacionPrevisionalEntity.setFolio(String.valueOf(legal.get("@folio")));
        cotizacionPrevisionalEntity.setFechaConsulta(LocalDate.now());
        //transaccion id de ecert
        cotizacionPrevisionalEntity.setCodigoAutorizacion(cotizacionRequestDTO.getCodAutoriza());
        cotizacionPrevisionalEntity.setTipoServicio(String.valueOf(tipoCCX.get("@tipo")));
        cotizacionPrevisionalEntity.setFirma(String.valueOf(legal.get("@firma")));
        cotizacionPrevisionalEntity.setLlave(String.valueOf(respuestaaut.get("llave")));
        cotizacionPrevisionalEntity.setUsuarioRegistro(USUARIO_TEMPORAL);
        cotizacionPrevisionalEntity.setVigencia(true);

        long codigoControl = Long.parseLong(String.valueOf(control.get(ATRIBUTO_CODIGO)));

        Optional<ParRespuestaCotizacionPrevisionalEntity> codigoControlEntityOptional = parRespuestaCotizacionPrevisionalRepository.findById(codigoControl);
        if (codigoControlEntityOptional.isEmpty()) {
            throw new BadRequestException(CODIGO_CONTROL_ERROR);
        }
        ParRespuestaCotizacionPrevisionalEntity parRespuestaCotizacionPrevisionalEntity = codigoControlEntityOptional.get();
        cotizacionPrevisionalEntity.setCodigoServicio(parRespuestaCotizacionPrevisionalEntity);

        Map<String, Object> controlAUT = (HashMap) tipoAUT.get(ATRIBUTO_CONTROL);
        long codigoControlAut = Long.parseLong(String.valueOf(controlAUT.get(ATRIBUTO_CODIGO)));

        Optional<ParRespuestaCotizacionPrevisionalEntity> codigoControlAutEntityOptional = parRespuestaCotizacionPrevisionalRepository.findById(codigoControlAut);
        if (codigoControlAutEntityOptional.isEmpty()) {
            throw new BadRequestException(CODIGO_CONTROL_ERROR);
        }
        ParRespuestaCotizacionPrevisionalEntity codigoControlAutEntity = codigoControlAutEntityOptional.get();
        cotizacionPrevisionalEntity.setCodigoAut(codigoControlAutEntity);

        Map<String, Object> controlCCX = (HashMap) tipoCCX.get(ATRIBUTO_CONTROL);
        long codigoControlDetalle = Long.parseLong(String.valueOf(controlCCX.get(ATRIBUTO_CODIGO)));

        Optional<ParRespuestaCotizacionPrevisionalEntity> codigoControlDetalleEntityOptional = parRespuestaCotizacionPrevisionalRepository.findById(codigoControlDetalle);
        if (codigoControlDetalleEntityOptional.isEmpty()) {
            throw new BadRequestException(CODIGO_CONTROL_ERROR);
        }

        ParRespuestaCotizacionPrevisionalEntity codigoControlDetalleEntity = codigoControlDetalleEntityOptional.get();
        cotizacionPrevisionalEntity.setCodigoDetalle(codigoControlDetalleEntity);

        cotizacionPrevisionalEntity.setProspectoEntity(prospectoEntity);

        invalidarCotizacionesPrevias(prospectoEntity);

        cotizacionPrevisionalRepository.save(cotizacionPrevisionalEntity);
        cotizacionPrevisionalRepository.flush();

        List<Map<String, Object>> cotizaciones = (List<Map<String, Object>>) respuestaccx.get("respuestaccx_detalle");


        //mapear Map a DTO
        List<CotizacionDetalleResponseDTO> listaDetalle = cotizaciones.stream().map(c -> {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(c, CotizacionDetalleResponseDTO.class);
        }).collect(Collectors.toList());

        //Convertir DTO a Entity
        List<CotizacionPrevisionalDetalleEntity> collect = listaDetalle.stream().map(c -> {
            CotizacionPrevisionalDetalleEntity cotizacionPrevisionalDetalleEntity = new CotizacionPrevisionalDetalleEntity();
            cotizacionPrevisionalDetalleEntity.setCotizacionPrevisionalEntity(cotizacionPrevisionalEntity);

            cotizacionPrevisionalDetalleEntity.setPeriodo(c.getMes());
            cotizacionPrevisionalDetalleEntity.setRemuneracionImponible(Long.parseLong(c.getRemuneracionImponible()));
            cotizacionPrevisionalDetalleEntity.setMonto(Long.parseLong(c.getMonto()));
            try {
                cotizacionPrevisionalDetalleEntity.setFechaPago(validaciones.convertirFechaSlashALocalDate(c.getFechaPago()));
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
            }
            cotizacionPrevisionalDetalleEntity.setTipoMovimiento(c.getTipoMovimiento());
            cotizacionPrevisionalDetalleEntity.setRutEmpleador(validaciones.formateaRutHaciaBD(c.getRutEmpleador()));
            cotizacionPrevisionalDetalleEntity.setAfp(c.getAfp());

            return cotizacionPrevisionalDetalleEntity;


        }).collect(Collectors.toList());

        cotizacionPrevisionalDetalleRepository.saveAll(collect);
        cotizacionPrevisionalDetalleRepository.flush();

        return map;
    }

    private void invalidarCotizacionesPrevias(ProspectoEntity prospectoEntity) {

        List<CotizacionPrevisionalEntity> cotizacionesPrevias = cotizacionPrevisionalRepository.findAllByProspectoEntity(prospectoEntity);

        cotizacionesPrevias.stream().forEach(c -> c.setVigencia(false));

        cotizacionPrevisionalRepository.saveAll(cotizacionesPrevias);
        cotizacionPrevisionalRepository.flush();

    }
}
