package menus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import controlador.SensorController;
import modelo.EstadoSensor;
import modelo.Sensor;
import modelo.TipoSensor;

public class MenuSensor implements Menu {

    
    private void menuMinMaxMediciones() {
        System.out.println("--- Min/Max de Mediciones ---");
        String tipoUbicacion = seleccionarUbicacion();
        if (tipoUbicacion == null) {
            return;
        }
        String valorUbicacion = seleccionarValorUbicacion(tipoUbicacion);
        if (valorUbicacion == null) {
            return;
        }
        String tipo = seleccionarTipo();
        if (tipo == null) {
            return;
        }
        Date[] fechas = ConsolaUtils.pedirRangoFechas(scanner);
        if (fechas == null) {
            return;
        }
        
        List<Double> montos = controlador.MedicionesController.getInstance()
                .obtenerMontosPorUbicacionYRango(tipoUbicacion, valorUbicacion, tipo, fechas[0], fechas[1]
                );
        if (montos.isEmpty()) {
            System.out.println("No hay mediciones para los filtros seleccionados.");
        } else {
            double min = montos.stream().min(Double::compare).get();
            double max = montos.stream().max(Double::compare).get();
            System.out.println("Monto mínimo: " + min);
            System.out.println("Monto máximo: " + max);
        }
        esperarConfirmacion();
    }

    
    private void menuPromedioMediciones() {
        System.out.println("--- Promedio de Mediciones ---");
        String tipoUbicacion = seleccionarUbicacion();
        if (tipoUbicacion == null) {
            return;
        }
        String valorUbicacion = seleccionarValorUbicacion(tipoUbicacion);
        if (valorUbicacion == null) {
            return;
        }
        String tipo = seleccionarTipo();
        if (tipo == null) {
            return;
        }
        Date[] fechas = ConsolaUtils.pedirRangoFechas(scanner);
        if (fechas == null) {
            return;
        }
        
        List<Double> montos = controlador.MedicionesController.getInstance()
                .obtenerMontosPorUbicacionYRango(tipoUbicacion, valorUbicacion, tipo, fechas[0], fechas[1]);
        if (montos.isEmpty()) {
            System.out.println("No hay mediciones para los filtros seleccionados.");
        } else {
            double promedio = montos.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
            System.out.println("Promedio de montos: " + promedio);
        }
        esperarConfirmacion();
    }

    private final SensorController sensorController;
    private final Scanner scanner;

    public MenuSensor(SensorController sensorController, Scanner scanner) {
        this.sensorController = sensorController;
        this.scanner = scanner;
    }

    @Override
    public void show() {
        boolean salir = false;
        while (!salir) {
            ConsolaUtils.limpiarConsola();
            System.out.println("\n--- Menú de Sensores ---");
            System.out.println("1. Crear sensor");
            System.out.println("2. Listar sensores");
            System.out.println("3. Crear medición para sensor");
            System.out.println("4. Ver mediciones");
            System.out.println("5. Min/Max de mediciones");
            System.out.println("6. Promedio de mediciones");
            System.out.println("7. Volver");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();
            ConsolaUtils.limpiarConsola();
            switch (opcion) {
                case "1":
                    crearSensor();
                    break;
                case "2":
                    listarSensores();
                    break;
                case "3":
                    menuMediciones();
                    break;
                case "4":
                    menuVerMediciones();
                    break;
                case "5":
                    menuMinMaxMediciones();
                    break;
                case "6":
                    menuPromedioMediciones();
                    break;
                case "7":
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
                    esperarConfirmacion();
            }
        }
    }

    private String seleccionarValorUbicacion(String tipoUbicacion) {
        List<String> opciones = new ArrayList<>();
        controlador.MedicionesController medicionesController = controlador.MedicionesController.getInstance();
        if ("zona".equals(tipoUbicacion)) {
            List<modelo.MedicionesPorZona> zonas = medicionesController.obtenerTodasZonas();
            Set<String> zonasSet = new HashSet<>();
            for (modelo.MedicionesPorZona m : zonas) {
                zonasSet.add(m.zona);
            }
            opciones.addAll(zonasSet);
        } else if ("ciudad".equals(tipoUbicacion)) {
            List<modelo.MedicionesPorCiudad> ciudades = medicionesController.obtenerTodasCiudades();
            Set<String> ciudadesSet = new HashSet<>();
            for (modelo.MedicionesPorCiudad m : ciudades) {
                ciudadesSet.add(m.ciudad);
            }
            opciones.addAll(ciudadesSet);
        } else if ("pais".equals(tipoUbicacion)) {
            List<modelo.MedicionesPorPais> paises = medicionesController.obtenerTodosPaises();
            Set<String> paisesSet = new HashSet<>();
            for (modelo.MedicionesPorPais m : paises) {
                paisesSet.add(m.pais);
            }
            opciones.addAll(paisesSet);
        }
        if (opciones.isEmpty()) {
            System.out.println("No hay datos disponibles para la ubicación seleccionada.");
            return null;
        }
        System.out.println("Opciones disponibles:");
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println((i + 1) + ". " + opciones.get(i));
        }
        System.out.print("Seleccione una opción: ");
        int idx = ConsolaUtils.leerEnteroEnRango(scanner, 1, opciones.size()) - 1;
        return opciones.get(idx);
    }

    
    private String seleccionarUbicacion() {
        System.out.println("Seleccione tipo de ubicación:");
        System.out.println("1. Zona");
        System.out.println("2. Ciudad");
        System.out.println("3. País");
        System.out.print("Opción: ");
        String op = scanner.nextLine();
        if ("1".equals(op)) {
            return "zona";
        } else if ("2".equals(op)) {
            return "ciudad";
        } else if ("3".equals(op)) {
            return "pais";
        } else {
            System.out.println("Opción inválida.");
            return null;
        }
    }

    private String seleccionarTipo() {
        System.out.println("Seleccione tipo de medición:");
        System.out.println("1. Temperatura");
        System.out.println("2. Humedad");
        System.out.print("Opción: ");
        String op = scanner.nextLine();
        if ("1".equals(op)) {
            return "TEMPERATURA";
        } else if ("2".equals(op)) {
            return "HUMEDAD";
        } else {
            System.out.println("Opción inválida.");
            return null;
        }
    }

    private void menuVerMediciones() {
        new MenuVerMediciones(sensorController, scanner).show();
    }

    private void menuMediciones() {
        new MenuMediciones(
                controlador.MedicionesController.getInstance(),
                sensorController,
                scanner
        ).show();
    }

    private void crearSensor() {
        System.out.print("Nombre o código: ");
        String nombre = scanner.nextLine();
        System.out.print("Tipo (temperatura/humedad): ");
        String tipoStr = scanner.nextLine().trim().toUpperCase();
        TipoSensor tipoSensor;
        switch (tipoStr) {
            case "TEMPERATURA":
                tipoSensor = TipoSensor.TEMPERATURA;
                break;
            case "HUMEDAD":
                tipoSensor = TipoSensor.HUMEDAD;
                break;
            default:
                System.out.println("Tipo inválido, se usará TEMPERATURA por defecto.");
                tipoSensor = TipoSensor.TEMPERATURA;
                break;
        }
        System.out.print("Latitud: ");
        double latitud = Double.parseDouble(scanner.nextLine());
        System.out.print("Longitud: ");
        double longitud = Double.parseDouble(scanner.nextLine());
        System.out.print("Ciudad: ");
        String ciudad = scanner.nextLine();
        System.out.print("País: ");
        String pais = scanner.nextLine();
        System.out.print("Zona: ");
        String zona = scanner.nextLine();
        System.out.print("Estado (activo/inactivo/falla): ");
        String estadoStr = scanner.nextLine().trim().toUpperCase();
        EstadoSensor estadoSensor;
        switch (estadoStr) {
            case "ACTIVO":
                estadoSensor = EstadoSensor.ACTIVO;
                break;
            case "INACTIVO":
                estadoSensor = EstadoSensor.INACTIVO;
                break;
            case "FALLA":
                estadoSensor = EstadoSensor.FALLA;
                break;
            default:
                System.out.println("Estado inválido, se usará ACTIVO por defecto.");
                estadoSensor = EstadoSensor.ACTIVO;
        }
        System.out.print("Monto por medición: ");
        double montoPorMedicion = Double.parseDouble(scanner.nextLine());
        Date fechaInicio = new Date();
        Sensor sensor = new Sensor(null, nombre, tipoSensor, latitud, longitud, ciudad, pais, zona, estadoSensor, fechaInicio, montoPorMedicion);
        sensorController.save(sensor);
        System.out.println("Sensor creado exitosamente.");
        esperarConfirmacion();
    }

    private void listarSensores() {
        List<Sensor> sensores = sensorController.findAll();
        if (sensores.isEmpty()) {
            System.out.println("No hay sensores registrados.");
            esperarConfirmacion();
            return;
        }
        System.out.println("\n--- Lista de Sensores ---");
        for (Sensor s : sensores) {
            System.out.println(s);
        }
        esperarConfirmacion();
    }

    private void esperarConfirmacion() {
        System.out.print("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }

}
