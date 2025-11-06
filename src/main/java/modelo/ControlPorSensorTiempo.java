package modelo;

import java.time.LocalDateTime;
import java.util.UUID;

public class ControlPorSensorTiempo {

    private UUID sensorId;
    private LocalDateTime fechaRevision;
    private UUID idControl;
    private String estadoSensor;
    private String observaciones;
    private UUID tecnicoId;

    public ControlPorSensorTiempo() {
    }

    public ControlPorSensorTiempo(UUID sensorId, LocalDateTime fechaRevision, UUID idControl, String estadoSensor, String observaciones, UUID tecnicoId) {
        this.sensorId = sensorId;
        this.fechaRevision = fechaRevision;
        this.idControl = idControl;
        this.estadoSensor = estadoSensor;
        this.observaciones = observaciones;
        this.tecnicoId = tecnicoId;
    }

}
