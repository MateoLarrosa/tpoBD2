package test;

import controlador.ProductController;
import connections.MongoPool;
import connections.CassandraPool;
import connections.RedisPool;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

import redis.clients.jedis.Jedis;

import exceptions.ErrorConectionMongoException;

public class Test {

	public static void main(String[] args) {
		System.out.println("=== Probando Conexiones a Bases de Datos ===");
		
		// Probar MongoDB
		probarMongo();
		
		// Probar Cassandra
		probarCassandra();
		
		// Probar Redis
		probarRedis();
		
		System.out.println("=== Pruebas Completadas ===");
	}
	
	private static void probarMongo() {
		System.out.println("\n--- Probando MongoDB ---");
		try {
			MongoPool pool = MongoPool.getInstancia();
			MongoDatabase db = pool.getConection("testdb");
			
			// Crear una colección de prueba
			MongoCollection<Document> collection = db.getCollection("test_collection");
			
			// Insertar un documento de prueba
			Document documento = new Document("nombre", "Test Usuario")
					.append("email", "test@example.com")
					.append("timestamp", System.currentTimeMillis());
			
			collection.insertOne(documento);
			System.out.println("✓ Documento insertado en MongoDB correctamente");
			
			// Leer el documento
			Document resultado = collection.find().first();
			if (resultado != null) {
				System.out.println("✓ Documento leído: " + resultado.toJson());
			}
			
			// Limpiar - borrar el documento de prueba
			collection.deleteMany(new Document("nombre", "Test Usuario"));
			System.out.println("✓ Documento de prueba eliminado");
			
		} catch (ErrorConectionMongoException e) {
			System.err.println("✗ Error conectando a MongoDB: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("✗ Error inesperado en MongoDB: " + e.getMessage());
		}
	}
	
	private static void probarCassandra() {
		System.out.println("\n--- Probando Cassandra ---");
		try {
			CassandraPool pool = CassandraPool.getInstancia();
			CqlSession session = pool.getSession();
			
			// Crear tabla de prueba si no existe
			String createTable = "CREATE TABLE IF NOT EXISTS test_table (" +
					"id UUID PRIMARY KEY, " +
					"nombre TEXT, " +
					"email TEXT, " +
					"timestamp BIGINT)";
			
			session.execute(createTable);
			System.out.println("✓ Tabla de prueba creada/verificada en Cassandra");
			
			// Insertar datos de prueba
			String insertData = "INSERT INTO test_table (id, nombre, email, timestamp) " +
					"VALUES (uuid(), 'Test Usuario Cassandra', 'test.cassandra@example.com', " +
					System.currentTimeMillis() + ")";
			
			session.execute(insertData);
			System.out.println("✓ Datos insertados en Cassandra correctamente");
			
			// Leer datos
			ResultSet resultado = session.execute("SELECT * FROM test_table LIMIT 5");
			for (Row row : resultado) {
				System.out.println("✓ Registro leído: " + 
						"ID=" + row.getUuid("id") + 
						", Nombre=" + row.getString("nombre") + 
						", Email=" + row.getString("email"));
			}
			
			// Limpiar datos de prueba
			session.execute("DELETE FROM test_table WHERE nombre = 'Test Usuario Cassandra'");
			System.out.println("✓ Datos de prueba eliminados");
			
		} catch (ErrorConectionMongoException e) {
			System.err.println("✗ Error conectando a Cassandra: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("✗ Error inesperado en Cassandra: " + e.getMessage());
		}
	}
	
	private static void probarRedis() {
		System.out.println("\n--- Probando Redis ---");
		try {
			RedisPool pool = RedisPool.getInstancia();
			
			// Usar el método execute para manejo automático de conexiones
			String resultado = pool.execute(jedis -> {
				// Establecer un valor
				jedis.set("test:usuario", "Test Usuario Redis");
				jedis.set("test:email", "test.redis@example.com");
				jedis.set("test:timestamp", String.valueOf(System.currentTimeMillis()));
				
				// Leer el valor
				return jedis.get("test:usuario");
			});
			
			System.out.println("✓ Datos escritos y leídos en Redis correctamente");
			System.out.println("✓ Usuario leído: " + resultado);
			
			// Probar operaciones de hash
			pool.execute(jedis -> {
				jedis.hset("test:user_hash", "nombre", "Hash Usuario");
				jedis.hset("test:user_hash", "email", "hash@example.com");
				jedis.hset("test:user_hash", "edad", "25");
				return null;
			});
			
			String nombreHash = pool.execute(jedis -> jedis.hget("test:user_hash", "nombre"));
			System.out.println("✓ Hash leído: " + nombreHash);
			
			// Limpiar datos de prueba
			pool.execute(jedis -> {
				jedis.del("test:usuario", "test:email", "test:timestamp", "test:user_hash");
				return null;
			});
			
			System.out.println("✓ Datos de prueba eliminados");
			
		} catch (ErrorConectionMongoException e) {
			System.err.println("✗ Error conectando a Redis: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("✗ Error inesperado en Redis: " + e.getMessage());
		}
	}
}
