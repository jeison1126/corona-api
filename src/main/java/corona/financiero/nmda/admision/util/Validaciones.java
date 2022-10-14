package corona.financiero.nmda.admision.util;

import corona.financiero.nmda.admision.enumeration.DireccionOrdenaEnum;
import corona.financiero.nmda.admision.ex.BadRequestException;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class Validaciones {
    private static final EmailValidator validator = EmailValidator.getInstance();

    public Boolean validaRut(String rut) {
        Pattern pattern = Pattern.compile("^[0-9]+-[0-9kK]{1}$");
        Matcher matcher = pattern.matcher(rut);
        if (!matcher.matches()) return false;
        String[] stringRut = rut.split("-");
        return stringRut[1].equalsIgnoreCase(dv(stringRut[0]));
    }


    @SuppressWarnings("java:S1121")
    private String dv(String rut) {
        Integer m = 0;
        Integer s = 1;
        Integer t = Integer.parseInt(rut);
        for (; t != 0; t = (int) Math.floor(t /= 10))
            s = (s + t % 10 * (9 - m++ % 6)) % 11;
        return (s > 0) ? String.valueOf(s - 1) : "k";
    }

    public Boolean validaEmail(String email) {
        return validator.isValid(email);
    }


    public Boolean validaNumeroMovil(String movil) {
        Pattern pattern = Pattern.compile("^\\d{9}$");

        return pattern.matcher(movil).matches();
    }

    public String formateaRutHaciaBD(String rut) {
        return rut.toUpperCase(Locale.ROOT).replace("-", "").replace(".", "");
    }

    public String formateaRutHaciaFront(String rut) {
        String rutFormateado = rut.substring(0, rut.length() - 1).concat("-").concat(rut.substring(rut.length() - 1));

        return rutFormateado.toUpperCase(Locale.ROOT);
    }

    public boolean validaEmailListaNegra(String email, List<String> listaNegra) {
        boolean existeEnListaNegra = false;

        String dominio = email.split("@")[1];
        dominio = "@".concat(dominio.toLowerCase(Locale.ROOT).trim());
        for (String lista : listaNegra) {
            if (lista.toLowerCase(Locale.ROOT).equals(dominio)) {
                existeEnListaNegra = true;
                break;
            }
        }
        return existeEnListaNegra;
    }

    public Boolean validarDireccionOrdenamiento(String direccionOrdena) {
        return (DireccionOrdenaEnum.ASC.name().equals(direccionOrdena)
                || DireccionOrdenaEnum.DESC.name().equals(direccionOrdena));
    }

    public boolean validarId(String id) {
        long idFormateado = 0;
        try {

            idFormateado = Long.parseLong(id);

            if (idFormateado <= 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public LocalDate convertirDateALocalDate(Date fecha) {
        return fecha.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public LocalDate convertirStringALocaldate(String fecha) {
        return LocalDate.parse(fecha);
    }

    @SuppressWarnings("java:S5411")
    public void validacionGeneralRut(String rut) {
        if (rut == null || rut.trim().isEmpty()) {
            throw new BadRequestException("El rut es requerido");
        }
        if (!validaRut(rut)) {
            throw new BadRequestException("El rut ingresado no es valido");
        }
    }

    public LocalDate convertirFechaSlashALocalDate(String fechaSlash) throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return convertirDateALocalDate(formato.parse(fechaSlash));
    }

    public Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }


    public int calcularEdad(LocalDate fechaNacimiento) {
        LocalDate ahora = LocalDate.now();

        Period periodo = Period.between(fechaNacimiento, ahora);
        return periodo.getYears();
    }

    public String insertaLogo() throws IOException {
        File file = new ClassPathResource("corona.png").getFile();

        ImageInputStream iis = new FileImageInputStream(file);
        BufferedImage inputStreamImage = ImageIO.read(iis);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(inputStreamImage, "png", output);
        byte[] data = output.toByteArray();
        String s = Base64.getEncoder().encodeToString(data);

        return "<img src=\"data:image/png;base64, " + s + "\"></img>";
    }
}
