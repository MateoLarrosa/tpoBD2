package menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controlador.MensajeriaController;
import controlador.UsuarioController;
import exceptions.ErrorConectionMongoException;
import modelo.Mensaje;
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
        options.add(new MenuOption("Ver chat con usuario", this::verChatConUsuario));
        options.add(new MenuOption("Iniciar nuevo chat", this::iniciarNuevoChat));
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

    private void verChatConUsuario() {
        System.out.print("\nIngrese el email del usuario con quien desea chatear: ");
        String emailDestino = scanner.nextLine();

        try {
            List<Usuario> usuarios = UsuarioController.getInstance().obtenerTodosLosUsuarios();
            Usuario destinatario = usuarios.stream()
                    .filter(u -> u.getEmail().equals(emailDestino))
                    .findFirst()
                    .orElse(null);

            if (destinatario == null) {
                System.out.println("Usuario no encontrado.");
                scanner.nextLine();
                return;
            }

            boolean continuar = true;
            while (continuar) {
                limpiarConsola();
                System.out.println("--- CHAT CON " + destinatario.getNombre() + " ---");

                // Mostrar historial de mensajes
                List<Mensaje> historial = MensajeriaController.getInstance().historial(usuario.getId(), destinatario.getId(), 20);
                for (Mensaje mensaje : historial) {
                    String prefijo = mensaje.getRemitenteId().equals(usuario.getId()) ? "Tú:" : destinatario.getNombre() + ":";
                    System.out.println(prefijo + " " + mensaje.getContenido());
                }

                System.out.println("\n1. Enviar mensaje");
                System.out.println("2. Actualizar chat");
                System.out.println("3. Volver al menú anterior");
                System.out.print("Seleccione una opción: ");

                String opcion = scanner.nextLine();
                switch (opcion) {
                    case "1":
                        System.out.print("Escriba su mensaje: ");
                        String texto = scanner.nextLine();
                        MensajeriaController.getInstance().enviar(usuario.getId(), destinatario.getId(), texto);
                        break;
                    case "2":
                        // No hace falta hacer nada, el ciclo mostrará los mensajes actualizados
                        break;
                    case "3":
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción inválida");
                        break;
                }
            }
        } catch (ErrorConectionMongoException e) {
            System.out.println("Error al acceder al chat: " + e.getMessage());
            scanner.nextLine();
        }
    }

    private void iniciarNuevoChat() {
        try {
            System.out.println("\n--- USUARIOS DISPONIBLES ---");
            List<Usuario> usuarios = UsuarioController.getInstance().obtenerTodosLosUsuarios();

            // Filtrar el usuario actual de la lista
            usuarios = usuarios.stream()
                    .filter(u -> !u.getId().equals(usuario.getId()))
                    .toList();

            if (usuarios.isEmpty()) {
                System.out.println("No hay otros usuarios disponibles para chatear.");
                scanner.nextLine();
                return;
            }

            // Mostrar lista de usuarios
            for (int i = 0; i < usuarios.size(); i++) {
                Usuario u = usuarios.get(i);
                System.out.println((i + 1) + ". " + u.getNombre() + " (" + u.getEmail() + ")");
            }

            System.out.print("\nSeleccione el número del usuario con quien desea chatear: ");
            try {
                int opcion = Integer.parseInt(scanner.nextLine()) - 1;
                if (opcion >= 0 && opcion < usuarios.size()) {
                    Usuario destinatario = usuarios.get(opcion);
                    System.out.println("\nIniciando chat con " + destinatario.getNombre());
                    System.out.print("Escriba su primer mensaje: ");
                    String mensaje = scanner.nextLine();

                    MensajeriaController.getInstance().enviar(usuario.getId(), destinatario.getId(), mensaje);
                    System.out.println("Mensaje enviado. Ahora puede continuar la conversación desde la opción 'Ver chat con usuario'.");
                } else {
                    System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.");
            }

            System.out.println("\nPresione Enter para continuar...");
            scanner.nextLine();
        } catch (ErrorConectionMongoException e) {
            System.out.println("Error al iniciar el chat: " + e.getMessage());
            scanner.nextLine();
        }
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
