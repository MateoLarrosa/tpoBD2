package connections;

import java.net.URI;

import exceptions.ErrorConectionMongoException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {

    private static RedisPool instance;
    private final String redisUrl;
    private final JedisPool jedisPool;

    private RedisPool() {
        String envRedisUrl = System.getenv("REDIS_URL");
        if (envRedisUrl == null || envRedisUrl.isEmpty()) {
            envRedisUrl = "redis://127.0.0.1:6379";
        }
        redisUrl = envRedisUrl;

        try {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(20);
            poolConfig.setMaxIdle(10);
            poolConfig.setMinIdle(2);
            poolConfig.setTestOnBorrow(true);
            poolConfig.setTestOnReturn(true);

            if (redisUrl.startsWith("redis://")) {
                URI redisUri = URI.create(redisUrl);
                jedisPool = new JedisPool(poolConfig, redisUri);
            } else {

                String[] hostPort = redisUrl.split(":");
                String host = hostPort[0];
                int port = hostPort.length > 1 ? Integer.parseInt(hostPort[1]) : 6379;
                jedisPool = new JedisPool(poolConfig, host, port);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error inicializando el pool de Redis: " + e.getMessage(), e);
        }
    }

    public static RedisPool getInstance() {
        if (instance == null) {
            instance = new RedisPool();
        }
        return instance;
    }

    public Jedis getConnection() throws ErrorConectionMongoException {
        try {
            Jedis jedis = jedisPool.getResource();

            jedis.ping();
            return jedis;
        } catch (Exception e) {
            throw new ErrorConectionMongoException("Error en la conexión a Redis: " + e.getMessage());
        }
    }

    public void returnConnection(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public void closePool() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();
        }
    }

    public <T> T execute(RedisOperation<T> operation) throws ErrorConectionMongoException {
        Jedis jedis = null;
        try {
            jedis = getConnection();
            return operation.execute(jedis);
        } finally {
            returnConnection(jedis);
        }
    }

    @FunctionalInterface
    public interface RedisOperation<T> {

        T execute(Jedis jedis);
    }
}
