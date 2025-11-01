package menus;

import java.util.List;
import java.util.Scanner;

import controlador.SensorController;
import modelo.MedicionesPorCiudad;
import modelo.MedicionesPorPais;
import modelo.MedicionesPorZona;
import repositories.MedicionesCassandraRepository;

public class MenuVerMediciones implements Menu {

    private final SensorController sensorController;
    private final Scanner scanner;

    public MenuVerMediciones(SensorController sensorController, Scanner scanner) {
        this.sensorController = sensorController;
        this.scanner = scanner;
    }

    @Override
    public void show() {
        boolean salir = false;
        while (!salir) {
            ConsolaUtils.limpiarConsola();
            System.out.println("\n--- Ver Mediciones ---");
            System.out.println("1. Ver mediciones por zona");
            System.out.println("2. Ver mediciones por país");
            System.out.println("3. Ver mediciones por ciudad");
            System.out.println("4. Volver");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();
            ConsolaUtils.limpiarConsola();
            switch (opcion) {
                case "1":
                    verMedicionesZona();
                    break;
                case "2":
                    verMedicionesPais();
                    break;
                case "3":
                    verMedicionesCiudad();
                    break;
                case "4":
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
                    esperarConfirmacion();
            }
        }
    }

    private void verMedicionesZona() {
        List<MedicionesPorZona> mediciones = MedicionesCassandraRepository.getInstance().findAllZona();
        if (mediciones.isEmpty()) {
            System.out.println("No hay mediciones por zona registradas.");
            esperarConfirmacion();
            return;
        }
        // Obtener zonas únicas
        java.util.Set<String> zonas = new java.util.HashSet<>();
        for (MedicionesPorZona m : mediciones) {
            zonas.add(m.zona);
        }
        if (zonas.isEmpty()) {
            System.out.println("No hay zonas disponibles.");
            esperarConfirmacion();
            return;
        }
        System.out.println("Zonas disponibles:");
        java.util.List<String> zonasList = new java.util.ArrayList<>(zonas);
        for (int i = 0; i < zonasList.size(); i++) {
            System.out.println((i + 1) + ". " + zonasList.get(i));
        }
        System.out.print("Seleccione una zona: ");
        int seleccion = -1;
        try {
            seleccion = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Selección inválida.");
            esperarConfirmacion();
            return;
        }
        if (seleccion < 1 || seleccion > zonasList.size()) {
            System.out.println("Selección fuera de rango.");
            esperarConfirmacion();
            return;
        }
        String zonaSeleccionada = zonasList.get(seleccion - 1);
        System.out.println("--- Mediciones para zona: " + zonaSeleccionada + " ---");
        for (MedicionesPorZona m : mediciones) {
            if (m.zona.equals(zonaSeleccionada)) {
                System.out.println(m);
            }
        }
        esperarConfirmacion();
    }

    private void verMedicionesPais() {
        List<MedicionesPorPais> mediciones = MedicionesCassandraRepository.getInstance().findAllPais();
        if (mediciones.isEmpty()) {
            System.out.println("No hay mediciones por país registradas.");
            esperarConfirmacion();
            return;
        }
        // Obtener países únicos
        java.util.Set<String> paises = new java.util.HashSet<>();
        for (MedicionesPorPais m : mediciones) {
            paises.add(m.pais);
        }
        if (paises.isEmpty()) {
            System.out.println("No hay países disponibles.");
            esperarConfirmacion();
            return;
        }
        System.out.println("Países disponibles:");
        java.util.List<String> paisesList = new java.util.ArrayList<>(paises);
        for (int i = 0; i < paisesList.size(); i++) {
            System.out.println((i + 1) + ". " + paisesList.get(i));
        }
        System.out.print("Seleccione un país: ");
        int seleccion = -1;
        try {
            seleccion = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Selección inválida.");
            esperarConfirmacion();
            return;
        }
        if (seleccion < 1 || seleccion > paisesList.size()) {
            System.out.println("Selección fuera de rango.");
            esperarConfirmacion();
            return;
        }
        String paisSeleccionado = paisesList.get(seleccion - 1);
        System.out.println("--- Mediciones para país: " + paisSeleccionado + " ---");
        for (MedicionesPorPais m : mediciones) {
            if (m.pais.equals(paisSeleccionado)) {
                System.out.println(m);
            }
        }
        esperarConfirmacion();
    }

    private void verMedicionesCiudad() {
        List<MedicionesPorCiudad> mediciones = MedicionesCassandraRepository.getInstance().findAllCiudad();
        if (mediciones.isEmpty()) {
            System.out.println("No hay mediciones por ciudad registradas.");
            esperarConfirmacion();
            return;
        }
        // Obtener ciudades únicas
        java.util.Set<String> ciudades = new java.util.HashSet<>();
        for (MedicionesPorCiudad m : mediciones) {
            ciudades.add(m.ciudad);
        }
        if (ciudades.isEmpty()) {
            System.out.println("No hay ciudades disponibles.");
            esperarConfirmacion();
            return;
        }
        System.out.println("Ciudades disponibles:");
        java.util.List<String> ciudadesList = new java.util.ArrayList<>(ciudades);
        for (int i = 0; i < ciudadesList.size(); i++) {
            System.out.println((i + 1) + ". " + ciudadesList.get(i));
        }
        System.out.print("Seleccione una ciudad: ");
        int seleccion = -1;
        try {
            seleccion = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Selección inválida.");
            esperarConfirmacion();
            return;
        }
        if (seleccion < 1 || seleccion > ciudadesList.size()) {
            System.out.println("Selección fuera de rango.");
            esperarConfirmacion();
            return;
        }
        String ciudadSeleccionada = ciudadesList.get(seleccion - 1);
        System.out.println("--- Mediciones para ciudad: " + ciudadSeleccionada + " ---");
        for (MedicionesPorCiudad m : mediciones) {
            if (m.ciudad.equals(ciudadSeleccionada)) {
                System.out.println(m);
            }
        }
        esperarConfirmacion();
    }

    private void esperarConfirmacion() {
        System.out.print("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }
}
