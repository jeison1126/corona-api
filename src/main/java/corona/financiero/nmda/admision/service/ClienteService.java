package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.adapter.CYCAdapter;
import corona.financiero.nmda.admision.dto.ClienteActivoDatosResponseDTO;
import corona.financiero.nmda.admision.dto.ClienteActivoRequestDTO;
import corona.financiero.nmda.admision.dto.RegistroClienteRequestDTO;
import corona.financiero.nmda.admision.dto.cyc.*;
import corona.financiero.nmda.admision.entity.*;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.*;
import corona.financiero.nmda.admision.util.Constantes;
import corona.financiero.nmda.admision.util.Validaciones;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
public class ClienteService {
    @Autowired
    private ParProfesionRepository parProfesionRepository;

    @Autowired
    private ParEstadoCivilRepository parEstadoCivilRepository;

    @Autowired
    private ParNacionalidadRepository parNacionalidadRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private Validaciones validaciones;

    @Autowired
    private ParActividadRepository parActividadRepository;

    @Autowired
    private TarjetaActivacionRepository tarjetaActivacionRepository;

    @Autowired
    private AdmisionFaseService admisionFaseService;

    @Autowired
    private SolicitudAdmisionRepository solicitudAdmisionRepository;

    private static final String USUARIO_TEMPORAL = "USR_TMP";

    @Autowired
    private CYCAdapter cycAdapter;

    private static final String CODIGO_PAIS = "56";

    @Autowired
    private RegionComunaService regionComunaService;

    @Autowired
    private TarjetaRepository tarjetaRepository;


    private void validacionesFormulario(RegistroClienteRequestDTO request) {

        if (request == null) {
            throw new BadRequestException();
        }

        validaciones.validacionGeneralRut(request.getRut());

        if (request.getNacionalidad() <= 0) {
            throw new BadRequestException("Debe indicar una nacionalidad valida");
        }

        if (request.getEstadoCivil() <= 0) {
            throw new BadRequestException("Debe indicar un estado civil valido");
        }

        if (request.getProfesion() <= 0) {
            throw new BadRequestException("Debe indicar una profesion valida");
        }

        if (request.getActividad() <= 0) {
            throw new BadRequestException("Debe indicar una actividad valida");
        }

        if (request.getCodigoRegion() == null || request.getCodigoRegion().trim().isEmpty()) {
            throw new BadRequestException("Debes indicar una region valida");
        }

        if (request.getCodigoComuna() == null || request.getCodigoComuna().trim().isEmpty()) {
            throw new BadRequestException("Debes indicar una comuna valida");
        }

        if (request.getCalle() == null || request.getCalle().trim().isEmpty()) {
            throw new BadRequestException("Debes indicar una calle valida");
        }

        if (request.getNumero() == null || request.getNumero().trim().isEmpty()) {
            throw new BadRequestException("Debes indicar una numeracion valida");
        }


    }

    public void registroDatosCliente(RegistroClienteRequestDTO request) {
        validacionesFormulario(request);

        String rutFormateado = validaciones.formateaRutHaciaBD(request.getRut());

        Optional<ParProfesionEntity> profesionEntityOptional = parProfesionRepository.findById(request.getProfesion());
        if (profesionEntityOptional.isEmpty()) {
            throw new BadRequestException("Profesion invalida");
        }

        ParProfesionEntity parProfesionEntity = profesionEntityOptional.get();

        Optional<ParNacionalidadEntity> nacionalidadEntityOptional = parNacionalidadRepository.findById(request.getNacionalidad());
        if (nacionalidadEntityOptional.isEmpty()) {
            throw new BadRequestException("Nacionalidad invalida");
        }
        ParNacionalidadEntity parNacionalidadEntity = nacionalidadEntityOptional.get();

        Optional<ParEstadoCivilEntity> estadoCivilEntityOptional = parEstadoCivilRepository.findById(request.getEstadoCivil());

        if (estadoCivilEntityOptional.isEmpty()) {
            throw new BadRequestException("Estado civil invalido");
        }
        ParEstadoCivilEntity parEstadoCivilEntity = estadoCivilEntityOptional.get();


        Optional<ParActividadEntity> actividadEntityOptional = parActividadRepository.findById(request.getActividad());
        if (actividadEntityOptional.isEmpty()) {
            throw new BadRequestException("Actividad invalida");
        }

        ParActividadEntity parActividadEntity = actividadEntityOptional.get();


        Optional<ClienteEntity> clienteEntityOptional = clienteRepository.findById(rutFormateado);


        if (clienteEntityOptional.isEmpty()) {
            throw new BadRequestException("No existe cliente");
        }
        ClienteEntity clienteEntity = clienteEntityOptional.get();

        LocalDate fechaNacimiento = clienteEntity.getProspectoEntity().getFechaNacimiento();
        clienteEntity.setFechaNacimiento(fechaNacimiento);

        clienteEntity.setParActividadEntity(parActividadEntity);
        clienteEntity.setParProfesionEntity(parProfesionEntity);
        clienteEntity.setParNacionalidadEntity(parNacionalidadEntity);
        clienteEntity.setParEstadoCivilEntity(parEstadoCivilEntity);

        clienteEntity.setCodigoComuna(request.getCodigoComuna());
        clienteEntity.setCodigoRegion(request.getCodigoRegion());
        clienteEntity.setCalle(request.getCalle());
        clienteEntity.setNumero(request.getNumero());
        clienteEntity.setDepartamento(request.getDepartamento());
        clienteEntity.setReferencia(request.getReferencias());

        if (request.getLatitud() != null)
            clienteEntity.setLatitud(request.getLatitud());

        if (request.getLongitud() != null)
            clienteEntity.setLongitud(request.getLongitud());

        if (request.getTelefonoAdicional() != null)
            clienteEntity.setTelefonoAdicional(Integer.parseInt(request.getTelefonoAdicional()));

        if (request.getDepartamento() != null)
            clienteEntity.setDepartamento(request.getDepartamento());

        clienteEntity.setReferencia(request.getReferencias());


        clienteEntity.setAutorizaSms(request.isEnvioSMS());
        clienteEntity.setEnvioEccEmail(request.isEnvioEcc());
        clienteEntity.setUsuarioModificacion(USUARIO_TEMPORAL);
        clienteEntity.setFechaModificacion(LocalDateTime.now());


        //registrar cliente en cyc
        CYCCrearCuentaResponseDTO cycCrearCuentaResponseDTO = crearCuentaCyc(request, clienteEntity);

        if (cycCrearCuentaResponseDTO.getCodigo().equals("00") && cycCrearCuentaResponseDTO.getDescripcion().equals("OK")) {
            clienteRepository.save(clienteEntity);
            SolicitudAdmisionEntity solicitudAdmisionEntity = solicitudAdmisionRepository.findByProspectoEntityRutAndVigenciaIsTrue(rutFormateado);
            admisionFaseService.actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_DATOS_CLIENTE);
        }

    }

    private CYCCrearCuentaResponseDTO crearCuentaCyc(RegistroClienteRequestDTO request, ClienteEntity clienteEntity) {

        log.debug("request adapter: {}",request);
        log.debug("Cliente adapter: {}",clienteEntity);

        CYCDatosCreacionCuentaDTO creacion = new CYCDatosCreacionCuentaDTO();
        //rut ejecutivo
        creacion.setRutUsuario("111111111");

        creacion.setTipoCliente("TI");
        creacion.setNumeroOperacion(String.valueOf(Instant.now().getEpochSecond()));
        creacion.setNumeroTarjeta(7 + "" + clienteEntity.getRut());

        LocalDateTime fechaActual = LocalDateTime.now();
        creacion.setFecha(fechaActual.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        creacion.setHora(fechaActual.format(DateTimeFormatter.ofPattern("HHmmss")));

        Optional<TarjetaEntity> tarjetaEntityOptional = tarjetaRepository.findByClienteEntityAndVigenciaIsTrue(clienteEntity);
        TarjetaEntity tarjetaEntity = tarjetaEntityOptional.get();

        creacion.setDiaPago(String.valueOf(tarjetaEntity.getDiaPago()));
        creacion.setCupoAsignado(String.valueOf(tarjetaEntity.getCupoAprobado()));

        //por validar como obtener codigo estado de cuenta
        creacion.setCodigoEstadoCuenta("1");

        CYCDatosPersonalesDTO datosPersonales = new CYCDatosPersonalesDTO();

        //renta por validar, hay flujos sin este datos (campana corona y flujo normal, con cupo de cortesia
        datosPersonales.setRenta("1200000");
        datosPersonales.setEmail(clienteEntity.getEmail());
        datosPersonales.setRutCliente(clienteEntity.getRut());
        datosPersonales.setApellidoPaterno(clienteEntity.getApellidoPaterno());
        datosPersonales.setApellidoMaterno(clienteEntity.getApellidoMaterno());
        datosPersonales.setPrimerNombre(clienteEntity.getNombre());
        //validar segundo nombre
        datosPersonales.setSegundoNombre("");
        //validar dato sexo, para completar desde formulario cliente
        datosPersonales.setSexo("M");

        if (clienteEntity.getFechaNacimiento() != null) {
            LocalDate fechaNacimiento = clienteEntity.getFechaNacimiento();
            datosPersonales.setFechaNacimiento(fechaNacimiento.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        }

        datosPersonales.setCondicionLaboral(clienteEntity.getParActividadEntity().getDescripcion());

        datosPersonales.setNumeroTelefono(String.valueOf(clienteEntity.getMovil()));

        //codigo nacionalidad es 56, pero si es extranjero, que valor se le asigna?
        datosPersonales.setNacionalidad(CODIGO_PAIS);
        //dato estado civil, es la descripcion?
        datosPersonales.setEstadoCivil(clienteEntity.getParEstadoCivilEntity().getDescripcion());
        datosPersonales.setProfesion(clienteEntity.getParProfesionEntity().getDescripcion());

        CYCDatosDemograficosDTO datosDemograficos = new CYCDatosDemograficosDTO();
        datosDemograficos.setCalle(clienteEntity.getCalle());
        datosDemograficos.setNumero(clienteEntity.getNumero());

        datosDemograficos.setRegion(request.getCodigoRegion());
        datosDemograficos.setComuna(request.getCodigoComuna());
        datosDemograficos.setCodigoPais(CODIGO_PAIS);

        datosDemograficos.setComentarioAdicional((request.getReferencias() != null && !request.getReferencias().isEmpty()) ? request.getReferencias() : "");
        datosDemograficos.setCasilla("");
        datosDemograficos.setCiudad("");


        log.debug("Antes de enviar a crear cuetna CYC");
        creacion.setDatosPersonales(datosPersonales);
        creacion.setDatosDemograficos(datosDemograficos);
        CYCCrearCuentaRequestDTO cycCrearCuentaRequestDTO = new CYCCrearCuentaRequestDTO();
        cycCrearCuentaRequestDTO.setDatosCreacionCuenta(creacion);

        log.debug("cycCrearCuentaRequestDTO: {}",cycCrearCuentaRequestDTO.toString());

        CYCCrearCuentaResponseDTO cycCrearCuentaResponseDTO = cycAdapter.crearCuentaCYC(cycCrearCuentaRequestDTO);

        log.debug("response: {}", cycCrearCuentaResponseDTO);

        return cycCrearCuentaResponseDTO;
    }


    public ClienteActivoDatosResponseDTO validarClienteTarjetaActiva(ClienteActivoRequestDTO request) {

        validaciones.validacionGeneralRut(request.getRut());

        String rutFormateado = validaciones.formateaRutHaciaBD(request.getRut());

        Optional<TarjetaActivacionEntity> tarjetaActivacionEntityOptional = tarjetaActivacionRepository.validarClienteTarjetaActiva(rutFormateado);

        if (tarjetaActivacionEntityOptional.isEmpty()) {
            log.debug("No se encontraron tarjetas asociadas");
            throw new NoContentException();
        }
        TarjetaActivacionEntity tarjetaActivacionEntity = tarjetaActivacionEntityOptional.get();
        TarjetaEntity tarjetaEntity = tarjetaActivacionEntity.getTarjetaEntity();
        ClienteEntity clienteEntity = tarjetaEntity.getClienteEntity();

        ClienteActivoDatosResponseDTO clienteActivoDatosResponseDTO = new ClienteActivoDatosResponseDTO();

        clienteActivoDatosResponseDTO.setRut(validaciones.formateaRutHaciaFront(clienteEntity.getRut()));
        clienteActivoDatosResponseDTO.setNombre(clienteEntity.getNombre());
        clienteActivoDatosResponseDTO.setApellidoPaterno(clienteEntity.getApellidoPaterno());
        clienteActivoDatosResponseDTO.setApellidoMaterno(clienteEntity.getApellidoMaterno());
        clienteActivoDatosResponseDTO.setEstadoTarjeta("Activa");

        ParTipoProductoEntity parTipoProductoEntity = tarjetaEntity.getEvaluacionProductoEntity().getParTipoProductoEntity();
        clienteActivoDatosResponseDTO.setDescripcionTarjeta(parTipoProductoEntity.getDescripcion());
        clienteActivoDatosResponseDTO.setNumTarjeta(String.valueOf(tarjetaEntity.getNumeroTarjeta()));


        return clienteActivoDatosResponseDTO;
    }
}
