package controlador;

import java.time.LocalDate;
import java.util.List;

import modelo.Pago;
import services.PagoService;

/**
 * Controlador para gestionar operaciones de pagos.
 * Actúa como fachada sobre el PagoService.
 * Implementa el patrón Singleton.
 */
public class PagoController {

    private static PagoController instance;
    private final PagoService pagoService;

    private PagoController() {
        this.pagoService = PagoService.getInstance();
    }

    public static PagoController getInstance() {
        if (instance == null) {
            instance = new PagoController();
        }
        return instance;
    }

    /**
     * Registra un pago para una factura
     */
    public Pago registrarPago(String facturaId, double montoPagado, String metodoPago) {
        return pagoService.registrarPago(facturaId, montoPagado, metodoPago);
    }

    /**
     * Obtiene el pago asociado a una factura
     */
    public Pago obtenerPagoPorFactura(String facturaId) {
        return pagoService.obtenerPagoPorFactura(facturaId);
    }

    /**
     * Obtiene un pago por ID
     */
    public Pago obtenerPagoPorId(String pagoId) {
        return pagoService.obtenerPagoPorId(pagoId);
    }

    /**
     * Obtiene todos los pagos
     */
    public List<Pago> obtenerTodosPagos() {
        return pagoService.obtenerTodosPagos();
    }

    /**
     * Obtiene pagos por método de pago
     */
    public List<Pago> obtenerPagosPorMetodo(String metodoPago) {
        return pagoService.obtenerPagosPorMetodo(metodoPago);
    }

    /**
     * Obtiene pagos en un rango de fechas
     */
    public List<Pago> obtenerPagosPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return pagoService.obtenerPagosPorRangoFechas(fechaInicio, fechaFin);
    }

    /**
     * Calcula el total pagado en un rango de fechas
     */
    public double calcularTotalPagadoEnRango(LocalDate fechaInicio, LocalDate fechaFin) {
        return pagoService.calcularTotalPagadoEnRango(fechaInicio, fechaFin);
    }

    /**
     * Verifica si una factura está pagada
     */
    public boolean facturaEstaPagada(String facturaId) {
        return pagoService.facturaEstaPagada(facturaId);
    }

    /**
     * Muestra estadísticas de pagos por método
     */
    public void mostrarEstadisticasPorMetodo() {
        pagoService.mostrarEstadisticasPorMetodo();
    }

    /**
     * Elimina un pago
     */
    public void eliminarPago(String pagoId) {
        pagoService.eliminarPago(pagoId);
    }
}
