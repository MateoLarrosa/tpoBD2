package services;

import java.util.List;

import modelo.Proceso;
import repositories.ProcesoRepository;

/**
 * Servicio para gestionar el catálogo de procesos disponibles en el sistema.
 * Implementa el patrón Singleton.
 */
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

    /**
     * Crea un nuevo proceso en el catálogo
     * @param nombre Nombre del proceso
     * @param descripcion Descripción del proceso
     * @param tipoProceso Tipo de proceso
     * @param costo Costo del proceso
     * @return El proceso creado
     */
    public Proceso crearProceso(String nombre, String descripcion, String tipoProceso, int costo) {
        Proceso proceso = new Proceso(nombre, descripcion, tipoProceso, costo);
        procesoRepository.save(proceso);
        return proceso;
    }

    /**
     * Obtiene un proceso por su ID
     * @param procesoId ID del proceso
     * @return El proceso encontrado o null
     */
    public Proceso obtenerProcesoPorId(String procesoId) {
        return procesoRepository.findById(procesoId);
    }

    /**
     * Obtiene un proceso por su nombre
     * @param nombre Nombre del proceso
     * @return El proceso encontrado o null
     */
    public Proceso obtenerProcesoPorNombre(String nombre) {
        return procesoRepository.findByNombre(nombre);
    }

    /**
     * Obtiene todos los procesos disponibles
     * @return Lista de todos los procesos
     */
    public List<Proceso> obtenerTodosProcesos() {
        return procesoRepository.findAll();
    }

    /**
     * Obtiene procesos por tipo
     * @param tipoProceso Tipo de proceso
     * @return Lista de procesos del tipo especificado
     */
    public List<Proceso> obtenerProcesosPorTipo(String tipoProceso) {
        return procesoRepository.findByTipo(tipoProceso);
    }

    /**
     * Actualiza un proceso existente
     * @param proceso Proceso con los datos actualizados
     */
    public void actualizarProceso(Proceso proceso) {
        procesoRepository.update(proceso);
    }

    /**
     * Elimina un proceso del catálogo
     * @param procesoId ID del proceso a eliminar
     */
    public void eliminarProceso(String procesoId) {
        procesoRepository.delete(procesoId);
    }

    /**
     * Inicializa el catálogo con procesos predefinidos
     * Solo debe ejecutarse una vez al iniciar el sistema
     */
    public void inicializarCatalogo() {
        // Verificar si ya hay procesos
        if (!procesoRepository.findAll().isEmpty()) {
            System.out.println("El catálogo de procesos ya está inicializado.");
            return;
        }

        System.out.println("Inicializando catálogo de procesos...");

        // ==================== INFORMES DE MÁXIMAS Y MÍNIMAS ====================
        // Por ciudades
        crearProceso(
            "INFORME_MAXMIN_CIUDAD_ANUAL",
            "Informe de temperaturas y humedad máximas y mínimas por ciudades (anualizado)",
            "INFORME",
            60
        );

        crearProceso(
            "INFORME_MAXMIN_CIUDAD_MENSUAL",
            "Informe de temperaturas y humedad máximas y mínimas por ciudades (mensualizado)",
            "INFORME",
            50
        );

        // Por zonas
        crearProceso(
            "INFORME_MAXMIN_ZONA_ANUAL",
            "Informe de temperaturas y humedad máximas y mínimas por zonas (anualizado)",
            "INFORME",
            60
        );

        crearProceso(
            "INFORME_MAXMIN_ZONA_MENSUAL",
            "Informe de temperaturas y humedad máximas y mínimas por zonas (mensualizado)",
            "INFORME",
            50
        );

        // Por países
        crearProceso(
            "INFORME_MAXMIN_PAIS_ANUAL",
            "Informe de temperaturas y humedad máximas y mínimas por países (anualizado)",
            "INFORME",
            60
        );

        crearProceso(
            "INFORME_MAXMIN_PAIS_MENSUAL",
            "Informe de temperaturas y humedad máximas y mínimas por países (mensualizado)",
            "INFORME",
            50
        );

        // ==================== INFORMES DE PROMEDIOS ====================
        // Por ciudades
        crearProceso(
            "INFORME_PROMEDIO_CIUDAD_ANUAL",
            "Informe de temperaturas y humedad promedio por ciudades (anualizado)",
            "INFORME",
            55
        );

        crearProceso(
            "INFORME_PROMEDIO_CIUDAD_MENSUAL",
            "Informe de temperaturas y humedad promedio por ciudades (mensualizado)",
            "INFORME",
            45
        );

        // Por zonas
        crearProceso(
            "INFORME_PROMEDIO_ZONA_ANUAL",
            "Informe de temperaturas y humedad promedio por zonas (anualizado)",
            "INFORME",
            55
        );

        crearProceso(
            "INFORME_PROMEDIO_ZONA_MENSUAL",
            "Informe de temperaturas y humedad promedio por zonas (mensualizado)",
            "INFORME",
            45
        );

        // Por países
        crearProceso(
            "INFORME_PROMEDIO_PAIS_ANUAL",
            "Informe de temperaturas y humedad promedio por países (anualizado)",
            "INFORME",
            55
        );

        crearProceso(
            "INFORME_PROMEDIO_PAIS_MENSUAL",
            "Informe de temperaturas y humedad promedio por países (mensualizado)",
            "INFORME",
            45
        );

        // ==================== ALERTAS ====================
        // Por ciudades
        crearProceso(
            "ALERTA_TEMPERATURA_CIUDAD",
            "Alertas de temperaturas en rango determinado por ciudad",
            "ALERTA",
            40
        );

        crearProceso(
            "ALERTA_HUMEDAD_CIUDAD",
            "Alertas de humedad en rango determinado por ciudad",
            "ALERTA",
            40
        );

        // Por zonas
        crearProceso(
            "ALERTA_TEMPERATURA_ZONA",
            "Alertas de temperaturas en rango determinado por zona",
            "ALERTA",
            40
        );

        crearProceso(
            "ALERTA_HUMEDAD_ZONA",
            "Alertas de humedad en rango determinado por zona",
            "ALERTA",
            40
        );

        // Por países
        crearProceso(
            "ALERTA_TEMPERATURA_PAIS",
            "Alertas de temperaturas en rango determinado por país",
            "ALERTA",
            40
        );

        crearProceso(
            "ALERTA_HUMEDAD_PAIS",
            "Alertas de humedad en rango determinado por país",
            "ALERTA",
            40
        );

        // ==================== CONSULTAS EN LÍNEA DE SENSORES ====================
        crearProceso(
            "CONSULTA_SENSORES_CIUDAD",
            "Consulta en línea de información de sensores por ciudad",
            "CONSULTA",
            10
        );

        crearProceso(
            "CONSULTA_SENSORES_ZONA",
            "Consulta en línea de información de sensores por zona",
            "CONSULTA",
            10
        );

        crearProceso(
            "CONSULTA_SENSORES_PAIS",
            "Consulta en línea de información de sensores por país",
            "CONSULTA",
            10
        );

        // ==================== CONSULTAS DE MEDICIONES EN LÍNEA ====================
        crearProceso(
            "CONSULTA_MEDICIONES_CIUDAD",
            "Consulta de mediciones por ciudad en rango de fechas",
            "CONSULTA",
            12
        );

        crearProceso(
            "CONSULTA_MEDICIONES_ZONA",
            "Consulta de mediciones por zona en rango de fechas",
            "CONSULTA",
            12
        );

        crearProceso(
            "CONSULTA_MEDICIONES_PAIS",
            "Consulta de mediciones por país en rango de fechas",
            "CONSULTA",
            12
        );

        // ==================== PROCESOS PERIÓDICOS ====================
        crearProceso(
            "PROCESO_PERIODICO_CIUDAD_ANUAL",
            "Proceso periódico de consultas de humedad y temperatura por ciudades (anual)",
            "PROCESO_PERIODICO",
            80
        );

        crearProceso(
            "PROCESO_PERIODICO_CIUDAD_MENSUAL",
            "Proceso periódico de consultas de humedad y temperatura por ciudades (mensual)",
            "PROCESO_PERIODICO",
            70
        );

        crearProceso(
            "PROCESO_PERIODICO_ZONA_ANUAL",
            "Proceso periódico de consultas de humedad y temperatura por zonas (anual)",
            "PROCESO_PERIODICO",
            80
        );

        crearProceso(
            "PROCESO_PERIODICO_ZONA_MENSUAL",
            "Proceso periódico de consultas de humedad y temperatura por zonas (mensual)",
            "PROCESO_PERIODICO",
            70
        );

        crearProceso(
            "PROCESO_PERIODICO_PAIS_ANUAL",
            "Proceso periódico de consultas de humedad y temperatura por países (anual)",
            "PROCESO_PERIODICO",
            80
        );

        crearProceso(
            "PROCESO_PERIODICO_PAIS_MENSUAL",
            "Proceso periódico de consultas de humedad y temperatura por países (mensual)",
            "PROCESO_PERIODICO",
            70
        );

        // ==================== FACTURACIÓN ====================
        crearProceso(
            "GENERAR_FACTURA",
            "Generación de factura mensual para usuario",
            "FACTURACION",
            30
        );

        crearProceso(
            "REGISTRAR_PAGO",
            "Registro de pago de factura",
            "FACTURACION",
            20
        );

        crearProceso(
            "ACREDITAR_CUENTA_CORRIENTE",
            "Acreditación en cuenta corriente del usuario",
            "FACTURACION",
            15
        );

        crearProceso(
            "DEBITAR_CUENTA_CORRIENTE",
            "Débito en cuenta corriente del usuario",
            "FACTURACION",
            15
        );

        crearProceso(
            "CONSULTAR_SALDO_CUENTA",
            "Consulta de saldo de cuenta corriente",
            "FACTURACION",
            5
        );

        // Crear procesos de gestión de mediciones
        crearProceso(
            "REGISTRAR_MEDICION",
            "Registro de nueva medición de sensor",
            "GESTION",
            20
        );

        // Crear procesos de gestión de sensores
        crearProceso(
            "ALTA_SENSOR",
            "Alta de nuevo sensor en el sistema",
            "GESTION",
            15
        );

        crearProceso(
            "MODIFICAR_SENSOR",
            "Modificación de datos de sensor",
            "GESTION",
            12
        );

        crearProceso(
            "BAJA_SENSOR",
            "Baja de sensor del sistema",
            "GESTION",
            10
        );

        crearProceso(
            "CONSULTA_SENSORES",
            "Consulta de sensores disponibles",
            "CONSULTA",
            5
        );

        // Crear procesos de mensajería
        crearProceso(
            "ENVIAR_MENSAJE",
            "Envío de mensaje a usuario o grupo",
            "MENSAJERIA",
            8
        );

        crearProceso(
            "LEER_MENSAJES",
            "Lectura de mensajes recibidos",
            "MENSAJERIA",
            5
        );

        // Crear procesos de usuario
        crearProceso(
            "CREAR_USUARIO",
            "Creación de nuevo usuario",
            "GESTION",
            15
        );

        crearProceso(
            "INICIAR_SESION",
            "Inicio de sesión de usuario",
            "AUTENTICACION",
            5
        );

        System.out.println("Catálogo de procesos inicializado correctamente.");
    }
}
