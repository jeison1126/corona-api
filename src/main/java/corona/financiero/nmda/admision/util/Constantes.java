package corona.financiero.nmda.admision.util;

public final class Constantes {

    private Constantes() {
    }

    //TIPO CANAL
    public static final long CANAL_TIENDA = 2l;

    //ESTADO SOLICITUD
    public static final long ESTADO_INICIADO = 1l;
    public static final long ESTADO_APROBADO = 2l;
    public static final long ESTADO_NO_APROBADO = 3l;
    public static final long ESTADO_INCOMPLETO = 4l;

    //FASES
    public static final long FASE_EVALUACION_COMERCIAL = 1;
    public static final long FASE_INFORMACION_OFERTA = 2;
    public static final long FASE_DATOS_CLIENTE = 3;
    public static final long FASE_FIRMA_CONTRATOS = 4;
    public static final long FASE_IMPRIMIR_ACTIVAR_TARJETA = 5;
    public static final long FASE_SEGUROS = 6;
    public static final long FASE_EVALUACION_INTERNA = 7;
    public static final long FASE_EVALUACION_SCORING = 8;
    public static final long FASE_EVALUACION_COTIZACIONES = 9;


    //Reglas de negocio fijas
    public static final Long REGLA_CLIENTE_ESTAFA = 1l;
    public static final Long REGLA_CLIENTE_NO_RECOMENDADOS = 2l;
    public static final Long REGLA_CLIENTE_R04 = 3l;
    public static final Long REGLA_CLIENTE_CAMARA_COMERCIO = 4l;
    public static final Long REGLA_CLIENTE_FUNCIONARIO = 5l;
    public static final Long REGLA_CLIENTE_EXISTENTE = 6l;

    public static final Long REGLA_PROBLEMA_SERVICIO_SCORING = 7l;

    public static final Long REGLA_EVALUACION_EDAD_MINIMA = 8l;
    public static final Long REGLA_EVALUACION_EDAD_MAXIMA = 9l;
    public static final Long REGLA_EVALUACION_SCORE_MINIMO = 10l;
    public static final Long REGLA_EVALUACION_PROTESTOS_CLIENTE = 11l;
    public static final Long REGLA_EVALUACION_MORA_CLIENTE = 12l;

    public static final Long REGLA_EVALUACION_DIAS_INTENTOS = 13l;

    public static final Integer ERROR_SCORING = -100;

    public static final Long BIOMETRIA_COTIZACIONES = 1l;
    public static final Long BIOMETRIA_FIRMA_CONTRATO = 2l;
    public static final Long BIOMETRIA_IMPRESION_TARJETA = 3l;
    public static final Long BIOMETRIA_FIRMA_CONTRATO_SEGURO = 4l;
    public static final Long BIOMETRIA_CARTA_RECHAZO = 5l;


    public static final Long TIPO_DOCUMENTO_CONTRATO = 1l;
    public static final Long TIPO_DOCUMENTO_RESUMEN = 2l;
    public static final Long TIPO_DOCUMENTO_SEGURO_DEGRAVAMEN = 3l;
    public static final Long TIPO_DOCUMENTO_SEGURO_CESANTIA = 4l;

    public static final String TP_DOCUMENTO_CONTRATO = "CC";
    public static final String TP_DOCUMENTO_RESUMEN = "HR";
    public static final String TP_DOCUMENTO_SEGURO_DEGRAVAMEN = "SD";
    public static final String TP_DOCUMENTO_SEGURO_CESANTIA = "SC";


}
