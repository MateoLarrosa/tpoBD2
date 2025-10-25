package main;

import java.util.List;

import connections.RedisPool;
import controlador.MensajeriaController;
import exceptions.ErrorConectionMongoException;
import modelo.Mensaje;
import repositories.RedisMessageRepository;
import services.MensajeriaService;

public class MainMensajeria {

    public static void main(String[] args) {
        try {
            System.out.println("Iniciando sistema de mensajería...");
            // Inyección manual de dependencias
            RedisPool pool = RedisPool.getInstancia();
            RedisMessageRepository repositorio = new RedisMessageRepository(pool);
            MensajeriaService servicio = new MensajeriaService(repositorio);
            MensajeriaController controlador = new MensajeriaController(servicio);

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
