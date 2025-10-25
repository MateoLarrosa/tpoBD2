package menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import modelo.Usuario;

public class MenuUsuario implements Menu {

    // Método para limpiar la consola
    private void limpiarConsola() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    private final Usuario usuario;
    private final Scanner scanner;
    private final List<MenuOption> options = new ArrayList<>();

    public MenuUsuario(Usuario usuario, Scanner scanner) {
        this.usuario = usuario;
        this.scanner = scanner;
        options.add(new MenuOption("Ver perfil", this::verPerfil));
        options.add(new MenuOption("Cerrar sesión", this::cerrarSesion));
    }

    private void verPerfil() {
        System.out.println("\n--- PERFIL DE USUARIO ---");
        System.out.println("Nombre: " + usuario.getNombre());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Rol: " + usuario.getRol());
        System.out.println("Estado: " + usuario.getEstado());
        System.out.println("Saldo cuenta corriente: " + usuario.getCuentaCorriente().getSaldo());
        System.out.println();
    }

    private void cerrarSesion() {
        System.out.println("Sesión cerrada.\n");
    }

    @Override
    public void show() {
        boolean continuar = true;
        while (continuar) {
            limpiarConsola();
            System.out.println("--- MENÚ DE USUARIO ---");
            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + ". " + options.get(i).getTitle());
            }
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();
            try {
                int idx = Integer.parseInt(opcion) - 1;
                if (idx >= 0 && idx < options.size()) {
                    options.get(idx).execute();
                    String title = options.get(idx).getTitle();
                    if (title.equals("Cerrar sesión")) {
                        continuar = false;
                    }
                } else {
                    System.out.println("Opción inválida.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.\n");
            }
        }
    }
}
