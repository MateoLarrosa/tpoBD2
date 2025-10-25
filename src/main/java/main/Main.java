package main;

import java.util.Scanner;

import controlador.UsuarioController;
import menus.MenuPrincipal;

public class Main {

    public static void main(String[] args) {
        // Obtener la instancia usando el patrón Singleton
        UsuarioController usuarioController = UsuarioController.getInstance();
        try (Scanner scanner = new Scanner(System.in)) {
            MenuPrincipal menuPrincipal = new MenuPrincipal(usuarioController, scanner);
            menuPrincipal.show();
        }
    }
}
