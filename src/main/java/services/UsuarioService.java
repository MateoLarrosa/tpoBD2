package services;

import java.util.ArrayList;

import modelo.Usuario;
import repositories.UsuarioRepository;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private static UsuarioService instance;

    private UsuarioService() {
        this.usuarioRepository = UsuarioRepository.getInstance();
    }

    public static UsuarioService getInstance() {
        if (instance == null) {
            instance = new UsuarioService();
        }
        return instance;
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

    public ArrayList<Usuario> obtenerTodosLosUsuarios() {
        return new ArrayList<>(usuarioRepository.findAll());
    }
}
