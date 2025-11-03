package menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controlador.SesionController;
import exceptions.ErrorConectionMongoException;
import modelo.Usuario;

public class MenuUsuario implements Menu {

    private final Usuario usuario;
    private final List<MenuOption> options = new ArrayList<>();
    private final Scanner scanner;
    private final SesionController sesionController;

    public MenuUsuario(Usuario usuario, Scanner scanner, SesionController sesionController) {
        this.usuario = usuario;
        this.scanner = scanner;
        this.sesionController = sesionController;
        options.add(new MenuOption("Ver perfil", this::verPerfil));
        options.add(new MenuOption("UADE Chat", this::abrirUadeChat));
        options.add(new MenuOption("Cerrar sesión", this::cerrarSesion));
    }

    private void abrirUadeChat() {
        new MenuChat(usuario, scanner).show();
    }

    private void verPerfil() {
        ConsolaUtils.limpiarConsola();
        ConsolaUtils.mostrarTitulo("PERFIL DE USUARIO");
        System.out.println("Nombre: " + usuario.getNombre());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Rol: " + usuario.getRol());
        System.out.println("Estado: " + usuario.getEstado());
        System.out.println("Saldo cuenta corriente: " + usuario.getCuentaCorriente().getSaldo());
        System.out.println();
        System.out.print("Presione Enter para continuar...");
        scanner.nextLine();
    }

    private void cerrarSesion() {
        try {
            sesionController.cerrarSesion();
            System.out.println("Sesión cerrada y registrada en Redis.\n");
        } catch (ErrorConectionMongoException e) {
            System.out.println("Error al cerrar sesión en Redis: " + e.getMessage());
        }
    }

    @Override
    public void show() {
        boolean continuar = true;
        while (continuar) {
            ConsolaUtils.limpiarConsola();
            ConsolaUtils.mostrarTitulo("MENÚ DE USUARIO");
            List<String> opcionesMenu = new ArrayList<>();
            for (MenuOption option : options) {
                opcionesMenu.add(option.getTitle());
            }
            ConsolaUtils.mostrarOpciones(opcionesMenu);
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
