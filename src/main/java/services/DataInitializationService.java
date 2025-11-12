package services;

import modelo.Proceso;

/**
 * Service responsible for initializing the database with required data.
 * This includes creating all the necessary process definitions.
 */
public class DataInitializationService {

    private static DataInitializationService instance;
    private final ProcesoService procesoService;

    private DataInitializationService() {
        this.procesoService = ProcesoService.getInstance();
    }

    public static DataInitializationService getInstance() {
        if (instance == null) {
            instance = new DataInitializationService();
        }
        return instance;
    }

    /**
     * Initializes all required processes in the database.
     * Only creates processes that don't already exist.
     */
    public void initializeProcesos() {
        System.out.println("Inicializando procesos en la base de datos...");

        String[] granularidades = {"ZONA", "CIUDAD", "PAIS"};
        String[] tiposMedicion = {"TEMPERATURA", "HUMEDAD"};
        String[] tiposProceso = {"min", "max", "promedio"};

        int procesosCreados = 0;
        int procesosExistentes = 0;

        // Create processes for all combinations
        for (String tipoProceso : tiposProceso) {
            for (String granularidad : granularidades) {
                for (String tipoMedicion : tiposMedicion) {
                    String nombreProceso = construirNombreProceso(tipoProceso, granularidad, tipoMedicion);
                    String descripcion = construirDescripcion(tipoProceso, granularidad, tipoMedicion);
                    int costo = calcularCosto(tipoProceso, granularidad);

                    // Check if process already exists
                    Proceso procesoExistente = procesoService.obtenerProcesoPorNombre(nombreProceso);
                    if (procesoExistente == null) {
                        procesoService.crearProceso(nombreProceso, descripcion, "INFORME", costo);
                        procesosCreados++;
                    } else {
                        procesosExistentes++;
                    }
                }
            }
        }

        System.out.println("Inicialización completada:");
    }

    /**
     * Constructs the process name following the pattern:
     * INFORME_{MIN|MAX|PROMEDIO}_{ZONA|CIUDAD|PAIS}_{TEMPERATURA|HUMEDAD}
     */
    private String construirNombreProceso(String tipoProceso, String granularidad, String tipoMedicion) {
        String tipo = tipoProceso.toUpperCase();
        if (tipo.equals("PROMEDIO")) {
            return "INFORME_PROMEDIO_" + granularidad + "_" + tipoMedicion;
        }
        return "INFORME_" + tipo + "_" + granularidad + "_" + tipoMedicion;
    }

    /**
     * Creates a human-readable description for the process
     */
    private String construirDescripcion(String tipoProceso, String granularidad, String tipoMedicion) {
        String tipoTexto;
        switch (tipoProceso.toLowerCase()) {
            case "min":
                tipoTexto = "mínima";
                break;
            case "max":
                tipoTexto = "máxima";
                break;
            case "promedio":
                tipoTexto = "promedio";
                break;
            default:
                tipoTexto = tipoProceso;
        }

        String granularidadTexto = granularidad.toLowerCase();
        String medicionTexto = tipoMedicion.toLowerCase();

        return String.format("Informe de medición %s de %s por %s", 
                            tipoTexto, medicionTexto, granularidadTexto);
    }

    /**
     * Calculates the cost of a process based on its type and granularity.
     * More detailed granularity = higher cost
     */
    private int calcularCosto(String tipoProceso, String granularidad) {
        int costoBase;
        
        // Base cost depends on granularity
        switch (granularidad) {
            case "ZONA":
                costoBase = 100;
                break;
            case "CIUDAD":
                costoBase = 200;
                break;
            case "PAIS":
                costoBase = 300;
                break;
            default:
                costoBase = 100;
        }

        // Promedio costs more because it requires more computation
        if (tipoProceso.equalsIgnoreCase("promedio")) {
            costoBase += 50;
        }

        return costoBase;
    }

    /**
     * Verifies if all required processes exist in the database
     */
    public boolean verificarProcesosExistentes() {
        String[] granularidades = {"ZONA", "CIUDAD", "PAIS"};
        String[] tiposMedicion = {"TEMPERATURA", "HUMEDAD"};
        String[] tiposProceso = {"min", "max", "promedio"};

        int totalEsperado = granularidades.length * tiposMedicion.length * tiposProceso.length;
        int encontrados = 0;

        for (String tipoProceso : tiposProceso) {
            for (String granularidad : granularidades) {
                for (String tipoMedicion : tiposMedicion) {
                    String nombreProceso = construirNombreProceso(tipoProceso, granularidad, tipoMedicion);
                    Proceso proceso = procesoService.obtenerProcesoPorNombre(nombreProceso);
                    if (proceso != null) {
                        encontrados++;
                    }
                }
            }
        }

        return encontrados == totalEsperado;
    }
}
