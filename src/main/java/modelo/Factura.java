package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Factura {

    private String id;
    private String usuarioId;
    private LocalDate fechaEmision;
    private List<SolicitudProceso> solicitudesFacturadas;
    private EstadoFactura estado;
    private double montoTotal;
    private LocalDate fechaVencimiento;

    public Factura() {
        this.solicitudesFacturadas = new ArrayList<>();
        this.estado = EstadoFactura.PENDIENTE;
    }

    public Factura(String id, String usuarioId, LocalDate fechaEmision, List<SolicitudProceso> solicitudesFacturadas,
            EstadoFactura estado, double montoTotal, LocalDate fechaVencimiento) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.fechaEmision = fechaEmision;
        this.solicitudesFacturadas = solicitudesFacturadas != null ? solicitudesFacturadas : new ArrayList<>();
        this.estado = estado;
        this.montoTotal = montoTotal;
        this.fechaVencimiento = fechaVencimiento;
    }

    public void agregarSolicitudFacturada(SolicitudProceso solicitud) {
        if (solicitud != null && !this.solicitudesFacturadas.contains(solicitud)) {
            this.solicitudesFacturadas.add(solicitud);
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

    public List<SolicitudProceso> getSolicitudesFacturadas() {
        return solicitudesFacturadas;
    }

    public void setSolicitudesFacturadas(List<SolicitudProceso> solicitudesFacturadas) {
        this.solicitudesFacturadas = solicitudesFacturadas;
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
        return "Factura{"
                + "id='" + id + '\''
                + ", usuarioId='" + usuarioId + '\''
                + ", fechaEmision=" + fechaEmision
                + ", solicitudesFacturadas=" + solicitudesFacturadas.size()
                + ", estado=" + estado
                + ", montoTotal=" + montoTotal
                + ", fechaVencimiento=" + fechaVencimiento
                + '}';
    }
}
