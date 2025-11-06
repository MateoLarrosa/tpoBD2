package repositories;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;

import modelo.Proceso;

public class ProcesoRepository implements IRepository<Proceso> {

    private final MongoDatabase database;
    private static final String COLLECTION_NAME = "procesos";
    private static ProcesoRepository instance;

    private ProcesoRepository() {
        this.database = MongoConnectionManager.getInstance().getDatabase();
    }

    public static ProcesoRepository getInstance() {
        if (instance == null) {
            instance = new ProcesoRepository();
        }
        return instance;
    }

    private Proceso mapDocumentToProceso(Document doc) {
        Proceso proceso = new Proceso();
        proceso.setId(doc.getObjectId("_id").toString());
        proceso.setNombre(doc.getString("nombre"));
        proceso.setDescripcion(doc.getString("descripcion"));
        proceso.setTipoProceso(doc.getString("tipoProceso"));
        proceso.setCosto(doc.getInteger("costo"));
        return proceso;
    }

    @Override
    public InsertOneResult save(Proceso proceso) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        Document doc = new Document("nombre", proceso.getNombre())
                .append("descripcion", proceso.getDescripcion())
                .append("tipoProceso", proceso.getTipoProceso())
                .append("costo", proceso.getCosto());

        InsertOneResult result = collection.insertOne(doc);
        if (result != null && result.getInsertedId() != null && result.getInsertedId().isObjectId()) {
            proceso.setId(result.getInsertedId().asObjectId().getValue().toString());
        }

        return result;
    }

    @Override
    public Proceso findById(String id) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? mapDocumentToProceso(doc) : null;
    }

    @Override
    public List<Proceso> findAll() {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<Proceso> procesos = new ArrayList<>();
        for (Document doc : collection.find()) {
            procesos.add(mapDocumentToProceso(doc));
        }
        return procesos;
    }

    public Proceso findByNombre(String nombre) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        Document doc = collection.find(Filters.eq("nombre", nombre)).first();
        return doc != null ? mapDocumentToProceso(doc) : null;
    }

    public List<Proceso> findByTipo(String tipoProceso) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<Proceso> procesos = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("tipoProceso", tipoProceso))) {
            procesos.add(mapDocumentToProceso(doc));
        }
        return procesos;
    }

    public void update(Proceso proceso) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        Document updateDoc = new Document("nombre", proceso.getNombre())
                .append("descripcion", proceso.getDescripcion())
                .append("tipoProceso", proceso.getTipoProceso())
                .append("costo", proceso.getCosto());

        collection.updateOne(
                Filters.eq("_id", new ObjectId(proceso.getId())),
                new Document("$set", updateDoc)
        );
    }

    @Override
    public void delete(String id) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }
}
