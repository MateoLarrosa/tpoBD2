package menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controlador.SensorController;
import controlador.SesionController;
import controlador.UsuarioController;
import exceptions.ErrorConectionMongoException;
import modelo.EstadoUsuario;
import modelo.Sesion;
import modelo.Usuario;

public class MenuPrincipal implements Menu {

    private final UsuarioController usuarioController;
    private final SesionController sesionController = SesionController.getInstance();
    private String idSesionActual;
    private final List<MenuOption> options = new ArrayList<>();
    private final SensorController sensorController;
    private final Scanner scanner;
    private boolean salir = false;

    public MenuPrincipal(UsuarioController usuarioController, Scanner scanner) {
        this.usuarioController = usuarioController;
        this.scanner = scanner;
        this.sensorController = SensorController.getInstance();
        options.add(new MenuOption("Iniciar sesión", this::iniciarSesion));
        options.add(new MenuOption("Crear usuario", this::crearUsuario));
        // El menú de sensores se agrega dinámicamente después del login si el usuario es ADMIN
        options.add(new MenuOption("Salir", this::salir));

    }

    private void menuSensores() {
        new MenuSensor(sensorController, scanner).show();
    }

    private void iniciarSesion() {
        ConsolaUtils.limpiarConsola();
        List<Usuario> usuarios = usuarioController.obtenerTodosLosUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.\n");
            return;
        }
        System.out.println("Usuarios disponibles:");
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println((i + 1) + ". " + usuarios.get(i).getNombre() + " (" + usuarios.get(i).getEmail() + ")");
        }
        System.out.print("Seleccione el número de usuario para iniciar sesión: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine()) - 1;
            if (idx >= 0 && idx < usuarios.size()) {
                Usuario usuarioLogueado = usuarios.get(idx);
                try {
                    Sesion sesion = sesionController.iniciarSesion(usuarioLogueado.getId());
                    idSesionActual = sesion.getIdSesion();
                    System.out.println("\nSesión iniciada como: " + usuarioLogueado.getNombre() + " (ID Sesión: " + idSesionActual + ")\n");
                    new MenuUsuario(usuarioLogueado, scanner, sesionController).show();
                } catch (ErrorConectionMongoException e) {
                    System.out.println("Error al iniciar sesión en Redis: " + e.getMessage());
                }
            } else {
                System.out.println("Selección inválida.\n");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.\n");
        }
    }

    private void crearUsuario() {
        ConsolaUtils.limpiarConsola();
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
        boolean creado = usuarioController.crearUsuario(null, nombre, email, password, estado, rol);
        if (creado) {
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
        if (usuarioController.obtenerTodosLosUsuarios().isEmpty()) {
            System.out.println("No hay usuarios registrados.\n");
        }
        while (!salir) {
            ConsolaUtils.limpiarConsola();
            ConsolaUtils.mostrarTitulo("MENÚ PRINCIPAL");
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
                } else {
                    System.out.println("Opción inválida.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.\n");
            }
        }
    }
}
