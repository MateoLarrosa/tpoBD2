package modelo;

import java.time.LocalDate;

public class Pago {

    private String id;
    private double monto;
    private LocalDate fechaPago;
    private String metodoPago;
    private Factura factura;

    public Pago(String id, double monto, LocalDate fechaPago, String metodoPago) {
        this.id = id;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.metodoPago = metodoPago;
    }

}
