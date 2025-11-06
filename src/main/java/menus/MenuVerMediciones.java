package menus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import controlador.MedicionesController;
import controlador.SensorController;
import modelo.MedicionesPorCiudad;
import modelo.MedicionesPorPais;
import modelo.MedicionesPorZona;
import repositories.MedicionesCassandraRepository;

public class MenuVerMediciones implements Menu {

    private final SensorController sensorController;
    private final MedicionesController medicionesController;
    private final Scanner scanner;
    private String usuarioId;

    public MenuVerMediciones(SensorController sensorController, Scanner scanner) {
        this.sensorController = sensorController;
        this.medicionesController = MedicionesController.getInstance();
        this.scanner = scanner;
    }
    
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
        if (usuarioId != null) {
            medicionesController.setUsuarioActual(usuarioId);
        }
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
        
        List<MedicionesPorZona> todas = MedicionesCassandraRepository.getInstance().findAllZona();
        if (todas.isEmpty()) {
            System.out.println("No hay mediciones por zona registradas.");
            esperarConfirmacion();
            return;
        }
        Set<String> zonas = new HashSet<>();
        Set<String> tipos = new HashSet<>();
        for (MedicionesPorZona m : todas) {
            zonas.add(m.zona);
            tipos.add(m.tipo);
        }
        if (zonas.isEmpty()) {
            System.out.println("No hay zonas disponibles.");
            esperarConfirmacion();
            return;
        }
        System.out.println("Zonas disponibles:");
        List<String> zonasList = new ArrayList<>(zonas);
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

        
        String tipoSeleccionado = null;
        if (tipos.size() > 1) {
            System.out.println("Tipos disponibles:");
            List<String> tiposList = new ArrayList<>(tipos);
            for (int i = 0; i < tiposList.size(); i++) {
                System.out.println((i + 1) + ". " + tiposList.get(i));
            }
            System.out.print("Seleccione un tipo: ");
            int selTipo = -1;
            try {
                selTipo = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Selección inválida.");
                esperarConfirmacion();
                return;
            }
            if (selTipo < 1 || selTipo > tiposList.size()) {
                System.out.println("Selección fuera de rango.");
                esperarConfirmacion();
                return;
            }
            tipoSeleccionado = tiposList.get(selTipo - 1);
        } else if (!tipos.isEmpty()) {
            tipoSeleccionado = tipos.iterator().next();
        }

        
        System.out.print("Ingrese fecha de inicio (dd/MM/yyyy): ");
        String fechaInicioStr = scanner.nextLine();
        System.out.print("Ingrese fecha de fin (dd/MM/yyyy): ");
        String fechaFinStr = scanner.nextLine();
        Date fechaInicio = null;
        Date fechaFin = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            fechaInicio = sdf.parse(fechaInicioStr);
            fechaFin = sdf.parse(fechaFinStr);
        } catch (Exception e) {
            System.out.println("Formato de fecha inválido. Use dd/MM/yyyy");
            esperarConfirmacion();
            return;
        }

        
        List<MedicionesPorZona> mediciones = medicionesController.obtenerMedicionesPorZonaYRango(zonaSeleccionada, tipoSeleccionado, fechaInicio, fechaFin);
        System.out.println("--- Mediciones para zona: " + zonaSeleccionada + " en rango " + fechaInicioStr + " a " + fechaFinStr + " ---");
        if (mediciones.isEmpty()) {
            System.out.println("No hay mediciones en ese rango.");
        } else {
            for (MedicionesPorZona m : mediciones) {
                System.out.println(m);
            }
        }
        esperarConfirmacion();
    }

    private void verMedicionesPais() {
        
        List<MedicionesPorPais> todas = MedicionesCassandraRepository.getInstance().findAllPais();
        if (todas.isEmpty()) {
            System.out.println("No hay mediciones por país registradas.");
            esperarConfirmacion();
            return;
        }
        Set<String> paises = new HashSet<>();
        Set<String> tipos = new HashSet<>();
        for (MedicionesPorPais m : todas) {
            paises.add(m.pais);
            tipos.add(m.tipo);
        }
        if (paises.isEmpty()) {
            System.out.println("No hay países disponibles.");
            esperarConfirmacion();
            return;
        }
        System.out.println("Países disponibles:");
        List<String> paisesList = new ArrayList<>(paises);
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

        
        String tipoSeleccionado = null;
        if (tipos.size() > 1) {
            System.out.println("Tipos disponibles:");
            List<String> tiposList = new ArrayList<>(tipos);
            for (int i = 0; i < tiposList.size(); i++) {
                System.out.println((i + 1) + ". " + tiposList.get(i));
            }
            System.out.print("Seleccione un tipo: ");
            int selTipo = -1;
            try {
                selTipo = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Selección inválida.");
                esperarConfirmacion();
                return;
            }
            if (selTipo < 1 || selTipo > tiposList.size()) {
                System.out.println("Selección fuera de rango.");
                esperarConfirmacion();
                return;
            }
            tipoSeleccionado = tiposList.get(selTipo - 1);
        } else if (!tipos.isEmpty()) {
            tipoSeleccionado = tipos.iterator().next();
        }

        
        System.out.print("Ingrese fecha de inicio (dd/MM/yyyy): ");
        String fechaInicioStr = scanner.nextLine();
        System.out.print("Ingrese fecha de fin (dd/MM/yyyy): ");
        String fechaFinStr = scanner.nextLine();
        Date fechaInicio = null;
        Date fechaFin = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            fechaInicio = sdf.parse(fechaInicioStr);
            fechaFin = sdf.parse(fechaFinStr);
        } catch (Exception e) {
            System.out.println("Formato de fecha inválido. Use dd/MM/yyyy");
            esperarConfirmacion();
            return;
        }

        
        List<MedicionesPorPais> mediciones = medicionesController.obtenerMedicionesPorPaisYRango(paisSeleccionado, tipoSeleccionado, fechaInicio, fechaFin);
        System.out.println("--- Mediciones para país: " + paisSeleccionado + " en rango " + fechaInicioStr + " a " + fechaFinStr + " ---");
        if (mediciones.isEmpty()) {
            System.out.println("No hay mediciones en ese rango.");
        } else {
            for (MedicionesPorPais m : mediciones) {
                System.out.println(m);
            }
        }
        esperarConfirmacion();
    }

    private void verMedicionesCiudad() {
        
        List<MedicionesPorCiudad> todas = MedicionesCassandraRepository.getInstance().findAllCiudad();
        if (todas.isEmpty()) {
            System.out.println("No hay mediciones por ciudad registradas.");
            esperarConfirmacion();
            return;
        }
        Set<String> ciudades = new HashSet<>();
        Set<String> tipos = new HashSet<>();
        for (MedicionesPorCiudad m : todas) {
            ciudades.add(m.ciudad);
            tipos.add(m.tipo);
        }
        if (ciudades.isEmpty()) {
            System.out.println("No hay ciudades disponibles.");
            esperarConfirmacion();
            return;
        }
        System.out.println("Ciudades disponibles:");
        List<String> ciudadesList = new ArrayList<>(ciudades);
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

        
        String tipoSeleccionado = null;
        if (tipos.size() > 1) {
            System.out.println("Tipos disponibles:");
            List<String> tiposList = new ArrayList<>(tipos);
            for (int i = 0; i < tiposList.size(); i++) {
                System.out.println((i + 1) + ". " + tiposList.get(i));
            }
            System.out.print("Seleccione un tipo: ");
            int selTipo = -1;
            try {
                selTipo = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Selección inválida.");
                esperarConfirmacion();
                return;
            }
            if (selTipo < 1 || selTipo > tiposList.size()) {
                System.out.println("Selección fuera de rango.");
                esperarConfirmacion();
                return;
            }
            tipoSeleccionado = tiposList.get(selTipo - 1);
        } else if (!tipos.isEmpty()) {
            tipoSeleccionado = tipos.iterator().next();
        }

        
        System.out.print("Ingrese fecha de inicio (dd/MM/yyyy): ");
        String fechaInicioStr = scanner.nextLine();
        System.out.print("Ingrese fecha de fin (dd/MM/yyyy): ");
        String fechaFinStr = scanner.nextLine();
        Date fechaInicio = null;
        Date fechaFin = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            fechaInicio = sdf.parse(fechaInicioStr);
            fechaFin = sdf.parse(fechaFinStr);
        } catch (Exception e) {
            System.out.println("Formato de fecha inválido. Use dd/MM/yyyy");
            esperarConfirmacion();
            return;
        }

        
        List<MedicionesPorCiudad> mediciones = medicionesController.obtenerMedicionesPorCiudadYRango(ciudadSeleccionada, tipoSeleccionado, fechaInicio, fechaFin);
        System.out.println("--- Mediciones para ciudad: " + ciudadSeleccionada + " en rango " + fechaInicioStr + " a " + fechaFinStr + " ---");
        if (mediciones.isEmpty()) {
            System.out.println("No hay mediciones en ese rango.");
        } else {
            for (MedicionesPorCiudad m : mediciones) {
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
