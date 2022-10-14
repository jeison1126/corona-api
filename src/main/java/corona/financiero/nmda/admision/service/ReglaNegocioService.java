package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.PaginacionReglaNegocioDTO;
import corona.financiero.nmda.admision.dto.ReglaNegocioBasicoDTO;
import corona.financiero.nmda.admision.dto.ReglaNegocioCompletoDTO;
import corona.financiero.nmda.admision.entity.ReglaNegocioEntity;
import corona.financiero.nmda.admision.enumeration.DireccionOrdenaEnum;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.ReglaNegocioNotFoundException;
import corona.financiero.nmda.admision.repository.ParFaseRepository;
import corona.financiero.nmda.admision.repository.ParMensajeRechazoRepository;
import corona.financiero.nmda.admision.repository.ReglaNegocioRepository;
import corona.financiero.nmda.admision.util.Validaciones;
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
public class ReglaNegocioService {

    @Autowired
    private Validaciones validaciones;

    @Autowired
    private ReglaNegocioRepository reglaNegocioRepository;

    @Value("${cantidad.elementos.paginacion}")
    private int paginacion;

    @Autowired
    private ParFaseRepository parFaseRepository;

    @Autowired
    private ParMensajeRechazoRepository parMensajeRechazoRepository;

    private static final String USUARIO_TEMPORAL = "USR_TMP";

    private static final long MENSAJE_RECHAZO_ID = 1l;
    private static final long PAR_FASE_ID = 1l;


    public void registrar(ReglaNegocioBasicoDTO reglaNegocioBasicoDTO) {

        validarRegistroReglaNegocioBasicoDTO(reglaNegocioBasicoDTO);

        ReglaNegocioEntity reglaNegocioEntityExistente = reglaNegocioRepository.verificarPreExistentePorDescripcion(reglaNegocioBasicoDTO.getDescripcion());

        if (reglaNegocioEntityExistente != null) {
            throw new BadRequestException("Ya existe una regla de negocio con la descripcion indicada");
        }

        ReglaNegocioEntity reglaNegocioEntity = convertirReglaNegocioBasicoDTOToReglaNegocioEntity(reglaNegocioBasicoDTO);

        reglaNegocioEntity.setVigencia(true);
        reglaNegocioEntity.setEditable(true);
        reglaNegocioEntity.setFechaIngreso(LocalDateTime.now());
        reglaNegocioEntity.setUsuarioIngreso(USUARIO_TEMPORAL);

        reglaNegocioEntity.setParMensajeRechazoEntity(parMensajeRechazoRepository.findById(MENSAJE_RECHAZO_ID).get());
        reglaNegocioEntity.setParFaseEntity(parFaseRepository.findById(PAR_FASE_ID).get());

        reglaNegocioRepository.save(reglaNegocioEntity);
    }

    public void actualizar(ReglaNegocioCompletoDTO reglaNegocioCompletoDTO) {

        validarActualizacionReglaNegocioCompletoDTO(reglaNegocioCompletoDTO);

        ReglaNegocioEntity reglaNegocioEntityExistente = reglaNegocioRepository.findById(reglaNegocioCompletoDTO.getId()).orElseThrow(ReglaNegocioNotFoundException::new);

        ReglaNegocioEntity reglaNegocioEntity = convertirReglaNegocioCompletoDTOToReglaNegocioEntity(reglaNegocioCompletoDTO);

        reglaNegocioEntity.setFechaIngreso(reglaNegocioEntityExistente.getFechaIngreso());
        reglaNegocioEntity.setUsuarioIngreso(reglaNegocioEntityExistente.getUsuarioIngreso());
        reglaNegocioEntity.setEditable(reglaNegocioEntityExistente.isEditable());
        reglaNegocioEntity.setFechaModificacion(LocalDateTime.now());
        reglaNegocioEntity.setUsuarioModificacion(USUARIO_TEMPORAL);

        //esto debe ser corregido en los proximos sprint, para asociar mensaje y fase que corresponda
        reglaNegocioEntity.setParMensajeRechazoEntity(parMensajeRechazoRepository.findById(MENSAJE_RECHAZO_ID).get());
        reglaNegocioEntity.setParFaseEntity(parFaseRepository.findById(PAR_FASE_ID).get());

        reglaNegocioRepository.save(reglaNegocioEntity);


    }

    public ReglaNegocioCompletoDTO recuperarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new BadRequestException("Debe ingresar un id");
        }

        if (!validaciones.validarId(id)) {
            throw new BadRequestException("El id no es válido");
        }
        Long idFormateado = Long.parseLong(id);


        ReglaNegocioEntity reglaNegocioEntity = reglaNegocioRepository.findById(idFormateado).orElseThrow(ReglaNegocioNotFoundException::new);

        return convertirReglaNegocioEntityToReglaNegocioCompletoDTO(reglaNegocioEntity);
    }

    public PaginacionReglaNegocioDTO listarPaginado(int numPagina, String campoOrdena, String direccionOrdena, String valorFiltro) {

        validarDatosPaginacion(numPagina, direccionOrdena);

        Sort sort = direccionOrdena.equals(DireccionOrdenaEnum.ASC.name()) ? Sort.by(campoOrdena).ascending() : Sort.by(campoOrdena).descending();

        Pageable pageable = PageRequest.of(numPagina, paginacion, sort);
        Page<ReglaNegocioEntity> pageReglaNegocio = null;

        if (valorFiltro == null || valorFiltro.trim().isEmpty()) {
            pageReglaNegocio = reglaNegocioRepository.listarEditablesPaginado(pageable);
        } else {
            valorFiltro = valorFiltro.toLowerCase(Locale.ROOT);
            pageReglaNegocio = reglaNegocioRepository.listarEditablesConFiltroPaginado(valorFiltro, pageable);
        }

        Stream<ReglaNegocioEntity> reglaNegocioEntityStream = pageReglaNegocio.get();

        PaginacionReglaNegocioDTO p = new PaginacionReglaNegocioDTO();

        p.setTotalPagina(pageReglaNegocio.getTotalPages());
        p.setPagina(pageReglaNegocio.getNumber());
        p.setTotalElementos(pageReglaNegocio.getTotalElements());
        p.setMensajes(
                reglaNegocioEntityStream.map(reglaNegocioEntity ->
                        convertirReglaNegocioEntityToReglaNegocioCompletoDTO(reglaNegocioEntity)
                ).collect(Collectors.toList()));


        return p;
    }

    public void eliminarPorId(String id) {

        if (id == null || id.trim().isEmpty()) {
            throw new BadRequestException("Debe ingresar un id");
        }

        if (!validaciones.validarId(id)) {
            throw new BadRequestException("El id no es válido");
        }
        Long idFormateado = Long.parseLong(id);

        ReglaNegocioEntity reglaNegocioEntity = reglaNegocioRepository.findById(idFormateado).orElseThrow(ReglaNegocioNotFoundException::new);

        reglaNegocioEntity.setVigencia(false);
        reglaNegocioEntity.setFechaModificacion(LocalDateTime.now());
        reglaNegocioEntity.setUsuarioModificacion(USUARIO_TEMPORAL);
        reglaNegocioRepository.save(reglaNegocioEntity);
    }

    private void validarRegistroReglaNegocioBasicoDTO(ReglaNegocioBasicoDTO reglaNegocioBasicoDTO) {
        if (reglaNegocioBasicoDTO == null) {
            throw new BadRequestException();
        }

        if (reglaNegocioBasicoDTO.getDescripcion() == null || reglaNegocioBasicoDTO.getDescripcion().trim().isEmpty()) {
            throw new BadRequestException("Debe ingresar una descripción");
        }

        if (reglaNegocioBasicoDTO.getValor() == null || reglaNegocioBasicoDTO.getValor().trim().isEmpty()) {
            throw new BadRequestException("Debe ingresar el valor de la regla");
        }
    }

    private void validarActualizacionReglaNegocioCompletoDTO(ReglaNegocioCompletoDTO reglaNegocioCompletoDTO) {
        this.validarRegistroReglaNegocioBasicoDTO(reglaNegocioCompletoDTO);

        if (reglaNegocioCompletoDTO.getId() == null || reglaNegocioCompletoDTO.getId().longValue() <= 0) {
            throw new BadRequestException("Id de regla de negocio inválido");
        }


    }

    public void validarDatosPaginacion(int numPagina, String direccionOrdena) {
        if (numPagina < 0) {
            throw new BadRequestException("Numero de pagina no permitida");
        }

        if (direccionOrdena == null || direccionOrdena.trim().isEmpty()) {
            throw new BadRequestException("Debe existir una dirección de ordenamiento");
        }

        if (validaciones.validarDireccionOrdenamiento(direccionOrdena).equals(false)) {
            throw new BadRequestException("La dirección del ordenamiento no es válido");
        }
    }

    private ReglaNegocioEntity convertirReglaNegocioBasicoDTOToReglaNegocioEntity(ReglaNegocioBasicoDTO reglaNegocioBasicoDTO) {

        ReglaNegocioEntity reglaNegocioEntity = new ReglaNegocioEntity();
        reglaNegocioEntity.setDescripcion(reglaNegocioBasicoDTO.getDescripcion());
        reglaNegocioEntity.setValor(reglaNegocioBasicoDTO.getValor());

        return reglaNegocioEntity;

    }

    private ReglaNegocioEntity convertirReglaNegocioCompletoDTOToReglaNegocioEntity(ReglaNegocioCompletoDTO reglaNegocioCompletoDTO) {
        ReglaNegocioEntity reglaNegocioEntity = convertirReglaNegocioBasicoDTOToReglaNegocioEntity(reglaNegocioCompletoDTO);
        reglaNegocioEntity.setId(reglaNegocioCompletoDTO.getId());
        reglaNegocioEntity.setVigencia(reglaNegocioCompletoDTO.isVigencia());

        return reglaNegocioEntity;
    }

    private ReglaNegocioCompletoDTO convertirReglaNegocioEntityToReglaNegocioCompletoDTO(ReglaNegocioEntity reglaNegocioEntity) {
        ReglaNegocioCompletoDTO reglaNegocioCompletoDTO = new ReglaNegocioCompletoDTO();
        reglaNegocioCompletoDTO.setId(reglaNegocioEntity.getId());
        reglaNegocioCompletoDTO.setDescripcion(reglaNegocioEntity.getDescripcion());
        reglaNegocioCompletoDTO.setValor(reglaNegocioEntity.getValor());
        reglaNegocioCompletoDTO.setVigencia(reglaNegocioEntity.isVigencia());

        return reglaNegocioCompletoDTO;
    }
}
