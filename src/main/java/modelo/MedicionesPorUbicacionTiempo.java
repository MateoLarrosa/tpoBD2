package modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Modelo para la tabla Cassandra "mediciones_por_ubicacion_tiempo".
 *
 * CQL sugerida:
 *
 * CREATE TABLE IF NOT EXISTS mediciones_por_ubicacion_tiempo (
 *   tipo_ubicacion text,
 *   nombre_ubicacion text,
 *   ano_mes int,
 *   fecha_hora timestamp,
 *   sensor_id uuid,
 *   temperatura decimal,
 *   humedad decimal,
 *   PRIMARY KEY ((tipo_ubicacion, nombre_ubicacion, ano_mes), fecha_hora)
 * ) WITH CLUSTERING ORDER BY (fecha_hora ASC);
 *
 * Justificación:
 * - Partition key compuesta por (tipo_ubicacion, nombre_ubicacion, ano_mes)
 *   para limitar el tamaño de cada partición por mes y ubicación.
 * - Clustering key: fecha_hora para ordenar cronológicamente las mediciones
 *   dentro del mes y permitir rangos/históricos.
 */
public class MedicionesPorUbicacionTiempo {

    private String tipoUbicacion; // 'PAIS', 'CIUDAD', 'ZONA'
    private String nombreUbicacion; // 'Argentina', 'Buenos Aires', etc.
    private int anoMes; // ej. 202501
    private LocalDateTime fechaHora;
    private UUID sensorId;
    private BigDecimal temperatura;
    private BigDecimal humedad;

    public MedicionesPorUbicacionTiempo() {
    }

    public MedicionesPorUbicacionTiempo(String tipoUbicacion, String nombreUbicacion, int anoMes, LocalDateTime fechaHora, UUID sensorId, BigDecimal temperatura, BigDecimal humedad) {
        this.tipoUbicacion = tipoUbicacion;
        this.nombreUbicacion = nombreUbicacion;
        this.anoMes = anoMes;
        this.fechaHora = fechaHora;
        this.sensorId = sensorId;
        this.temperatura = temperatura;
        this.humedad = humedad;
    }

}
