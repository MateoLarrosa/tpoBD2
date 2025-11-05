package controlador;

import java.time.LocalDate;
import java.util.List;

import modelo.EstadoFactura;
import modelo.Factura;
import modelo.SolicitudProceso;
import services.FacturaService;

/**
 * Controlador para gestionar operaciones de facturación.
 * Actúa como fachada sobre el FacturaService.
 * Implementa el patrón Singleton.
 */
public class FacturaController {

    private static FacturaController instance;
    private final FacturaService facturaService;

    private FacturaController() {
        this.facturaService = FacturaService.getInstance();
    }

    public static FacturaController getInstance() {
        if (instance == null) {
            instance = new FacturaController();
        }
        return instance;
    }

    /**
     * Genera una factura para un usuario con todas sus solicitudes completadas
     */
    public Factura generarFacturaParaUsuario(String usuarioId, int diasVencimiento) {
        return facturaService.generarFacturaParaUsuario(usuarioId, diasVencimiento);
    }

    /**
     * Genera una factura para un usuario en un período específico
     */
    public Factura generarFacturaPorPeriodo(String usuarioId, LocalDate fechaInicio, 
                                            LocalDate fechaFin, int diasVencimiento) {
        return facturaService.generarFacturaParaUsuarioPorPeriodo(usuarioId, fechaInicio, fechaFin, diasVencimiento);
    }

    /**
     * Marca una factura como pagada
     */
    public void marcarFacturaComoPagada(String facturaId) {
        facturaService.marcarFacturaComoPagada(facturaId);
    }

    /**
     * Actualiza todas las facturas vencidas en el sistema
     */
    public void actualizarFacturasVencidas() {
        facturaService.actualizarFacturasVencidas();
    }

    /**
     * Obtiene todas las facturas de un usuario
     */
    public List<Factura> obtenerFacturasPorUsuario(String usuarioId) {
        return facturaService.obtenerFacturasPorUsuario(usuarioId);
    }

    /**
     * Obtiene facturas por estado
     */
    public List<Factura> obtenerFacturasPorEstado(EstadoFactura estado) {
        return facturaService.obtenerFacturasPorEstado(estado);
    }

    /**
     * Obtiene facturas pendientes de un usuario
     */
    public List<Factura> obtenerFacturasPendientes(String usuarioId) {
        return facturaService.obtenerFacturasPendientesPorUsuario(usuarioId);
    }

    /**
     * Obtiene facturas pagadas de un usuario
     */
    public List<Factura> obtenerFacturasPagadas(String usuarioId) {
        return facturaService.obtenerFacturasPagadasPorUsuario(usuarioId);
    }

    /**
     * Obtiene facturas vencidas de un usuario
     */
    public List<Factura> obtenerFacturasVencidas(String usuarioId) {
        return facturaService.obtenerFacturasVencidasPorUsuario(usuarioId);
    }

    /**
     * Obtiene una factura por ID
     */
    public Factura obtenerFacturaPorId(String facturaId) {
        return facturaService.obtenerFacturaPorId(facturaId);
    }

    /**
     * Calcula el monto total adeudado por un usuario
     */
    public double calcularMontoAdeudado(String usuarioId) {
        return facturaService.calcularMontoAdeudadoPorUsuario(usuarioId);
    }

    /**
     * Obtiene estadísticas de facturación de un usuario
     */
    public double[] obtenerEstadisticasFacturacion(String usuarioId) {
        return facturaService.obtenerEstadisticasFacturacionPorUsuario(usuarioId);
    }

    /**
     * Obtiene el detalle de procesos de una factura
     */
    public List<SolicitudProceso> obtenerDetalleProcesosDeLaFactura(String facturaId) {
        return facturaService.obtenerDetalleProcesosDeLaFactura(facturaId);
    }

    /**
     * Elimina una factura
     */
    public void eliminarFactura(String facturaId) {
        facturaService.eliminarFactura(facturaId);
    }
}
