package test;

import com.datastax.oss.driver.api.core.CqlSession;

import connections.CassandraPool;

public class CassandraConnectionTest {

    public static void main(String[] args) {
        try {
            CassandraPool pool = CassandraPool.getInstance();
            CqlSession session = pool.getSession();
            System.out.println("Conexión a Cassandra exitosa: " + session.getName());
            pool.closeSession();
        } catch (Exception e) {
            System.err.println("Error al conectar con Cassandra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
