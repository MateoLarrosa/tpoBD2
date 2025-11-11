package main;

import java.util.Scanner;

import controlador.UsuarioController;
import menus.MenuPrincipal;
import services.DataInitializationService;

public class Main {

    public static void main(String[] args) {

        // Initialize required data in the database
        DataInitializationService dataInitService = DataInitializationService.getInstance();
        dataInitService.initializeProcesos();

        UsuarioController usuarioController = UsuarioController.getInstance();
        try (Scanner scanner = new Scanner(System.in)) {
            MenuPrincipal menuPrincipal = new MenuPrincipal(usuarioController, scanner);
            menuPrincipal.show();
        }
    }
}
