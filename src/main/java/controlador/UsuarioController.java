package controlador;

import java.util.List;

import modelo.CuentaCorriente;
import modelo.EstadoUsuario;
import modelo.Usuario;
import services.UsuarioService;

public class UsuarioController {

    private final UsuarioService usuarioService;
    private static UsuarioController instance;

    private UsuarioController() {
        this.usuarioService = UsuarioService.getInstance();
    }

    public static UsuarioController getInstance() {
        if (instance == null) {
            instance = new UsuarioController();
        }
        return instance;
    }

    public boolean crearUsuario(String id, String nombre, String email, String password, EstadoUsuario estado, String rol) {
        Usuario usuario = new Usuario(id, nombre, email, password, estado, rol, new CuentaCorriente(0.0));
        return usuarioService.crearUsuario(usuario);
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioService.obtenerTodosLosUsuarios();
    }
}
