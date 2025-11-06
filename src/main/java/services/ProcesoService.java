package services;

import java.util.List;

import modelo.Proceso;
import repositories.ProcesoRepository;

public class ProcesoService {

    private static ProcesoService instance;
    private final ProcesoRepository procesoRepository;

    private ProcesoService() {
        this.procesoRepository = ProcesoRepository.getInstance();
    }

    public static ProcesoService getInstance() {
        if (instance == null) {
            instance = new ProcesoService();
        }
        return instance;
    }

    public Proceso crearProceso(String nombre, String descripcion, String tipoProceso, int costo) {
        Proceso proceso = new Proceso(nombre, descripcion, tipoProceso, costo);
        procesoRepository.save(proceso);
        return proceso;
    }

    public Proceso obtenerProcesoPorId(String procesoId) {
        return procesoRepository.findById(procesoId);
    }

    public Proceso obtenerProcesoPorNombre(String nombre) {
        return procesoRepository.findByNombre(nombre);
    }

    public List<Proceso> obtenerTodosProcesos() {
        return procesoRepository.findAll();
    }

    public List<Proceso> obtenerProcesosPorTipo(String tipoProceso) {
        return procesoRepository.findByTipo(tipoProceso);
    }

    public void actualizarProceso(Proceso proceso) {
        procesoRepository.update(proceso);
    }

    public void eliminarProceso(String procesoId) {
        procesoRepository.delete(procesoId);
    }

}
