package controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.Sensor;
import services.MedicionesService;
import services.SolicitudProcesoService;

public class MedicionesController {

    private static MedicionesController instance;
    private String usuarioActualId; 

    public List<Double> obtenerMontosPorUbicacionYRango(String tipoUbicacion, String valorUbicacion, String tipo, Date fechaInicio, Date fechaFin) {
        List<Double> montos = new ArrayList<>();
        if ("zona".equals(tipoUbicacion)) {
            List<modelo.MedicionesPorZona> mediciones = obtenerMedicionesPorZonaYRango(valorUbicacion, tipo, fechaInicio, fechaFin);
            for (modelo.MedicionesPorZona m : mediciones) {
                montos.add(m.monto);
            }
        } else if ("ciudad".equals(tipoUbicacion)) {
            List<modelo.MedicionesPorCiudad> mediciones = obtenerMedicionesPorCiudadYRango(valorUbicacion, tipo, fechaInicio, fechaFin);
            for (modelo.MedicionesPorCiudad m : mediciones) {
                montos.add(m.monto);
            }
        } else if ("pais".equals(tipoUbicacion)) {
            List<modelo.MedicionesPorPais> mediciones = obtenerMedicionesPorPaisYRango(valorUbicacion, tipo, fechaInicio, fechaFin);
            for (modelo.MedicionesPorPais m : mediciones) {
                montos.add(m.monto);
            }
        }
        return montos;
    }

    public List<Double> obtenerValoresPorUbicacionYRango(String tipoUbicacion, String valorUbicacion, String tipo, Date fechaInicio, Date fechaFin) {
        List<Double> valores = new ArrayList<>();
        if ("zona".equals(tipoUbicacion)) {
            List<modelo.MedicionesPorZona> mediciones = obtenerMedicionesPorZonaYRango(valorUbicacion, tipo, fechaInicio, fechaFin);
            for (modelo.MedicionesPorZona m : mediciones) {
                valores.add(m.valor);
            }
        } else if ("ciudad".equals(tipoUbicacion)) {
            List<modelo.MedicionesPorCiudad> mediciones = obtenerMedicionesPorCiudadYRango(valorUbicacion, tipo, fechaInicio, fechaFin);
            for (modelo.MedicionesPorCiudad m : mediciones) {
                valores.add(m.valor);
            }
        } else if ("pais".equals(tipoUbicacion)) {
            List<modelo.MedicionesPorPais> mediciones = obtenerMedicionesPorPaisYRango(valorUbicacion, tipo, fechaInicio, fechaFin);
            for (modelo.MedicionesPorPais m : mediciones) {
                valores.add(m.valor);
            }
        }
        return valores;
    }

    public static MedicionesController getInstance() {
        if (instance == null) {
            instance = new MedicionesController();
        }
        return instance;
    }

    public void setUsuarioActual(String usuarioId) {
        this.usuarioActualId = usuarioId;
    }

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

    
    public List<modelo.MedicionesPorZona> obtenerMedicionesPorZonaYRango(String zona, String tipo, Date fechaInicio, Date fechaFin) {
        long inicio = System.currentTimeMillis();
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("zona", zona);
        parametros.put("tipo", tipo);
        parametros.put("fechaInicio", fechaInicio);
        parametros.put("fechaFin", fechaFin);

        String proceso = getProcesoNombre("zona", tipo, "maxmin", fechaInicio, fechaFin);
        try {
            List<modelo.MedicionesPorZona> resultado = services.MedicionesService.getInstance().obtenerMedicionesPorZonaYRango(zona, tipo, fechaInicio, fechaFin);
            registrarSolicitud(proceso, parametros, "ÉXITO - " + resultado.size() + " registros", inicio);
            return resultado;
        } catch (Exception e) {
            registrarSolicitud(proceso, parametros, "ERROR: " + e.getMessage(), inicio);
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

        String proceso = getProcesoNombre("pais", tipo, "maxmin", fechaInicio, fechaFin);
        try {
            List<modelo.MedicionesPorPais> resultado = services.MedicionesService.getInstance().obtenerMedicionesPorPaisYRango(pais, tipo, fechaInicio, fechaFin);
            registrarSolicitud(proceso, parametros, "ÉXITO - " + resultado.size() + " registros", inicio);
            return resultado;
        } catch (Exception e) {
            registrarSolicitud(proceso, parametros, "ERROR: " + e.getMessage(), inicio);
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

        String proceso = getProcesoNombre("ciudad", tipo, "maxmin", fechaInicio, fechaFin);
        try {
            List<modelo.MedicionesPorCiudad> resultado = services.MedicionesService.getInstance().obtenerMedicionesPorCiudadYRango(ciudad, tipo, fechaInicio, fechaFin);
            registrarSolicitud(proceso, parametros, "ÉXITO - " + resultado.size() + " registros", inicio);
            return resultado;
        } catch (Exception e) {
            registrarSolicitud(proceso, parametros, "ERROR: " + e.getMessage(), inicio);
            throw e;
        }
    }

    
    private String getProcesoNombre(String ubicacion, String tipo, String operacion, Date fechaInicio, Date fechaFin) {
        
        
        
        
        String periodo = "ANUAL";
        if (fechaInicio != null && fechaFin != null) {
            java.util.Calendar calIni = java.util.Calendar.getInstance();
            java.util.Calendar calFin = java.util.Calendar.getInstance();
            calIni.setTime(fechaInicio);
            calFin.setTime(fechaFin);
            if (calIni.get(java.util.Calendar.YEAR) == calFin.get(java.util.Calendar.YEAR)
                    && calIni.get(java.util.Calendar.MONTH) == calFin.get(java.util.Calendar.MONTH)) {
                periodo = "MENSUAL";
            }
        }
        String base = "";
        if ("maxmin".equals(operacion)) {
            base = "INFORME_MAXMIN_";
        } else if ("promedio".equals(operacion)) {
            base = "INFORME_PROMEDIO_";
        }
        String ubic = ubicacion.toUpperCase();
        return base + ubic + "_" + periodo;
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
        parametros.put("monto", sensor.getMontoPorMedicion());

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
        Sensor sensor = MedicionesService.getInstance().getSensorRepo().findById(sensorId);
        if (sensor != null) {
            parametros.put("monto", sensor.getMontoPorMedicion());
        }

        try {
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

    private void registrarSolicitud(String nombreProceso, Map<String, Object> parametros, String resultado, long inicio) {
        if (usuarioActualId != null && !usuarioActualId.isEmpty()) {
            try {
                long tiempoEjecucion = System.currentTimeMillis() - inicio;
                SolicitudProcesoService.getInstance().registrarSolicitudCompletada(
                        usuarioActualId, nombreProceso, parametros, resultado, tiempoEjecucion
                );
            } catch (Exception e) {
                
                System.err.println("Error al registrar solicitud de proceso: " + e.getMessage());
            }
        }
    }
}
