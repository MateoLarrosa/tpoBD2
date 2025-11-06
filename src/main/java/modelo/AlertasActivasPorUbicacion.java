package modelo;

import java.time.LocalDate;
import java.util.UUID;

public class AlertasActivasPorUbicacion {

    private String pais;
    private String estado; 
    private LocalDate fechaHora;
    private UUID alertaId;
    private String tipo; 
    private String descripcion;

    public AlertasActivasPorUbicacion() {
    }

    public AlertasActivasPorUbicacion(String pais, String estado, LocalDate fechaHora, UUID alertaId, String tipo, String descripcion) {
        this.pais = pais;
        this.estado = estado;
        this.fechaHora = fechaHora;
        this.alertaId = alertaId;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

}
