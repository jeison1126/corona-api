package corona.financiero.nmda.admision.util;

public enum EnumOrigenSolcitudAdmision {
    NORMAL(1),
    CAMPANIA_EQUIFAX(2),
    CAMPANIA_CORONA(3),
    REEVALUADOS(4),
    NORMAL_EXCEPCION(5);

    private long codigo;

    EnumOrigenSolcitudAdmision(long codigo) {
        this.codigo = codigo;
    }

    public long getCodigo() {
        return codigo;
    }

}
