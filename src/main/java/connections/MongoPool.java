package connections;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import exceptions.ErrorConectionMongoException;

public class MongoPool {

    private static MongoPool instance;
    private final String url;
    private final MongoClient mongoClient;
    private final String DBSTRING;

    private MongoPool() {
        String envUrl = System.getenv("MONGO_URL");
        if (envUrl == null || envUrl.isEmpty()) {
            envUrl = "mongodb://localhost:27017";
        }
        url = envUrl;
        mongoClient = MongoClients.create(url);
        String envDb = System.getenv("MONGO_DB");
        DBSTRING = (envDb != null && !envDb.isEmpty()) ? envDb : "tpdb";
    }

    public static MongoPool getInstance() {
        if (instance == null) {
            instance = new MongoPool();
        }
        return instance;
    }

    public MongoDatabase getConection(String database) throws ErrorConectionMongoException {
        try {
            String dbName = (database != null && !database.isEmpty()) ? database : DBSTRING;
            MongoDatabase db = mongoClient.getDatabase(dbName);
            return db;
        } catch (Exception e) {
            throw new ErrorConectionMongoException("Error en la conexión a MongoDB: " + e.getMessage());
        }
    }
}
