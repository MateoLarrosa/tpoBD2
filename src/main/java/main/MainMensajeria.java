package main;

import java.util.List;

import controlador.MensajeriaController;
import exceptions.ErrorConectionMongoException;
import modelo.Mensaje;

public class MainMensajeria {

    public static void main(String[] args) {
        try {
            System.out.println("Iniciando sistema de mensajería...");
            // Obtener las instancias usando el patrón Singleton
            MensajeriaController controlador = MensajeriaController.getInstance();

            // Escenario: Ana ↔ Bruno
            String ana = "user_101";
            String bruno = "user_202";

            // 1) Ana envía
            controlador.enviar(ana, bruno, "Hola Bruno, ¿revisaste el sensor de la zona norte?");

            // 2) Bruno responde
            controlador.enviar(bruno, ana, "Hola Ana, sí, todo en orden por allá.");

            // 3) Ana confirma
            controlador.enviar(ana, bruno, "Perfecto, gracias!");

            // 4) Historial (últimos 50)
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
