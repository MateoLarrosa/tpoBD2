package services;

import java.util.Collections;
import java.util.List;

import exceptions.ErrorConectionMongoException;
import modelo.ChatUtils;
import modelo.Mensaje;
import repositories.RedisMessageRepository;

public class MensajeriaService {

    private final RedisMessageRepository repositorio;
    private static MensajeriaService instance;

    private MensajeriaService() {
        this.repositorio = RedisMessageRepository.getInstance();
    }

    public static MensajeriaService getInstance() {
        if (instance == null) {
            instance = new MensajeriaService();
        }
        return instance;
    }

    public Mensaje enviarMensaje(String remitenteId, String destinatarioId, String texto)
            throws ErrorConectionMongoException {
        String claveChat = ChatUtils.chatKeyForUsers(remitenteId, destinatarioId);
        String id = repositorio.appendMessage(claveChat, texto);
        return new Mensaje(id, texto, null, claveChat);
    }

    public List<Mensaje> obtenerHistorial(String usuarioA, String usuarioB, int cantidad)
            throws ErrorConectionMongoException {

        String claveChat = ChatUtils.chatKeyForUsers(usuarioA, usuarioB);
        List<Mensaje> mensajes = repositorio.getLastMessages(claveChat, cantidad);
        Collections.reverse(mensajes);
        return mensajes;
    }

}
