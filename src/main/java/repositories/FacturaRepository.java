package repositories;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;

import modelo.EstadoFactura;
import modelo.Factura;

/**
 * Repositorio para gestionar facturas de procesos.
 * Implementa el patrón Singleton.
 */
public class FacturaRepository implements IRepository<Factura> {

    private final MongoDatabase database;
    private static final String COLLECTION_NAME = "facturas";
    private static FacturaRepository instance;

    private FacturaRepository() {
        this.database = MongoConnectionManager.getInstance().getDatabase();
    }

    public static FacturaRepository getInstance() {
        if (instance == null) {
            instance = new FacturaRepository();
        }
        return instance;
    }

    private Factura mapDocumentToFactura(Document doc) {
        Factura factura = new Factura();
        factura.setId(doc.getObjectId("_id").toString());
        factura.setUsuarioId(doc.getString("usuarioId"));
        
        Date fechaEmisionDate = doc.getDate("fechaEmision");
        if (fechaEmisionDate != null) {
            factura.setFechaEmision(fechaEmisionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        
        List<String> procesosIds = doc.getList("procesosFacturadosIds", String.class);
        factura.setProcesosFacturadosIds(procesosIds != null ? procesosIds : new ArrayList<>());
        
        String estadoStr = doc.getString("estado");
        factura.setEstado(estadoStr != null ? EstadoFactura.valueOf(estadoStr) : EstadoFactura.PENDIENTE);
        
        factura.setMontoTotal(doc.getDouble("montoTotal"));
        
        Date fechaVencimientoDate = doc.getDate("fechaVencimiento");
        if (fechaVencimientoDate != null) {
            factura.setFechaVencimiento(fechaVencimientoDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        
        return factura;
    }

    @Override
    public InsertOneResult save(Factura factura) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        Document doc = new Document("usuarioId", factura.getUsuarioId())
                .append("fechaEmision", Date.from(factura.getFechaEmision().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .append("procesosFacturadosIds", factura.getProcesosFacturadosIds())
                .append("estado", factura.getEstado().name())
                .append("montoTotal", factura.getMontoTotal())
                .append("fechaVencimiento", Date.from(factura.getFechaVencimiento().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        InsertOneResult result = collection.insertOne(doc);
        if (result != null && result.getInsertedId() != null && result.getInsertedId().isObjectId()) {
            factura.setId(result.getInsertedId().asObjectId().getValue().toString());
        }

        return result;
    }

    @Override
    public Factura findById(String id) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? mapDocumentToFactura(doc) : null;
    }

    @Override
    public List<Factura> findAll() {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<Factura> facturas = new ArrayList<>();
        collection.find().forEach(doc -> facturas.add(mapDocumentToFactura(doc)));
        return facturas;
    }

    public void update(Factura factura) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        Document updateDoc = new Document("usuarioId", factura.getUsuarioId())
                .append("fechaEmision", Date.from(factura.getFechaEmision().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .append("procesosFacturadosIds", factura.getProcesosFacturadosIds())
                .append("estado", factura.getEstado().name())
                .append("montoTotal", factura.getMontoTotal())
                .append("fechaVencimiento", Date.from(factura.getFechaVencimiento().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        collection.updateOne(
                Filters.eq("_id", new ObjectId(factura.getId())),
                new Document("$set", updateDoc)
        );
    }

    @Override
    public void delete(String id) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }

    // Métodos de consulta específicos

    /**
     * Obtiene todas las facturas de un usuario
     */
    public List<Factura> findByUsuarioId(String usuarioId) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<Factura> facturas = new ArrayList<>();
        collection.find(Filters.eq("usuarioId", usuarioId))
                .sort(new Document("fechaEmision", -1))
                .forEach(doc -> facturas.add(mapDocumentToFactura(doc)));
        return facturas;
    }

    /**
     * Obtiene facturas por estado
     */
    public List<Factura> findByEstado(EstadoFactura estado) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<Factura> facturas = new ArrayList<>();
        collection.find(Filters.eq("estado", estado.name()))
                .sort(new Document("fechaEmision", -1))
                .forEach(doc -> facturas.add(mapDocumentToFactura(doc)));
        return facturas;
    }

    /**
     * Obtiene facturas de un usuario por estado
     */
    public List<Factura> findByUsuarioIdYEstado(String usuarioId, EstadoFactura estado) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<Factura> facturas = new ArrayList<>();
        collection.find(Filters.and(
                Filters.eq("usuarioId", usuarioId),
                Filters.eq("estado", estado.name())
        ))
                .sort(new Document("fechaEmision", -1))
                .forEach(doc -> facturas.add(mapDocumentToFactura(doc)));
        return facturas;
    }

    /**
     * Obtiene facturas vencidas (pendientes y con fecha de vencimiento pasada)
     */
    public List<Factura> findFacturasVencidas() {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<Factura> facturas = new ArrayList<>();
        Date hoy = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        collection.find(Filters.and(
                Filters.eq("estado", EstadoFactura.PENDIENTE.name()),
                Filters.lt("fechaVencimiento", hoy)
        ))
                .sort(new Document("fechaEmision", -1))
                .forEach(doc -> facturas.add(mapDocumentToFactura(doc)));
        return facturas;
    }

    /**
     * Obtiene facturas de un usuario en un rango de fechas
     */
    public List<Factura> findByUsuarioIdYRangoFechas(String usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<Factura> facturas = new ArrayList<>();
        
        Date inicio = Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date fin = Date.from(fechaFin.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        collection.find(Filters.and(
                Filters.eq("usuarioId", usuarioId),
                Filters.gte("fechaEmision", inicio),
                Filters.lte("fechaEmision", fin)
        ))
                .sort(new Document("fechaEmision", -1))
                .forEach(doc -> facturas.add(mapDocumentToFactura(doc)));
        return facturas;
    }
}
