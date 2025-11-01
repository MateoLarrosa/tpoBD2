package menus;

public class ConsolaUtils {

    public static void limpiarConsola() {
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

    public static void mostrarOpciones(java.util.List<String> opciones) {
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println((i + 1) + ". " + opciones.get(i));
        }
    }

    /**
     * Lee un entero desde la consola, mostrando mensaje de error si no es
     * válido.
     */
    public static int leerEntero(java.util.Scanner scanner) {
        while (true) {
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número entero válido: ");
            }
        }
    }

    /**
     * Lee un double desde la consola, mostrando mensaje de error si no es
     * válido.
     */
    public static double leerDouble(java.util.Scanner scanner) {
        while (true) {
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número decimal válido: ");
            }
        }
    }

    /**
     * Lee un entero dentro de un rango [min, max] desde la consola.
     */
    public static int leerEnteroEnRango(java.util.Scanner scanner, int min, int max) {
        while (true) {
            int valor = leerEntero(scanner);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.printf("Ingrese un número entre %d y %d: ", min, max);
        }
    }
}
