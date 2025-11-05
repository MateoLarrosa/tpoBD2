package modelo;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Representa una solicitud/ejecución de un proceso por parte de un usuario.
 * Registra cada vez que un usuario utiliza un servicio del sistema.
 */
public class SolicitudProceso {

    private String id;
    private String usuarioId;
    private String procesoId;
    private LocalDateTime fechaSolicitud;
    private EstadoSolicitud estado;
    private Map<String, Object> parametros;
    private String resultado;
    private long tiempoEjecucionMs;
    private LocalDateTime fechaCompletado;

    public SolicitudProceso() {
    }

    public SolicitudProceso(String usuarioId, String procesoId, Map<String, Object> parametros) {
        this.usuarioId = usuarioId;
        this.procesoId = procesoId;
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

    public String getProcesoId() {
        return procesoId;
    }

    public void setProcesoId(String procesoId) {
        this.procesoId = procesoId;
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

    /**
     * Marca la solicitud como completada
     */
    public void completar(String resultado, long tiempoEjecucionMs) {
        this.estado = EstadoSolicitud.COMPLETADO;
        this.resultado = resultado;
        this.tiempoEjecucionMs = tiempoEjecucionMs;
        this.fechaCompletado = LocalDateTime.now();
    }

    /**
     * Marca la solicitud como error
     */
    public void marcarError(String mensajeError, long tiempoEjecucionMs) {
        this.estado = EstadoSolicitud.ERROR;
        this.resultado = "ERROR: " + mensajeError;
        this.tiempoEjecucionMs = tiempoEjecucionMs;
        this.fechaCompletado = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "SolicitudProceso{" +
                "id='" + id + '\'' +
                ", usuarioId='" + usuarioId + '\'' +
                ", procesoId='" + procesoId + '\'' +
                ", fechaSolicitud=" + fechaSolicitud +
                ", estado=" + estado +
                ", resultado='" + resultado + '\'' +
                ", tiempoEjecucionMs=" + tiempoEjecucionMs +
                '}';
    }
}
