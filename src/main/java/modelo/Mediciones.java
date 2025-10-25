// package com.dataind.tpp.modelo;

// import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
// import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
// import com.datastax.oss.driver.api.core.type
// .1.cql.Ordering;
// import com.datastax.oss.driver.api.core.type
// .1.cql.PrimaryKeyType;

// import java.time.Instant;
// import java.io.Serializable;
// import java.util.UUID;

// @PrimaryKeyClass
// public class MedicionPorSensorTiempoKey implements Serializable {

//     // Clave de Partición (Partition Key): sensorId
//     @PrimaryKeyColumn(name = "sensor_id", type = PrimaryKeyType.PARTITIONED)
//     private UUID sensorId;

//     // Clave de Agrupamiento (Clustering Key): fechaHora (para serie de tiempo)
//     @PrimaryKeyColumn(name = "fecha_hora", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
//     private Instant fechaHora;

//     // Constructor vacío, completo, equals, hashCode y Getters/Setters
//     // ... (Implementar todos estos métodos)
// }
