package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.*;
import corona.financiero.nmda.admision.service.PerfilamientoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/perfilamiento")
@Tag(name = "Perfilamiento", description = "APIs para Perfilamiento")
public class PerfilamientoController {

    @Autowired
    private PerfilamientoService perfilamientoService;

    @GetMapping("/accion")
    public ResponseEntity<List<AccionDTO>> listarAcciones() {
        return new ResponseEntity<>(perfilamientoService.listarAcciones(), HttpStatus.OK);
    }

    @GetMapping("/rol")
    public ResponseEntity<PaginacionRolDTO> listarRoles(@RequestParam(name = "pagina", required = false, defaultValue = "0") int pagina,
                                                        @RequestParam(name = "filtro", required = false, defaultValue = "") String filtro) {
        return new ResponseEntity<>(perfilamientoService.listarRoles(pagina, filtro), HttpStatus.OK);
    }

    @GetMapping("/rol/vigente")
    public ResponseEntity<PaginacionRolDTO> listarRolesVigentes(@RequestParam(name = "pagina", required = false, defaultValue = "0") int pagina,
                                                        @RequestParam(name = "filtro", required = false, defaultValue = "") String filtro) {
        return new ResponseEntity<>(perfilamientoService.listarRolesVigentes(pagina, filtro), HttpStatus.OK);
    }

    @GetMapping("/rol/{rolId}")
    public ResponseEntity<RolDTO> obtenerRol(@PathVariable Long rolId) {
        return new ResponseEntity<>(perfilamientoService.obtenerRol(rolId), HttpStatus.OK);
    }

    @PostMapping("/rol")
    public ResponseEntity<Void> crearRol(@RequestBody @Valid CrearRolRequestDTO request) {
        perfilamientoService.crearRol(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/rol")
    public ResponseEntity<Void> actualizarRol(@RequestBody @Valid ActualizarRolRequestDTO request) {
        perfilamientoService.actualizarRol(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/modulo")
    public ResponseEntity<List<ModuloDTO>> listarModulos() {
        return new ResponseEntity<>(perfilamientoService.listarModulos(), HttpStatus.OK);
    }

    @DeleteMapping("/rol/{rolId}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Long rolId) {

        perfilamientoService.eliminarRol(rolId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/perfil")
    public ResponseEntity<PaginacionPerfilDTO> listarPerfiles(@RequestParam(name = "pagina", required = false, defaultValue = "0") int pagina,
                                                              @RequestParam(name = "filtro", required = false, defaultValue = "") String filtro) {
        return new ResponseEntity<>(perfilamientoService.listarPerfiles(pagina, filtro), HttpStatus.OK);
    }

    @PostMapping("/perfil")
    public ResponseEntity<Void> crearPerfil(@RequestBody @Valid CrearPerfilDTO request) {
        perfilamientoService.crearPerfil(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/perfil")
    public ResponseEntity<Void> modificarPerfil(@RequestBody @Valid ModificaPerfilDTO request) {
        perfilamientoService.modificarPerfil(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/perfil/{perfilId}")
    public ResponseEntity<Void> eliminarPerfil(@PathVariable Long perfilId) {
        perfilamientoService.eliminarPerfil(perfilId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/perfil/{perfilId}")
    public ResponseEntity<PerfilDTO> obtenerPerfil(@PathVariable Long perfilId) {
        return new ResponseEntity<>(perfilamientoService.obtenerPerfil(perfilId), HttpStatus.OK);
    }
    
    @GetMapping("/perfil/vigente")
    public ResponseEntity<PaginacionPerfilResponseDTO> listarPerfilesVigentesPorFiltro(@RequestParam(name = "pagina", required = false, defaultValue = "0") int pagina,
            											  @RequestParam(name = "filtro", required = false, defaultValue = "") String filtro) {
        return new ResponseEntity<>(perfilamientoService.listarPerfilesVigentesPorFiltro(pagina, filtro), HttpStatus.OK);
    }
    
    @GetMapping("/perfil/funcionario/{rutFuncionario}")
    public ResponseEntity<List<PerfilResponseDTO>> listarPerfilesPorFuncionario(@PathVariable String rutFuncionario) {
        return new ResponseEntity<>(perfilamientoService.listarPerfilesPorFuncionario(rutFuncionario), HttpStatus.OK);
    }
    
    @PostMapping("/perfilFuncionario")
    public ResponseEntity<Void> crearPerfilesFuncionario(@RequestBody @Valid CrearPerfilFuncionarioRequestDTO request) {
        perfilamientoService.crearPerfilesFuncionario(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/perfilFuncionario")
    public ResponseEntity<Void> actualizarPerfilesFuncionario(@RequestBody @Valid ActualizarPerfilFuncionarioRequestDTO request) {
        perfilamientoService.actualizarPerfilesFuncionario(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
