package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.*;
import corona.financiero.nmda.admision.entity.*;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PerfilamientoService {

    @Autowired
    private AccionRepository accionRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private ModuloRepository moduloRepository;

    @Value("${cantidad.elementos.paginacion}")
    private int paginacion;

    private static final String USUARIO_TEMPORAL = "USR_TMP";

    @Autowired
    private RolAccionRepository rolAccionRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PerfilRolRepository perfilRolRepository;

    @Autowired
    private PerfilFuncionarioRepository perfilFuncionarioRepository;

    @Autowired
    private FuncionariosRepository funcionariosRepository;

    @Autowired
    private FuncionariosService funcionariosService;

    @Autowired
    private Validaciones validaciones;

    private static final String ID_PERFIL_INVALIDO = "id perfil invalido";

    private static final String NUM_PAGINA_INVALIDA= "Numero de pagina no permitida";

    private static final String ORDEN_FECHA_REGISTRO = "fechaRegistro";

    public List<AccionDTO> listarAcciones() {

        List<AccionEntity> acciones = accionRepository.findAll();

        if (acciones == null || acciones.isEmpty()) {
            throw new NoContentException();
        }

        return acciones.stream().map(a -> {
            AccionDTO accionDTO = new AccionDTO();
            accionDTO.setAccionId(a.getAccionId());
            accionDTO.setNombre(a.getNombre());
            return accionDTO;
        }).collect(Collectors.toList());
    }

    public PaginacionRolDTO listarRoles(int numPagina, String filtro) {

        if (numPagina < 0) {
            throw new BadRequestException(NUM_PAGINA_INVALIDA);
        }

        Pageable pageable = PageRequest.of(numPagina, paginacion, Sort.by(ORDEN_FECHA_REGISTRO));

        Page<RolEntity> all = null;

        if (filtro == null || filtro.trim().isEmpty()) {
            all = rolRepository.findAll(pageable);
        } else {
            filtro = filtro.toLowerCase(Locale.ROOT);
            all = rolRepository.listarRoles(filtro, pageable);
        }
        return mapearResultadoRoles(all);
    }

    private PaginacionRolDTO mapearResultadoRoles(Page<RolEntity> all) {
        PaginacionRolDTO paginacionRolDTO = new PaginacionRolDTO();
        paginacionRolDTO.setTotalElementos(all.getTotalElements());
        paginacionRolDTO.setTotalPagina(all.getTotalPages());
        paginacionRolDTO.setPagina(all.getNumber());

        List<ListarRolDTO> roles = all.stream().map(r -> {
            ListarRolDTO rolDTO = new ListarRolDTO();
            rolDTO.setRolId(r.getRolId());
            rolDTO.setNombre(r.getNombre());
            rolDTO.setVigencia(r.isVigencia());
            return rolDTO;
        }).collect(Collectors.toList());

        paginacionRolDTO.setRoles(roles);

        return paginacionRolDTO;
    }

    public List<ModuloDTO> listarModulos() {
        List<ModuloEntity> modulos = moduloRepository.findAll();

        if (modulos == null || modulos.isEmpty()) {
            throw new NoContentException();
        }

        return modulos.stream().map(m -> {
            ModuloDTO modulODTO = new ModuloDTO();
            modulODTO.setModuloId(m.getModuloId());
            modulODTO.setNombre(m.getNombre());
            return modulODTO;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void crearRol(CrearRolRequestDTO request) {

        validarRequestRol(request);
        log.debug("Request: {}", request.toString());

        RolEntity rolEntity = rolRepository.verificaRolPreexistente(request.getNombre());
        if (rolEntity != null) {
            throw new BadRequestException("Ya existe un rol con el mismo nombre");
        }


        Optional<ModuloEntity> moduloEntityOptional = moduloRepository.findByModuloId(request.getModulo());
        log.debug("moduloEntityOptional: {}", moduloEntityOptional.isPresent());

        if (moduloEntityOptional.isEmpty()) {
            throw new BadRequestException("Modulo invalido para asociar");
        }
        ModuloEntity moduloEntity = moduloEntityOptional.get();
        List<AccionEntity> acciones = accionRepository.findAllById(request.getAcciones());

        RolEntity rolEntityFinal = new RolEntity();
        rolEntityFinal.setNombre(request.getNombre());
        rolEntityFinal.setModuloEntity(moduloEntity);
        rolEntityFinal.setUsuarioRegistro(USUARIO_TEMPORAL);
        rolEntityFinal.setVigencia(true);
        rolEntityFinal.setFechaRegistro(LocalDateTime.now());
        rolRepository.save(rolEntityFinal);

        List<RolAccionEntity> rolAcciones = acciones.stream().map(a -> {
            RolAccionEntity rolAccionEntity = new RolAccionEntity();
            rolAccionEntity.setAccionEntity(a);
            rolAccionEntity.setRolEntity(rolEntityFinal);

            return rolAccionEntity;
        }).collect(Collectors.toList());

        rolAccionRepository.saveAll(rolAcciones);
    }


    @Transactional
    public void eliminarRol(Long rolId) {
        if (rolId == null || rolId <= 0) {
            throw new BadRequestException("Debe ingresar un id rol valido");
        }
        Optional<RolEntity> rolEntityOptional = rolRepository.findById(rolId);
        if (rolEntityOptional.isEmpty()) {
            throw new BadRequestException("No se encontro el rol a eliminar");
        }
        RolEntity rolEntity = rolEntityOptional.get();

        List<PerfilRolEntity> rolEnUso = perfilRolRepository.findAllByRolEntity(rolEntity);
        if (!rolEnUso.isEmpty()) {
            throw new BadRequestException("No se puede eliminar el rol seleccionado, se encuentra en uso");
        }
        rolEntity.setFechaModificacion(LocalDateTime.now());
        rolEntity.setUsuarioModificacion(USUARIO_TEMPORAL);
        rolEntity.setVigencia(false);
        rolRepository.save(rolEntity);
    }

    private void validarRequestRol(CrearRolRequestDTO request) {
        if (request == null) {
            log.error("Request con valores nulo");
            throw new BadRequestException();
        }

        if (request.getAcciones() == null || request.getAcciones().isEmpty()) {
            log.error("Debe asociar al menos 1 accion");
            throw new BadRequestException("Debe asociar al menos 1 accion");
        }

        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            log.error("Debe ingresar un nombre para el rol");
            throw new BadRequestException("Debe ingresar un nombre para el rol");
        }
    }

    @Transactional
    public void actualizarRol(ActualizarRolRequestDTO request) {

        validarRequestRol(request);

        if (request.getRolId() == null || request.getRolId() <= 0) {
            throw new BadRequestException("id rol invalido");
        }

        Long rolRepetido = rolRepository.validarRolMismoNombreDistintoId(request.getNombre(), request.getRolId());

        if (rolRepetido > 0) {
            throw new BadRequestException("Ya existe un rol con el nombre indicado");
        }

        Optional<RolEntity> rolEntityOptional = rolRepository.findById(request.getRolId());

        if (rolEntityOptional.isEmpty()) {
            throw new BadRequestException("No existe el rol seleccionado");
        }

        RolEntity rolEntity = rolEntityOptional.get();

        // Eliminar acciones antiguas
        List<RolAccionEntity> rolAcciones = rolAccionRepository.findAllByRolEntityRolId(request.getRolId());
        rolAccionRepository.deleteAll(rolAcciones);
        rolAccionRepository.flush();


        Optional<ModuloEntity> moduloEntityOptional = moduloRepository.findByModuloId(request.getModulo());

        if (moduloEntityOptional.isEmpty()) {
            throw new BadRequestException("Modulo invalido para asociar");
        }

        ModuloEntity moduloEntity = moduloEntityOptional.get();
        List<AccionEntity> acciones = accionRepository.findAllById(request.getAcciones());

        // Actualizar datos y nuevas acciones
        rolEntity.setNombre(request.getNombre());
        rolEntity.setModuloEntity(moduloEntity);
        rolEntity.setVigencia(request.isVigencia());
        rolEntity.setUsuarioModificacion(USUARIO_TEMPORAL);
        rolEntity.setFechaModificacion(LocalDateTime.now());
        rolRepository.save(rolEntity);


        rolAcciones = acciones.stream().map(a -> {
            RolAccionEntity rolAccionEntity = new RolAccionEntity();
            rolAccionEntity.setAccionEntity(a);
            rolAccionEntity.setRolEntity(rolEntity);

            return rolAccionEntity;
        }).collect(Collectors.toList());
        rolAccionRepository.saveAll(rolAcciones);

    }

    public RolDTO obtenerRol(Long rolId) {

        if (rolId == null || rolId <= 0) {
            throw new BadRequestException("id rol invalido");
        }

        Optional<RolEntity> rolEntityOptional = rolRepository.findById(rolId);

        if (rolEntityOptional.isEmpty()) {
            throw new NoContentException();
        }

        RolEntity rolEntity = rolEntityOptional.get();


        RolDTO rolDTO = new RolDTO();
        rolDTO.setRolId(rolEntity.getRolId());
        rolDTO.setNombre(rolEntity.getNombre());
        rolDTO.setModuloId(rolEntity.getModuloEntity().getModuloId());
        rolDTO.setVigencia(rolEntity.isVigencia());


        List<RolAccionEntity> allByRolRolId = rolAccionRepository.findAllByRolEntityRolId(rolEntity.getRolId());
        rolDTO.setAcciones(allByRolRolId.stream().map(a -> a.getAccionEntity().getAccionId()).collect(Collectors.toList()));

        return rolDTO;

    }

    public PaginacionPerfilDTO listarPerfiles(int numPagina, String filtro) {

        if (numPagina < 0) {
            throw new BadRequestException(NUM_PAGINA_INVALIDA);
        }

        Pageable pageable = PageRequest.of(numPagina, paginacion, Sort.by(ORDEN_FECHA_REGISTRO));

        Page<PerfilEntity> all = null;

        if (filtro == null || filtro.trim().isEmpty()) {
            all = perfilRepository.findAll(pageable);
        } else {
            filtro = filtro.toLowerCase(Locale.ROOT);
            all = perfilRepository.listarPerfiles(filtro, pageable);
        }

        PaginacionPerfilDTO paginacionPerfilDTO = new PaginacionPerfilDTO();
        paginacionPerfilDTO.setTotalElementos(all.getTotalElements());
        paginacionPerfilDTO.setTotalPagina(all.getTotalPages());
        paginacionPerfilDTO.setPagina(all.getNumber());


        List<ListarPerfilDTO> perfiles = all.stream().map(p -> {

            ListarPerfilDTO perfilDTO = new ListarPerfilDTO();
            perfilDTO.setPerfilId(p.getPerfilId());
            perfilDTO.setNombre(p.getNombre());
            perfilDTO.setVigencia(p.isVigencia());

            return perfilDTO;

        }).collect(Collectors.toList());

        paginacionPerfilDTO.setPerfiles(perfiles);

        return paginacionPerfilDTO;
    }

    private void validarRequestPerfil(CrearPerfilDTO request) {
        if (request == null) {
            log.error("request con valores nulos");
            throw new BadRequestException();
        }

        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            log.error("Debe asociar al menos 1 rol");
            throw new BadRequestException("Debe asociar al menos 1 rol");
        }

        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            log.error("Debe ingresar un nombre para el perfil");
            throw new BadRequestException("Debe ingresar un nombre para el perfil");
        }
    }


    @Transactional
    public void crearPerfil(CrearPerfilDTO request) {

        validarRequestPerfil(request);

        if (perfilRepository.existePerfilConMismoNombre(request.getNombre())) {
            throw new BadRequestException("El perfil " + request.getNombre() + " ya se encuentra creado");
        }

        log.debug("Crear perfil: {}", request);

        PerfilEntity perfilEntity = new PerfilEntity();
        perfilEntity.setNombre(request.getNombre());
        perfilEntity.setFechaRegistro(LocalDateTime.now());
        perfilEntity.setUsuarioRegistro(USUARIO_TEMPORAL);
        perfilEntity.setVigencia(true);

        perfilRepository.save(perfilEntity);

        log.debug("Roles: {}", request.getRoles());

        List<RolEntity> roles = request.getRoles().stream().map(r -> {
            Optional<RolEntity> byId = rolRepository.findById(r);
            return byId.get();
        }).collect(Collectors.toList());

        List<PerfilRolEntity> perfileRoles = roles.stream().map(r -> {
            PerfilRolEntity perfilRolEntity = new PerfilRolEntity();
            perfilRolEntity.setPerfilEntity(perfilEntity);
            perfilRolEntity.setRolEntity(r);

            return perfilRolEntity;
        }).collect(Collectors.toList());

        perfilRolRepository.saveAll(perfileRoles);
    }

    @Transactional
    public void modificarPerfil(ModificaPerfilDTO request) {

        validarRequestPerfil(request);

        if (request.getPerfilId() == null || request.getPerfilId() <= 0) {
            throw new BadRequestException(ID_PERFIL_INVALIDO);
        }


        if (perfilRepository.existePerfilMismoNombreDistintoPerfil(
                request.getNombre(), request.getPerfilId())) {
            throw new BadRequestException("El perfil " + request.getNombre() + " ya se encuentra creado");
        }

        Optional<PerfilEntity> perfilEntityOptional = perfilRepository.findById(request.getPerfilId());

        if (perfilEntityOptional.isEmpty()) {
            throw new BadRequestException();
        }

        PerfilEntity perfilEntity = perfilEntityOptional.get();
        perfilEntity.setNombre(request.getNombre());
        perfilEntity.setUsuarioModificacion(USUARIO_TEMPORAL);
        perfilEntity.setFechaModificacion(LocalDateTime.now());
        perfilEntity.setVigencia(request.isVigencia());
        perfilRepository.save(perfilEntity);

        List<PerfilRolEntity> perfilesRoles = perfilRolRepository
                .findAllByPerfilEntityPerfilId(request.getPerfilId());
        perfilRolRepository.deleteAll(perfilesRoles);
        perfilRolRepository.flush();

        List<RolEntity> roles = rolRepository.findAllById(request.getRoles());

        List<PerfilRolEntity> perfileRoles = roles.stream().map(r -> {
            PerfilRolEntity perfilRolEntity = new PerfilRolEntity();
            perfilRolEntity.setPerfilEntity(perfilEntity);
            perfilRolEntity.setRolEntity(r);

            return perfilRolEntity;
        }).collect(Collectors.toList());

        perfilRolRepository.saveAll(perfileRoles);
    }

    @Transactional
    public void eliminarPerfil(Long perfilId) {

        if (perfilId == null || perfilId <= 0) {
            throw new BadRequestException(ID_PERFIL_INVALIDO);
        }

        Optional<PerfilEntity> perfilEntityOptional = perfilRepository.findById(perfilId);

        if (perfilEntityOptional.isEmpty()) {
            throw new BadRequestException("No existe el perfil indicado");
        }
        PerfilEntity perfilEntity = perfilEntityOptional.get();

        List<PerfilFuncionarioEntity> perfilEnUso = perfilFuncionarioRepository.findAllByPerfilEntityPerfilId(perfilId);
        if (!perfilEnUso.isEmpty()) {
            throw new BadRequestException("No se puede eliminar el perfil seleccionado, se encuentra en uso");
        }

        perfilEntity.setVigencia(false);
        perfilEntity.setFechaModificacion(LocalDateTime.now());
        perfilEntity.setUsuarioModificacion(USUARIO_TEMPORAL);

        perfilRepository.save(perfilEntity);
    }

    public PerfilDTO obtenerPerfil(Long perfilId) {
        if (perfilId == null || perfilId <= 0) {
            throw new BadRequestException(ID_PERFIL_INVALIDO);
        }

        Optional<PerfilEntity> perfilEntityOptional = perfilRepository.findById(perfilId);

        if (perfilEntityOptional.isEmpty()) {
            throw new NoContentException();
        }

        PerfilEntity perfilEntity = perfilEntityOptional.get();


        PerfilDTO perfilDTO = new PerfilDTO();
        perfilDTO.setPerfilId(perfilEntity.getPerfilId());
        perfilDTO.setNombre(perfilEntity.getNombre());
        perfilDTO.setVigencia(perfilEntity.isVigencia());


        List<PerfilRolEntity> allByPerfilId = perfilRolRepository.findAllByPerfilEntityPerfilId(perfilEntity.getPerfilId());
        perfilDTO.setRoles(allByPerfilId.stream().map(a -> a.getRolEntity().getRolId()).collect(Collectors.toList()));

        return perfilDTO;
    }

    public PaginacionPerfilResponseDTO listarPerfilesVigentesPorFiltro(int numPagina, String filtro) {

        if (numPagina < 0) {
            throw new BadRequestException("Numero de página no permitida");
        }

        Pageable pageable = PageRequest.of(numPagina, paginacion, Sort.by("nombre"));

        Page<PerfilEntity> all = null;

        if (filtro == null || filtro.trim().isEmpty()) {
            all = perfilRepository.listarPerfilesVigentes(pageable);
        } else {
            filtro = filtro.toLowerCase(Locale.ROOT);
            all = perfilRepository.listarPerfilesVigentesPorFiltro(filtro, pageable);
        }

        PaginacionPerfilResponseDTO paginacionPerfilResponseDTO = new PaginacionPerfilResponseDTO();
        paginacionPerfilResponseDTO.setTotalElementos(all.getTotalElements());
        paginacionPerfilResponseDTO.setTotalPagina(all.getTotalPages());
        paginacionPerfilResponseDTO.setPagina(all.getNumber());

        List<PerfilResponseDTO> perfiles = all.stream().map(p -> {
            PerfilResponseDTO perfilResponseDTO = new PerfilResponseDTO();
            perfilResponseDTO.setPerfilId(p.getPerfilId());
            perfilResponseDTO.setNombre(p.getNombre());
            return perfilResponseDTO;
        }).collect(Collectors.toList());

        paginacionPerfilResponseDTO.setPerfiles(perfiles);

        return paginacionPerfilResponseDTO;

    }

    public List<PerfilResponseDTO> listarPerfilesPorFuncionario(String rutFuncionario) {

        funcionariosService.validarRut(rutFuncionario);
        String rutFormateado = validaciones.formateaRutHaciaBD(rutFuncionario);

        List<PerfilFuncionarioEntity> lstPerfilesFuncionario = perfilFuncionarioRepository.findAllByFuncionariosEntityRut(rutFormateado);

        List<PerfilResponseDTO> perfiles = new ArrayList<>();

        if (!lstPerfilesFuncionario.isEmpty()) {
            perfiles = lstPerfilesFuncionario.stream().map(p -> {
                PerfilResponseDTO perfilResponseDTO = new PerfilResponseDTO();
                perfilResponseDTO.setPerfilId(p.getPerfilEntity().getPerfilId());
                perfilResponseDTO.setNombre(p.getPerfilEntity().getNombre());
                return perfilResponseDTO;
            }).collect(Collectors.toList());
        }

        return perfiles;

    }

    @Transactional
    public void crearPerfilesFuncionario(CrearPerfilFuncionarioRequestDTO request) {
        if (request == null) {
            throw new BadRequestException();
        }
        log.debug("Request: {}", request.toString());

        if (request.getFuncionario() == null) {
            throw new BadRequestException("No existen datos del funcionario");
        }

        if (request.getPerfiles() == null || request.getPerfiles().isEmpty()) {
            throw new BadRequestException("No existen perfiles a asignar al usuario");
        }

        List<PerfilEntity> perfiles = perfilRepository.findAllById(request.getPerfiles());
        if (perfiles.size() != request.getPerfiles().size()) {
            throw new BadRequestException("Los perfiles seleccionados no son válidos");
        }

        FuncionarioRequestDTO funcionarioRequestDTO = request.getFuncionario();

        funcionariosService.validarRut(funcionarioRequestDTO.getRut());
        String rutFormateado = validaciones.formateaRutHaciaBD(funcionarioRequestDTO.getRut());


        List<PerfilFuncionarioEntity> lstPerfilesFuncionario = perfilFuncionarioRepository.findAllByFuncionariosEntityRut(rutFormateado);
        if (!lstPerfilesFuncionario.isEmpty()) {
            throw new BadRequestException("El funcionario ya tiene perfil/es asignado/s");
        }


        Optional<FuncionariosEntity> funcionariosEntityOptional = funcionariosRepository.findByRut(rutFormateado);
        log.debug("funcionariosEntityOptional: {}", funcionariosEntityOptional.isPresent());

        FuncionariosEntity funcionariosEntity = null;
        if (funcionariosEntityOptional.isEmpty()) {
            funcionariosEntity = funcionariosService.crearFuncionario(funcionarioRequestDTO);
        } else {
            funcionariosEntity = funcionariosEntityOptional.get();
        }

        FuncionariosEntity finalFuncionariosEntity = funcionariosEntity;
        List<PerfilFuncionarioEntity> perfilFuncionarioEntities = perfiles.stream().map(perfilEntity -> {
            PerfilFuncionarioEntity perfilFuncionarioEntity = new PerfilFuncionarioEntity();
            perfilFuncionarioEntity.setFuncionariosEntity(finalFuncionariosEntity);
            perfilFuncionarioEntity.setPerfilEntity(perfilEntity);

            return perfilFuncionarioEntity;
        }).collect(Collectors.toList());

        perfilFuncionarioRepository.saveAll(perfilFuncionarioEntities);
    }

    @Transactional
    public void actualizarPerfilesFuncionario(ActualizarPerfilFuncionarioRequestDTO request) {
        funcionariosService.validarRut(request.getRutFuncionario());
        String rutFormateado = validaciones.formateaRutHaciaBD(request.getRutFuncionario());

        Optional<FuncionariosEntity> funcionariosEntityOptional = funcionariosRepository.findByRut(rutFormateado);

        if (funcionariosEntityOptional.isEmpty()) {
            throw new BadRequestException("No existe el funcionario seleccionado");
        }

        List<PerfilEntity> perfiles = null;
        if (request.getPerfiles() != null && !request.getPerfiles().isEmpty()) {
            perfiles = perfilRepository.findAllById(request.getPerfiles());
            if (perfiles.size() != request.getPerfiles().size()) {
                throw new BadRequestException("Los perfiles seleccionados no son válidos");
            }
        }

        FuncionariosEntity funcionarioEntity = funcionariosEntityOptional.get();

        // Eliminar perfiles antiguos
        List<PerfilFuncionarioEntity> perfilesFuncionario = perfilFuncionarioRepository.findAllByFuncionariosEntityRut(rutFormateado);
        perfilFuncionarioRepository.deleteAll(perfilesFuncionario);
        perfilFuncionarioRepository.flush();

        if (perfiles != null) {
            perfilesFuncionario = perfiles.stream().map(perfilEntity -> {
                PerfilFuncionarioEntity perfilFuncionarioEntity = new PerfilFuncionarioEntity();
                perfilFuncionarioEntity.setFuncionariosEntity(funcionarioEntity);
                perfilFuncionarioEntity.setPerfilEntity(perfilEntity);

                return perfilFuncionarioEntity;
            }).collect(Collectors.toList());
            perfilFuncionarioRepository.saveAll(perfilesFuncionario);
        }

    }

    public PaginacionRolDTO listarRolesVigentes(int numPagina, String filtro) {
        if (numPagina < 0) {
            throw new BadRequestException(NUM_PAGINA_INVALIDA);
        }

        Pageable pageable = PageRequest.of(numPagina, paginacion, Sort.by(ORDEN_FECHA_REGISTRO));

        Page<RolEntity> all = null;

        if (filtro == null || filtro.trim().isEmpty()) {
            all = rolRepository.findAllByVigenciaIsTrue(pageable);
        } else {
            filtro = filtro.toLowerCase(Locale.ROOT);
            all = rolRepository.listarRolesVigentes(filtro, pageable);
        }

        return mapearResultadoRoles(all);
    }
}
