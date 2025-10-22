package connections;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import exceptions.ErrorConectionMongoException;
import java.net.InetSocketAddress;

public class CassandraPool {

    private static CassandraPool instancia;
    private final String contactPoints;
    private final String keyspace;
    private final CqlSession session;
    
    private CassandraPool() {
        // Leer contact points y keyspace desde variables de entorno, con valores por defecto
        String envContactPoints = System.getenv("CASSANDRA_CONTACT_POINTS");
        if (envContactPoints == null || envContactPoints.isEmpty()) {
            envContactPoints = "127.0.0.1:9042"; // valor por defecto
        }
        contactPoints = envContactPoints;
        
        String envKeyspace = System.getenv("CASSANDRA_KEYSPACE");
        keyspace = (envKeyspace != null && !envKeyspace.isEmpty()) ? envKeyspace : "mi_keyspace";
        
        // Crear sesión de Cassandra
        CqlSessionBuilder builder = CqlSession.builder();
        
        // Parsear contact points (formato: host:puerto,host2:puerto2,...)
        String[] points = contactPoints.split(",");
        for (String point : points) {
            String[] hostPort = point.trim().split(":");
            String host = hostPort[0];
            int port = hostPort.length > 1 ? Integer.parseInt(hostPort[1]) : 9042;
            builder.addContactPoint(new InetSocketAddress(host, port));
        }
        
        session = builder.withKeyspace(keyspace).build();
    }
    
    public static CassandraPool getInstancia() {
        if (instancia == null)
            instancia = new CassandraPool();
        return instancia;
    }
    
    public CqlSession getSession() throws ErrorConectionMongoException {
        try {
            if (session.isClosed()) {
                throw new ErrorConectionMongoException("La sesión de Cassandra está cerrada");
            }
            return session;
        } catch (Exception e) {
            throw new ErrorConectionMongoException("Error en la conexión a Cassandra: " + e.getMessage());
        }
    }
    
    public CqlSession getSession(String customKeyspace) throws ErrorConectionMongoException {
        try {
            // Para usar un keyspace diferente, crear una nueva sesión temporal
            CqlSessionBuilder builder = CqlSession.builder();
            String[] points = contactPoints.split(",");
            for (String point : points) {
                String[] hostPort = point.trim().split(":");
                String host = hostPort[0];
                int port = hostPort.length > 1 ? Integer.parseInt(hostPort[1]) : 9042;
                builder.addContactPoint(new InetSocketAddress(host, port));
            }
            return builder.withKeyspace(customKeyspace).build();
        } catch (Exception e) {
            throw new ErrorConectionMongoException("Error en la conexión a Cassandra con keyspace " + customKeyspace + ": " + e.getMessage());
        }
    }
    
    public void closeSession() {
        if (session != null && !session.isClosed()) {
            session.close();
        }
    }
}