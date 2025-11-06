package menus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import controlador.ProcesoController;
import controlador.SolicitudProcesoController;
import modelo.EstadoSolicitud;
import modelo.Proceso;
import modelo.SolicitudProceso;

public class MenuProcesos {

    private final Scanner scanner;
    private final ProcesoController procesoController;
    private final SolicitudProcesoController solicitudController;
    private final DateTimeFormatter formatter;

    public MenuProcesos(Scanner scanner) {
        this.scanner = scanner;
        this.procesoController = ProcesoController.getInstance();
        this.solicitudController = SolicitudProcesoController.getInstance();
        this.formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    }

    public void mostrar(String usuarioId) {
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n╔══════════════════════════════════════════╗");
            System.out.println("║    GESTIÓN DE PROCESOS Y SOLICITUDES    ║");
            System.out.println("╚══════════════════════════════════════════╝");
            System.out.println();
            System.out.println("1. Ver mis solicitudes");
            System.out.println("2. Ver solicitudes pendientes");
            System.out.println("3. Ver solicitudes completadas");
            System.out.println("4. Ver estadísticas de uso");
            System.out.println("5. Calcular costo total de mis solicitudes");
            System.out.println("6. Ver historial por rango de fechas");
            System.out.println("0. Volver al menú principal");
            System.out.println();
            System.out.print("Seleccione una opción: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        verMisSolicitudes(usuarioId);
                        break;
                    case 2:
                        verSolicitudesPendientes(usuarioId);
                        break;
                    case 3:
                        verSolicitudesCompletadas(usuarioId);
                        break;
                    case 4:
                        verEstadisticas(usuarioId);
                        break;
                    case 5:
                        calcularCostoTotal(usuarioId);
                        break;
                    case 6:
                        verHistorialPorRango(usuarioId);
                        break;
                    case 0:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void verMisSolicitudes(String usuarioId) {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("            MIS SOLICITUDES");
        System.out.println("═══════════════════════════════════════════");

        List<SolicitudProceso> solicitudes = solicitudController.obtenerSolicitudesPorUsuario(usuarioId);

        if (solicitudes.isEmpty()) {
            System.out.println("No tiene solicitudes registradas.");
            return;
        }

        mostrarListaSolicitudes(solicitudes);
    }

    private void verSolicitudesPendientes(String usuarioId) {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("         SOLICITUDES PENDIENTES");
        System.out.println("═══════════════════════════════════════════");

        List<SolicitudProceso> solicitudes = solicitudController.obtenerSolicitudesPorUsuarioYEstado(
                usuarioId, EstadoSolicitud.PENDIENTE);

        if (solicitudes.isEmpty()) {
            System.out.println("No tiene solicitudes pendientes.");
            return;
        }

        mostrarListaSolicitudes(solicitudes);
    }

    private void verSolicitudesCompletadas(String usuarioId) {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("        SOLICITUDES COMPLETADAS");
        System.out.println("═══════════════════════════════════════════");

        List<SolicitudProceso> solicitudes = solicitudController.obtenerSolicitudesPorUsuarioYEstado(
                usuarioId, EstadoSolicitud.COMPLETADO);

        if (solicitudes.isEmpty()) {
            System.out.println("No tiene solicitudes completadas.");
            return;
        }

        mostrarListaSolicitudes(solicitudes);
    }

    private void verEstadisticas(String usuarioId) {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("         ESTADÍSTICAS DE USO");
        System.out.println("═══════════════════════════════════════════");

        int[] stats = solicitudController.obtenerEstadisticasPorUsuario(usuarioId);

        System.out.println("\nSolicitudes totales:     " + stats[0]);
        System.out.println("Completadas:             " + stats[1]);
        System.out.println("Pendientes:              " + stats[2]);
        System.out.println("Con errores:             " + stats[3]);

        if (stats[0] > 0) {
            double tasaExito = (stats[1] * 100.0) / stats[0];
            System.out.printf("Tasa de éxito:           %.2f%%%n", tasaExito);
        }
    }

    private void calcularCostoTotal(String usuarioId) {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("          CÁLCULO DE COSTOS");
        System.out.println("═══════════════════════════════════════════");

        int costoTotal = solicitudController.calcularCostoTotalPorUsuario(usuarioId);

        System.out.println("\nCosto total de solicitudes completadas: $" + costoTotal);
    }

    private void verHistorialPorRango(String usuarioId) {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("      HISTORIAL POR RANGO DE FECHAS");
        System.out.println("═══════════════════════════════════════════");

        System.out.print("\nIngrese fecha inicio (dd/MM/yyyy): ");
        String fechaInicioStr = scanner.nextLine();

        System.out.print("Ingrese fecha fin (dd/MM/yyyy): ");
        String fechaFinStr = scanner.nextLine();

        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr + " 00:00:00",
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            LocalDateTime fechaFin = LocalDateTime.parse(fechaFinStr + " 23:59:59",
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

            List<SolicitudProceso> solicitudes = solicitudController.obtenerSolicitudesPorUsuarioYRango(
                    usuarioId, fechaInicio, fechaFin);

            if (solicitudes.isEmpty()) {
                System.out.println("No hay solicitudes en el rango especificado.");
                return;
            }

            mostrarListaSolicitudes(solicitudes);

            int costoTotal = solicitudController.calcularCostoTotalPorUsuarioYRango(
                    usuarioId, fechaInicio, fechaFin);
            System.out.println("\nCosto total en el rango: $" + costoTotal);

        } catch (Exception e) {
            System.out.println("Error al procesar las fechas: " + e.getMessage());
        }
    }

    private void mostrarListaSolicitudes(List<SolicitudProceso> solicitudes) {
        System.out.println();
        System.out.printf("%-20s %-35s %-12s %-10s%n", "FECHA", "PROCESO", "ESTADO", "TIEMPO(ms)");
        System.out.println("────────────────────────────────────────────────────────────────────────────");

        for (SolicitudProceso solicitud : solicitudes) {
            Proceso proceso = solicitud.getProceso();
            String nombreProceso = proceso != null ? proceso.getNombre() : "Desconocido";

            System.out.printf("%-20s %-35s %-12s %-10d%n",
                    solicitud.getFechaSolicitud().format(formatter),
                    nombreProceso.length() > 35 ? nombreProceso.substring(0, 32) + "..." : nombreProceso,
                    solicitud.getEstado(),
                    solicitud.getTiempoEjecucionMs());

            if (solicitud.getResultado() != null && !solicitud.getResultado().isEmpty()) {
                System.out.println("    └─> " + solicitud.getResultado());
            }

            if (solicitud.getParametros() != null && !solicitud.getParametros().isEmpty()) {
                System.out.println("    └─> Parámetros: " + solicitud.getParametros());
            }
        }

        System.out.println("\nTotal de solicitudes: " + solicitudes.size());
    }
}
