package services;

import modelo.Usuario;
import repositories.UsuarioRepository;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepository();
    }

    public boolean crearUsuario(Usuario usuario) {
        try {
            usuarioRepository.save(usuario);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario obtenerUsuarioPorId(String id) {
        return usuarioRepository.findById(id);
    }

    public boolean eliminarUsuario(String id) {
        try {
            usuarioRepository.delete(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public java.util.List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }
}