package menus;

import java.util.List;
import java.util.Scanner;

import controlador.MedicionesController;
import controlador.SensorController;
import modelo.Sensor;

public class MenuMediciones implements Menu {

    private final MedicionesController medicionesController;
    private final SensorController sensorController;
    private final Scanner scanner;

    public MenuMediciones(MedicionesController medicionesController, SensorController sensorController, Scanner scanner) {
        this.medicionesController = medicionesController;
        this.sensorController = sensorController;
        this.scanner = scanner;
    }

    @Override
    public void show() {
        System.out.println("--- Crear medición para un sensor ---");
        List<Sensor> sensores = sensorController.findAll();
        if (sensores.isEmpty()) {
            System.out.println("No hay sensores disponibles.");
            return;
        }
        for (int i = 0; i < sensores.size(); i++) {
            Sensor s = sensores.get(i);
            System.out.printf("%d. %s (ID: %s, Tipo: %s, Ubicación: %s/%s/%s)\n", i + 1, s.getNombre(), s.getId(), s.getTipo(), s.getZona(), s.getCiudad(), s.getPais());
        }
        System.out.print("Seleccione el número de sensor: ");
        int idx = ConsolaUtils.leerEnteroEnRango(scanner, 1, sensores.size()) - 1;
        Sensor sensorSeleccionado = sensores.get(idx);

        String tipo = sensorSeleccionado.getTipo() != null ? sensorSeleccionado.getTipo().toString() : "";
        System.out.print("Año: ");
        int anio = ConsolaUtils.leerEntero(scanner);
        System.out.print("Mes: ");
        int mes = ConsolaUtils.leerEntero(scanner);
        System.out.print("Fecha (YYYY-MM-DD): ");
        String fechaStr = scanner.nextLine();
        // Validar formato simple, pero pasar como String
        try {
            java.sql.Date.valueOf(fechaStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Formato de fecha inválido. Use YYYY-MM-DD.");
            return;
        }
        System.out.print("Valor: ");
        double valor = ConsolaUtils.leerDouble(scanner);

        try {
            medicionesController.agregarMedicion(sensorSeleccionado, tipo, anio, mes, fechaStr, valor);
            System.out.println("Medición registrada correctamente.");
        } catch (Exception e) {
            System.out.println("Error al registrar la medición: " + e.getMessage());
        }
    }
}
