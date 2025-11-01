package services;

import java.util.List;

import modelo.Sensor;
import repositories.SensorRepository;

public class SensorService {

    private static SensorService instance;

    private SensorService() {
    }

    public static SensorService getInstance() {
        if (instance == null) {
            instance = new SensorService();
        }
        return instance;
    }

    public void save(Sensor sensor) {
        SensorRepository.getInstance().save(sensor);
    }

    public List<Sensor> findAll() {
        return SensorRepository.getInstance().findAll();
    }
}
