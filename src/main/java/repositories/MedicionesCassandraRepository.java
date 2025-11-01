package repositories;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchStatementBuilder;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;

import connections.CassandraPool;
import exceptions.ErrorConectionMongoException;
import modelo.MedicionesPorCiudad;
import modelo.MedicionesPorPais;
import modelo.MedicionesPorZona;

public class MedicionesCassandraRepository {

    public java.util.List<MedicionesPorZona> findAllZona() {
        java.util.List<MedicionesPorZona> result = new java.util.ArrayList<>();
        com.datastax.oss.driver.api.core.cql.ResultSet rs = session.execute("SELECT zona, tipo, anio, mes, fecha, idSensor, valor FROM mediciones_por_zona");
        for (com.datastax.oss.driver.api.core.cql.Row row : rs) {
            java.time.Instant fechaInstant = row.getInstant("fecha");
            java.util.Date fecha = (fechaInstant != null) ? java.util.Date.from(fechaInstant) : null;
            MedicionesPorZona m = new MedicionesPorZona(
                    row.getString("idSensor"),
                    row.getString("tipo"),
                    row.getString("zona"),
                    row.getInt("anio"),
                    row.getInt("mes"),
                    fecha,
                    row.getDouble("valor")
            );
            result.add(m);
        }
        return result;
    }

    public java.util.List<MedicionesPorPais> findAllPais() {
        java.util.List<MedicionesPorPais> result = new java.util.ArrayList<>();
        com.datastax.oss.driver.api.core.cql.ResultSet rs = session.execute("SELECT pais, tipo, anio, mes, fecha, idSensor, valor FROM mediciones_por_pais");
        for (com.datastax.oss.driver.api.core.cql.Row row : rs) {
            java.time.Instant fechaInstant = row.getInstant("fecha");
            java.util.Date fecha = (fechaInstant != null) ? java.util.Date.from(fechaInstant) : null;
            MedicionesPorPais m = new MedicionesPorPais(
                    row.getString("idSensor"),
                    row.getString("tipo"),
                    row.getString("pais"),
                    row.getInt("anio"),
                    row.getInt("mes"),
                    fecha,
                    row.getDouble("valor")
            );
            result.add(m);
        }
        return result;
    }

    public java.util.List<MedicionesPorCiudad> findAllCiudad() {
        java.util.List<MedicionesPorCiudad> result = new java.util.ArrayList<>();
        com.datastax.oss.driver.api.core.cql.ResultSet rs = session.execute("SELECT ciudad, tipo, anio, mes, fecha, idSensor, valor FROM mediciones_por_ciudad");
        for (com.datastax.oss.driver.api.core.cql.Row row : rs) {
            java.time.Instant fechaInstant = row.getInstant("fecha");
            java.util.Date fecha = (fechaInstant != null) ? java.util.Date.from(fechaInstant) : null;
            MedicionesPorCiudad m = new MedicionesPorCiudad(
                    row.getString("idSensor"),
                    row.getString("tipo"),
                    row.getString("ciudad"),
                    row.getInt("anio"),
                    row.getInt("mes"),
                    fecha,
                    row.getDouble("valor")
            );
            result.add(m);
        }
        return result;
    }

    private static MedicionesCassandraRepository instance;

    private final CqlSession session;

    private final PreparedStatement insertZonaPs;
    private final PreparedStatement insertPaisPs;
    private final PreparedStatement insertCiudadPs;

    private MedicionesCassandraRepository() {
        try {
            this.session = CassandraPool.getInstance().getSession();
            crearTablasSiNoExisten();

            this.insertZonaPs = session.prepare(
                    "INSERT INTO mediciones_por_zona (zona, tipo, anio, mes, fecha, idSensor, valor) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            this.insertPaisPs = session.prepare(
                    "INSERT INTO mediciones_por_pais (pais, tipo, anio, mes, fecha, idSensor, valor) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            this.insertCiudadPs = session.prepare(
                    "INSERT INTO mediciones_por_ciudad (ciudad, tipo, anio, mes, fecha, idSensor, valor) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
        } catch (ErrorConectionMongoException e) {
            throw new RuntimeException("No se pudo obtener la sesión de Cassandra", e);
        }
    }

    private void crearTablasSiNoExisten() {
        session.execute("CREATE TABLE IF NOT EXISTS mediciones_por_zona ("
                + "zona text, "
                + "tipo text, "
                + "anio int, "
                + "mes int, "
                + "fecha timestamp, "
                + "idSensor text, "
                + "valor double, "
                + "PRIMARY KEY ((zona, tipo, anio, mes), fecha, idSensor)"
                + ") WITH CLUSTERING ORDER BY (fecha ASC);");

        session.execute("CREATE TABLE IF NOT EXISTS mediciones_por_pais ("
                + "pais text, "
                + "tipo text, "
                + "anio int, "
                + "mes int, "
                + "fecha timestamp, "
                + "idSensor text, "
                + "valor double, "
                + "PRIMARY KEY ((pais, tipo, anio, mes), fecha, idSensor)"
                + ") WITH CLUSTERING ORDER BY (fecha ASC);");

        session.execute("CREATE TABLE IF NOT EXISTS mediciones_por_ciudad ("
                + "ciudad text, "
                + "tipo text, "
                + "anio int, "
                + "mes int, "
                + "fecha timestamp, "
                + "idSensor text, "
                + "valor double, "
                + "PRIMARY KEY ((ciudad, tipo, anio, mes), fecha, idSensor)"
                + ") WITH CLUSTERING ORDER BY (fecha ASC);");
    }

    public static MedicionesCassandraRepository getInstance() {
        if (instance == null) {
            instance = new MedicionesCassandraRepository();
        }
        return instance;
    }

    public void insertZona(MedicionesPorZona m) {
        java.time.Instant fechaInstant = (m.fecha != null) ? m.fecha.toInstant() : null;
        BoundStatement bs = insertZonaPs.bind(
                m.zona, m.tipo, m.anio, m.mes, fechaInstant, m.idSensor, m.valor
        );
        session.execute(bs);
    }

    public void insertPais(MedicionesPorPais m) {
        java.time.Instant fechaInstant = (m.fecha != null) ? m.fecha.toInstant() : null;
        BoundStatement bs = insertPaisPs.bind(
                m.pais, m.tipo, m.anio, m.mes, fechaInstant, m.idSensor, m.valor
        );
        session.execute(bs);
    }

    public void insertCiudad(MedicionesPorCiudad m) {
        java.time.Instant fechaInstant = (m.fecha != null) ? m.fecha.toInstant() : null;
        BoundStatement bs = insertCiudadPs.bind(
                m.ciudad, m.tipo, m.anio, m.mes, fechaInstant, m.idSensor, m.valor
        );
        session.execute(bs);
    }

    public void insertTrio(MedicionesPorZona mz, MedicionesPorPais mp, MedicionesPorCiudad mc) {
        BatchStatementBuilder batch = BatchStatement.builder(BatchType.LOGGED);

        if (mz != null && mz.zona != null && !mz.zona.isBlank()) {
            java.time.Instant fechaZona = (mz.fecha != null) ? mz.fecha.toInstant() : null;
            batch.addStatement(insertZonaPs.bind(mz.zona, mz.tipo, mz.anio, mz.mes, fechaZona, mz.idSensor, mz.valor));
        }
        if (mp != null && mp.pais != null && !mp.pais.isBlank()) {
            java.time.Instant fechaPais = (mp.fecha != null) ? mp.fecha.toInstant() : null;
            batch.addStatement(insertPaisPs.bind(mp.pais, mp.tipo, mp.anio, mp.mes, fechaPais, mp.idSensor, mp.valor));
        }
        if (mc != null && mc.ciudad != null && !mc.ciudad.isBlank()) {
            java.time.Instant fechaCiudad = (mc.fecha != null) ? mc.fecha.toInstant() : null;
            batch.addStatement(insertCiudadPs.bind(mc.ciudad, mc.tipo, mc.anio, mc.mes, fechaCiudad, mc.idSensor, mc.valor));
        }

        if (batch.build().size() == 0) {
            return;
        }
        session.execute(batch.build());
    }
}
