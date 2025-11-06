package modelo;

public class Proceso {

    private String id;
    private String nombre;
    private String descripcion;
    private String tipoProceso;
    private int costo;

    public Proceso() {
    }

    public Proceso(String nombre, String descripcion, String tipoProceso, int costo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoProceso = tipoProceso;
        this.costo = costo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoProceso() {
        return tipoProceso;
    }

    public void setTipoProceso(String tipoProceso) {
        this.tipoProceso = tipoProceso;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    @Override
    public String toString() {
        return "Proceso{"
                + "id='" + id + '\''
                + ", nombre='" + nombre + '\''
                + ", descripcion='" + descripcion + '\''
                + ", tipoProceso='" + tipoProceso + '\''
                + ", costo=" + costo
                + '}';
    }
}
