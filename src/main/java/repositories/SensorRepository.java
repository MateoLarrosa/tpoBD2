package repositories;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;

import modelo.EstadoSensor;
import modelo.Sensor;
import modelo.TipoSensor;

public class SensorRepository implements IRepository<Sensor> {

    private static SensorRepository instance;
    private final MongoCollection<Document> collection;

    private SensorRepository() {
        MongoDatabase db = MongoConnectionManager.getInstance().getDatabase();
        this.collection = db.getCollection("sensores");
    }

    public static SensorRepository getInstance() {
        if (instance == null) {
            instance = new SensorRepository();
        }
        return instance;
    }

    @Override
    public InsertOneResult save(Sensor sensor) {
        Document doc = new Document()
                .append("nombre", sensor.getNombre())
                .append("tipo", sensor.getTipo() != null ? sensor.getTipo().name() : null)
                .append("latitud", sensor.getLatitud())
                .append("longitud", sensor.getLongitud())
                .append("ciudad", sensor.getCiudad())
                .append("pais", sensor.getPais())
                .append("zona", sensor.getZona())
                .append("estado", sensor.getEstado() != null ? sensor.getEstado().name() : null)
                .append("fechaInicioEmision", sensor.getFechaInicioEmision());
        InsertOneResult result = collection.insertOne(doc);
        if (doc.getObjectId("_id") != null) {
            sensor.setId(doc.getObjectId("_id").toHexString());
        }
        return result;
    }

    @Override
    public List<Sensor> findAll() {
        List<Sensor> sensores = new ArrayList<>();
        for (Document doc : collection.find()) {
            sensores.add(documentToSensor(doc));
        }
        return sensores;
    }

    @Override
    public Sensor findById(String id) {
        Document doc = collection.find(new Document("_id", new org.bson.types.ObjectId(id))).first();
        if (doc != null) {
            return documentToSensor(doc);
        }
        return null;
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(new Document("_id", new org.bson.types.ObjectId(id)));
    }

    private Sensor documentToSensor(Document doc) {
        Sensor s = new Sensor();
        s.setId(doc.getObjectId("_id").toHexString());
        s.setNombre(doc.getString("nombre"));
        String tipoStr = doc.getString("tipo");
        if (tipoStr != null) {
            try {
                s.setTipo(TipoSensor.valueOf(tipoStr));
            } catch (Exception e) {
                s.setTipo(null);
            }
        }
        s.setLatitud(doc.getDouble("latitud"));
        s.setLongitud(doc.getDouble("longitud"));
        s.setCiudad(doc.getString("ciudad"));
        s.setPais(doc.getString("pais"));
        s.setZona(doc.getString("zona"));
        String estadoStr = doc.getString("estado");
        if (estadoStr != null) {
            try {
                s.setEstado(EstadoSensor.valueOf(estadoStr));
            } catch (Exception e) {
                s.setEstado(null);
            }
        }
        s.setFechaInicioEmision(doc.getDate("fechaInicioEmision"));
        return s;
    }
}
