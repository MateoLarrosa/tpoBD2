package menus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import controlador.MedicionesController;
import controlador.SolicitudProcesoController;
import services.MedicionesService;

public class MenuSolicitarProceso implements Menu {

    private final MedicionesController medicionesController = MedicionesController.getInstance();

    private final Scanner scanner;
    private String usuarioId;
    private final SolicitudProcesoController solicitudController = SolicitudProcesoController.getInstance();

    public MenuSolicitarProceso(Scanner scanner) {
        this.scanner = scanner;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public void show() {
        boolean salir = false;
        while (!salir) {
            ConsolaUtils.limpiarConsola();
            System.out.println("\n--- Solicitar Proceso de Mediciones ---");
            System.out.println("1. Solicitar medición mínima");
            System.out.println("2. Solicitar medición máxima");
            System.out.println("3. Solicitar medición promedio");
            System.out.println("4. Volver");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();
            ConsolaUtils.limpiarConsola();
            switch (opcion) {
                case "1" ->
                    solicitarProceso("min");
                case "2" ->
                    solicitarProceso("max");
                case "3" ->
                    solicitarProceso("promedio");
                case "4" ->
                    salir = true;
                default -> {
                    System.out.println("Opción inválida.");
                    esperarConfirmacion();
                }
            }
        }
    }

    private void solicitarProceso(String tipo) {

        System.out.println("Seleccione tipo de ubicación:");
        System.out.println("1. Zona");
        System.out.println("2. Ciudad");
        System.out.println("3. País");
        System.out.print("Opción: ");
        String op = scanner.nextLine();
        String granularidad;
        if ("1".equals(op)) {
            granularidad = "ZONA";
        } else if ("2".equals(op)) {
            granularidad = "CIUDAD";
        } else if ("3".equals(op)) {
            granularidad = "PAIS";
        } else {
            System.out.println("Opción inválida.");
            esperarConfirmacion();
            return;
        }

        System.out.println("Seleccione tipo de medición:");
        System.out.println("1. TEMPERATURA");
        System.out.println("2. HUMEDAD");
        System.out.print("Opción: ");
        String opTipo = scanner.nextLine();
        String tipoMedicion;
        if ("1".equals(opTipo)) {
            tipoMedicion = "TEMPERATURA";
        } else if ("2".equals(opTipo)) {
            tipoMedicion = "HUMEDAD";
        } else {
            System.out.println("Opción inválida.");
            esperarConfirmacion();
            return;
        }

        List<String> opciones = new ArrayList<>();
        if (granularidad.equals("ZONA")) {
            List<modelo.MedicionesPorZona> zonas = medicionesController.obtenerTodasZonas();
            Set<String> zonasSet = new HashSet<>();
            for (modelo.MedicionesPorZona m : zonas) {
                zonasSet.add(m.zona);
            }
            opciones.addAll(zonasSet);
        } else if (granularidad.equals("CIUDAD")) {
            List<modelo.MedicionesPorCiudad> ciudades = medicionesController.obtenerTodasCiudades();
            Set<String> ciudadesSet = new HashSet<>();
            for (modelo.MedicionesPorCiudad m : ciudades) {
                ciudadesSet.add(m.ciudad);
            }
            opciones.addAll(ciudadesSet);
        } else if (granularidad.equals("PAIS")) {
            List<modelo.MedicionesPorPais> paises = medicionesController.obtenerTodosPaises();
            Set<String> paisesSet = new HashSet<>();
            for (modelo.MedicionesPorPais m : paises) {
                paisesSet.add(m.pais);
            }
            opciones.addAll(paisesSet);
        }
        if (opciones.isEmpty()) {
            System.out.println("No hay datos disponibles para la ubicación seleccionada.");
            esperarConfirmacion();
            return;
        }
        System.out.println("Opciones disponibles:");
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println((i + 1) + ". " + opciones.get(i));
        }
        System.out.print("Seleccione una opción: ");
        int idx = ConsolaUtils.leerEnteroEnRango(scanner, 1, opciones.size()) - 1;
        String nombre = opciones.get(idx);

        System.out.print("Ingrese fecha de inicio (dd/MM/yyyy): ");
        String fechaInicio = scanner.nextLine();
        System.out.print("Ingrese fecha de fin (dd/MM/yyyy): ");
        String fechaFin = scanner.nextLine();

        String nombreProceso = obtenerNombreProcesoDinamico(tipo, granularidad, tipoMedicion);
        if (nombreProceso == null) {
            System.out.println("No se encontró un proceso para esa combinación (" + tipo + ", " + granularidad + ", " + tipoMedicion + ").");
            esperarConfirmacion();
            return;
        }

        Map<String, Object> parametros = new HashMap<>();
        parametros.put(granularidad.toLowerCase(), nombre);
        parametros.put("fechaInicio", fechaInicio);
        parametros.put("fechaFin", fechaFin);
        parametros.put("tipoMedicion", tipoMedicion);

        try {
            String resultado = obtenerResultadoProceso(tipo, granularidad, nombre, parametros);
            long tiempoEjecucionMs = 0L;
            // Si el resultado es numérico, agregarlo como valorMedicion y calcular el monto
            if (resultado != null && (resultado.startsWith("Resultado min") || resultado.startsWith("Resultado max") || resultado.startsWith("Resultado promedio") || resultado.startsWith("Resultado promedio para") || resultado.startsWith("Resultado min para") || resultado.startsWith("Resultado max para"))) {
                String[] partes = resultado.split(":");
                if (partes.length == 2) {
                    try {
                        double valor = Double.parseDouble(partes[1].replaceAll("[^0-9.-]", "").trim());
                        parametros.put("valorMedicion", valor);
                    } catch (NumberFormatException e) {
                        // No es un número, no guardar
                    }
                }
            }
            // Calcular monto estimado y agregarlo a los parámetros
            MedicionesService medicionesService = MedicionesService.getInstance();
            double monto = 0.0;
            String zona = (String) parametros.get("zona");
            String ciudad = (String) parametros.get("ciudad");
            String pais = (String) parametros.get("pais");
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            java.util.Date fechaInicioDate = null;
            java.util.Date fechaFinDate = null;
            try {
                fechaInicioDate = sdf.parse(fechaInicio);
                fechaFinDate = sdf.parse(fechaFin);
            } catch (Exception e) {
                // ignorar error de parseo
            }
            if (fechaInicioDate != null && fechaFinDate != null) {
                if (zona != null) {
                    monto = medicionesService.obtenerMedicionesPorZonaYRango(zona, tipoMedicion, fechaInicioDate, fechaFinDate)
                            .stream().mapToDouble(m -> m.monto).sum();
                } else if (ciudad != null) {
                    monto = medicionesService.obtenerMedicionesPorCiudadYRango(ciudad, tipoMedicion, fechaInicioDate, fechaFinDate)
                            .stream().mapToDouble(m -> m.monto).sum();
                } else if (pais != null) {
                    monto = medicionesService.obtenerMedicionesPorPaisYRango(pais, tipoMedicion, fechaInicioDate, fechaFinDate)
                            .stream().mapToDouble(m -> m.monto).sum();
                }
            }
            parametros.put("monto", monto);
            solicitudController.registrarSolicitudCompletada(usuarioId, nombreProceso, parametros, resultado, tiempoEjecucionMs);
            System.out.println("Solicitud registrada y completada para proceso: " + nombreProceso);
        } catch (Exception e) {
            System.out.println("Error al registrar la solicitud: " + e.getMessage());
        }

    }

    private String obtenerResultadoProceso(String tipo, String granularidad, String nombre, Map<String, Object> parametros) {
        MedicionesService medicionesService = MedicionesService.getInstance();
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            Date fechaInicio = sdf.parse((String) parametros.get("fechaInicio"));
            Date fechaFin = sdf.parse((String) parametros.get("fechaFin"));
            String tipoMedicion = (String) parametros.get("tipoMedicion");
            Double resultado = null;
            switch (tipo) {
                case "min":
                    if (granularidad.equals("ZONA")) {
                        resultado = medicionesService.obtenerMinimoPorZona(nombre, tipoMedicion, fechaInicio, fechaFin);
                    } else if (granularidad.equals("CIUDAD")) {
                        resultado = medicionesService.obtenerMinimoPorCiudad(nombre, tipoMedicion, fechaInicio, fechaFin);
                    } else if (granularidad.equals("PAIS")) {
                        resultado = medicionesService.obtenerMinimoPorPais(nombre, tipoMedicion, fechaInicio, fechaFin);
                    }
                    break;
                case "max":
                    if (granularidad.equals("ZONA")) {
                        resultado = medicionesService.obtenerMaximoPorZona(nombre, tipoMedicion, fechaInicio, fechaFin);
                    } else if (granularidad.equals("CIUDAD")) {
                        resultado = medicionesService.obtenerMaximoPorCiudad(nombre, tipoMedicion, fechaInicio, fechaFin);
                    } else if (granularidad.equals("PAIS")) {
                        resultado = medicionesService.obtenerMaximoPorPais(nombre, tipoMedicion, fechaInicio, fechaFin);
                    }
                    break;
                case "promedio":
                    if (granularidad.equals("ZONA")) {
                        resultado = medicionesService.obtenerPromedioPorZona(nombre, tipoMedicion, fechaInicio, fechaFin);
                    } else if (granularidad.equals("CIUDAD")) {
                        resultado = medicionesService.obtenerPromedioPorCiudad(nombre, tipoMedicion, fechaInicio, fechaFin);
                    } else if (granularidad.equals("PAIS")) {
                        resultado = medicionesService.obtenerPromedioPorPais(nombre, tipoMedicion, fechaInicio, fechaFin);
                    }
                    break;
            }
            if (resultado == null || resultado.isNaN()) {
                return "No hay datos para el rango seleccionado.";
            }
            return "Resultado " + tipo + " para " + granularidad + " '" + nombre + "' (" + tipoMedicion + "): " + resultado;
        } catch (Exception e) {
            return "Error al calcular resultado: " + e.getMessage();
        }
    }

    private String obtenerNombreProcesoDinamico(String tipo, String granularidad) {
        return obtenerNombreProcesoDinamico(tipo, granularidad, null);
    }

    private String obtenerNombreProcesoDinamico(String tipo, String granularidad, String tipoMedicion) {
        tipo = tipo.toLowerCase();
        granularidad = granularidad.toUpperCase();
        String sufijo = (tipoMedicion != null && !tipoMedicion.isEmpty()) ? ("_" + tipoMedicion.toUpperCase()) : "";
        switch (tipo) {
            case "min":
                return "INFORME_MIN_" + granularidad + sufijo;
            case "max":
                return "INFORME_MAX_" + granularidad + sufijo;
            case "promedio":
                return "INFORME_PROMEDIO_" + granularidad + sufijo;
            default:
                return null;
        }
    }

    private void esperarConfirmacion() {
        System.out.print("Presione Enter para continuar...");
        scanner.nextLine();
    }
}
