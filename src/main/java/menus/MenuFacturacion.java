package menus;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controlador.FacturaController;
import controlador.PagoController;
import modelo.Factura;
import modelo.Pago;
import modelo.Proceso;
import modelo.SolicitudProceso;
import services.ProcesoService;

public class MenuFacturacion implements Menu {

    private final Scanner scanner;
    private final String usuarioId;
    private final FacturaController facturaController;
    private final PagoController pagoController;
    private final ProcesoService procesoService;
    private final DateTimeFormatter formatter;

    public MenuFacturacion(Scanner scanner, String usuarioId) {
        this.scanner = scanner;
        this.usuarioId = usuarioId;
        this.facturaController = FacturaController.getInstance();
        this.pagoController = PagoController.getInstance();
        this.procesoService = ProcesoService.getInstance();
        this.formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    @Override
    public void show() {
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    generarFacturaMensual();
                    break;
                case "2":
                    verMisFacturas();
                    break;
                case "3":
                    verFacturasPendientes();
                    break;
                case "4":
                    verFacturasPagadas();
                    break;
                case "5":
                    verDetalleFactura();
                    break;
                case "6":
                    pagarFactura();
                    break;
                case "7":
                    verEstadisticasFacturacion();
                    break;
                case "8":
                    verMontoAdeudado();
                    break;
                case "0":
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }

            if (continuar) {
                System.out.print("\nPresione Enter para continuar...");
                scanner.nextLine();
            }
        }
    }

    private void mostrarMenu() {
        ConsolaUtils.limpiarConsola();
        ConsolaUtils.mostrarTitulo("MIS FACTURAS Y PAGOS");
        List<String> opciones = new ArrayList<>();
        opciones.add("Generar factura mensual");
        opciones.add("Ver todas mis facturas");
        opciones.add("Ver facturas pendientes");
        opciones.add("Ver facturas pagadas");
        opciones.add("Ver detalle de una factura");
        opciones.add("Pagar una factura");
        opciones.add("Ver estadísticas de facturación");
        opciones.add("Ver monto total adeudado");
        opciones.add("Volver");
        ConsolaUtils.mostrarOpciones(opciones);
    }

    private void generarFacturaMensual() {
        System.out.println("\n--- GENERAR FACTURA MENSUAL ---");
        System.out.println("Se generará una factura con todos los procesos completados.");
        System.out.print("Días de vencimiento (ejemplo: 30): ");

        int dias = ConsolaUtils.leerEntero(scanner);

        Factura factura = facturaController.generarFacturaParaUsuario(usuarioId, dias);

        if (factura != null) {
            System.out.println("\n✓ Factura generada exitosamente:");
            System.out.println("ID: " + factura.getId());
            System.out.println("Fecha emisión: " + factura.getFechaEmision().format(formatter));
            System.out.println("Fecha vencimiento: " + factura.getFechaVencimiento().format(formatter));
            System.out.println("Procesos incluidos: " + factura.getSolicitudesFacturadas().size());
            System.out.println("Monto total: $" + String.format("%.2f", factura.getMontoTotal()));
            System.out.println("Estado: " + factura.getEstado());
        } else {
            System.out.println("\n✗ No se pudo generar la factura (no hay procesos para facturar).");
        }
    }

    private void verMisFacturas() {
        System.out.println("\n--- MIS FACTURAS ---");
        List<Factura> facturas = facturaController.obtenerFacturasPorUsuario(usuarioId);

        if (facturas.isEmpty()) {
            System.out.println("No tiene facturas registradas.");
            return;
        }

        System.out.println("Total de facturas: " + facturas.size());
        System.out.println();

        for (int i = 0; i < facturas.size(); i++) {
            Factura f = facturas.get(i);
            System.out.println((i + 1) + ". ID: " + f.getId());
            System.out.println("   Fecha emisión: " + f.getFechaEmision().format(formatter));
            System.out.println("   Fecha vencimiento: " + f.getFechaVencimiento().format(formatter));
            System.out.println("   Estado: " + f.getEstado());
            System.out.println("   Monto: $" + String.format("%.2f", f.getMontoTotal()));
            System.out.println("   Procesos: " + f.getSolicitudesFacturadas().size());
            System.out.println();
        }
    }

    private void verFacturasPendientes() {
        System.out.println("\n--- FACTURAS PENDIENTES ---");
        List<Factura> facturas = facturaController.obtenerFacturasPendientes(usuarioId);

        if (facturas.isEmpty()) {
            System.out.println("✓ No tiene facturas pendientes.");
            return;
        }

        System.out.println("Total: " + facturas.size());
        double totalPendiente = 0.0;

        for (int i = 0; i < facturas.size(); i++) {
            Factura f = facturas.get(i);
            System.out.println("\n" + (i + 1) + ". ID: " + f.getId());
            System.out.println("   Fecha emisión: " + f.getFechaEmision().format(formatter));
            System.out.println("   Fecha vencimiento: " + f.getFechaVencimiento().format(formatter));
            System.out.println("   Monto: $" + String.format("%.2f", f.getMontoTotal()));

            if (f.estaVencida()) {
                System.out.println("   ⚠️  VENCIDA");
            }

            totalPendiente += f.getMontoTotal();
        }

        System.out.println("\n--- TOTAL PENDIENTE: $" + String.format("%.2f", totalPendiente) + " ---");
    }

    private void verFacturasPagadas() {
        System.out.println("\n--- FACTURAS PAGADAS ---");
        List<Factura> facturas = facturaController.obtenerFacturasPagadas(usuarioId);

        if (facturas.isEmpty()) {
            System.out.println("No tiene facturas pagadas.");
            return;
        }

        System.out.println("Total: " + facturas.size());

        for (int i = 0; i < facturas.size(); i++) {
            Factura f = facturas.get(i);
            System.out.println("\n" + (i + 1) + ". ID: " + f.getId());
            System.out.println("   Fecha emisión: " + f.getFechaEmision().format(formatter));
            System.out.println("   Monto: $" + String.format("%.2f", f.getMontoTotal()));

            Pago pago = pagoController.obtenerPagoPorFactura(f.getId());
            if (pago != null) {
                System.out.println("   Pagado el: " + pago.getFechaPago().format(formatter));
                System.out.println("   Método: " + pago.getMetodoPago());
            }
        }
    }

    private void verDetalleFactura() {
        System.out.println("\n--- DETALLE DE FACTURA ---");
        System.out.print("Ingrese el ID de la factura: ");
        String facturaId = scanner.nextLine();

        Factura factura = facturaController.obtenerFacturaPorId(facturaId);
        if (factura == null) {
            System.out.println("✗ Factura no encontrada.");
            return;
        }

        if (!factura.getUsuarioId().equals(usuarioId)) {
            System.out.println("✗ Esta factura no le pertenece.");
            return;
        }

        System.out.println("\n=== FACTURA " + factura.getId() + " ===");
        System.out.println("Usuario ID: " + factura.getUsuarioId());
        System.out.println("Fecha emisión: " + factura.getFechaEmision().format(formatter));
        System.out.println("Fecha vencimiento: " + factura.getFechaVencimiento().format(formatter));
        System.out.println("Estado: " + factura.getEstado());
        System.out.println("Monto total: $" + String.format("%.2f", factura.getMontoTotal()));

        System.out.println("\n--- PROCESOS FACTURADOS ---");
        List<SolicitudProceso> solicitudes = facturaController.obtenerDetalleProcesosDeLaFactura(facturaId);

        if (solicitudes.isEmpty()) {
            System.out.println("No se encontraron detalles de procesos.");
        } else {
            for (int i = 0; i < solicitudes.size(); i++) {
                SolicitudProceso sol = solicitudes.get(i);
                Proceso proceso = sol.getProceso();

                System.out.println((i + 1) + ". " + (proceso != null ? proceso.getNombre() : "Proceso desconocido"));
                System.out.println("   Fecha: " + sol.getFechaSolicitud().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                System.out.println("   Estado: " + sol.getEstado());
                System.out.println("   Tiempo: " + sol.getTiempoEjecucionMs() + " ms");
                System.out.println("   Costo: $" + (proceso != null ? proceso.getCosto() : "0.00"));
            }
        }

        if (factura.getEstado().name().equals("PAGADA")) {
            Pago pago = pagoController.obtenerPagoPorFactura(facturaId);
            if (pago != null) {
                System.out.println("\n--- INFORMACIÓN DEL PAGO ---");
                System.out.println("Fecha de pago: " + pago.getFechaPago().format(formatter));
                System.out.println("Monto pagado: $" + String.format("%.2f", pago.getMontoPagado()));
                System.out.println("Método de pago: " + pago.getMetodoPago());
            }
        }
    }

    private void pagarFactura() {
        System.out.println("\n--- PAGAR FACTURA ---");

        List<Factura> pendientes = facturaController.obtenerFacturasPendientes(usuarioId);
        if (pendientes.isEmpty()) {
            System.out.println("✓ No tiene facturas pendientes de pago.");
            return;
        }

        System.out.println("Facturas pendientes:");
        for (int i = 0; i < pendientes.size(); i++) {
            Factura f = pendientes.get(i);
            System.out.println((i + 1) + ". ID: " + f.getId() + " - Monto: $"
                    + String.format("%.2f", f.getMontoTotal())
                    + " - Vencimiento: " + f.getFechaVencimiento().format(formatter));
        }

        System.out.print("\nSeleccione el número de factura a pagar: ");
        int idx = ConsolaUtils.leerEnteroEnRango(scanner, 1, pendientes.size()) - 1;
        Factura factura = pendientes.get(idx);

        System.out.println("\nFactura seleccionada:");
        System.out.println("ID: " + factura.getId());
        System.out.println("Monto a pagar: $" + String.format("%.2f", factura.getMontoTotal()));

        System.out.println("\nMétodos de pago disponibles:");
        List<String> metodos = List.of("Tarjeta de crédito", "Tarjeta de débito",
                "Transferencia bancaria", "Efectivo", "MercadoPago");
        for (int i = 0; i < metodos.size(); i++) {
            System.out.println((i + 1) + ". " + metodos.get(i));
        }

        System.out.print("\nSeleccione el método de pago: ");
        int metodoIdx = ConsolaUtils.leerEnteroEnRango(scanner, 1, metodos.size()) - 1;
        String metodoPago = metodos.get(metodoIdx);

        System.out.print("\n¿Confirmar pago de $" + String.format("%.2f", factura.getMontoTotal())
                + " con " + metodoPago + "? (S/N): ");
        String confirmacion = scanner.nextLine();

        if (confirmacion.equalsIgnoreCase("S")) {
            Pago pago = pagoController.registrarPago(factura.getId(), factura.getMontoTotal(), metodoPago);
            if (pago != null) {
                System.out.println("\n✓ Pago procesado exitosamente!");
                System.out.println("ID del pago: " + pago.getId());
                System.out.println("La factura ahora está PAGADA.");
            }
        } else {
            System.out.println("\nPago cancelado.");
        }
    }

    private void verEstadisticasFacturacion() {
        System.out.println("\n--- ESTADÍSTICAS DE FACTURACIÓN ---");
        double[] stats = facturaController.obtenerEstadisticasFacturacion(usuarioId);

        System.out.println("Total de facturas: " + (int) stats[0]);
        System.out.println("Facturas pendientes: " + (int) stats[1]);
        System.out.println("Facturas pagadas: " + (int) stats[2]);
        System.out.println("Facturas vencidas: " + (int) stats[3]);
        System.out.println("Monto total facturado: $" + String.format("%.2f", stats[4]));
        System.out.println("Monto adeudado: $" + String.format("%.2f", stats[5]));
    }

    private void verMontoAdeudado() {
        System.out.println("\n--- MONTO TOTAL ADEUDADO ---");
        double montoAdeudado = facturaController.calcularMontoAdeudado(usuarioId);

        if (montoAdeudado == 0) {
            System.out.println("✓ No tiene deudas pendientes. ¡Está al día!");
        } else {
            System.out.println("⚠️  Monto adeudado: $" + String.format("%.2f", montoAdeudado));

            List<Factura> pendientes = facturaController.obtenerFacturasPendientes(usuarioId);
            List<Factura> vencidas = facturaController.obtenerFacturasVencidas(usuarioId);

            if (!pendientes.isEmpty()) {
                System.out.println("\nFacturas pendientes: " + pendientes.size());
            }
            if (!vencidas.isEmpty()) {
                System.out.println("⚠️  Facturas vencidas: " + vencidas.size() + " (requieren atención urgente)");
            }
        }
    }
}
