package controlador;

import java.time.LocalDate;
import java.util.List;

import modelo.Pago;
import services.PagoService;

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

    public Pago registrarPago(String facturaId, double montoPagado, String metodoPago) {
        return pagoService.registrarPago(facturaId, montoPagado, metodoPago);
    }

    public Pago obtenerPagoPorFactura(String facturaId) {
        return pagoService.obtenerPagoPorFactura(facturaId);
    }

    public Pago obtenerPagoPorId(String pagoId) {
        return pagoService.obtenerPagoPorId(pagoId);
    }

    public List<Pago> obtenerTodosPagos() {
        return pagoService.obtenerTodosPagos();
    }

    public List<Pago> obtenerPagosPorMetodo(String metodoPago) {
        return pagoService.obtenerPagosPorMetodo(metodoPago);
    }

    public List<Pago> obtenerPagosPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return pagoService.obtenerPagosPorRangoFechas(fechaInicio, fechaFin);
    }

    public double calcularTotalPagadoEnRango(LocalDate fechaInicio, LocalDate fechaFin) {
        return pagoService.calcularTotalPagadoEnRango(fechaInicio, fechaFin);
    }

    public boolean facturaEstaPagada(String facturaId) {
        return pagoService.facturaEstaPagada(facturaId);
    }

    public void mostrarEstadisticasPorMetodo() {
        pagoService.mostrarEstadisticasPorMetodo();
    }

    public void eliminarPago(String pagoId) {
        pagoService.eliminarPago(pagoId);
    }
}
