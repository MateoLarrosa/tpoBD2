package controlador;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import modelo.EstadoSolicitud;
import modelo.SolicitudProceso;
import services.SolicitudProcesoService;

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

    public SolicitudProceso crearSolicitud(String usuarioId, String nombreProceso, Map<String, Object> parametros) {
        return SolicitudProcesoService.getInstance().crearSolicitud(usuarioId, nombreProceso, parametros);
    }

    public void completarSolicitud(String solicitudId, String resultado, long tiempoEjecucionMs) {
        SolicitudProcesoService.getInstance().completarSolicitud(solicitudId, resultado, tiempoEjecucionMs);
    }

    public void marcarError(String solicitudId, String mensajeError, long tiempoEjecucionMs) {
        SolicitudProcesoService.getInstance().marcarError(solicitudId, mensajeError, tiempoEjecucionMs);
    }

    public SolicitudProceso registrarSolicitudCompletada(String usuarioId, String nombreProceso, 
                                                         Map<String, Object> parametros, 
                                                         String resultado, long tiempoEjecucionMs) {
        return SolicitudProcesoService.getInstance().registrarSolicitudCompletada(
            usuarioId, nombreProceso, parametros, resultado, tiempoEjecucionMs);
    }

    public SolicitudProceso registrarSolicitudError(String usuarioId, String nombreProceso, 
                                                    Map<String, Object> parametros, 
                                                    String mensajeError, long tiempoEjecucionMs) {
        return SolicitudProcesoService.getInstance().registrarSolicitudError(
            usuarioId, nombreProceso, parametros, mensajeError, tiempoEjecucionMs);
    }

    public SolicitudProceso obtenerSolicitudPorId(String solicitudId) {
        return SolicitudProcesoService.getInstance().obtenerSolicitudPorId(solicitudId);
    }

    public List<SolicitudProceso> obtenerSolicitudesPorUsuario(String usuarioId) {
        return SolicitudProcesoService.getInstance().obtenerSolicitudesPorUsuario(usuarioId);
    }

    public List<SolicitudProceso> obtenerSolicitudesPorEstado(EstadoSolicitud estado) {
        return SolicitudProcesoService.getInstance().obtenerSolicitudesPorEstado(estado);
    }

    public List<SolicitudProceso> obtenerSolicitudesPorUsuarioYEstado(String usuarioId, EstadoSolicitud estado) {
        return SolicitudProcesoService.getInstance().obtenerSolicitudesPorUsuarioYEstado(usuarioId, estado);
    }

    public List<SolicitudProceso> obtenerSolicitudesPorUsuarioYRango(String usuarioId, 
                                                                     LocalDateTime fechaInicio, 
                                                                     LocalDateTime fechaFin) {
        return SolicitudProcesoService.getInstance().obtenerSolicitudesPorUsuarioYRango(
            usuarioId, fechaInicio, fechaFin);
    }

    public List<SolicitudProceso> obtenerTodasSolicitudes() {
        return SolicitudProcesoService.getInstance().obtenerTodasSolicitudes();
    }

    public int calcularCostoTotalPorUsuario(String usuarioId) {
        return SolicitudProcesoService.getInstance().calcularCostoTotalPorUsuario(usuarioId);
    }

    public int calcularCostoTotalPorUsuarioYRango(String usuarioId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return SolicitudProcesoService.getInstance().calcularCostoTotalPorUsuarioYRango(
            usuarioId, fechaInicio, fechaFin);
    }

    public int[] obtenerEstadisticasPorUsuario(String usuarioId) {
        return SolicitudProcesoService.getInstance().obtenerEstadisticasPorUsuario(usuarioId);
    }

    public void eliminarSolicitud(String solicitudId) {
        SolicitudProcesoService.getInstance().eliminarSolicitud(solicitudId);
    }
}
