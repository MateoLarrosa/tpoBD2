package modelo;

import java.time.LocalDateTime;

public class Usuario {
    private String id;
    private String nombre;
    private String email;
    private String password;
    private Rol rol;
    private EstadoUsuario estado;
    private LocalDateTime fechaRegistro;

    public Usuario(String id, String nombre, String email, String password, EstadoUsuario estado, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.estado = estado;
        this.rol = rol;
        this.fechaRegistro = LocalDateTime.now();
    }

}
