package controlador;

import java.util.List;

import modelo.CuentaCorriente;
import modelo.EstadoUsuario;
import modelo.Usuario;
import services.UsuarioService;

public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public boolean crearUsuario(String id, String nombre, String email, String password, EstadoUsuario estado, String rol) {
        Usuario usuario = new Usuario(id, nombre, email, password, estado, rol, new CuentaCorriente(0.0));
        return usuarioService.crearUsuario(usuario);
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioService.obtenerTodosLosUsuarios();
    }
}
