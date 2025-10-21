package repositories;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;

import connections.MongoPool;
import exceptions.ErrorConectionMongoException;
import modelo.Producto;

public class MongoRepository {
	
	private static MongoRepository instance;
	
	private MongoRepository() {}
	
	public static MongoRepository getInstance() {
		if(instance == null)
			instance = new MongoRepository();
		return instance;
	}
	
	
	public static MongoCollection<Document> getProduct() {
		try {
			MongoDatabase db = MongoPool.getInstancia().getConection("");
			return db.getCollection("Producto");
		} catch (ErrorConectionMongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void insertProduct(Producto p) {
		try {
			MongoDatabase db = MongoPool.getInstancia().getConection("");
			MongoCollection<Document> collection = db.getCollection("Producto");
			Document data = new Document().append("nombre", p.getNombre()).append("categoria", p.getCategoria());
			InsertOneResult insertOneResult =  collection.insertOne(data);
			System.out.println(insertOneResult);
			//return db.getCollection("Producto");
		} catch (ErrorConectionMongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return null;
	}
	
}
