package modelo;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Modelo para la tabla Cassandra "control_por_sensor_tiempo".
 *
 * CQL sugerida:
 *
 * CREATE TABLE IF NOT EXISTS control_por_sensor_tiempo (
 *   sensor_id uuid,
 *   fecha_revision timestamp,
 *   id_control uuid,
 *   estado_sensor text,
 *   observaciones text,
 *   tecnico_id uuid,
 *   PRIMARY KEY (sensor_id, fecha_revision)
 * ) WITH CLUSTERING ORDER BY (fecha_revision DESC);
 *
 * Justificación:
 * - Partition key: sensor_id para agrupar todo el historial de controles por sensor.
 * - Clustering key: fecha_revision DESC para obtener primero la revisión más reciente.
 */
public class ControlPorSensorTiempo {

    private UUID sensorId;
    private LocalDateTime fechaRevision;
    private UUID idControl;
    private String estadoSensor; // 'activo', 'inactivo', 'falla'
    private String observaciones;
    private UUID tecnicoId; // referencia a técnico en MongoDB

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
