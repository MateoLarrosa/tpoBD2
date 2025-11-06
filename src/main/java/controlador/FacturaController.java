package controlador;

import java.time.LocalDate;
import java.util.List;

import modelo.EstadoFactura;
import modelo.Factura;
import modelo.SolicitudProceso;
import services.FacturaService;

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

    public Factura generarFacturaParaUsuario(String usuarioId, int diasVencimiento) {
        return facturaService.generarFacturaParaUsuario(usuarioId, diasVencimiento);
    }

    public Factura generarFacturaPorPeriodo(String usuarioId, LocalDate fechaInicio,
            LocalDate fechaFin, int diasVencimiento) {
        return facturaService.generarFacturaParaUsuarioPorPeriodo(usuarioId, fechaInicio, fechaFin, diasVencimiento);
    }

    public void marcarFacturaComoPagada(String facturaId) {
        facturaService.marcarFacturaComoPagada(facturaId);
    }

    public void actualizarFacturasVencidas() {
        facturaService.actualizarFacturasVencidas();
    }

    public List<Factura> obtenerFacturasPorUsuario(String usuarioId) {
        return facturaService.obtenerFacturasPorUsuario(usuarioId);
    }

    public List<Factura> obtenerFacturasPorEstado(EstadoFactura estado) {
        return facturaService.obtenerFacturasPorEstado(estado);
    }

    public List<Factura> obtenerFacturasPendientes(String usuarioId) {
        return facturaService.obtenerFacturasPendientesPorUsuario(usuarioId);
    }

    public List<Factura> obtenerFacturasPagadas(String usuarioId) {
        return facturaService.obtenerFacturasPagadasPorUsuario(usuarioId);
    }

    public List<Factura> obtenerFacturasVencidas(String usuarioId) {
        return facturaService.obtenerFacturasVencidasPorUsuario(usuarioId);
    }

    public Factura obtenerFacturaPorId(String facturaId) {
        return facturaService.obtenerFacturaPorId(facturaId);
    }

    public double calcularMontoAdeudado(String usuarioId) {
        return facturaService.calcularMontoAdeudadoPorUsuario(usuarioId);
    }

    public double[] obtenerEstadisticasFacturacion(String usuarioId) {
        return facturaService.obtenerEstadisticasFacturacionPorUsuario(usuarioId);
    }

    public List<SolicitudProceso> obtenerDetalleProcesosDeLaFactura(String facturaId) {
        return facturaService.obtenerDetalleProcesosDeLaFactura(facturaId);
    }

    public void eliminarFactura(String facturaId) {
        facturaService.eliminarFactura(facturaId);
    }
}
