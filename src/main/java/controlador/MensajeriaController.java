package controlador;

import java.util.List;

import exceptions.ErrorConectionMongoException;
import modelo.Mensaje;
import services.MensajeriaService;

/**
 * MensajeriaController: actúa como puente entre la interfaz (o endpoints) y el
 * servicio. En un entorno REST sería el encargado de manejar las rutas
 * /mensajeria/...
 */
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

    // POST /mensajeria/enviar
    public Mensaje enviar(String remitenteId, String destinatarioId, String texto)
            throws ErrorConectionMongoException {
        return servicio.enviarMensaje(remitenteId, destinatarioId, texto);
    }

    // GET /mensajeria/historial?usuarioA=...&usuarioB=...&cantidad=50
    public List<Mensaje> historial(String usuarioA, String usuarioB, int cantidad)
            throws ErrorConectionMongoException {
        return servicio.obtenerHistorial(usuarioA, usuarioB, cantidad);
    }

    // GET /mensajeria/nuevos?usuario=...&otro=...&consumidor=...
    public List<Mensaje> nuevos(String usuario, String otroUsuario, String consumidorId)
            throws ErrorConectionMongoException {
        // Bloquea hasta 20 s esperando nuevos mensajes
        return servicio.leerNuevos(usuario, otroUsuario, consumidorId, 50, 20000);
    }

    // POST /mensajeria/confirmar
    public long confirmar(String usuario, String otroUsuario, List<String> ids)
            throws ErrorConectionMongoException {
        return servicio.confirmarLeidos(usuario, otroUsuario, ids);
    }
}
