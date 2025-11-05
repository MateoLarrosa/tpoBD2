package controlador;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import modelo.EstadoSolicitud;
import modelo.SolicitudProceso;
import services.SolicitudProcesoService;

/**
 * Controlador para gestionar las solicitudes de procesos de los usuarios
 */
public class SolicitudProcesoController {

    private static SolicitudProcesoController instance;

    private SolicitudProcesoController() {
    }

    public static SolicitudProcesoController getInstance() {
        if (instance == null) {
            instance = new SolicitudProcesoController();
        }
        return instance;
    }

    /**
     * Crea una nueva solicitud de proceso
     */
    public SolicitudProceso crearSolicitud(String usuarioId, String nombreProceso, Map<String, Object> parametros) {
        return SolicitudProcesoService.getInstance().crearSolicitud(usuarioId, nombreProceso, parametros);
    }

    /**
     * Marca una solicitud como completada
     */
    public void completarSolicitud(String solicitudId, String resultado, long tiempoEjecucionMs) {
        SolicitudProcesoService.getInstance().completarSolicitud(solicitudId, resultado, tiempoEjecucionMs);
    }

    /**
     * Marca una solicitud como error
     */
    public void marcarError(String solicitudId, String mensajeError, long tiempoEjecucionMs) {
        SolicitudProcesoService.getInstance().marcarError(solicitudId, mensajeError, tiempoEjecucionMs);
    }

    /**
     * Registra una solicitud completada en un solo paso
     */
    public SolicitudProceso registrarSolicitudCompletada(String usuarioId, String nombreProceso, 
                                                         Map<String, Object> parametros, 
                                                         String resultado, long tiempoEjecucionMs) {
        return SolicitudProcesoService.getInstance().registrarSolicitudCompletada(
            usuarioId, nombreProceso, parametros, resultado, tiempoEjecucionMs);
    }

    /**
     * Registra una solicitud con error
     */
    public SolicitudProceso registrarSolicitudError(String usuarioId, String nombreProceso, 
                                                    Map<String, Object> parametros, 
                                                    String mensajeError, long tiempoEjecucionMs) {
        return SolicitudProcesoService.getInstance().registrarSolicitudError(
            usuarioId, nombreProceso, parametros, mensajeError, tiempoEjecucionMs);
    }

    /**
     * Obtiene una solicitud por su ID
     */
    public SolicitudProceso obtenerSolicitudPorId(String solicitudId) {
        return SolicitudProcesoService.getInstance().obtenerSolicitudPorId(solicitudId);
    }

    /**
     * Obtiene todas las solicitudes de un usuario
     */
    public List<SolicitudProceso> obtenerSolicitudesPorUsuario(String usuarioId) {
        return SolicitudProcesoService.getInstance().obtenerSolicitudesPorUsuario(usuarioId);
    }

    /**
     * Obtiene solicitudes por estado
     */
    public List<SolicitudProceso> obtenerSolicitudesPorEstado(EstadoSolicitud estado) {
        return SolicitudProcesoService.getInstance().obtenerSolicitudesPorEstado(estado);
    }

    /**
     * Obtiene solicitudes de un usuario por estado
     */
    public List<SolicitudProceso> obtenerSolicitudesPorUsuarioYEstado(String usuarioId, EstadoSolicitud estado) {
        return SolicitudProcesoService.getInstance().obtenerSolicitudesPorUsuarioYEstado(usuarioId, estado);
    }

    /**
     * Obtiene solicitudes de un usuario en un rango de fechas
     */
    public List<SolicitudProceso> obtenerSolicitudesPorUsuarioYRango(String usuarioId, 
                                                                     LocalDateTime fechaInicio, 
                                                                     LocalDateTime fechaFin) {
        return SolicitudProcesoService.getInstance().obtenerSolicitudesPorUsuarioYRango(
            usuarioId, fechaInicio, fechaFin);
    }

    /**
     * Obtiene todas las solicitudes registradas
     */
    public List<SolicitudProceso> obtenerTodasSolicitudes() {
        return SolicitudProcesoService.getInstance().obtenerTodasSolicitudes();
    }

    /**
     * Calcula el costo total de todas las solicitudes completadas de un usuario
     */
    public int calcularCostoTotalPorUsuario(String usuarioId) {
        return SolicitudProcesoService.getInstance().calcularCostoTotalPorUsuario(usuarioId);
    }

    /**
     * Calcula el costo total en un rango de fechas para un usuario
     */
    public int calcularCostoTotalPorUsuarioYRango(String usuarioId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return SolicitudProcesoService.getInstance().calcularCostoTotalPorUsuarioYRango(
            usuarioId, fechaInicio, fechaFin);
    }

    /**
     * Obtiene estadísticas de solicitudes de un usuario
     * @return Array con [total, completadas, pendientes, errores]
     */
    public int[] obtenerEstadisticasPorUsuario(String usuarioId) {
        return SolicitudProcesoService.getInstance().obtenerEstadisticasPorUsuario(usuarioId);
    }

    /**
     * Elimina una solicitud
     */
    public void eliminarSolicitud(String solicitudId) {
        SolicitudProcesoService.getInstance().eliminarSolicitud(solicitudId);
    }
}
