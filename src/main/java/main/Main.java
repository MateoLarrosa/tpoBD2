package main;

import java.util.Scanner;

import controlador.UsuarioController;
import menus.MenuPrincipal;
import services.UsuarioService;

public class Main {

    public static void main(String[] args) {
        UsuarioService usuarioService = new UsuarioService();
        UsuarioController usuarioController = new UsuarioController(usuarioService);
        try (Scanner scanner = new Scanner(System.in)) {
            MenuPrincipal menuPrincipal = new MenuPrincipal(usuarioController, scanner);
            menuPrincipal.show();
        }
    }
}
