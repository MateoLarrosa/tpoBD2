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

import modelo.Pago;

public class PagoRepository implements IRepository<Pago> {

    private final MongoDatabase database;
    private static final String COLLECTION_NAME = "pagos";
    private static PagoRepository instance;

    private PagoRepository() {
        this.database = MongoConnectionManager.getInstance().getDatabase();
    }

    public static PagoRepository getInstance() {
        if (instance == null) {
            instance = new PagoRepository();
        }
        return instance;
    }

    private Pago mapDocumentToPago(Document doc) {
        Pago pago = new Pago();
        pago.setId(doc.getObjectId("_id").toString());
        pago.setFacturaId(doc.getString("facturaId"));

        Date fechaPagoDate = doc.getDate("fechaPago");
        if (fechaPagoDate != null) {
            pago.setFechaPago(fechaPagoDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }

        pago.setMontoPagado(doc.getDouble("montoPagado"));
        pago.setMetodoPago(doc.getString("metodoPago"));

        return pago;
    }

    @Override
    public InsertOneResult save(Pago pago) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        Document doc = new Document("facturaId", pago.getFacturaId())
                .append("fechaPago", Date.from(pago.getFechaPago().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .append("montoPagado", pago.getMontoPagado())
                .append("metodoPago", pago.getMetodoPago());

        InsertOneResult result = collection.insertOne(doc);
        if (result != null && result.getInsertedId() != null && result.getInsertedId().isObjectId()) {
            pago.setId(result.getInsertedId().asObjectId().getValue().toString());
        }

        return result;
    }

    @Override
    public Pago findById(String id) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? mapDocumentToPago(doc) : null;
    }

    @Override
    public List<Pago> findAll() {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<Pago> pagos = new ArrayList<>();
        collection.find().forEach(doc -> pagos.add(mapDocumentToPago(doc)));
        return pagos;
    }

    public void update(Pago pago) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        Document updateDoc = new Document("facturaId", pago.getFacturaId())
                .append("fechaPago", Date.from(pago.getFechaPago().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .append("montoPagado", pago.getMontoPagado())
                .append("metodoPago", pago.getMetodoPago());

        collection.updateOne(
                Filters.eq("_id", new ObjectId(pago.getId())),
                new Document("$set", updateDoc)
        );
    }

    @Override
    public void delete(String id) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }

    public Pago findByFacturaId(String facturaId) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        Document doc = collection.find(Filters.eq("facturaId", facturaId)).first();
        return doc != null ? mapDocumentToPago(doc) : null;
    }

    public List<Pago> findByMetodoPago(String metodoPago) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<Pago> pagos = new ArrayList<>();
        collection.find(Filters.eq("metodoPago", metodoPago))
                .sort(new Document("fechaPago", -1))
                .forEach(doc -> pagos.add(mapDocumentToPago(doc)));
        return pagos;
    }

    public List<Pago> findByRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<Pago> pagos = new ArrayList<>();

        Date inicio = Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date fin = Date.from(fechaFin.atStartOfDay(ZoneId.systemDefault()).toInstant());

        collection.find(Filters.and(
                Filters.gte("fechaPago", inicio),
                Filters.lte("fechaPago", fin)
        ))
                .sort(new Document("fechaPago", -1))
                .forEach(doc -> pagos.add(mapDocumentToPago(doc)));
        return pagos;
    }

    public double calcularMontoTotalPagado(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Pago> pagos = findByRangoFechas(fechaInicio, fechaFin);
        return pagos.stream().mapToDouble(Pago::getMontoPagado).sum();
    }
}
