package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import modelo.Factura;
import modelo.Pago;
import repositories.PagoRepository;

/**
 * Servicio para gestionar pagos de facturas.
 * Implementa el patrón Singleton.
 */
public class PagoService {

    private static PagoService instance;
    private final PagoRepository pagoRepository;
    private final FacturaService facturaService;

    private PagoService() {
        this.pagoRepository = PagoRepository.getInstance();
        this.facturaService = FacturaService.getInstance();
    }

    public static PagoService getInstance() {
        if (instance == null) {
            instance = new PagoService();
        }
        return instance;
    }

    /**
     * Registra un pago para una factura
     */
    public Pago registrarPago(String facturaId, double montoPagado, String metodoPago) {
        // Verificar que la factura existe
        Factura factura = facturaService.obtenerFacturaPorId(facturaId);
        if (factura == null) {
            System.out.println("Error: La factura no existe.");
            return null;
        }

        // Verificar que el monto pagado coincide con el monto de la factura
        if (montoPagado != factura.getMontoTotal()) {
            System.out.println("Advertencia: El monto pagado ($" + montoPagado + 
                             ") no coincide con el monto de la factura ($" + factura.getMontoTotal() + ")");
        }

        // Crear el pago
        Pago pago = new Pago();
        pago.setFacturaId(facturaId);
        pago.setFechaPago(LocalDate.now());
        pago.setMontoPagado(montoPagado);
        pago.setMetodoPago(metodoPago);

        pagoRepository.save(pago);

        // Marcar la factura como pagada
        facturaService.marcarFacturaComoPagada(facturaId);

        System.out.println("Pago registrado exitosamente por $" + montoPagado + " usando " + metodoPago);
        return pago;
    }

    /**
     * Obtiene el pago asociado a una factura
     */
    public Pago obtenerPagoPorFactura(String facturaId) {
        return pagoRepository.findByFacturaId(facturaId);
    }

    /**
     * Obtiene un pago por ID
     */
    public Pago obtenerPagoPorId(String pagoId) {
        return pagoRepository.findById(pagoId);
    }

    /**
     * Obtiene todos los pagos
     */
    public List<Pago> obtenerTodosPagos() {
        return pagoRepository.findAll();
    }

    /**
     * Obtiene pagos por método de pago
     */
    public List<Pago> obtenerPagosPorMetodo(String metodoPago) {
        return pagoRepository.findByMetodoPago(metodoPago);
    }

    /**
     * Obtiene pagos en un rango de fechas
     */
    public List<Pago> obtenerPagosPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return pagoRepository.findByRangoFechas(fechaInicio, fechaFin);
    }

    /**
     * Calcula el total pagado en un rango de fechas
     */
    public double calcularTotalPagadoEnRango(LocalDate fechaInicio, LocalDate fechaFin) {
        return pagoRepository.calcularMontoTotalPagado(fechaInicio, fechaFin);
    }

    /**
     * Verifica si una factura está pagada
     */
    public boolean facturaEstaPagada(String facturaId) {
        Pago pago = pagoRepository.findByFacturaId(facturaId);
        return pago != null;
    }

    /**
     * Obtiene estadísticas de pagos por método
     * Útil para reportes de administración
     */
    public void mostrarEstadisticasPorMetodo() {
        List<String> metodos = List.of("Tarjeta de crédito", "Tarjeta de débito", 
                                       "Transferencia bancaria", "Efectivo", "MercadoPago");
        
        System.out.println("\n--- ESTADÍSTICAS DE PAGOS POR MÉTODO ---");
        for (String metodo : metodos) {
            List<Pago> pagos = obtenerPagosPorMetodo(metodo);
            double total = pagos.stream().mapToDouble(Pago::getMontoPagado).sum();
            System.out.printf("%s: %d pagos - Total: $%.2f\n", metodo, pagos.size(), total);
        }
    }

    /**
     * Elimina un pago
     */
    public void eliminarPago(String pagoId) {
        pagoRepository.delete(pagoId);
    }
}
