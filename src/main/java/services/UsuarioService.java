package services;

import java.util.ArrayList;

import modelo.Usuario;
import repositories.UsuarioRepository;

public class UsuarioService {

    private static UsuarioService instance;

    private UsuarioService() {
    }

    public static UsuarioService getInstance() {
        if (instance == null) {
            instance = new UsuarioService();
        }
        return instance;
    }

    public boolean crearUsuario(Usuario usuario) {
        try {
            UsuarioRepository.getInstance().save(usuario);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario obtenerUsuarioPorId(String id) {
        return UsuarioRepository.getInstance().findById(id);
    }

    public boolean eliminarUsuario(String id) {
        try {
            UsuarioRepository.getInstance().delete(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Usuario> obtenerTodosLosUsuarios() {
        return new ArrayList<>(UsuarioRepository.getInstance().findAll());
    }
}
