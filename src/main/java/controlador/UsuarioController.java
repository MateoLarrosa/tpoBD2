package controlador;

import modelo.CuentaCorriente;
import modelo.EstadoUsuario;
import modelo.Rol;
import modelo.Usuario;
import services.UsuarioService;

public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void crearUsuario(String id, String nombre, String email, String password, EstadoUsuario estado, Rol rol) {
        Usuario usuario = new Usuario(id, nombre, email, password, estado, rol, new CuentaCorriente(0.0));
        usuarioService.crearUsuario(usuario);
    }
}
