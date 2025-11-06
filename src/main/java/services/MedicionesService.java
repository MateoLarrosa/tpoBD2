package services;

import java.util.Date;
import java.util.List;

import modelo.MedicionesPorCiudad;
import modelo.MedicionesPorPais;
import modelo.MedicionesPorZona;
import modelo.Sensor;
import repositories.MedicionesCassandraRepository;
import repositories.SensorRepository;

public class MedicionesService {

    
    public List<MedicionesPorZona> obtenerTodasZonas() {
        return getCassandraRepo().findAllZona();
    }

    public List<MedicionesPorPais> obtenerTodosPaises() {
        return getCassandraRepo().findAllPais();
    }

    public List<MedicionesPorCiudad> obtenerTodasCiudades() {
        return getCassandraRepo().findAllCiudad();
    }

    
    public List<MedicionesPorZona> obtenerMedicionesPorZonaYRango(String zona, String tipo, Date fechaInicio, Date fechaFin) {
        return getCassandraRepo().findByZonaYRangoFechas(zona, tipo, fechaInicio, fechaFin);
    }

    public List<MedicionesPorPais> obtenerMedicionesPorPaisYRango(String pais, String tipo, Date fechaInicio, Date fechaFin) {
        return getCassandraRepo().findByPaisYRangoFechas(pais, tipo, fechaInicio, fechaFin);
    }

    public List<MedicionesPorCiudad> obtenerMedicionesPorCiudadYRango(String ciudad, String tipo, Date fechaInicio, Date fechaFin) {
        return getCassandraRepo().findByCiudadYRangoFechas(ciudad, tipo, fechaInicio, fechaFin);
    }

    
    public Double obtenerMinimoPorZona(String zona, String tipo, Date fechaInicio, Date fechaFin) {
        return obtenerMedicionesPorZonaYRango(zona, tipo, fechaInicio, fechaFin)
                .stream().mapToDouble(m -> m.valor).min().orElse(Double.NaN);
    }

    public Double obtenerMinimoPorCiudad(String ciudad, String tipo, Date fechaInicio, Date fechaFin) {
        return obtenerMedicionesPorCiudadYRango(ciudad, tipo, fechaInicio, fechaFin)
                .stream().mapToDouble(m -> m.valor).min().orElse(Double.NaN);
    }

    public Double obtenerMinimoPorPais(String pais, String tipo, Date fechaInicio, Date fechaFin) {
        return obtenerMedicionesPorPaisYRango(pais, tipo, fechaInicio, fechaFin)
                .stream().mapToDouble(m -> m.valor).min().orElse(Double.NaN);
    }

    
    public Double obtenerMaximoPorZona(String zona, String tipo, Date fechaInicio, Date fechaFin) {
        return obtenerMedicionesPorZonaYRango(zona, tipo, fechaInicio, fechaFin)
                .stream().mapToDouble(m -> m.valor).max().orElse(Double.NaN);
    }

    public Double obtenerMaximoPorCiudad(String ciudad, String tipo, Date fechaInicio, Date fechaFin) {
        return obtenerMedicionesPorCiudadYRango(ciudad, tipo, fechaInicio, fechaFin)
                .stream().mapToDouble(m -> m.valor).max().orElse(Double.NaN);
    }

    public Double obtenerMaximoPorPais(String pais, String tipo, Date fechaInicio, Date fechaFin) {
        return obtenerMedicionesPorPaisYRango(pais, tipo, fechaInicio, fechaFin)
                .stream().mapToDouble(m -> m.valor).max().orElse(Double.NaN);
    }

    
    public Double obtenerPromedioPorZona(String zona, String tipo, Date fechaInicio, Date fechaFin) {
        return obtenerMedicionesPorZonaYRango(zona, tipo, fechaInicio, fechaFin)
                .stream().mapToDouble(m -> m.valor).average().orElse(Double.NaN);
    }

    public Double obtenerPromedioPorCiudad(String ciudad, String tipo, Date fechaInicio, Date fechaFin) {
        return obtenerMedicionesPorCiudadYRango(ciudad, tipo, fechaInicio, fechaFin)
                .stream().mapToDouble(m -> m.valor).average().orElse(Double.NaN);
    }

    public Double obtenerPromedioPorPais(String pais, String tipo, Date fechaInicio, Date fechaFin) {
        return obtenerMedicionesPorPaisYRango(pais, tipo, fechaInicio, fechaFin)
                .stream().mapToDouble(m -> m.valor).average().orElse(Double.NaN);
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

    public void registrarMedicion(Sensor sensor, String tipo, int anio, int mes, String fecha, double valor) {
        
        String zona = (sensor.getZona() != null && !sensor.getZona().isBlank()) ? sensor.getZona() : null;
        String pais = (sensor.getPais() != null && !sensor.getPais().isBlank()) ? sensor.getPais() : null;
        String ciudad = (sensor.getCiudad() != null && !sensor.getCiudad().isBlank()) ? sensor.getCiudad() : null;
        String sensorId = sensor.getId();
        String nombre = (sensor.getNombre() != null && !sensor.getNombre().isBlank()) ? sensor.getNombre() : null;
        Double latitud = (sensor.getLatitud() != 0.0) ? sensor.getLatitud() : null;
        Double longitud = (sensor.getLongitud() != 0.0) ? sensor.getLongitud() : null;
        double monto = sensor.getMontoPorMedicion();

        
        Date fechaDate = null;
        if (fecha != null && !fecha.isBlank()) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                fechaDate = sdf.parse(fecha);
            } catch (Exception e) {
                
            }
        }

        MedicionesPorZona mz = (zona == null || zona.isBlank()) ? null
                : new MedicionesPorZona(sensorId, tipo, zona, anio, mes, fechaDate, valor, monto, nombre, latitud, longitud, ciudad, pais);

        MedicionesPorPais mp = (pais == null || pais.isBlank()) ? null
                : new MedicionesPorPais(sensorId, tipo, pais, anio, mes, fechaDate, valor, monto, nombre, latitud, longitud, ciudad, zona);

        MedicionesPorCiudad mc = (ciudad == null || ciudad.isBlank()) ? null
                : new MedicionesPorCiudad(sensorId, tipo, ciudad, anio, mes, fechaDate, valor, monto, nombre, latitud, longitud, pais, zona);

        getCassandraRepo().insertTrio(mz, mp, mc);
    }
}
