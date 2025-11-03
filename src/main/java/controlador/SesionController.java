package controlador;

import exceptions.ErrorConectionMongoException;
import modelo.Sesion;
import services.SesionService;

public class SesionController {

    private static SesionController instance;
    private String idSesionActual;

    private SesionController() {
    }

    public static SesionController getInstance() {
        if (instance == null) {
            instance = new SesionController();
        }
        return instance;
    }

    public Sesion iniciarSesion(String idUsuario) throws ErrorConectionMongoException {
        Sesion sesion = SesionService.getInstance().crearSesion(idUsuario);
        this.idSesionActual = sesion.getIdSesion();
        return sesion;
    }

    public void cerrarSesion() throws ErrorConectionMongoException {
        if (idSesionActual != null) {
            SesionService.getInstance().cerrarSesion(idSesionActual);
            idSesionActual = null;
        }
    }

    public String getIdSesionActual() {
        return idSesionActual;
    }
}
