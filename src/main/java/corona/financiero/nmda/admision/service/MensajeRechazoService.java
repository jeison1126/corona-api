package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.MensajeRechazoDTO;
import corona.financiero.nmda.admision.dto.NuevoMensajeRechazoDTO;
import corona.financiero.nmda.admision.dto.PaginacionMensajeRechazoDTO;
import corona.financiero.nmda.admision.entity.ParMensajeRechazoEntity;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.MensajeRechazoNotFoundException;
import corona.financiero.nmda.admision.repository.ParMensajeRechazoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MensajeRechazoService {

    @Autowired
    private ParMensajeRechazoRepository parMensajeRechazoRepository;
    @Value("${cantidad.elementos.paginacion}")
    private int paginacion;

    private static final String USUARIO_TEMPORAL = "USR_TMP";

    public PaginacionMensajeRechazoDTO listarMensajesRechazo(int numPagina, String filtro) {

        if (numPagina < 0) {
            throw new BadRequestException("Numero de pagina no permitida");
        }

        Pageable pageable = PageRequest.of(numPagina, paginacion, Sort.by("fechaRegistro"));
        Page<ParMensajeRechazoEntity> all = null;

        if (filtro == null || filtro.trim().isEmpty()) {
            all = parMensajeRechazoRepository.findAll(pageable);
        } else {
            filtro = filtro.toLowerCase(Locale.ROOT);
            all = parMensajeRechazoRepository.listarMensajesRechazoConFiltro(filtro, pageable);
        }

        Stream<ParMensajeRechazoEntity> parMensajeRechazoEntityStream = all.get();

        PaginacionMensajeRechazoDTO p = new PaginacionMensajeRechazoDTO();
        p.setTotalPagina(all.getTotalPages());
        p.setPagina(all.getNumber());
        p.setTotalElementos(all.getTotalElements());
        p.setMensajes(
                parMensajeRechazoEntityStream.map(m -> {
                    MensajeRechazoDTO mensajeRechazoDTO = new MensajeRechazoDTO();
                    mensajeRechazoDTO.setMensajeFuncional(m.getMensajeFuncional());
                    mensajeRechazoDTO.setDescripcion(m.getDescripcion());
                    mensajeRechazoDTO.setCausalRechazoId(m.getMensajeRechazoId());
                    mensajeRechazoDTO.setVigencia(m.isVigencia());
                    return mensajeRechazoDTO;
                }).collect(Collectors.toList()));
        return p;
    }

    public void actualizarMensajeRechazo(MensajeRechazoDTO request) {

        if (request == null) {
            throw new BadRequestException();
        }

        if (request.getCausalRechazoId() <= 0) {
            throw new BadRequestException("Id de mensaje de rechazo invalido");
        }

        if ((request.getDescripcion() == null || request.getDescripcion().trim().isEmpty()) && (request.getMensajeFuncional() == null || request.getMensajeFuncional().trim().isEmpty())) {
            throw new BadRequestException();
        }

        ParMensajeRechazoEntity parMensajeRechazoEntity = parMensajeRechazoRepository.findById(request.getCausalRechazoId()).orElseThrow(MensajeRechazoNotFoundException::new);

        if (request.getMensajeFuncional() != null)
            parMensajeRechazoEntity.setMensajeFuncional(request.getMensajeFuncional());

        if (request.getDescripcion() != null)
            parMensajeRechazoEntity.setDescripcion(request.getDescripcion());

        parMensajeRechazoEntity.setVigencia(request.isVigencia());
        parMensajeRechazoEntity.setUsuarioModificacion(USUARIO_TEMPORAL);
        parMensajeRechazoEntity.setFechaModificacion(LocalDateTime.now());
        parMensajeRechazoRepository.save(parMensajeRechazoEntity);
    }

    public void eliminarMensajeRechazo(long mensajeRechazoId) {

        if (mensajeRechazoId <= 0) {
            throw new BadRequestException("Id de mensaje de rechazo invalido");
        }
        ParMensajeRechazoEntity parMensajeRechazoEntity = parMensajeRechazoRepository.findById(mensajeRechazoId).orElseThrow(MensajeRechazoNotFoundException::new);

        parMensajeRechazoEntity.setVigencia(false);
        parMensajeRechazoEntity.setUsuarioModificacion(USUARIO_TEMPORAL);
        parMensajeRechazoEntity.setFechaModificacion(LocalDateTime.now());
        parMensajeRechazoRepository.save(parMensajeRechazoEntity);
    }

    public void crearMensajeRechazo(NuevoMensajeRechazoDTO request) {

        if (request == null) {
            throw new BadRequestException();
        }

        if (request.getDescripcion() == null || request.getDescripcion().trim().isEmpty()) {
            throw new BadRequestException("Falta ingresar descripcion");
        }

        if (request.getMensajeFuncional() == null || request.getMensajeFuncional().trim().isEmpty()) {
            throw new BadRequestException("Falta ingresar mensaje funcional");
        }

        ParMensajeRechazoEntity parMensajeRechazoEntity = parMensajeRechazoRepository.verificaMensajePreExistente(request.getDescripcion());

        if (parMensajeRechazoEntity != null) {
            throw new BadRequestException("Ya existe un mensaje de rechazo con la descripcion indicada");
        }

        parMensajeRechazoEntity = new ParMensajeRechazoEntity();
        parMensajeRechazoEntity.setMensajeFuncional(request.getMensajeFuncional());
        parMensajeRechazoEntity.setDescripcion(request.getDescripcion());
        parMensajeRechazoEntity.setFechaRegistro(LocalDateTime.now());
        parMensajeRechazoEntity.setUsuarioRegistro(USUARIO_TEMPORAL);
        parMensajeRechazoEntity.setVigencia(true);
        parMensajeRechazoRepository.save(parMensajeRechazoEntity);
    }
}
