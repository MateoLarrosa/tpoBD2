package main;

import java.util.Scanner;

import controlador.UsuarioController;
import menus.MenuPrincipal;
import services.UsuarioService;

public class Main {
    public static void main(String[] args) {
        UsuarioService usuarioService = new UsuarioService();
        UsuarioController usuarioController = new UsuarioController(usuarioService);
        Scanner scanner = new Scanner(System.in);

        MenuPrincipal menuPrincipal = new MenuPrincipal(usuarioService, usuarioController, scanner);
        menuPrincipal.show();
        scanner.close();
    }
}
