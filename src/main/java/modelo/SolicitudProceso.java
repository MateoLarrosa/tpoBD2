package modelo;

import java.time.LocalDateTime;
import java.util.Map;

public class SolicitudProceso {

    private String id;
    private String usuarioId;
    private Proceso proceso;
    private LocalDateTime fechaSolicitud;
    private EstadoSolicitud estado;
    private Map<String, Object> parametros;
    private String resultado;
    private long tiempoEjecucionMs;
    private LocalDateTime fechaCompletado;

    public SolicitudProceso() {
    }

    public SolicitudProceso(String usuarioId, Proceso proceso, Map<String, Object> parametros) {
        this.usuarioId = usuarioId;
        this.proceso = proceso;
        this.parametros = parametros;
        this.fechaSolicitud = LocalDateTime.now();
        this.estado = EstadoSolicitud.PENDIENTE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public long getTiempoEjecucionMs() {
        return tiempoEjecucionMs;
    }

    public void setTiempoEjecucionMs(long tiempoEjecucionMs) {
        this.tiempoEjecucionMs = tiempoEjecucionMs;
    }

    public LocalDateTime getFechaCompletado() {
        return fechaCompletado;
    }

    public void setFechaCompletado(LocalDateTime fechaCompletado) {
        this.fechaCompletado = fechaCompletado;
    }

    public void completar(String resultado, long tiempoEjecucionMs) {
        this.estado = EstadoSolicitud.COMPLETADO;
        this.resultado = resultado;
        this.tiempoEjecucionMs = tiempoEjecucionMs;
        this.fechaCompletado = LocalDateTime.now();
    }

    public void marcarError(String mensajeError, long tiempoEjecucionMs) {
        this.estado = EstadoSolicitud.ERROR;
        this.resultado = "ERROR: " + mensajeError;
        this.tiempoEjecucionMs = tiempoEjecucionMs;
        this.fechaCompletado = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "SolicitudProceso{"
                + "id='" + id + '\''
                + ", usuarioId='" + usuarioId + '\''
                + ", proceso=" + (proceso != null ? proceso.getNombre() : "null")
                + ", fechaSolicitud=" + fechaSolicitud
                + ", estado=" + estado
                + ", resultado='" + resultado + '\''
                + ", tiempoEjecucionMs=" + tiempoEjecucionMs
                + '}';
    }
}
