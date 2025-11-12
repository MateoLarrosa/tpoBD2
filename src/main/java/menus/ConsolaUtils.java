package menus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import modelo.MedicionesPorCiudad;
import modelo.MedicionesPorPais;
import modelo.MedicionesPorZona;
import repositories.MedicionesCassandraRepository;

public class ConsolaUtils {

    
    public static Date[] pedirRangoFechas(Scanner scanner) {
        try {
            System.out.print("Ingrese fecha de inicio (dd/MM/yyyy): ");
            String inicio = scanner.nextLine();
            System.out.print("Ingrese fecha de fin (dd/MM/yyyy): ");
            String fin = scanner.nextLine();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date f1 = sdf.parse(inicio);
            Date f2 = sdf.parse(fin);
            return new Date[]{f1, f2};
        } catch (Exception e) {
            System.out.println("Formato de fecha inválido. Use dd/MM/yyyy");
            return null;
        }
    }

    
    public static List<Double> obtenerValoresMediciones(String ubicacion, String tipo, Date fechaInicio, Date fechaFin) {
        List<Double> valores = new ArrayList<>();
        if ("zona".equals(ubicacion)) {
            List<MedicionesPorZona> todas = MedicionesCassandraRepository.getInstance().findAllZona();
            for (MedicionesPorZona m : todas) {
                if (m.tipo.equalsIgnoreCase(tipo) && m.fecha != null && !m.fecha.before(fechaInicio) && !m.fecha.after(fechaFin)) {
                    valores.add(m.valor);
                }
            }
        } else if ("ciudad".equals(ubicacion)) {
            List<MedicionesPorCiudad> todas = MedicionesCassandraRepository.getInstance().findAllCiudad();
            for (MedicionesPorCiudad m : todas) {
                if (m.tipo.equalsIgnoreCase(tipo) && m.fecha != null && !m.fecha.before(fechaInicio) && !m.fecha.after(fechaFin)) {
                    valores.add(m.valor);
                }
            }
        } else if ("pais".equals(ubicacion)) {
            List<MedicionesPorPais> todas = MedicionesCassandraRepository.getInstance().findAllPais();
            for (MedicionesPorPais m : todas) {
                if (m.tipo.equalsIgnoreCase(tipo) && m.fecha != null && !m.fecha.before(fechaInicio) && !m.fecha.after(fechaFin)) {
                    valores.add(m.valor);
                }
            }
        }
        return valores;
    }

    public static void limpiarConsola() {
        // No hacer nada - mantener el historial visible
    }

    public static void limpiarConsolaConPausa(Scanner scanner) {
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (InterruptedException | java.io.IOException e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    public static void mostrarTitulo(String titulo) {
        System.out.println("==============================");
        System.out.println(titulo);
        System.out.println("==============================");
    }

    public static void mostrarOpciones(List<String> opciones) {
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println((i + 1) + ". " + opciones.get(i));
        }
    }

    public static int leerEntero(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número entero válido: ");
            }
        }
    }

    public static double leerDouble(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número decimal válido: ");
            }
        }
    }

    public static int leerEnteroEnRango(Scanner scanner, int min, int max) {
        while (true) {
            int valor = leerEntero(scanner);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.printf("Ingrese un número entre %d y %d: ", min, max);
        }
    }
}
