package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import modelo.EstadoFactura;
import modelo.EstadoSolicitud;
import modelo.Factura;
import modelo.Proceso;
import modelo.SolicitudProceso;
import repositories.FacturaRepository;

/**
 * Servicio para gestionar facturas de procesos.
 * Implementa el patrón Singleton.
 */
public class FacturaService {

    private static FacturaService instance;
    private final FacturaRepository facturaRepository;
    private final SolicitudProcesoService solicitudProcesoService;
    private final ProcesoService procesoService;

    private FacturaService() {
        this.facturaRepository = FacturaRepository.getInstance();
        this.solicitudProcesoService = SolicitudProcesoService.getInstance();
        this.procesoService = ProcesoService.getInstance();
    }

    public static FacturaService getInstance() {
        if (instance == null) {
            instance = new FacturaService();
        }
        return instance;
    }

    /**
     * Genera una factura para un usuario basándose en solicitudes completadas sin facturar
     */
    public Factura generarFacturaParaUsuario(String usuarioId, int diasVencimiento) {
        // Obtener todas las solicitudes completadas del usuario
        List<SolicitudProceso> solicitudesCompletadas = solicitudProcesoService
                .obtenerSolicitudesPorUsuarioYEstado(usuarioId, EstadoSolicitud.COMPLETADO);

        if (solicitudesCompletadas.isEmpty()) {
            System.out.println("No hay solicitudes completadas para facturar.");
            return null;
        }

        // Filtrar solo las no facturadas (esto requeriría un campo adicional en SolicitudProceso)
        // Por ahora, asumimos que todas las completadas se facturan
        
        List<String> procesosIds = new ArrayList<>();
        double montoTotal = 0.0;

        for (SolicitudProceso solicitud : solicitudesCompletadas) {
            procesosIds.add(solicitud.getId());
            
            // Obtener el costo del proceso
            Proceso proceso = procesoService.obtenerProcesoPorId(solicitud.getProcesoId());
            if (proceso != null) {
                montoTotal += proceso.getCosto();
            }
        }

        // Crear la factura
        Factura factura = new Factura();
        factura.setUsuarioId(usuarioId);
        factura.setFechaEmision(LocalDate.now());
        factura.setProcesosFacturadosIds(procesosIds);
        factura.setEstado(EstadoFactura.PENDIENTE);
        factura.setMontoTotal(montoTotal);
        factura.setFechaVencimiento(LocalDate.now().plusDays(diasVencimiento));

        facturaRepository.save(factura);
        return factura;
    }

    /**
     * Genera una factura basada en solicitudes en un rango de fechas
     */
    public Factura generarFacturaParaUsuarioPorPeriodo(String usuarioId, LocalDate fechaInicio, 
                                                       LocalDate fechaFin, int diasVencimiento) {
        // Convertir LocalDate a LocalDateTime
        java.time.LocalDateTime fechaInicioTime = fechaInicio.atStartOfDay();
        java.time.LocalDateTime fechaFinTime = fechaFin.atTime(23, 59, 59);
        
        List<SolicitudProceso> solicitudes = solicitudProcesoService
                .obtenerSolicitudesPorUsuarioYRango(usuarioId, fechaInicioTime, fechaFinTime);

        // Filtrar solo completadas
        List<SolicitudProceso> solicitudesCompletadas = solicitudes.stream()
                .filter(s -> s.getEstado() == EstadoSolicitud.COMPLETADO)
                .toList();

        if (solicitudesCompletadas.isEmpty()) {
            System.out.println("No hay solicitudes completadas en el período para facturar.");
            return null;
        }

        List<String> procesosIds = new ArrayList<>();
        double montoTotal = 0.0;

        for (SolicitudProceso solicitud : solicitudesCompletadas) {
            procesosIds.add(solicitud.getId());
            
            Proceso proceso = procesoService.obtenerProcesoPorId(solicitud.getProcesoId());
            if (proceso != null) {
                montoTotal += proceso.getCosto();
            }
        }

        Factura factura = new Factura();
        factura.setUsuarioId(usuarioId);
        factura.setFechaEmision(LocalDate.now());
        factura.setProcesosFacturadosIds(procesosIds);
        factura.setEstado(EstadoFactura.PENDIENTE);
        factura.setMontoTotal(montoTotal);
        factura.setFechaVencimiento(LocalDate.now().plusDays(diasVencimiento));

        facturaRepository.save(factura);
        return factura;
    }

    /**
     * Marca una factura como pagada
     */
    public void marcarFacturaComoPagada(String facturaId) {
        Factura factura = facturaRepository.findById(facturaId);
        if (factura != null) {
            factura.marcarComoPagada();
            facturaRepository.update(factura);
        }
    }

    /**
     * Actualiza facturas vencidas
     */
    public void actualizarFacturasVencidas() {
        List<Factura> facturasVencidas = facturaRepository.findFacturasVencidas();
        for (Factura factura : facturasVencidas) {
            factura.marcarComoVencida();
            facturaRepository.update(factura);
        }
        System.out.println("Se actualizaron " + facturasVencidas.size() + " facturas vencidas.");
    }

    /**
     * Obtiene todas las facturas de un usuario
     */
    public List<Factura> obtenerFacturasPorUsuario(String usuarioId) {
        return facturaRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Obtiene facturas por estado
     */
    public List<Factura> obtenerFacturasPorEstado(EstadoFactura estado) {
        return facturaRepository.findByEstado(estado);
    }

    /**
     * Obtiene facturas pendientes de un usuario
     */
    public List<Factura> obtenerFacturasPendientesPorUsuario(String usuarioId) {
        return facturaRepository.findByUsuarioIdYEstado(usuarioId, EstadoFactura.PENDIENTE);
    }

    /**
     * Obtiene facturas pagadas de un usuario
     */
    public List<Factura> obtenerFacturasPagadasPorUsuario(String usuarioId) {
        return facturaRepository.findByUsuarioIdYEstado(usuarioId, EstadoFactura.PAGADA);
    }

    /**
     * Obtiene facturas vencidas de un usuario
     */
    public List<Factura> obtenerFacturasVencidasPorUsuario(String usuarioId) {
        return facturaRepository.findByUsuarioIdYEstado(usuarioId, EstadoFactura.VENCIDA);
    }

    /**
     * Obtiene una factura por ID
     */
    public Factura obtenerFacturaPorId(String facturaId) {
        return facturaRepository.findById(facturaId);
    }

    /**
     * Calcula el monto total adeudado por un usuario (facturas pendientes y vencidas)
     */
    public double calcularMontoAdeudadoPorUsuario(String usuarioId) {
        List<Factura> pendientes = obtenerFacturasPendientesPorUsuario(usuarioId);
        List<Factura> vencidas = obtenerFacturasVencidasPorUsuario(usuarioId);
        
        double montoPendiente = pendientes.stream().mapToDouble(Factura::getMontoTotal).sum();
        double montoVencido = vencidas.stream().mapToDouble(Factura::getMontoTotal).sum();
        
        return montoPendiente + montoVencido;
    }

    /**
     * Obtiene estadísticas de facturación de un usuario
     * Retorna [total facturas, pendientes, pagadas, vencidas, monto total facturado, monto adeudado]
     */
    public double[] obtenerEstadisticasFacturacionPorUsuario(String usuarioId) {
        List<Factura> todasFacturas = obtenerFacturasPorUsuario(usuarioId);
        
        long totalFacturas = todasFacturas.size();
        long pendientes = todasFacturas.stream().filter(f -> f.getEstado() == EstadoFactura.PENDIENTE).count();
        long pagadas = todasFacturas.stream().filter(f -> f.getEstado() == EstadoFactura.PAGADA).count();
        long vencidas = todasFacturas.stream().filter(f -> f.getEstado() == EstadoFactura.VENCIDA).count();
        
        double montoTotalFacturado = todasFacturas.stream().mapToDouble(Factura::getMontoTotal).sum();
        double montoAdeudado = calcularMontoAdeudadoPorUsuario(usuarioId);
        
        return new double[]{totalFacturas, pendientes, pagadas, vencidas, montoTotalFacturado, montoAdeudado};
    }

    /**
     * Obtiene el detalle de procesos facturados en una factura
     */
    public List<SolicitudProceso> obtenerDetalleProcesosDeLaFactura(String facturaId) {
        Factura factura = facturaRepository.findById(facturaId);
        if (factura == null) {
            return new ArrayList<>();
        }

        List<SolicitudProceso> solicitudes = new ArrayList<>();
        for (String solicitudId : factura.getProcesosFacturadosIds()) {
            SolicitudProceso solicitud = solicitudProcesoService.obtenerSolicitudPorId(solicitudId);
            if (solicitud != null) {
                solicitudes.add(solicitud);
            }
        }
        
        return solicitudes;
    }

    /**
     * Elimina una factura
     */
    public void eliminarFactura(String facturaId) {
        facturaRepository.delete(facturaId);
    }
}
