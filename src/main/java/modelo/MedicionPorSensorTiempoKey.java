package modelo;

import java.time.Instant;
import java.util.UUID;

public class MedicionPorSensorTiempoKey {

    // Clave Primaria (Partition Key)
    private String sensorId; // Usamos String para ser coherentes con el ID del sensor

    // Clave Primaria (Clustering Key)
    private Instant fechaHora; // Corresponde al 'timestamp' de CQL

    // Atributos de la medición
    private UUID medicionId; // ID de medición
    private float temperatura; // Temperatura
    private float humedad; // Humedad

    // Constructor vacío (necesario para la mayoría de los mapeadores)
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
