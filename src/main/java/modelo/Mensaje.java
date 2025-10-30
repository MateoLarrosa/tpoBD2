package modelo;

import java.time.Instant;
import java.util.Map;

public class Mensaje {
    private String idRedis;
    private String contenido;
    private Instant timestamp;
    private String chatKey;

    public Mensaje() {}

    public Mensaje(String idRedis, String contenido, Instant timestamp, String chatKey) {
        this.idRedis = idRedis;
        this.contenido = contenido;
        this.timestamp = timestamp;
        this.chatKey = chatKey;
    }

    public static Mensaje fromStreamEntry(String chatKey, String id, Map<String, String> fields) {
        String contenido = fields.getOrDefault("mensaje", "");
        long millis = 0L;
        try { millis = Long.parseLong(id.split("-")[0]); } catch (Exception ignored) {}
        return new Mensaje(id, contenido, Instant.ofEpochMilli(millis), chatKey);
    }
    // Getters y setters limpios
    public String getIdRedis() { return idRedis; }
    public void setIdRedis(String idRedis) { this.idRedis = idRedis; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public String getChatKey() { return chatKey; }
    public void setChatKey(String chatKey) { this.chatKey = chatKey; }

}