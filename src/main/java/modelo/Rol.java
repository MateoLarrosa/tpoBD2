package modelo;

public class Rol {
    private String id;
    private String nombre;

    public Rol(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
