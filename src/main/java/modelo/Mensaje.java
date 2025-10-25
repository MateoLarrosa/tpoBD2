package modelo;

import java.time.Instant;
import java.util.Map;

public class Mensaje {
    private String idRedis;     // ID de stream (15 digitos-0 o similar). Null al enviar, se completa al XADD.
    private String remitenteId; // user_101
    private String contenido;   // "Hola..."
    private Instant timestamp;  // derivado del idRedis o del servidor app
    private String chatKey;     // messages:user_101:user_202

    public Mensaje() {}

    public Mensaje(String idRedis, String remitenteId, String contenido, Instant timestamp, String chatKey) {
        this.idRedis = idRedis;
        this.remitenteId = remitenteId;
        this.contenido = contenido;
        this.timestamp = timestamp;
        this.chatKey = chatKey;
    }

    public static Mensaje fromStreamEntry(String chatKey, String id, Map<String, String> fields) {
        String remitente = fields.getOrDefault("remitente", "");
        String contenido = fields.getOrDefault("contenido", "");
        // El ID de stream es millisecondTime-seq. Usamos la parte ms para timestamp aproximado.
        long millis = 0L;
        try { millis = Long.parseLong(id.split("-")[0]); } catch (Exception ignored) {}
        return new Mensaje(id, remitente, contenido, Instant.ofEpochMilli(millis), chatKey);
    }

    public String getIdRedis() { return idRedis; }
    public String getRemitenteId() { return remitenteId; }
    public String getContenido() { return contenido; }
    public Instant getTimestamp() { return timestamp; }
    public String getChatKey() { return chatKey; }

}