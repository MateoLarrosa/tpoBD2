package main;

import java.util.List;

import controlador.MensajeriaController;
import exceptions.ErrorConectionMongoException;
import modelo.Mensaje;

public class MainMensajeria {

    public static void main(String[] args) {
        try {
            System.out.println("Iniciando sistema de mensajería...");
            MensajeriaController controlador = MensajeriaController.getInstance();

            String ana = "user_101";
            String bruno = "user_202";

            controlador.enviar(ana, bruno, "Hola Bruno, ¿revisaste el sensor de la zona norte?");
            controlador.enviar(bruno, ana, "Hola Ana, sí, todo en orden por allá.");
            controlador.enviar(ana, bruno, "Perfecto, gracias!");

            List<Mensaje> historial = controlador.historial(ana, bruno, 50);
            System.out.println("===== HISTORIAL DEL CHAT =====");
            for (Mensaje m : historial) {
                System.out.printf("[%s] %s: %s%n", m.getIdRedis(), m.getRemitenteId(), m.getContenido());
            }

        } catch (ErrorConectionMongoException e) {
            e.printStackTrace();
        }
    }
}
