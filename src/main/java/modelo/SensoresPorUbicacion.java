package modelo;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Modelo para la tabla Cassandra "sensores_por_ubicacion".
 *
 * CQL sugerida:
 *
 * CREATE TABLE IF NOT EXISTS sensores_por_ubicacion (
 *   tipo_ubicacion text,
 *   nombre_ubicacion text,
 *   estado_sensor text,
 *   sensor_id uuid,
 *   tipo_sensor text,
 *   fecha_inicio_emision timestamp,
 *   PRIMARY KEY ((tipo_ubicacion, nombre_ubicacion), estado_sensor, sensor_id)
 * ) WITH CLUSTERING ORDER BY (estado_sensor ASC, sensor_id ASC);
 *
 * Justificación:
 * - Partition key compuesta por (tipo_ubicacion, nombre_ubicacion) para
 *   agrupar sensores por eje y valor geográfico.
 * - Clustering keys: primero `estado_sensor` para poder filtrar/ordenar por
 *   estado ('activo', 'inactivo', 'falla'), y luego `sensor_id` como
 *   identificador único dentro del estado.
 */
public class SensoresPorUbicacion {

    private String tipoUbicacion; // 'PAIS', 'CIUDAD', 'ZONA'
    private String nombreUbicacion; // nombre de la ubicación
    private String estadoSensor; // 'activo', 'inactivo', 'falla'
    private UUID sensorId;
    private String tipoSensor; // 'temperatura', 'humedad'
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
