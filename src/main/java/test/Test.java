package test;

import java.util.Scanner;

import controlador.UsuarioController;
import modelo.EstadoUsuario;
import modelo.Rol;
import services.UsuarioService;

public class Test {

	public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Create services and controller
        UsuarioService usuarioService = new UsuarioService();
        UsuarioController usuarioController = new UsuarioController(usuarioService);

        // Get user input
        System.out.println("Ingrese el ID del usuario:");
        String id = scanner.nextLine();

        System.out.println("Ingrese el nombre del usuario:");
        String nombre = scanner.nextLine();

        System.out.println("Ingrese el email del usuario:");
        String email = scanner.nextLine();

        System.out.println("Ingrese el password del usuario:");
        String password = scanner.nextLine();

        System.out.println("Ingrese el estado del usuario (ACTIVO/INACTIVO):");
        EstadoUsuario estado = EstadoUsuario.valueOf(scanner.nextLine().toUpperCase());

        System.out.println("Ingrese el rol del usuario:");
        Rol rol = new Rol("1", scanner.nextLine());

        // Create user
        usuarioController.crearUsuario(id, nombre, email, password, estado, rol);

        System.out.println("Usuario creado exitosamente.");

        scanner.close();
	}
}