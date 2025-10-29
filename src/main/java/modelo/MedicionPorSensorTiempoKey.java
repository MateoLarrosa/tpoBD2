package modelo;

import java.time.Instant;
import java.util.UUID;

public class MedicionPorSensorTiempoKey {

    private String sensorId;
    private Instant fechaHora;
    private UUID medicionId;
    private float temperatura;
    private float humedad;
    public MedicionPorSensorTiempoKey() {
    }

    public MedicionPorSensorTiempoKey(String sensorId, Instant fechaHora, UUID medicionId, float temperatura, float humedad) {
        this.sensorId = sensorId;
        this.fechaHora = fechaHora;
        this.medicionId = medicionId;
        this.temperatura = temperatura;
        this.humedad = humedad;
    }

}
