package corona.financiero.nmda.admision.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import corona.financiero.nmda.admision.dto.*;
import corona.financiero.nmda.admision.service.PerfilamientoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PerfilamientoController.class)
class PerfilamientoControllerTest {

    @MockBean
    private PerfilamientoService perfilamientoService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarAcciones_Status200() throws Exception {

        var accion = new AccionDTO();
        accion.setAccionId(1l);
        accion.setNombre("CREAR");

        List<AccionDTO> accionDTOS = Arrays.asList(accion);
        when(perfilamientoService.listarAcciones()).thenReturn(accionDTOS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/perfilamiento/accion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void listarModulos_Status200() throws Exception {

        var modulos = new ModuloDTO();
        modulos.setModuloId(1l);
        modulos.setNombre("Solicitud Admision");

        List<ModuloDTO> moduloDTOS = Arrays.asList(modulos);

        when(perfilamientoService.listarModulos()).thenReturn(moduloDTOS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/perfilamiento/modulo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void listarRoles_Status200() throws Exception {
        List<Long> acciones = new ArrayList<>();
        acciones.add(1l);
        acciones.add(2l);
        var response = new ListarRolDTO();
        response.setRolId(1);
        response.setNombre("Rol 1");
        response.setVigencia(true);


        var paginacion = new PaginacionRolDTO();
        paginacion.setRoles(Arrays.asList(response));
        paginacion.setPagina(0);
        paginacion.setTotalElementos(1);
        paginacion.setTotalPagina(1);

        when(perfilamientoService.listarRoles(anyInt(), anyString())).thenReturn(paginacion);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/perfilamiento/rol?pagina=0&filtro=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void listarRolesVigentes_Status200() throws Exception {
        List<Long> acciones = new ArrayList<>();
        acciones.add(1l);
        acciones.add(2l);
        var response = new ListarRolDTO();
        response.setRolId(1);
        response.setNombre("Rol 1");
        response.setVigencia(true);


        var paginacion = new PaginacionRolDTO();
        paginacion.setRoles(Arrays.asList(response));
        paginacion.setPagina(0);
        paginacion.setTotalElementos(1);
        paginacion.setTotalPagina(1);

        when(perfilamientoService.listarRolesVigentes(anyInt(), anyString())).thenReturn(paginacion);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/perfilamiento/rol/vigente?pagina=0&filtro=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    void crearRol_Status200() throws Exception {
        List<Long> acciones = new ArrayList<>();
        acciones.add(1l);
        acciones.add(2l);
        var request = new CrearRolRequestDTO();
        request.setNombre("Rol 1");
        request.setModulo(1l);
        request.setAcciones(acciones);

        doNothing().when(perfilamientoService).crearRol(request);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/perfilamiento/rol")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void actualizarRol_Status200() throws Exception {
        List<Long> acciones = new ArrayList<>();
        acciones.add(1l);

        var request = new ActualizarRolRequestDTO();

        request.setRolId(1l);
        request.setNombre("Actualiacion nombre rol");
        request.setAcciones(acciones);
        request.setModulo(1l);
        request.setVigencia(true);

        doNothing().when(perfilamientoService).actualizarRol(request);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/perfilamiento/rol")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void listarPerfiles_Status200() throws Exception {
        List<Long> roles = new ArrayList<>();
        roles.add(1l);
        roles.add(2l);


        var perfil = new ListarPerfilDTO();
        perfil.setPerfilId(1l);
        perfil.setVigencia(true);
        perfil.setNombre("Administrador");

        var paginacion = new PaginacionPerfilDTO();
        paginacion.setPerfiles(Arrays.asList(perfil));
        paginacion.setPagina(0);
        paginacion.setTotalElementos(1);
        paginacion.setTotalPagina(1);

        when(perfilamientoService.listarPerfiles(anyInt(), anyString())).thenReturn(paginacion);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/perfilamiento/perfil?pagina=0&filtro=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void crearPerfl_Status200() throws Exception {
        List<Long> roles = new ArrayList<>();
        roles.add(1l);
        roles.add(2l);
        var request = new CrearPerfilDTO();
        request.setNombre("Rol 1");
        request.setRoles(roles);

        doNothing().when(perfilamientoService).crearPerfil(request);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/perfilamiento/perfil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void actualizarPerfil_Status200() throws Exception {
        List<Long> roles = new ArrayList<>();
        roles.add(1l);

        var request = new ModificaPerfilDTO();

        request.setPerfilId(1l);
        request.setRoles(roles);
        request.setNombre("Actualiacion nombre perfil");
        request.setVigencia(true);

        doNothing().when(perfilamientoService).modificarPerfil(request);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/perfilamiento/perfil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void eliminarRol_Status200() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/perfilamiento/rol/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void obtenerRol_Status200() throws Exception {

        var rol = new RolDTO();
        rol.setRolId(1l);
        rol.setVigencia(true);
        rol.setNombre("Rol Admin");

        when(perfilamientoService.obtenerRol(any())).thenReturn(rol);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/perfilamiento/rol/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPerfil_Status200() throws Exception {

        var perfil = new PerfilDTO();
        perfil.setPerfilId(1l);
        perfil.setNombre("Administrador Full");
        perfil.setVigencia(true);

        when(perfilamientoService.obtenerPerfil(any())).thenReturn(perfil);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/perfilamiento/perfil/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void eliminaPerfil_Status200() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/perfilamiento/perfil/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
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
      
    @Test
    void listarPerfilesVigentesPorFiltro_Status200() throws Exception {
        var response = new PerfilResponseDTO();
        response.setNombre("Perfil 1");
        response.setPerfilId(1);

        var paginacion = new PaginacionPerfilResponseDTO();
        paginacion.setPagina(0);
        paginacion.setTotalElementos(1);
        paginacion.setTotalPagina(1);
        paginacion.setPerfiles(Arrays.asList(response));

        when(perfilamientoService.listarPerfilesVigentesPorFiltro(anyInt(), anyString())).thenReturn(paginacion);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/perfilamiento/perfil/vigente?pagina=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
    
    @Test
    void listarPerfilesPorFuncionario_Status200() throws Exception {
    	var response = new PerfilResponseDTO();
        response.setNombre("Perfil 1");
        response.setPerfilId(1);
        
    	List<PerfilResponseDTO> lstPerfilesFuncionario = Arrays.asList(response);

    	String rut = "11111111-1";
    	
        when(perfilamientoService.listarPerfilesPorFuncionario(anyString())).thenReturn(lstPerfilesFuncionario);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/perfilamiento/perfil/funcionario/"+rut)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
    
    @Test
    void crearPerfilesFuncionario_Status200() throws Exception {
    	
    	var funcionario = new FuncionarioRequestDTO();
    	funcionario.setRut("11111111-1");
    	funcionario.setNombreCompleto("Soto");
        
        var request = new CrearPerfilFuncionarioRequestDTO();
        request.setFuncionario(funcionario);
        request.setPerfiles(Arrays.asList(Long.valueOf(1)));

        doNothing().when(perfilamientoService).crearPerfilesFuncionario(request);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/perfilamiento/perfilFuncionario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
    
    @Test
    void actualizarPerfilesFuncionario_Status200() throws Exception {

        var request = new ActualizarPerfilFuncionarioRequestDTO();
        request.setRutFuncionario("11111111-1");
        request.setPerfiles(Arrays.asList(Long.valueOf(1)));

        doNothing().when(perfilamientoService).actualizarPerfilesFuncionario(request);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/perfilamiento/perfilFuncionario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
