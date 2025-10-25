package menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controlador.UsuarioController;
import modelo.CuentaCorriente;
import modelo.EstadoUsuario;
import modelo.Usuario;
import services.UsuarioService;

public class MenuPrincipal implements Menu {
    private final UsuarioService usuarioService;
    private final UsuarioController usuarioController;
    private final Scanner scanner;
    private final List<MenuOption> options = new ArrayList<>();
    private boolean salir = false;

    public MenuPrincipal(UsuarioService usuarioService, UsuarioController usuarioController, Scanner scanner) {
        this.usuarioService = usuarioService;
        this.usuarioController = usuarioController;
        this.scanner = scanner;
        options.add(new MenuOption("Iniciar sesión", this::iniciarSesion));
        options.add(new MenuOption("Crear usuario", this::crearUsuario));
        options.add(new MenuOption("Salir", this::salir));
    }

    private void iniciarSesion() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.\n");
            return;
        }
        System.out.println("Usuarios disponibles:");
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println((i+1) + ". " + usuarios.get(i).getNombre() + " (" + usuarios.get(i).getEmail() + ")");
        }
        System.out.print("Seleccione el número de usuario para iniciar sesión: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine()) - 1;
            if (idx >= 0 && idx < usuarios.size()) {
                Usuario usuarioLogueado = usuarios.get(idx);
                System.out.println("\nSesión iniciada como: " + usuarioLogueado.getNombre() + "\n");
                new MenuUsuario(usuarioLogueado, scanner).show();
            } else {
                System.out.println("Selección inválida.\n");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.\n");
        }
    }

    private void crearUsuario() {
        System.out.print("Ingrese el nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese el email: ");
        String email = scanner.nextLine();
        System.out.print("Ingrese el password: ");
        String password = scanner.nextLine();
        System.out.print("Ingrese el estado (ACTIVO/INACTIVO): ");
        EstadoUsuario estado = EstadoUsuario.valueOf(scanner.nextLine().toUpperCase());
        System.out.print("Ingrese el rol: ");
        String rol = scanner.nextLine();
        Usuario nuevoUsuario = new Usuario(null, nombre, email, password, estado, rol, new CuentaCorriente(0.0));
        if (usuarioService.crearUsuario(nuevoUsuario)) {
            System.out.println("Usuario creado exitosamente.\n");
        } else {
            System.out.println("Error al crear usuario.\n");
        }
    }

    private void salir() {
        System.out.println("Saliendo...");
        this.salir = true;
    }

    @Override
    public void show() {
        if (usuarioService.obtenerTodosLosUsuarios().isEmpty()) {
            System.out.println("No hay usuarios registrados.\n");
            return;
        }
        while (!salir) {
            System.out.println("--- MENÚ PRINCIPAL ---");
            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + ". " + options.get(i).getTitle());
            }
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();
            try {
                int idx = Integer.parseInt(opcion) - 1;
                if (idx >= 0 && idx < options.size()) {
                    options.get(idx).execute();
                } else {
                    System.out.println("Opción inválida.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.\n");
            }
        }
    }
}
