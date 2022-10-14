package corona.financiero.nmda.admision.util;

public enum EnumAcciones {
    CREAR(1,"Crear"),
    ELIMINAR(2,"Eliminar"),
    MODIFICAR(3,"Modificar"),
    CONSULTAR(4,"Consultar");

    private String descripcion;
    private int codigo;

    EnumAcciones(int codigo, String descripcion){
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCodigo() {
        return codigo;
    }
}
