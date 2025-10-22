package connections;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import exceptions.ErrorConectionMongoException;
import java.net.URI;

public class RedisPool {

    private static RedisPool instancia;
    private final String redisUrl;
    private final JedisPool jedisPool;
    
    private RedisPool() {
        String envRedisUrl = System.getenv("REDIS_URL");
        if (envRedisUrl == null || envRedisUrl.isEmpty()) {
            envRedisUrl = "redis://127.0.0.1:6379";
        }
        redisUrl = envRedisUrl;
        
        try {
            // Configuración del pool de conexiones
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(20); // máximo 20 conexiones
            poolConfig.setMaxIdle(10);  // máximo 10 conexiones idle
            poolConfig.setMinIdle(2);   // mínimo 2 conexiones idle
            poolConfig.setTestOnBorrow(true);
            poolConfig.setTestOnReturn(true);
            
            // Crear pool usando la URL
            if (redisUrl.startsWith("redis://")) {
                URI redisUri = URI.create(redisUrl);
                jedisPool = new JedisPool(poolConfig, redisUri);
            } else {
                // Fallback para host:puerto sin protocolo
                String[] hostPort = redisUrl.split(":");
                String host = hostPort[0];
                int port = hostPort.length > 1 ? Integer.parseInt(hostPort[1]) : 6379;
                jedisPool = new JedisPool(poolConfig, host, port);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error inicializando el pool de Redis: " + e.getMessage(), e);
        }
    }
    
    public static RedisPool getInstancia() {
        if (instancia == null)
            instancia = new RedisPool();
        return instancia;
    }
    
    public Jedis getConnection() throws ErrorConectionMongoException {
        try {
            Jedis jedis = jedisPool.getResource();
            // Probar la conexión con un ping
            jedis.ping();
            return jedis;
        } catch (Exception e) {
            throw new ErrorConectionMongoException("Error en la conexión a Redis: " + e.getMessage());
        }
    }
    
    public void returnConnection(Jedis jedis) {
        if (jedis != null) {
            jedis.close(); // En Jedis 3.x+, close() devuelve la conexión al pool
        }
    }
    
    public void closePool() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();
        }
    }
    
    // Método de conveniencia para ejecutar operaciones con manejo automático de conexiones
    public <T> T execute(RedisOperation<T> operation) throws ErrorConectionMongoException {
        Jedis jedis = null;
        try {
            jedis = getConnection();
            return operation.execute(jedis);
        } finally {
            returnConnection(jedis);
        }
    }
    
    // Interface funcional para operaciones Redis
    @FunctionalInterface
    public interface RedisOperation<T> {
        T execute(Jedis jedis);
    }
}