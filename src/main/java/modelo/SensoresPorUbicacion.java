package modelo;

import java.time.LocalDateTime;
import java.util.UUID;

public class SensoresPorUbicacion {

    private String tipoUbicacion;
    private String nombreUbicacion;
    private String estadoSensor;
    private UUID sensorId;
    private String tipoSensor;
    private LocalDateTime fechaInicioEmision;

    public SensoresPorUbicacion() {
    }

    public SensoresPorUbicacion(String tipoUbicacion, String nombreUbicacion, String estadoSensor, UUID sensorId, String tipoSensor, LocalDateTime fechaInicioEmision) {
        this.tipoUbicacion = tipoUbicacion;
        this.nombreUbicacion = nombreUbicacion;
        this.estadoSensor = estadoSensor;
        this.sensorId = sensorId;
        this.tipoSensor = tipoSensor;
        this.fechaInicioEmision = fechaInicioEmision;
    }

}
