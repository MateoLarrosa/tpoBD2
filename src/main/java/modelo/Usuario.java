package modelo;

import java.time.LocalDateTime;

public class Usuario {

    private String id;
    private String nombre;
    private String email;
    private String password;
    private String rol;
    private EstadoUsuario estado;
    private LocalDateTime fechaRegistro;
    private CuentaCorriente cuentaCorriente;

    public Usuario(String id, String nombre, String email, String password, EstadoUsuario estado, String rol, CuentaCorriente cuentaCorriente) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.estado = estado;
        this.rol = rol;
        this.fechaRegistro = LocalDateTime.now();
        this.cuentaCorriente = cuentaCorriente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getRol() {
        return rol;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public CuentaCorriente getCuentaCorriente() {
        return cuentaCorriente;
    }
}
