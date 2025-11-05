package main;

import java.util.Scanner;

import controlador.ProcesoController;
import controlador.UsuarioController;
import menus.MenuPrincipal;

public class Main {

    public static void main(String[] args) {
        // Inicializar catálogo de procesos al inicio de la aplicación
        System.out.println("Inicializando catálogo de procesos...");
        ProcesoController procesoController = ProcesoController.getInstance();
        procesoController.inicializarCatalogo();
        System.out.println("Catálogo de procesos inicializado.\n");
        
        UsuarioController usuarioController = UsuarioController.getInstance();
        try (Scanner scanner = new Scanner(System.in)) {
            MenuPrincipal menuPrincipal = new MenuPrincipal(usuarioController, scanner);
            menuPrincipal.show();
        }
    }
}
