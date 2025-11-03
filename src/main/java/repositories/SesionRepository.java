package repositories;

import com.fasterxml.jackson.databind.ObjectMapper;

import connections.RedisPool;
import exceptions.ErrorConectionMongoException;
import modelo.Sesion;

public class SesionRepository {

    private static final String KEY_PREFIX = "sesion:";
    private static SesionRepository instance;
    private final ObjectMapper mapper = new ObjectMapper();

    private SesionRepository() {
    }

    public static SesionRepository getInstance() {
        if (instance == null) {
            instance = new SesionRepository();
        }
        return instance;
    }

    public void guardarSesion(Sesion sesion) throws ErrorConectionMongoException {
        RedisPool.getInstance().execute(jedis -> {
            try {
                jedis.set(KEY_PREFIX + sesion.getIdSesion(), mapper.writeValueAsString(sesion));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    public Sesion obtenerSesion(String idSesion) throws ErrorConectionMongoException {
        return RedisPool.getInstance().execute(jedis -> {
            try {
                String json = jedis.get(KEY_PREFIX + idSesion);
                return json != null ? mapper.readValue(json, Sesion.class) : null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void actualizarSesion(Sesion sesion) throws ErrorConectionMongoException {
        guardarSesion(sesion);
    }
}
