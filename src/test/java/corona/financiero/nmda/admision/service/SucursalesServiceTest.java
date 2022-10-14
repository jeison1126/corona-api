package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.entity.SucursalesEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.SucursalesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SucursalesServiceTest {

    @Mock
    private SucursalesRepository sucursalesRepository;

    @InjectMocks
    private SucursalesService sucursalesService;


    @Test
    void listarSucursalesTest() {

        SucursalesEntity sucursalesEntity = new SucursalesEntity();
        sucursalesEntity.setCodigoSucursal(1l);
        sucursalesEntity.setDescripcionSucursal("Concepcion");
        sucursalesEntity.setFechaRegistro(LocalDateTime.now());
        sucursalesEntity.setVigencia(true);
        sucursalesEntity.setUsuarioRegistro("TMP_USR");
        sucursalesEntity.setDescripcionZonaGeografica("Centro");

        when(sucursalesRepository.findAllByVigenciaIsTrueOrderByDescripcionSucursalAsc()).thenReturn(Arrays.asList(sucursalesEntity));

        sucursalesService.listarSucursales(null);
    }

    @Test
    void listarSucursalesVacioTest() {

        SucursalesEntity sucursalesEntity = new SucursalesEntity();
        sucursalesEntity.setCodigoSucursal(1l);
        sucursalesEntity.setDescripcionSucursal("Concepcion");
        sucursalesEntity.setFechaRegistro(LocalDateTime.now());
        sucursalesEntity.setVigencia(true);
        sucursalesEntity.setUsuarioRegistro("TMP_USR");
        sucursalesEntity.setDescripcionZonaGeografica("Centro");

        when(sucursalesRepository.findAllByVigenciaIsTrueOrderByDescripcionSucursalAsc()).thenReturn(Arrays.asList(sucursalesEntity));

        sucursalesService.listarSucursales("");
    }

    @Test
    void listarSucursalesPorZonaTest() {

        SucursalesEntity sucursalesEntity = new SucursalesEntity();
        sucursalesEntity.setCodigoSucursal(1l);
        sucursalesEntity.setDescripcionSucursal("Concepcion");
        sucursalesEntity.setFechaRegistro(LocalDateTime.now());
        sucursalesEntity.setVigencia(true);
        sucursalesEntity.setUsuarioRegistro("TMP_USR");
        sucursalesEntity.setDescripcionZonaGeografica("Centro");

        when(sucursalesRepository.findAllByVigenciaIsTrueAndDescripcionZonaGeograficaOrderByDescripcionSucursalAsc(any())).thenReturn(Arrays.asList(sucursalesEntity));

        sucursalesService.listarSucursales("Centro");
    }

    @Test
    void listarSucursalesNoContentTest() {
        when(sucursalesRepository.findAllByVigenciaIsTrueOrderByDescripcionSucursalAsc()).thenReturn(new ArrayList<>());

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> sucursalesService.listarSucursales(null))
                .withNoCause();

    }


    @Test
    void listarZonasGeograficasSucursalesTest() {

        String zona = "Santiago Centro";

        when(sucursalesRepository.listarZonasGeograficasSucursal()).thenReturn(Arrays.asList(zona));

        sucursalesService.listarZonasGeograficasSucursal();
    }

    @Test
    void listarZonasGeograficasSucursalesEmptyTest() {

        String zona = "Santiago Centro";

        when(sucursalesRepository.listarZonasGeograficasSucursal()).thenReturn(new ArrayList<>());

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> sucursalesService.listarZonasGeograficasSucursal())
                .withNoCause();
    }
}
