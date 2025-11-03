package services;

import java.util.Date;
import java.util.UUID;

import exceptions.ErrorConectionMongoException;
import modelo.Sesion;
import repositories.SesionRepository;

public class SesionService {

    private static SesionService instance;
    private final SesionRepository sesionRepository;

    private SesionService() {
        this.sesionRepository = SesionRepository.getInstance();
    }

    public static SesionService getInstance() {
        if (instance == null) {
            instance = new SesionService();
        }
        return instance;
    }

    public Sesion crearSesion(String idUsuario) throws ErrorConectionMongoException {
        String idSesion = UUID.randomUUID().toString();
        Sesion sesion = new Sesion(idSesion, idUsuario, new Date(), "activa");
        sesionRepository.guardarSesion(sesion);
        return sesion;
    }

    public void cerrarSesion(String idSesion) throws ErrorConectionMongoException {
        Sesion sesion = sesionRepository.obtenerSesion(idSesion);
        if (sesion != null && "activa".equals(sesion.getEstado())) {
            sesion.setFechaHoraCierre(new Date());
            sesion.setEstado("inactiva");
            sesionRepository.actualizarSesion(sesion);
        }
    }
}
