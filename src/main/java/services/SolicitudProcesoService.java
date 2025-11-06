package services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import modelo.EstadoSolicitud;
import modelo.Proceso;
import modelo.SolicitudProceso;
import repositories.SolicitudProcesoRepository;

public class SolicitudProcesoService {

    // Método público para exponer actualización de solicitud
    public void updateSolicitud(SolicitudProceso solicitud) {
        solicitudRepository.update(solicitud);
    }

    private static SolicitudProcesoService instance;
    private final SolicitudProcesoRepository solicitudRepository;
    private final ProcesoService procesoService;

    private SolicitudProcesoService() {
        this.solicitudRepository = SolicitudProcesoRepository.getInstance();
        this.procesoService = ProcesoService.getInstance();
    }

    public static SolicitudProcesoService getInstance() {
        if (instance == null) {
            instance = new SolicitudProcesoService();
        }
        return instance;
    }

    public SolicitudProceso crearSolicitud(String usuarioId, String nombreProceso, Map<String, Object> parametros) {

        Proceso proceso = procesoService.obtenerProcesoPorNombre(nombreProceso);

        if (proceso == null) {
            throw new IllegalArgumentException("Proceso no encontrado: " + nombreProceso);
        }

        SolicitudProceso solicitud = new SolicitudProceso(usuarioId, proceso, parametros);
        solicitudRepository.save(solicitud);

        return solicitud;
    }

    public void completarSolicitud(String solicitudId, String resultado, long tiempoEjecucionMs) {
        SolicitudProceso solicitud = solicitudRepository.findById(solicitudId);
        if (solicitud == null) {
            throw new IllegalArgumentException("Solicitud no encontrada: " + solicitudId);
        }

        solicitud.completar(resultado, tiempoEjecucionMs);
        solicitudRepository.update(solicitud);
    }

    public void marcarError(String solicitudId, String mensajeError, long tiempoEjecucionMs) {
        SolicitudProceso solicitud = solicitudRepository.findById(solicitudId);
        if (solicitud == null) {
            throw new IllegalArgumentException("Solicitud no encontrada: " + solicitudId);
        }

        solicitud.marcarError(mensajeError, tiempoEjecucionMs);
        solicitudRepository.update(solicitud);
    }

    public SolicitudProceso registrarSolicitudCompletada(String usuarioId, String nombreProceso,
            Map<String, Object> parametros,
            String resultado, long tiempoEjecucionMs) {
        SolicitudProceso solicitud = crearSolicitud(usuarioId, nombreProceso, parametros);
        return solicitud;
    }

    public SolicitudProceso registrarSolicitudError(String usuarioId, String nombreProceso,
            Map<String, Object> parametros,
            String mensajeError, long tiempoEjecucionMs) {
        SolicitudProceso solicitud = crearSolicitud(usuarioId, nombreProceso, parametros);
        solicitud.marcarError(mensajeError, tiempoEjecucionMs);
        solicitudRepository.update(solicitud);
        return solicitud;
    }

    public SolicitudProceso obtenerSolicitudPorId(String solicitudId) {
        return solicitudRepository.findById(solicitudId);
    }

    public List<SolicitudProceso> obtenerSolicitudesPorUsuario(String usuarioId) {
        return solicitudRepository.findByUsuarioId(usuarioId);
    }

    public List<SolicitudProceso> obtenerSolicitudesPorEstado(EstadoSolicitud estado) {
        return solicitudRepository.findByEstado(estado);
    }

    public List<SolicitudProceso> obtenerSolicitudesPorUsuarioYEstado(String usuarioId, EstadoSolicitud estado) {
        return solicitudRepository.findByUsuarioIdYEstado(usuarioId, estado);
    }

    public List<SolicitudProceso> obtenerSolicitudesPorUsuarioYRango(String usuarioId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {
        return solicitudRepository.findByUsuarioIdYRangoFechas(usuarioId, fechaInicio, fechaFin);
    }

    public List<SolicitudProceso> obtenerTodasSolicitudes() {
        return solicitudRepository.findAll();
    }

    public int calcularCostoTotalPorUsuario(String usuarioId) {
        List<SolicitudProceso> solicitudes = solicitudRepository.findByUsuarioIdYEstado(usuarioId, EstadoSolicitud.COMPLETADO);
        int costoTotal = 0;

        for (SolicitudProceso solicitud : solicitudes) {
            if (solicitud.getProceso() != null) {
                costoTotal += solicitud.getProceso().getCosto();
            }
        }

        return costoTotal;
    }

    public int calcularCostoTotalPorUsuarioYRango(String usuarioId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<SolicitudProceso> solicitudes = solicitudRepository.findByUsuarioIdYRangoFechas(usuarioId, fechaInicio, fechaFin);
        int costoTotal = 0;

        for (SolicitudProceso solicitud : solicitudes) {
            if (solicitud.getEstado() == EstadoSolicitud.COMPLETADO && solicitud.getProceso() != null) {
                costoTotal += solicitud.getProceso().getCosto();
            }
        }

        return costoTotal;
    }

    public int[] obtenerEstadisticasPorUsuario(String usuarioId) {
        List<SolicitudProceso> solicitudes = solicitudRepository.findByUsuarioId(usuarioId);

        int total = solicitudes.size();
        int completadas = 0;
        int pendientes = 0;
        int errores = 0;

        for (SolicitudProceso solicitud : solicitudes) {
            switch (solicitud.getEstado()) {
                case COMPLETADO:
                    completadas++;
                    break;
                case PENDIENTE:
                    pendientes++;
                    break;
                case ERROR:
                    errores++;
                    break;
            }
        }

        return new int[]{total, completadas, pendientes, errores};
    }

    public void eliminarSolicitud(String solicitudId) {
        solicitudRepository.delete(solicitudId);
    }
}
