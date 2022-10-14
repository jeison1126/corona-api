package corona.financiero.nmda.admision.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import corona.financiero.nmda.admision.dto.FuncionarioRequestDTO;
import corona.financiero.nmda.admision.entity.FuncionariosEntity;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.repository.FuncionariosRepository;
import corona.financiero.nmda.admision.util.Validaciones;

@ExtendWith(MockitoExtension.class)
class FuncionariosServiceTest {

    @Mock
    private FuncionariosRepository funcionariosRepository;

    @InjectMocks
    private FuncionariosService funcionariosService;

    @Mock
    private Validaciones validaciones;

    @BeforeEach
    public void initEach() {
        ReflectionTestUtils.setField(funcionariosService, "paginacion", 15);
    }

    @Test
    void listarFuncionariosVigentesFiltroNullTest() {
        int numPagina = 1;
        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("nombreCompleto"));
        Page<FuncionariosEntity> page = Mockito.mock(Page.class);
        when(funcionariosRepository.listarFuncionariosVigentes(pageable)).thenReturn(page);

        funcionariosService.listarFuncionariosVigentes(numPagina, null);
    }

    @Test
    void listarFuncionariosVigentesFiltroEmptyTest() {
        int numPagina = 1;
        String filtro = "";
        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("nombreCompleto"));
        Page<FuncionariosEntity> page = Mockito.mock(Page.class);
        when(funcionariosRepository.listarFuncionariosVigentes(pageable)).thenReturn(page);

        funcionariosService.listarFuncionariosVigentes(numPagina, filtro);
    }

    @Test
    void listarFuncionariosVigentesConFiltroTest() {
        int numPagina = 1;
        String filtro = "juan";
        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("nombreCompleto"));

        FuncionariosEntity funcionariosEntity = datosFuncionariosEntity();
        List<FuncionariosEntity> lista = Arrays.asList(funcionariosEntity);
        Page<FuncionariosEntity> pagedResponse = new PageImpl(lista);
        when(funcionariosRepository.listarFuncionariosVigentesPorFiltro(filtro, pageable)).thenReturn(pagedResponse);

        funcionariosService.listarFuncionariosVigentes(numPagina, filtro);
    }

    @Test
    void listarFuncionariosVigentesBadRequestTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> funcionariosService.listarFuncionariosVigentes(-1, null))
                .withNoCause();

    }

    @Test
    void crearFuncionarioTest() {
    	FuncionarioRequestDTO request = datosFuncionarioRequestDTO();
        when(validaciones.validaRut(request.getRut())).thenReturn(true);
        
        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        Optional<FuncionariosEntity> funcionariosEntityOptional = Optional.empty();

        when(funcionariosRepository.findByRut(rutFormateado)).thenReturn(funcionariosEntityOptional);

        funcionariosService.crearFuncionario(request);
    }

    @Test
    void crearFuncionarioYaExistenteTest() {
    	FuncionarioRequestDTO request = datosFuncionarioRequestDTO();
        when(validaciones.validaRut(request.getRut())).thenReturn(true);
        
        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        FuncionariosEntity funcionariosEntity = datosFuncionariosEntity();
        Optional<FuncionariosEntity> funcionariosEntityOptional = Optional.of(funcionariosEntity);
        
        when(funcionariosRepository.findByRut(rutFormateado)).thenReturn(funcionariosEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> funcionariosService.crearFuncionario(request))
                .withNoCause();
    }

    @Test
    void crearFuncionarioNullErrorTest() {

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> funcionariosService.crearFuncionario(null))
                .withNoCause();
    }

    @Test
    void crearFuncionarioRutNullErrorTest() {
    	FuncionarioRequestDTO request = datosFuncionarioRequestDTO();
        request.setRut(null);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> funcionariosService.crearFuncionario(request))
                .withNoCause();
    }

    @Test
    void crearFuncionarioInvalidRutErrorTest() {
    	FuncionarioRequestDTO request = datosFuncionarioRequestDTO();
        request.setRut("11111111-2");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> funcionariosService.crearFuncionario(request))
                .withNoCause();
    }

    @Test
    void crearFuncionarioRutEmptyErrorTest() {
        FuncionarioRequestDTO request = datosFuncionarioRequestDTO();
        request.setRut("");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> funcionariosService.crearFuncionario(request))
                .withNoCause();
    }
    
    @Test
    void crearFuncionarioNombreCompletoNullErrorTest() {
        FuncionarioRequestDTO request = datosFuncionarioRequestDTO();
        request.setNombreCompleto(null);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> funcionariosService.crearFuncionario(request))
                .withNoCause();
    }

    @Test
    void crearFuncionarioNombreCompletoEmptyErrorTest() {
        FuncionarioRequestDTO request = datosFuncionarioRequestDTO();
        request.setNombreCompleto("");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> funcionariosService.crearFuncionario(request))
                .withNoCause();
    }

    @Test
    void crearFuncionarioNombreUsuarioNullErrorTest() {
        FuncionarioRequestDTO request = datosFuncionarioRequestDTO();
        request.setNombreUsuario(null);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> funcionariosService.crearFuncionario(request))
                .withNoCause();
    }

    @Test
    void crearFuncionarioNombreUsuarioEmptyErrorTest() {
        FuncionarioRequestDTO request = datosFuncionarioRequestDTO();
        request.setNombreUsuario("");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> funcionariosService.crearFuncionario(request))
                .withNoCause();
    }
    
    @Test
    void crearFuncionarioNombreSucursalNullErrorTest() {
        FuncionarioRequestDTO request = datosFuncionarioRequestDTO();
        request.setNombreSucursal(null);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> funcionariosService.crearFuncionario(request))
                .withNoCause();
    }

    @Test
    void crearFuncionarioNombreSucursalEmptyErrorTest() {
        FuncionarioRequestDTO request = datosFuncionarioRequestDTO();
        request.setNombreSucursal("");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> funcionariosService.crearFuncionario(request))
                .withNoCause();
    }

    @Test
    void crearFuncionarioNombreCargoNullErrorTest() {
        FuncionarioRequestDTO request = datosFuncionarioRequestDTO();
        request.setNombreCargo(null);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> funcionariosService.crearFuncionario(request))
                .withNoCause();
    }

    @Test
    void crearFuncionarioNombreCargoEmptyErrorTest() {
        FuncionarioRequestDTO request = datosFuncionarioRequestDTO();
        request.setNombreCargo("");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> funcionariosService.crearFuncionario(request))
                .withNoCause();
    }
    
    @Test
    void crearFuncionarioSucursalIdInvalidoErrorTest() {
        FuncionarioRequestDTO request = datosFuncionarioRequestDTO();
        request.setSucursalId(-1);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> funcionariosService.crearFuncionario(request))
                .withNoCause();
    }

    @Test
    void crearFuncionarioCargoIdInvalidoErrorTest() {
        FuncionarioRequestDTO request = datosFuncionarioRequestDTO();
        request.setCargoId(-1);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> funcionariosService.crearFuncionario(request))
                .withNoCause();
    }
    
    @Test
    void existeFuncionarioSiExisteTest() {
    	String rut = "11111111-1";
        when(validaciones.validaRut(rut)).thenReturn(true);
        
        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(rut)).thenReturn(rutFormateado);

        FuncionariosEntity funcionariosEntity = datosFuncionariosEntity();
        Optional<FuncionariosEntity> funcionariosEntityOptional = Optional.of(funcionariosEntity);

        when(funcionariosRepository.findByRut(rutFormateado)).thenReturn(funcionariosEntityOptional);

        funcionariosService.existeFuncionario(rut);
    }
    
    @Test
    void existeFuncionarioNoExisteTest() {
    	String rut = "11111111-1";
        when(validaciones.validaRut(rut)).thenReturn(true);
        
        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(rut)).thenReturn(rutFormateado);

        Optional<FuncionariosEntity> funcionariosEntityOptional = Optional.empty();

        when(funcionariosRepository.findByRut(rutFormateado)).thenReturn(funcionariosEntityOptional);

        funcionariosService.existeFuncionario(rut);
    }
    
    private FuncionariosEntity datosFuncionariosEntity() {
    	FuncionariosEntity funcionariosEntity = new FuncionariosEntity();
    	funcionariosEntity.setRut("111111111");
    	funcionariosEntity.setNombreCompleto("juan perez");
    	funcionariosEntity.setNombreUsuario("juan.perez");
    	funcionariosEntity.setNombreCargo("Administrador");
    	funcionariosEntity.setNombreSucursal("Santiago");
    	funcionariosEntity.setSucursalId(1);
    	funcionariosEntity.setCargoId(1);
    	funcionariosEntity.setVigencia(true);

        return funcionariosEntity;
    }
    
    private FuncionarioRequestDTO datosFuncionarioRequestDTO() {
    	FuncionarioRequestDTO funcionarioRequestDTO = new FuncionarioRequestDTO();
    	funcionarioRequestDTO.setRut("11111111-1");
    	funcionarioRequestDTO.setNombreCompleto("Juan Perez");
    	funcionarioRequestDTO.setNombreUsuario("juan.perez");
    	funcionarioRequestDTO.setNombreCargo("Administrador");
    	funcionarioRequestDTO.setNombreSucursal("Santiago");
    	funcionarioRequestDTO.setSucursalId(1);
    	funcionarioRequestDTO.setCargoId(1);

        return funcionarioRequestDTO;
    }

}
