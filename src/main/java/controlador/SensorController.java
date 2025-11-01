package controlador;

import java.util.List;

import modelo.Sensor;
import services.SensorService;

public class SensorController {

    private static SensorController instance;

    private SensorController() {
    }

    public static SensorController getInstance() {
        if (instance == null) {
            instance = new SensorController();
        }
        return instance;
    }

    public void save(Sensor sensor) {
        SensorService.getInstance().save(sensor);
    }

    public List<Sensor> findAll() {
        return SensorService.getInstance().findAll();
    }
}
