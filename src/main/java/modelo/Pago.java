package modelo;

import java.time.LocalDate;

public class Pago {

    private String id;
    private String facturaId;
    private LocalDate fechaPago;
    private double montoPagado;
    private String metodoPago;

    public Pago() {
        this.fechaPago = LocalDate.now();
    }

    public Pago(String id, String facturaId, LocalDate fechaPago, double montoPagado, String metodoPago) {
        this.id = id;
        this.facturaId = facturaId;
        this.fechaPago = fechaPago;
        this.montoPagado = montoPagado;
        this.metodoPago = metodoPago;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(String facturaId) {
        this.facturaId = facturaId;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    @Override
    public String toString() {
        return "Pago{" +
                "id='" + id + '\'' +
                ", facturaId='" + facturaId + '\'' +
                ", fechaPago=" + fechaPago +
                ", montoPagado=" + montoPagado +
                ", metodoPago='" + metodoPago + '\'' +
                '}';
    }
}
