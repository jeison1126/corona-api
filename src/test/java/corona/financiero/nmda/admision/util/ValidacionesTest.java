package corona.financiero.nmda.admision.util;

import corona.financiero.nmda.admision.ex.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
class ValidacionesTest {

    @InjectMocks
    private Validaciones validaciones;

    @Test
    void validarRutTest() {
        String rut = "11111111-1";
        boolean response = validaciones.validaRut(rut);
        assertThat(response).isTrue();
    }

    @Test
    void validarRutKTest() {
        String rut = "15518258-k";
        boolean response = validaciones.validaRut(rut);
        assertThat(response).isTrue();
    }

    @Test
    void validarRutErrorTest() {
        String rut = "11111111-2";
        boolean response = validaciones.validaRut(rut);
        assertThat(response).isFalse();
    }

    @Test
    void validarRutInvalidoErrorTest() {
        String rut = "";
        boolean response = validaciones.validaRut(rut);
        assertThat(response).isFalse();
    }

    @Test
    void validaEstructuraEmailTest() {
        String email = "correo@dominio.com";
        Boolean response = validaciones.validaEmail(email);
        assertThat(response).isTrue();
    }

    @Test
    void validaEstructuraEmailErrorTest() {
        String email = "correo@do@minio.com";
        Boolean response = validaciones.validaEmail(email);
        assertThat(response).isFalse();
    }

    @Test
    void validaEmailListaNegraTest() {
        String email = "a@b.cl";
        String listaNegra = "@corona.cl";
        boolean response = validaciones.validaEmailListaNegra(email, Arrays.asList(listaNegra));
        assertThat(response).isFalse();
    }

    @Test
    void validaEmailListaNegraErrorTest() {
        String email = "a@corona.cl";
        String listaNegra = "@corona.cl";
        boolean response = validaciones.validaEmailListaNegra(email, Arrays.asList(listaNegra));
        assertThat(response).isTrue();
    }

    @Test
    void validaNumeroMovilTest() {
        String movil = "987654321";
        Boolean response = validaciones.validaNumeroMovil(movil);
        assertThat(response).isTrue();
    }

    @Test
    void formateoRutHaciaBDTest() {
        String rut = "11111111-1";
        String rutFormateado = validaciones.formateaRutHaciaBD(rut);
        assertThat(rutFormateado).isEqualTo("111111111");
    }

    @Test
    void formateoRuHaciaFrontTest() {
        String rut = "111111111";
        String rutFormateado = validaciones.formateaRutHaciaFront(rut);
        assertThat(rutFormateado).isEqualTo("11111111-1");
    }

    @Test
    void validarDireccionOrdenamientoASCTest() {
        String direccionOrdenamiento = "ASC";
        Boolean response = validaciones.validarDireccionOrdenamiento(direccionOrdenamiento);
        assertThat(response).isTrue();
    }

    @Test
    void validarDireccionOrdenamientoDESCTest() {
        String direccionOrdenamiento = "DESC";
        Boolean response = validaciones.validarDireccionOrdenamiento(direccionOrdenamiento);
        assertThat(response).isTrue();
    }

    @Test
    void validarDireccionOrdenamientoOtroTest() {
        String direccionOrdenamiento = "DES";
        Boolean response = validaciones.validarDireccionOrdenamiento(direccionOrdenamiento);
        assertThat(response).isFalse();
    }

    @Test
    void validarIdInvalidoTest() {
        String id = "0";
        Boolean response = validaciones.validarId(id);
        assertThat(response).isFalse();
    }

    @Test
    void validarIdCorrectoTest() {
        String id = "1";
        Boolean response = validaciones.validarId(id);
        assertThat(response).isTrue();
    }

    @Test
    void validarIdIncorrectoTest() {
        String id = "a";
        Boolean response = validaciones.validarId(id);
        assertThat(response).isFalse();
    }

    @Test
    void validarStringAFecha() {
        String fecha = "2022-06-09";
        validaciones.convertirStringALocaldate(fecha);
    }

    @Test
    void validarFechaALocalDate() {
        Date d = new Date();
        validaciones.convertirDateALocalDate(d);
    }

    @Test
    void validacionGeneralRutRutInvalidoTest() {
        String rut = "11111111-2";
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> validaciones.validacionGeneralRut(rut))
                .withNoCause();
    }

    @Test
    void validacionGeneralRutRutValidoTest() {
        String rut = "11111111-1";
        validaciones.validacionGeneralRut(rut);
    }

    @Test
    void validacionGeneralRutRutNullTest() {
        String rut = null;
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> validaciones.validacionGeneralRut(rut))
                .withNoCause();
    }

    @Test
    void validacionGeneralRutRutVacioTest() {
        String rut = "";
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> validaciones.validacionGeneralRut(rut))
                .withNoCause();
    }

    @Test
    void convertirFechaSlashALocalDateTest() throws ParseException {
        String fecha = "10/06/2022";

        validaciones.convertirFechaSlashALocalDate(fecha);
    }

    @Test
    void insertaLogoTest() throws IOException {
        validaciones.insertaLogo();
    }

    @Test
    void calculaEdadTest() {
        LocalDate date = LocalDate.now().minusYears(30);

        validaciones.calcularEdad(date);
    }

    @Test
    void convertToDateViaSqlDateTest() {
        LocalDate date = LocalDate.now();
        validaciones.convertToDateViaSqlDate(date);
    }
}
