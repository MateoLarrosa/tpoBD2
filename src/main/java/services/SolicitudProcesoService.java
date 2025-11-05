package services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import modelo.EstadoSolicitud;
import modelo.Proceso;
import modelo.SolicitudProceso;
import repositories.SolicitudProcesoRepository;

/**
 * Servicio para gestionar las solicitudes de procesos realizadas por usuarios.
 * Implementa el patrón Singleton.
 */
public class SolicitudProcesoService {

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

    /**
     * Crea una nueva solicitud de proceso
     * @param usuarioId ID del usuario que realiza la solicitud
     * @param nombreProceso Nombre del proceso a ejecutar
     * @param parametros Parámetros de la solicitud
     * @return La solicitud creada
     */
    public SolicitudProceso crearSolicitud(String usuarioId, String nombreProceso, Map<String, Object> parametros) {
        // Buscar el proceso en el catálogo
        Proceso proceso = procesoService.obtenerProcesoPorNombre(nombreProceso);
        
        if (proceso == null) {
            throw new IllegalArgumentException("Proceso no encontrado: " + nombreProceso);
        }

        // Crear la solicitud
        SolicitudProceso solicitud = new SolicitudProceso(usuarioId, proceso.getId(), parametros);
        solicitudRepository.save(solicitud);
        
        return solicitud;
    }

    /**
     * Marca una solicitud como completada
     * @param solicitudId ID de la solicitud
     * @param resultado Resultado de la ejecución
     * @param tiempoEjecucionMs Tiempo de ejecución en milisegundos
     */
    public void completarSolicitud(String solicitudId, String resultado, long tiempoEjecucionMs) {
        SolicitudProceso solicitud = solicitudRepository.findById(solicitudId);
        if (solicitud == null) {
            throw new IllegalArgumentException("Solicitud no encontrada: " + solicitudId);
        }

        solicitud.completar(resultado, tiempoEjecucionMs);
        solicitudRepository.update(solicitud);
    }

    /**
     * Marca una solicitud como error
     * @param solicitudId ID de la solicitud
     * @param mensajeError Mensaje de error
     * @param tiempoEjecucionMs Tiempo de ejecución en milisegundos
     */
    public void marcarError(String solicitudId, String mensajeError, long tiempoEjecucionMs) {
        SolicitudProceso solicitud = solicitudRepository.findById(solicitudId);
        if (solicitud == null) {
            throw new IllegalArgumentException("Solicitud no encontrada: " + solicitudId);
        }

        solicitud.marcarError(mensajeError, tiempoEjecucionMs);
        solicitudRepository.update(solicitud);
    }

    /**
     * Registra una solicitud de proceso completa (crear, ejecutar y completar en un solo paso)
     * Útil para operaciones síncronas
     * @param usuarioId ID del usuario
     * @param nombreProceso Nombre del proceso
     * @param parametros Parámetros de la solicitud
     * @param resultado Resultado de la ejecución
     * @param tiempoEjecucionMs Tiempo de ejecución
     * @return La solicitud completada
     */
    public SolicitudProceso registrarSolicitudCompletada(String usuarioId, String nombreProceso, 
                                                         Map<String, Object> parametros, 
                                                         String resultado, long tiempoEjecucionMs) {
        SolicitudProceso solicitud = crearSolicitud(usuarioId, nombreProceso, parametros);
        solicitud.completar(resultado, tiempoEjecucionMs);
        solicitudRepository.update(solicitud);
        return solicitud;
    }

    /**
     * Registra una solicitud de proceso con error
     * @param usuarioId ID del usuario
     * @param nombreProceso Nombre del proceso
     * @param parametros Parámetros de la solicitud
     * @param mensajeError Mensaje de error
     * @param tiempoEjecucionMs Tiempo de ejecución
     * @return La solicitud con error
     */
    public SolicitudProceso registrarSolicitudError(String usuarioId, String nombreProceso, 
                                                    Map<String, Object> parametros, 
                                                    String mensajeError, long tiempoEjecucionMs) {
        SolicitudProceso solicitud = crearSolicitud(usuarioId, nombreProceso, parametros);
        solicitud.marcarError(mensajeError, tiempoEjecucionMs);
        solicitudRepository.update(solicitud);
        return solicitud;
    }

    /**
     * Obtiene una solicitud por su ID
     * @param solicitudId ID de la solicitud
     * @return La solicitud encontrada
     */
    public SolicitudProceso obtenerSolicitudPorId(String solicitudId) {
        return solicitudRepository.findById(solicitudId);
    }

    /**
     * Obtiene todas las solicitudes de un usuario
     * @param usuarioId ID del usuario
     * @return Lista de solicitudes del usuario
     */
    public List<SolicitudProceso> obtenerSolicitudesPorUsuario(String usuarioId) {
        return solicitudRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Obtiene solicitudes por estado
     * @param estado Estado de la solicitud
     * @return Lista de solicitudes con ese estado
     */
    public List<SolicitudProceso> obtenerSolicitudesPorEstado(EstadoSolicitud estado) {
        return solicitudRepository.findByEstado(estado);
    }

    /**
     * Obtiene solicitudes de un usuario por estado
     * @param usuarioId ID del usuario
     * @param estado Estado de la solicitud
     * @return Lista de solicitudes
     */
    public List<SolicitudProceso> obtenerSolicitudesPorUsuarioYEstado(String usuarioId, EstadoSolicitud estado) {
        return solicitudRepository.findByUsuarioIdYEstado(usuarioId, estado);
    }

    /**
     * Obtiene solicitudes de un usuario en un rango de fechas
     * @param usuarioId ID del usuario
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Lista de solicitudes en el rango
     */
    public List<SolicitudProceso> obtenerSolicitudesPorUsuarioYRango(String usuarioId, 
                                                                     LocalDateTime fechaInicio, 
                                                                     LocalDateTime fechaFin) {
        return solicitudRepository.findByUsuarioIdYRangoFechas(usuarioId, fechaInicio, fechaFin);
    }

    /**
     * Obtiene todas las solicitudes registradas
     * @return Lista de todas las solicitudes
     */
    public List<SolicitudProceso> obtenerTodasSolicitudes() {
        return solicitudRepository.findAll();
    }

    /**
     * Calcula el costo total de todas las solicitudes completadas de un usuario
     * @param usuarioId ID del usuario
     * @return Costo total
     */
    public int calcularCostoTotalPorUsuario(String usuarioId) {
        List<SolicitudProceso> solicitudes = solicitudRepository.findByUsuarioIdYEstado(usuarioId, EstadoSolicitud.COMPLETADO);
        int costoTotal = 0;
        
        for (SolicitudProceso solicitud : solicitudes) {
            Proceso proceso = procesoService.obtenerProcesoPorId(solicitud.getProcesoId());
            if (proceso != null) {
                costoTotal += proceso.getCosto();
            }
        }
        
        return costoTotal;
    }

    /**
     * Calcula el costo total en un rango de fechas para un usuario
     * @param usuarioId ID del usuario
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Costo total en el rango
     */
    public int calcularCostoTotalPorUsuarioYRango(String usuarioId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<SolicitudProceso> solicitudes = solicitudRepository.findByUsuarioIdYRangoFechas(usuarioId, fechaInicio, fechaFin);
        int costoTotal = 0;
        
        for (SolicitudProceso solicitud : solicitudes) {
            if (solicitud.getEstado() == EstadoSolicitud.COMPLETADO) {
                Proceso proceso = procesoService.obtenerProcesoPorId(solicitud.getProcesoId());
                if (proceso != null) {
                    costoTotal += proceso.getCosto();
                }
            }
        }
        
        return costoTotal;
    }

    /**
     * Obtiene estadísticas de solicitudes de un usuario
     * @param usuarioId ID del usuario
     * @return Array con [total, completadas, pendientes, errores]
     */
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

    /**
     * Elimina una solicitud
     * @param solicitudId ID de la solicitud a eliminar
     */
    public void eliminarSolicitud(String solicitudId) {
        solicitudRepository.delete(solicitudId);
    }
}
