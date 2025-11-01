package menus;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

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
            System.out.println("5. Volver");
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
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
                    esperarConfirmacion();
            }
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
}
