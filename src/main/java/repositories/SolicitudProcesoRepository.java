package repositories;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.InsertOneResult;

import modelo.EstadoSolicitud;
import modelo.SolicitudProceso;

/**
 * Repositorio para gestionar las solicitudes de procesos realizadas por usuarios.
 * Implementa el patrón Singleton.
 */
public class SolicitudProcesoRepository implements IRepository<SolicitudProceso> {

    private final MongoDatabase database;
    private static final String COLLECTION_NAME = "solicitudes_proceso";
    private static SolicitudProcesoRepository instance;

    private SolicitudProcesoRepository() {
        this.database = MongoConnectionManager.getInstance().getDatabase();
    }

    public static SolicitudProcesoRepository getInstance() {
        if (instance == null) {
            instance = new SolicitudProcesoRepository();
        }
        return instance;
    }

    private SolicitudProceso mapDocumentToSolicitud(Document doc) {
        SolicitudProceso solicitud = new SolicitudProceso();
        solicitud.setId(doc.getObjectId("_id").toString());
        solicitud.setUsuarioId(doc.getString("usuarioId"));
        solicitud.setProcesoId(doc.getString("procesoId"));
        
        // Convertir parametros de Document a Map
        Document parametrosDoc = doc.get("parametros", Document.class);
        if (parametrosDoc != null) {
            Map<String, Object> parametros = new HashMap<>(parametrosDoc);
            solicitud.setParametros(parametros);
        }
        
        // Convertir fechas de Date a LocalDateTime
        Date fechaSolicitud = doc.getDate("fechaSolicitud");
        if (fechaSolicitud != null) {
            solicitud.setFechaSolicitud(fechaSolicitud.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        
        Date fechaCompletado = doc.getDate("fechaCompletado");
        if (fechaCompletado != null) {
            solicitud.setFechaCompletado(fechaCompletado.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        
        String estadoStr = doc.getString("estado");
        if (estadoStr != null) {
            solicitud.setEstado(EstadoSolicitud.valueOf(estadoStr));
        }
        
        solicitud.setResultado(doc.getString("resultado"));
        
        Long tiempoEjecucion = doc.getLong("tiempoEjecucionMs");
        if (tiempoEjecucion != null) {
            solicitud.setTiempoEjecucionMs(tiempoEjecucion);
        }
        
        return solicitud;
    }

    @Override
    public InsertOneResult save(SolicitudProceso solicitud) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        // Convertir LocalDateTime a Date para MongoDB
        Date fechaSolicitud = null;
        if (solicitud.getFechaSolicitud() != null) {
            fechaSolicitud = Date.from(solicitud.getFechaSolicitud().atZone(ZoneId.systemDefault()).toInstant());
        }

        Date fechaCompletado = null;
        if (solicitud.getFechaCompletado() != null) {
            fechaCompletado = Date.from(solicitud.getFechaCompletado().atZone(ZoneId.systemDefault()).toInstant());
        }

        Document doc = new Document("usuarioId", solicitud.getUsuarioId())
                .append("procesoId", solicitud.getProcesoId())
                .append("fechaSolicitud", fechaSolicitud)
                .append("estado", solicitud.getEstado().name())
                .append("parametros", solicitud.getParametros() != null ? new Document(solicitud.getParametros()) : null)
                .append("resultado", solicitud.getResultado())
                .append("tiempoEjecucionMs", solicitud.getTiempoEjecucionMs())
                .append("fechaCompletado", fechaCompletado);

        InsertOneResult result = collection.insertOne(doc);
        if (result != null && result.getInsertedId() != null && result.getInsertedId().isObjectId()) {
            solicitud.setId(result.getInsertedId().asObjectId().getValue().toString());
        }

        return result;
    }

    @Override
    public SolicitudProceso findById(String id) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? mapDocumentToSolicitud(doc) : null;
    }

    @Override
    public List<SolicitudProceso> findAll() {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<SolicitudProceso> solicitudes = new ArrayList<>();
        for (Document doc : collection.find().sort(Sorts.descending("fechaSolicitud"))) {
            solicitudes.add(mapDocumentToSolicitud(doc));
        }
        return solicitudes;
    }

    /**
     * Busca todas las solicitudes de un usuario
     * @param usuarioId ID del usuario
     * @return Lista de solicitudes del usuario
     */
    public List<SolicitudProceso> findByUsuarioId(String usuarioId) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<SolicitudProceso> solicitudes = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("usuarioId", usuarioId))
                .sort(Sorts.descending("fechaSolicitud"))) {
            solicitudes.add(mapDocumentToSolicitud(doc));
        }
        return solicitudes;
    }

    /**
     * Busca solicitudes por estado
     * @param estado Estado de la solicitud
     * @return Lista de solicitudes con ese estado
     */
    public List<SolicitudProceso> findByEstado(EstadoSolicitud estado) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<SolicitudProceso> solicitudes = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("estado", estado.name()))
                .sort(Sorts.descending("fechaSolicitud"))) {
            solicitudes.add(mapDocumentToSolicitud(doc));
        }
        return solicitudes;
    }

    /**
     * Busca solicitudes de un usuario por estado
     * @param usuarioId ID del usuario
     * @param estado Estado de la solicitud
     * @return Lista de solicitudes
     */
    public List<SolicitudProceso> findByUsuarioIdYEstado(String usuarioId, EstadoSolicitud estado) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<SolicitudProceso> solicitudes = new ArrayList<>();
        for (Document doc : collection.find(
                Filters.and(
                    Filters.eq("usuarioId", usuarioId),
                    Filters.eq("estado", estado.name())
                )
            ).sort(Sorts.descending("fechaSolicitud"))) {
            solicitudes.add(mapDocumentToSolicitud(doc));
        }
        return solicitudes;
    }

    /**
     * Busca solicitudes de un usuario en un rango de fechas
     * @param usuarioId ID del usuario
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Lista de solicitudes en el rango
     */
    public List<SolicitudProceso> findByUsuarioIdYRangoFechas(String usuarioId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        
        Date dateInicio = Date.from(fechaInicio.atZone(ZoneId.systemDefault()).toInstant());
        Date dateFin = Date.from(fechaFin.atZone(ZoneId.systemDefault()).toInstant());
        
        List<SolicitudProceso> solicitudes = new ArrayList<>();
        for (Document doc : collection.find(
                Filters.and(
                    Filters.eq("usuarioId", usuarioId),
                    Filters.gte("fechaSolicitud", dateInicio),
                    Filters.lte("fechaSolicitud", dateFin)
                )
            ).sort(Sorts.descending("fechaSolicitud"))) {
            solicitudes.add(mapDocumentToSolicitud(doc));
        }
        return solicitudes;
    }

    /**
     * Busca solicitudes por proceso
     * @param procesoId ID del proceso
     * @return Lista de solicitudes de ese proceso
     */
    public List<SolicitudProceso> findByProcesoId(String procesoId) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<SolicitudProceso> solicitudes = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("procesoId", procesoId))
                .sort(Sorts.descending("fechaSolicitud"))) {
            solicitudes.add(mapDocumentToSolicitud(doc));
        }
        return solicitudes;
    }

    /**
     * Actualiza una solicitud existente
     * @param solicitud Solicitud con los datos actualizados
     */
    public void update(SolicitudProceso solicitud) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        
        Date fechaCompletado = null;
        if (solicitud.getFechaCompletado() != null) {
            fechaCompletado = Date.from(solicitud.getFechaCompletado().atZone(ZoneId.systemDefault()).toInstant());
        }

        Document updateDoc = new Document("estado", solicitud.getEstado().name())
                .append("resultado", solicitud.getResultado())
                .append("tiempoEjecucionMs", solicitud.getTiempoEjecucionMs())
                .append("fechaCompletado", fechaCompletado);

        collection.updateOne(
            Filters.eq("_id", new ObjectId(solicitud.getId())),
            new Document("$set", updateDoc)
        );
    }

    @Override
    public void delete(String id) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }
}
