package services;

import java.util.Date;
import java.util.List;

import modelo.MedicionesPorCiudad;
import modelo.MedicionesPorPais;
import modelo.MedicionesPorZona;
import modelo.Sensor;
import repositories.MedicionesCassandraRepository;
import repositories.SensorRepository;

/**
 * Orquesta la creación de objetos de medición y delega al repository de
 * Cassandra. Singleton como en las instrucciones.
 */
public class MedicionesService {

    // Métodos para obtener todas las ubicaciones
    public List<MedicionesPorZona> obtenerTodasZonas() {
        return getCassandraRepo().findAllZona();
    }

    public List<MedicionesPorPais> obtenerTodosPaises() {
        return getCassandraRepo().findAllPais();
    }

    public List<MedicionesPorCiudad> obtenerTodasCiudades() {
        return getCassandraRepo().findAllCiudad();
    }

    // Métodos para obtener mediciones filtradas por ubicación, tipo y rango de fechas
    public List<MedicionesPorZona> obtenerMedicionesPorZonaYRango(String zona, String tipo, Date fechaInicio, Date fechaFin) {
        return getCassandraRepo().findByZonaYRangoFechas(zona, tipo, fechaInicio, fechaFin);
    }

    public List<MedicionesPorPais> obtenerMedicionesPorPaisYRango(String pais, String tipo, Date fechaInicio, Date fechaFin) {
        return getCassandraRepo().findByPaisYRangoFechas(pais, tipo, fechaInicio, fechaFin);
    }

    public List<MedicionesPorCiudad> obtenerMedicionesPorCiudadYRango(String ciudad, String tipo, Date fechaInicio, Date fechaFin) {
        return getCassandraRepo().findByCiudadYRangoFechas(ciudad, tipo, fechaInicio, fechaFin);
    }

    private static MedicionesService instance;

    private MedicionesService() {
    }

    public static MedicionesService getInstance() {
        if (instance == null) {
            instance = new MedicionesService();
        }
        return instance;
    }

    public SensorRepository getSensorRepo() {
        return SensorRepository.getInstance();
    }

    private MedicionesCassandraRepository getCassandraRepo() {
        return MedicionesCassandraRepository.getInstance();
    }

    /**
     * Crea las tres mediciones a partir del sensor (Mongo) y las inserta en
     * Cassandra.
     *
     * @param sensorId id del sensor en Mongo
     * @param tipo tipo de medición (ej.: "TEMPERATURA") -> particiona junto con
     * anio/mes
     * @param anio año de la medición
     * @param mes mes de la medición (1..12)
     * @param fecha fecha exacta (texto ISO-8601 o tu formato elegido, coherente
     * con Cassandra)
     * @param valor valor numérico de la medición
     */
    public void registrarMedicion(Sensor sensor, String tipo, int anio, int mes, String fecha, double valor) {
        // Usar los datos del sensor para las ubicaciones
    String zona = sensor.getZona();
    String pais = sensor.getPais();
    String ciudad = sensor.getCiudad();
    String sensorId = sensor.getId();
    String nombre = sensor.getNombre();
    Double latitud = sensor.getLatitud();
    Double longitud = sensor.getLongitud();

    // Convertir String fecha a java.util.Date usando LocalDate (YYYY-MM-DD)
    Date fechaDate = null;
    if (fecha != null && !fecha.isBlank()) {
        try {
        java.time.LocalDate localDate = java.time.LocalDate.parse(fecha);
        fechaDate = Date.from(localDate.atStartOfDay(java.time.ZoneOffset.UTC).toInstant());
        } catch (Exception e) {
        // Manejo simple: dejar fechaDate en null si falla el parseo
        }
    }

    MedicionesPorZona mz = (zona == null || zona.isBlank()) ? null
        : new MedicionesPorZona(sensorId, tipo, zona, anio, mes, fechaDate, valor, nombre, latitud, longitud, ciudad, pais);

    MedicionesPorPais mp = (pais == null || pais.isBlank()) ? null
        : new MedicionesPorPais(sensorId, tipo, pais, anio, mes, fechaDate, valor, nombre, latitud, longitud, ciudad, zona);

    MedicionesPorCiudad mc = (ciudad == null || ciudad.isBlank()) ? null
        : new MedicionesPorCiudad(sensorId, tipo, ciudad, anio, mes, fechaDate, valor, nombre, latitud, longitud, pais, zona);

    getCassandraRepo().insertTrio(mz, mp, mc);
    }
}
