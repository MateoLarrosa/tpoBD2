package repositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import io.github.cdimascio.dotenv.Dotenv;

public class MongoConnectionManager {

    private static MongoConnectionManager instance;
    private final MongoDatabase database;

    private MongoConnectionManager() {
        Dotenv dotenv = Dotenv.load();
        String connectionString = dotenv.get("MONGO_URI", "mongodb://localhost:27017");
        String databaseName = dotenv.get("MONGO_DB", "tpdb");

        MongoClient mongoClient = MongoClients.create(connectionString);
        this.database = mongoClient.getDatabase(databaseName);
    }

    public static MongoConnectionManager getInstance() {
        if (instance == null) {
            instance = new MongoConnectionManager();
        }
        return instance;
    }

    public MongoDatabase getDatabase() {
        return database;
    }
}