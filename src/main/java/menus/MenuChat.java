
package menus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import controlador.MensajeriaController;
import modelo.Mensaje;
import modelo.Usuario;

public class MenuChat implements Menu {
    private final Usuario usuario;
    private final Scanner scanner;
    private boolean salir = false;

    public MenuChat(Usuario usuario, Scanner scanner) {
        this.usuario = usuario;
        this.scanner = scanner;
    }

    @Override
    public void show() {
        while (!salir) {
            ConsolaUtils.limpiarConsola();
            System.out.println("\n--- UADE Chat ---");
            System.out.println("1. Ver chats / Crear chat");
            System.out.println("2. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1":
                    verChatsYCrear();
                    break;
                case "2":
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void verChatsYCrear() {
        ConsolaUtils.limpiarConsola();
        try {
            List<Usuario> usuarios = controlador.UsuarioController.getInstance().obtenerTodosLosUsuarios();
            Set<String> chats = new HashSet<>();
            Map<String, Usuario> idToUsuario = new HashMap<>();
            for (Usuario u : usuarios) {
                if (!u.getId().equals(usuario.getId())) {
                    idToUsuario.put(u.getId(), u);
                    try {
                        List<Mensaje> historial = MensajeriaController.getInstance().historial(usuario.getId(), u.getId(), 1);
                        if (!historial.isEmpty()) {
                            chats.add(u.getId());
                        }
                    } catch (exceptions.ErrorConectionMongoException ex) {
                        System.out.println("Error de conexión al obtener historial: " + ex.getMessage());
                    }
                }
            }
            System.out.println("\n--- Tus chats ---");
            System.out.println("1. Crear chat nuevo");
            int i = 2;
            List<String> chatIds = new java.util.ArrayList<>(chats);
            for (String id : chatIds) {
                Usuario otro = idToUsuario.get(id);
                System.out.println(i + ". " + otro.getNombre() + " (" + otro.getEmail() + ")");
                i++;
            }
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();
            try {
                int idx = Integer.parseInt(opcion);
                if (idx == 1) {
                    crearChatNuevo(usuarios, chats);
                } else if (idx > 1 && idx <= chatIds.size() + 1) {
                    String chatUserId = chatIds.get(idx - 2);
                    mostrarChatCon(chatUserId, idToUsuario.get(chatUserId));
                } else {
                    System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.");
            }
        } catch (RuntimeException e) {
            System.out.println("Error al obtener los chats: " + e.getMessage());
        }
    }

    private void crearChatNuevo(List<Usuario> usuarios, Set<String> chats) {
        ConsolaUtils.limpiarConsola();
        List<Usuario> disponibles = new java.util.ArrayList<>();
        for (Usuario u : usuarios) {
            if (!u.getId().equals(usuario.getId()) && !chats.contains(u.getId())) {
                disponibles.add(u);
            }
        }
        if (disponibles.isEmpty()) {
            System.out.println("No hay usuarios disponibles para crear un chat nuevo.");
            return;
        }
        System.out.println("Usuarios disponibles para chatear:");
        for (int i = 0; i < disponibles.size(); i++) {
            Usuario u = disponibles.get(i);
            System.out.println((i + 1) + ". " + u.getNombre() + " (" + u.getEmail() + ")");
        }
        System.out.print("Seleccione el número de usuario: ");
        String opcion = scanner.nextLine();
        try {
            int idx = Integer.parseInt(opcion) - 1;
            if (idx >= 0 && idx < disponibles.size()) {
                Usuario destino = disponibles.get(idx);
                mostrarChatCon(destino.getId(), destino);
            } else {
                System.out.println("Opción inválida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Opción inválida.");
        }
    }

    private void mostrarChatCon(String otroId, Usuario otro) {
        ConsolaUtils.limpiarConsola();
        try {
            List<Mensaje> historial = MensajeriaController.getInstance().historial(usuario.getId(), otroId, 20);
            System.out.println("\n--- Chat con " + otro.getNombre() + " ---");
            for (Mensaje m : historial) {
                String quien = m.getRemitenteId().equals(usuario.getId()) ? "Tú" : otro.getNombre();
                System.out.println("[" + quien + "] " + m.getContenido());
            }
            System.out.print("Escriba un mensaje (o vacío para volver): ");
            String texto = scanner.nextLine();
            if (!texto.isBlank()) {
                try {
                    MensajeriaController.getInstance().enviar(usuario.getId(), otroId, texto);
                    System.out.println("Mensaje enviado.");
                } catch (exceptions.ErrorConectionMongoException ex) {
                    System.out.println("Error de conexión al enviar mensaje: " + ex.getMessage());
                }
            }
        } catch (exceptions.ErrorConectionMongoException e) {
            System.out.println("Error de conexión al mostrar historial: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Error al mostrar/enviar mensaje: " + e.getMessage());
        }
    }
}
