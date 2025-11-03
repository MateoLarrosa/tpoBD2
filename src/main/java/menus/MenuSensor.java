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

    // --- NUEVAS FUNCIONALIDADES ---
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
        List<Double> valores = obtenerValoresFiltrados(tipoUbicacion, valorUbicacion, tipo, fechas[0], fechas[1]);
        if (valores.isEmpty()) {
            System.out.println("No hay mediciones para los filtros seleccionados.");
        } else {
            double min = valores.stream().min(Double::compare).get();
            double max = valores.stream().max(Double::compare).get();
            System.out.println("Valor mínimo: " + min);
            System.out.println("Valor máximo: " + max);
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
        List<Double> valores = obtenerValoresFiltrados(tipoUbicacion, valorUbicacion, tipo, fechas[0], fechas[1]);
        if (valores.isEmpty()) {
            System.out.println("No hay mediciones para los filtros seleccionados.");
        } else {
            double promedio = valores.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
            System.out.println("Promedio: " + promedio);
        }
        esperarConfirmacion();
    }

    // --- Selección específica de zona, ciudad o país ---
    private String seleccionarValorUbicacion(String tipoUbicacion) {
        List<String> opciones = new ArrayList<>();
        controlador.MedicionesController medicionesController = controlador.MedicionesController.getInstance();
        switch (tipoUbicacion) {
            case "zona": {
                List<modelo.MedicionesPorZona> zonas = medicionesController.obtenerTodasZonas();
                Set<String> zonasSet = new HashSet<>();
                for (modelo.MedicionesPorZona m : zonas) {
                    zonasSet.add(m.zona);
                }
                opciones.addAll(zonasSet);
                break;
            }
            case "ciudad": {
                List<modelo.MedicionesPorCiudad> ciudades = medicionesController.obtenerTodasCiudades();
                Set<String> ciudadesSet = new HashSet<>();
                for (modelo.MedicionesPorCiudad m : ciudades) {
                    ciudadesSet.add(m.ciudad);
                }
                opciones.addAll(ciudadesSet);
                break;
            }
            case "pais": {
                List<modelo.MedicionesPorPais> paises = medicionesController.obtenerTodosPaises();
                Set<String> paisesSet = new HashSet<>();
                for (modelo.MedicionesPorPais m : paises) {
                    paisesSet.add(m.pais);
                }
                opciones.addAll(paisesSet);
                break;
            }
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

    // Métodos auxiliares para selección
    private String seleccionarUbicacion() {
        System.out.println("Seleccione tipo de ubicación:");
        System.out.println("1. Zona");
        System.out.println("2. Ciudad");
        System.out.println("3. País");
        System.out.print("Opción: ");
        String op = scanner.nextLine();
        switch (op) {
            case "1":
                return "zona";
            case "2":
                return "ciudad";
            case "3":
                return "pais";
            default:
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
        switch (op) {
            case "1":
                return "TEMPERATURA";
            case "2":
                return "HUMEDAD";
            default:
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
        TipoSensor tipo;
        switch (tipoStr) {
            case "TEMPERATURA":
                tipo = TipoSensor.TEMPERATURA;
                break;
            case "HUMEDAD":
                tipo = TipoSensor.HUMEDAD;
                break;
            default:
                System.out.println("Tipo inválido, se usará TEMPERATURA por defecto.");
                tipo = TipoSensor.TEMPERATURA;
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
        EstadoSensor estado;
        switch (estadoStr) {
            case "ACTIVO":
                estado = EstadoSensor.ACTIVO;
                break;
            case "INACTIVO":
                estado = EstadoSensor.INACTIVO;
                break;
            case "FALLA":
                estado = EstadoSensor.FALLA;
                break;
            default:
                System.out.println("Estado inválido, se usará ACTIVO por defecto.");
                estado = EstadoSensor.ACTIVO;
        }
        Date fechaInicio = new Date();
        Sensor sensor = new Sensor(null, nombre, tipo, latitud, longitud, ciudad, pais, zona, estado, fechaInicio);
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
            System.out.println("ID: " + s.getId() + ", Nombre: " + s.getNombre() + ", Tipo: " + s.getTipo() + ", Estado: " + s.getEstado() + ", Ciudad: " + s.getCiudad() + ", País: " + s.getPais() + ", Zona: " + s.getZona());
        }
        esperarConfirmacion();
    }

    private void esperarConfirmacion() {
        System.out.print("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }

    // --- Utilidad para filtrar valores según selección ---
    private List<Double> obtenerValoresFiltrados(String tipoUbicacion, String valorUbicacion, String tipo, Date fechaInicio, Date fechaFin) {
        List<Double> valores = new ArrayList<>();
        controlador.MedicionesController medicionesController = controlador.MedicionesController.getInstance();
        switch (tipoUbicacion) {
            case "zona": {
                List<modelo.MedicionesPorZona> mediciones = medicionesController.obtenerMedicionesPorZonaYRango(valorUbicacion, tipo, fechaInicio, fechaFin);
                for (modelo.MedicionesPorZona m : mediciones) {
                    valores.add(m.valor);
                }
                break;
            }
            case "ciudad": {
                List<modelo.MedicionesPorCiudad> mediciones = medicionesController.obtenerMedicionesPorCiudadYRango(valorUbicacion, tipo, fechaInicio, fechaFin);
                for (modelo.MedicionesPorCiudad m : mediciones) {
                    valores.add(m.valor);
                }
                break;
            }
            case "pais": {
                List<modelo.MedicionesPorPais> mediciones = medicionesController.obtenerMedicionesPorPaisYRango(valorUbicacion, tipo, fechaInicio, fechaFin);
                for (modelo.MedicionesPorPais m : mediciones) {
                    valores.add(m.valor);
                }
                break;
            }
        }
        return valores;
    }
}
