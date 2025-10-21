package modelo;

import java.time.LocalDate;

public class Factura {

    private String id;
    private EstadoFactura estado;
    private LocalDate fechaEmision;

    public Factura(String id, EstadoFactura estado, LocalDate fechaEmision) {
        this.id = id;
        this.estado = estado;
        this.fechaEmision = fechaEmision;
    }

}
