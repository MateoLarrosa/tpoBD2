package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Factura {

    private String id;
    private String usuarioId;
    private LocalDate fechaEmision;
    private List<String> procesosFacturadosIds; // IDs de SolicitudProceso
    private EstadoFactura estado;
    private double montoTotal;
    private LocalDate fechaVencimiento;

    public Factura() {
        this.procesosFacturadosIds = new ArrayList<>();
        this.estado = EstadoFactura.PENDIENTE;
    }

    public Factura(String id, String usuarioId, LocalDate fechaEmision, List<String> procesosFacturadosIds, 
                   EstadoFactura estado, double montoTotal, LocalDate fechaVencimiento) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.fechaEmision = fechaEmision;
        this.procesosFacturadosIds = procesosFacturadosIds != null ? procesosFacturadosIds : new ArrayList<>();
        this.estado = estado;
        this.montoTotal = montoTotal;
        this.fechaVencimiento = fechaVencimiento;
    }

    // Métodos de negocio
    public void agregarProcesoFacturado(String solicitudProcesoId) {
        if (!this.procesosFacturadosIds.contains(solicitudProcesoId)) {
            this.procesosFacturadosIds.add(solicitudProcesoId);
        }
    }

    public void marcarComoPagada() {
        this.estado = EstadoFactura.PAGADA;
    }

    public void marcarComoVencida() {
        this.estado = EstadoFactura.VENCIDA;
    }

    public boolean estaVencida() {
        return LocalDate.now().isAfter(fechaVencimiento) && estado == EstadoFactura.PENDIENTE;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public List<String> getProcesosFacturadosIds() {
        return procesosFacturadosIds;
    }

    public void setProcesosFacturadosIds(List<String> procesosFacturadosIds) {
        this.procesosFacturadosIds = procesosFacturadosIds;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override
    public String toString() {
        return "Factura{" +
                "id='" + id + '\'' +
                ", usuarioId='" + usuarioId + '\'' +
                ", fechaEmision=" + fechaEmision +
                ", procesosFacturados=" + procesosFacturadosIds.size() +
                ", estado=" + estado +
                ", montoTotal=" + montoTotal +
                ", fechaVencimiento=" + fechaVencimiento +
                '}';
    }
}
