package controlador;

import java.util.List;

import modelo.Proceso;
import services.ProcesoService;

public class ProcesoController {

    private static ProcesoController instance;

    private ProcesoController() {
    }

    public static ProcesoController getInstance() {
        if (instance == null) {
            instance = new ProcesoController();
        }
        return instance;
    }

    public Proceso crearProceso(String nombre, String descripcion, String tipoProceso, int costo) {
        return ProcesoService.getInstance().crearProceso(nombre, descripcion, tipoProceso, costo);
    }

    public Proceso obtenerProcesoPorId(String procesoId) {
        return ProcesoService.getInstance().obtenerProcesoPorId(procesoId);
    }

    public Proceso obtenerProcesoPorNombre(String nombre) {
        return ProcesoService.getInstance().obtenerProcesoPorNombre(nombre);
    }

    public List<Proceso> obtenerTodosProcesos() {
        return ProcesoService.getInstance().obtenerTodosProcesos();
    }

    public List<Proceso> obtenerProcesosPorTipo(String tipoProceso) {
        return ProcesoService.getInstance().obtenerProcesosPorTipo(tipoProceso);
    }

    public void actualizarProceso(Proceso proceso) {
        ProcesoService.getInstance().actualizarProceso(proceso);
    }

    public void eliminarProceso(String procesoId) {
        ProcesoService.getInstance().eliminarProceso(procesoId);
    }

}
