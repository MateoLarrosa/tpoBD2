package controlador;

import java.util.List;

import exceptions.ErrorConectionMongoException;
import modelo.Mensaje;
import services.MensajeriaService;

public class MensajeriaController {

    private final MensajeriaService servicio;
    private static MensajeriaController instance;

    private MensajeriaController() {
        this.servicio = MensajeriaService.getInstance();
    }

    public static MensajeriaController getInstance() {
        if (instance == null) {
            instance = new MensajeriaController();
        }
        return instance;
    }

    public Mensaje enviar(String remitenteId, String destinatarioId, String texto)
            throws ErrorConectionMongoException {
        return servicio.enviarMensaje(remitenteId, destinatarioId, texto);
    }

    public List<Mensaje> historial(String usuarioA, String usuarioB, int cantidad)
            throws ErrorConectionMongoException {
        return servicio.obtenerHistorial(usuarioA, usuarioB, cantidad);
    }

    public List<Mensaje> nuevos(String usuario, String otroUsuario, String consumidorId)
            throws ErrorConectionMongoException {
        return servicio.leerNuevos(usuario, otroUsuario, consumidorId, 50, 20000);
    }

    public long confirmar(String usuario, String otroUsuario, List<String> ids)
            throws ErrorConectionMongoException {
        return servicio.confirmarLeidos(usuario, otroUsuario, ids);
    }
}
