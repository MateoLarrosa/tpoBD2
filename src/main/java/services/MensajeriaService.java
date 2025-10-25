package services;

import java.util.Collections;
import java.util.List;

import exceptions.ErrorConectionMongoException;
import modelo.ChatUtils;
import modelo.Mensaje;
import repositories.RedisMessageRepository;

/**
 * MensajeriaService: se encarga de orquestar la lógica del chat. Usa
 * RedisMessageRepository para interactuar con Redis.
 */
public class MensajeriaService {

    private final RedisMessageRepository repositorio;

    public MensajeriaService(RedisMessageRepository repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Enviar mensaje 1 a 1 *
     */
    public Mensaje enviarMensaje(String remitenteId, String destinatarioId, String texto)
            throws ErrorConectionMongoException {

        String claveChat = ChatUtils.chatKeyForUsers(remitenteId, destinatarioId);
        String id = repositorio.appendMessage(claveChat, remitenteId, texto);
        return new Mensaje(id, remitenteId, texto, null, claveChat);
    }

    /**
     * Obtener historial (últimos n mensajes en orden cronológico) *
     */
    public List<Mensaje> obtenerHistorial(String usuarioA, String usuarioB, int cantidad)
            throws ErrorConectionMongoException {

        String claveChat = ChatUtils.chatKeyForUsers(usuarioA, usuarioB);
        List<Mensaje> mensajes = repositorio.getLastMessages(claveChat, cantidad);
        Collections.reverse(mensajes); // Para mostrar del más viejo al más nuevo
        return mensajes;
    }

    /**
     * Lectura en "tiempo real" usando consumer groups *
     */
    public List<Mensaje> leerNuevos(String usuario, String otroUsuario, String consumidorId,
            int maxMensajes, int tiempoBloqueoMs)
            throws ErrorConectionMongoException {

        String claveChat = ChatUtils.chatKeyForUsers(usuario, otroUsuario);
        String grupo = ChatUtils.consumerGroupForChat(claveChat);

        repositorio.ensureConsumerGroup(claveChat, grupo);
        return repositorio.readNewMessages(claveChat, grupo, consumidorId, maxMensajes, tiempoBloqueoMs);
    }

    /**
     * Confirmar mensajes como leídos (ACK) *
     */
    public long confirmarLeidos(String usuario, String otroUsuario, List<String> ids)
            throws ErrorConectionMongoException {

        String claveChat = ChatUtils.chatKeyForUsers(usuario, otroUsuario);
        String grupo = ChatUtils.consumerGroupForChat(claveChat);
        return repositorio.ackMessages(claveChat, grupo, ids);
    }
}
