package connections;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import exceptions.ErrorConectionMongoException;


public class MongoPool {

	private static MongoPool instancia;
	private final String url ;
	private final MongoClient mongoClient;
	private final String DBSTRING;
    
	private MongoPool() {
		String envUrl = System.getenv("MONGO_URL");
		if (envUrl == null || envUrl.isEmpty()) {
			envUrl = "mongodb://127.0.0.1:27017";
		}
		url = envUrl;
		mongoClient = MongoClients.create(url);
		String envDb = System.getenv("MONGO_DB");
		DBSTRING = (envDb != null && !envDb.isEmpty()) ? envDb : "tpdb";
	}
	
	public static MongoPool getInstancia(){
		if(instancia == null)
			instancia = new MongoPool();
		return instancia;
	}
	
	public MongoDatabase getConection(String database) throws ErrorConectionMongoException {
		try {
			String dbName = (database != null && !database.isEmpty()) ? database : DBSTRING;
			MongoDatabase db = mongoClient.getDatabase(dbName);
			return db;
		}
		catch (Exception e) {
			throw new ErrorConectionMongoException("Error en la conexión a MongoDB: " + e.getMessage());
		}
	}
}
