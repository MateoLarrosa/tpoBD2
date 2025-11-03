package controlador;

import java.util.Date;
import java.util.List;

import modelo.Sensor;
import services.MedicionesService;

public class MedicionesController {

    private static MedicionesController instance;

    private MedicionesController() {
    }

    public static MedicionesController getInstance() {
        if (instance == null) {
            instance = new MedicionesController();
        }
        return instance;
    }

    public List<modelo.MedicionesPorZona> obtenerTodasZonas() {
        return services.MedicionesService.getInstance().obtenerTodasZonas();
    }

    public List<modelo.MedicionesPorPais> obtenerTodosPaises() {
        return services.MedicionesService.getInstance().obtenerTodosPaises();
    }

    public List<modelo.MedicionesPorCiudad> obtenerTodasCiudades() {
        return services.MedicionesService.getInstance().obtenerTodasCiudades();
    }

    // Métodos para obtener mediciones filtradas
    public List<modelo.MedicionesPorZona> obtenerMedicionesPorZonaYRango(String zona, String tipo, Date fechaInicio, Date fechaFin) {
        return services.MedicionesService.getInstance().obtenerMedicionesPorZonaYRango(zona, tipo, fechaInicio, fechaFin);
    }

    public List<modelo.MedicionesPorPais> obtenerMedicionesPorPaisYRango(String pais, String tipo, Date fechaInicio, Date fechaFin) {
        return services.MedicionesService.getInstance().obtenerMedicionesPorPaisYRango(pais, tipo, fechaInicio, fechaFin);
    }

    public List<modelo.MedicionesPorCiudad> obtenerMedicionesPorCiudadYRango(String ciudad, String tipo, Date fechaInicio, Date fechaFin) {
        return services.MedicionesService.getInstance().obtenerMedicionesPorCiudadYRango(ciudad, tipo, fechaInicio, fechaFin);
    }

    public void agregarMedicion(Sensor sensor, String tipo, int anio, int mes, String fecha, double valor) {
        MedicionesService.getInstance().registrarMedicion(sensor, tipo, anio, mes, fecha, valor);
    }

    public void agregarMedicionPorId(String sensorId, String tipo, int anio, int mes, String fecha, double valor) {
        Sensor sensor = MedicionesService.getInstance().getSensorRepo().findById(sensorId);
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor no encontrado con id: " + sensorId);
        }
        MedicionesService.getInstance().registrarMedicion(sensor, tipo, anio, mes, fecha, valor);
    }
}
