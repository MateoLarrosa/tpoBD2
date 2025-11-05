package controlador;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.Sensor;
import services.MedicionesService;
import services.SolicitudProcesoService;

public class MedicionesController {

    private static MedicionesController instance;
    private String usuarioActualId; // ID del usuario que está usando el controlador

    private MedicionesController() {
    }

    public static MedicionesController getInstance() {
        if (instance == null) {
            instance = new MedicionesController();
        }
        return instance;
    }

    /**
     * Establece el usuario actual para registrar sus consultas
     * @param usuarioId ID del usuario
     */
    public void setUsuarioActual(String usuarioId) {
        this.usuarioActualId = usuarioId;
    }

    /**
     * Obtiene el ID del usuario actual
     * @return ID del usuario actual
     */
    public String getUsuarioActual() {
        return this.usuarioActualId;
    }

    public List<modelo.MedicionesPorZona> obtenerTodasZonas() {
        long inicio = System.currentTimeMillis();
        try {
            List<modelo.MedicionesPorZona> resultado = services.MedicionesService.getInstance().obtenerTodasZonas();
            registrarSolicitud("CONSULTA_MEDICIONES_ZONA", new HashMap<>(), "ÉXITO - " + resultado.size() + " zonas", inicio);
            return resultado;
        } catch (Exception e) {
            registrarSolicitud("CONSULTA_MEDICIONES_ZONA", new HashMap<>(), "ERROR: " + e.getMessage(), inicio);
            throw e;
        }
    }

    public List<modelo.MedicionesPorPais> obtenerTodosPaises() {
        long inicio = System.currentTimeMillis();
        try {
            List<modelo.MedicionesPorPais> resultado = services.MedicionesService.getInstance().obtenerTodosPaises();
            registrarSolicitud("CONSULTA_MEDICIONES_PAIS", new HashMap<>(), "ÉXITO - " + resultado.size() + " países", inicio);
            return resultado;
        } catch (Exception e) {
            registrarSolicitud("CONSULTA_MEDICIONES_PAIS", new HashMap<>(), "ERROR: " + e.getMessage(), inicio);
            throw e;
        }
    }

    public List<modelo.MedicionesPorCiudad> obtenerTodasCiudades() {
        long inicio = System.currentTimeMillis();
        try {
            List<modelo.MedicionesPorCiudad> resultado = services.MedicionesService.getInstance().obtenerTodasCiudades();
            registrarSolicitud("CONSULTA_MEDICIONES_CIUDAD", new HashMap<>(), "ÉXITO - " + resultado.size() + " ciudades", inicio);
            return resultado;
        } catch (Exception e) {
            registrarSolicitud("CONSULTA_MEDICIONES_CIUDAD", new HashMap<>(), "ERROR: " + e.getMessage(), inicio);
            throw e;
        }
    }

    // Métodos para obtener mediciones filtradas
    public List<modelo.MedicionesPorZona> obtenerMedicionesPorZonaYRango(String zona, String tipo, Date fechaInicio, Date fechaFin) {
        long inicio = System.currentTimeMillis();
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("zona", zona);
        parametros.put("tipo", tipo);
        parametros.put("fechaInicio", fechaInicio);
        parametros.put("fechaFin", fechaFin);
        
        try {
            List<modelo.MedicionesPorZona> resultado = services.MedicionesService.getInstance().obtenerMedicionesPorZonaYRango(zona, tipo, fechaInicio, fechaFin);
            registrarSolicitud("CONSULTA_MEDICIONES_ZONA", parametros, "ÉXITO - " + resultado.size() + " registros", inicio);
            return resultado;
        } catch (Exception e) {
            registrarSolicitud("CONSULTA_MEDICIONES_ZONA", parametros, "ERROR: " + e.getMessage(), inicio);
            throw e;
        }
    }

    public List<modelo.MedicionesPorPais> obtenerMedicionesPorPaisYRango(String pais, String tipo, Date fechaInicio, Date fechaFin) {
        long inicio = System.currentTimeMillis();
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("pais", pais);
        parametros.put("tipo", tipo);
        parametros.put("fechaInicio", fechaInicio);
        parametros.put("fechaFin", fechaFin);
        
        try {
            List<modelo.MedicionesPorPais> resultado = services.MedicionesService.getInstance().obtenerMedicionesPorPaisYRango(pais, tipo, fechaInicio, fechaFin);
            registrarSolicitud("CONSULTA_MEDICIONES_PAIS", parametros, "ÉXITO - " + resultado.size() + " registros", inicio);
            return resultado;
        } catch (Exception e) {
            registrarSolicitud("CONSULTA_MEDICIONES_PAIS", parametros, "ERROR: " + e.getMessage(), inicio);
            throw e;
        }
    }

    public List<modelo.MedicionesPorCiudad> obtenerMedicionesPorCiudadYRango(String ciudad, String tipo, Date fechaInicio, Date fechaFin) {
        long inicio = System.currentTimeMillis();
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("ciudad", ciudad);
        parametros.put("tipo", tipo);
        parametros.put("fechaInicio", fechaInicio);
        parametros.put("fechaFin", fechaFin);
        
        try {
            List<modelo.MedicionesPorCiudad> resultado = services.MedicionesService.getInstance().obtenerMedicionesPorCiudadYRango(ciudad, tipo, fechaInicio, fechaFin);
            registrarSolicitud("CONSULTA_MEDICIONES_CIUDAD", parametros, "ÉXITO - " + resultado.size() + " registros", inicio);
            return resultado;
        } catch (Exception e) {
            registrarSolicitud("CONSULTA_MEDICIONES_CIUDAD", parametros, "ERROR: " + e.getMessage(), inicio);
            throw e;
        }
    }

    public void agregarMedicion(Sensor sensor, String tipo, int anio, int mes, String fecha, double valor) {
        long inicio = System.currentTimeMillis();
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("sensorId", sensor.getId());
        parametros.put("tipo", tipo);
        parametros.put("anio", anio);
        parametros.put("mes", mes);
        parametros.put("fecha", fecha);
        parametros.put("valor", valor);
        
        try {
            MedicionesService.getInstance().registrarMedicion(sensor, tipo, anio, mes, fecha, valor);
            registrarSolicitud("REGISTRAR_MEDICION", parametros, "ÉXITO", inicio);
        } catch (Exception e) {
            registrarSolicitud("REGISTRAR_MEDICION", parametros, "ERROR: " + e.getMessage(), inicio);
            throw e;
        }
    }

    public void agregarMedicionPorId(String sensorId, String tipo, int anio, int mes, String fecha, double valor) {
        long inicio = System.currentTimeMillis();
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("sensorId", sensorId);
        parametros.put("tipo", tipo);
        parametros.put("anio", anio);
        parametros.put("mes", mes);
        parametros.put("fecha", fecha);
        parametros.put("valor", valor);
        
        try {
            Sensor sensor = MedicionesService.getInstance().getSensorRepo().findById(sensorId);
            if (sensor == null) {
                registrarSolicitud("REGISTRAR_MEDICION", parametros, "ERROR: Sensor no encontrado", inicio);
                throw new IllegalArgumentException("Sensor no encontrado con id: " + sensorId);
            }
            MedicionesService.getInstance().registrarMedicion(sensor, tipo, anio, mes, fecha, valor);
            registrarSolicitud("REGISTRAR_MEDICION", parametros, "ÉXITO", inicio);
        } catch (Exception e) {
            registrarSolicitud("REGISTRAR_MEDICION", parametros, "ERROR: " + e.getMessage(), inicio);
            throw e;
        }
    }

    /**
     * Método auxiliar para registrar solicitudes de proceso
     */
    private void registrarSolicitud(String nombreProceso, Map<String, Object> parametros, String resultado, long inicio) {
        if (usuarioActualId != null && !usuarioActualId.isEmpty()) {
            try {
                long tiempoEjecucion = System.currentTimeMillis() - inicio;
                SolicitudProcesoService.getInstance().registrarSolicitudCompletada(
                    usuarioActualId, nombreProceso, parametros, resultado, tiempoEjecucion
                );
            } catch (Exception e) {
                // Error al registrar la solicitud, pero no interrumpir la operación principal
                System.err.println("Error al registrar solicitud de proceso: " + e.getMessage());
            }
        }
    }
}
