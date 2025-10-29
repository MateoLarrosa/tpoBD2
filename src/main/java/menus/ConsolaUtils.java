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
}
