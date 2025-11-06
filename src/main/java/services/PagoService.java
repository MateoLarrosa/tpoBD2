package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import modelo.Factura;
import modelo.Pago;
import repositories.PagoRepository;

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

    public Pago registrarPago(String facturaId, double montoPagado, String metodoPago) {
        
        Factura factura = facturaService.obtenerFacturaPorId(facturaId);
        if (factura == null) {
            System.out.println("Error: La factura no existe.");
            return null;
        }

        
        if (montoPagado != factura.getMontoTotal()) {
            System.out.println("Advertencia: El monto pagado ($" + montoPagado + 
                             ") no coincide con el monto de la factura ($" + factura.getMontoTotal() + ")");
        }

        
        Pago pago = new Pago();
        pago.setFacturaId(facturaId);
        pago.setFechaPago(LocalDate.now());
        pago.setMontoPagado(montoPagado);
        pago.setMetodoPago(metodoPago);

        pagoRepository.save(pago);

        
        facturaService.marcarFacturaComoPagada(facturaId);

        System.out.println("Pago registrado exitosamente por $" + montoPagado + " usando " + metodoPago);
        return pago;
    }

    public Pago obtenerPagoPorFactura(String facturaId) {
        return pagoRepository.findByFacturaId(facturaId);
    }

    public Pago obtenerPagoPorId(String pagoId) {
        return pagoRepository.findById(pagoId);
    }

    public List<Pago> obtenerTodosPagos() {
        return pagoRepository.findAll();
    }

    public List<Pago> obtenerPagosPorMetodo(String metodoPago) {
        return pagoRepository.findByMetodoPago(metodoPago);
    }

    public List<Pago> obtenerPagosPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return pagoRepository.findByRangoFechas(fechaInicio, fechaFin);
    }

    public double calcularTotalPagadoEnRango(LocalDate fechaInicio, LocalDate fechaFin) {
        return pagoRepository.calcularMontoTotalPagado(fechaInicio, fechaFin);
    }

    public boolean facturaEstaPagada(String facturaId) {
        Pago pago = pagoRepository.findByFacturaId(facturaId);
        return pago != null;
    }

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

    public void eliminarPago(String pagoId) {
        pagoRepository.delete(pagoId);
    }
}
