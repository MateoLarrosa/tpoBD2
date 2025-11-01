package controlador;

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
