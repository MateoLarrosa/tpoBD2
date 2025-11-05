package controlador;

import java.util.List;

import modelo.Proceso;
import services.ProcesoService;

/**
 * Controlador para gestionar el catálogo de procesos disponibles
 */
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

    /**
     * Crea un nuevo proceso en el catálogo
     */
    public Proceso crearProceso(String nombre, String descripcion, String tipoProceso, int costo) {
        return ProcesoService.getInstance().crearProceso(nombre, descripcion, tipoProceso, costo);
    }

    /**
     * Obtiene un proceso por su ID
     */
    public Proceso obtenerProcesoPorId(String procesoId) {
        return ProcesoService.getInstance().obtenerProcesoPorId(procesoId);
    }

    /**
     * Obtiene un proceso por su nombre
     */
    public Proceso obtenerProcesoPorNombre(String nombre) {
        return ProcesoService.getInstance().obtenerProcesoPorNombre(nombre);
    }

    /**
     * Obtiene todos los procesos disponibles
     */
    public List<Proceso> obtenerTodosProcesos() {
        return ProcesoService.getInstance().obtenerTodosProcesos();
    }

    /**
     * Obtiene procesos por tipo
     */
    public List<Proceso> obtenerProcesosPorTipo(String tipoProceso) {
        return ProcesoService.getInstance().obtenerProcesosPorTipo(tipoProceso);
    }

    /**
     * Actualiza un proceso existente
     */
    public void actualizarProceso(Proceso proceso) {
        ProcesoService.getInstance().actualizarProceso(proceso);
    }

    /**
     * Elimina un proceso del catálogo
     */
    public void eliminarProceso(String procesoId) {
        ProcesoService.getInstance().eliminarProceso(procesoId);
    }

    /**
     * Inicializa el catálogo con procesos predefinidos
     */
    public void inicializarCatalogo() {
        ProcesoService.getInstance().inicializarCatalogo();
    }
}
