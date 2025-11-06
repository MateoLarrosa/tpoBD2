package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import modelo.EstadoFactura;
import modelo.EstadoSolicitud;
import modelo.Factura;
import modelo.MedicionesPorCiudad;
import modelo.MedicionesPorPais;
import modelo.MedicionesPorZona;
import modelo.SolicitudProceso;
import repositories.FacturaRepository;

public class FacturaService {

    private static FacturaService instance;
    private final FacturaRepository facturaRepository;
    private final SolicitudProcesoService solicitudProcesoService;
    private final MedicionesService medicionesService;

    private FacturaService() {
        this.facturaRepository = FacturaRepository.getInstance();
        this.solicitudProcesoService = SolicitudProcesoService.getInstance();
        this.medicionesService = MedicionesService.getInstance();
    }

    public static FacturaService getInstance() {
        if (instance == null) {
            instance = new FacturaService();
        }
        return instance;
    }

    public Factura generarFacturaParaUsuario(String usuarioId, int diasVencimiento) {

        // 1. Buscar solicitudes pendientes
        List<SolicitudProceso> solicitudesPendientes = solicitudProcesoService
                .obtenerSolicitudesPorUsuarioYEstado(usuarioId, EstadoSolicitud.PENDIENTE);

        if (solicitudesPendientes.isEmpty()) {
            System.out.println("No hay solicitudes pendientes para facturar.");
            return null;
        }

        double montoTotal = 0.0;

        for (SolicitudProceso solicitud : solicitudesPendientes) {
            String ciudad = null;
            String zona = null;
            String pais = null;
            java.util.Date fechaInicio = null;
            java.util.Date fechaFin = null;
            if (solicitud.getParametros() != null) {
                if (solicitud.getParametros().get("ciudad") != null) {
                    ciudad = solicitud.getParametros().get("ciudad").toString();
                }
                if (solicitud.getParametros().get("zona") != null) {
                    zona = solicitud.getParametros().get("zona").toString();
                }
                if (solicitud.getParametros().get("pais") != null) {
                    pais = solicitud.getParametros().get("pais").toString();
                }
                if (solicitud.getParametros().get("fechaInicio") != null) {
                    Object fechaIniObj = solicitud.getParametros().get("fechaInicio");
                    if (fechaIniObj instanceof java.util.Date) {
                        fechaInicio = (java.util.Date) fechaIniObj;
                    } else if (fechaIniObj instanceof String) {
                        try {
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                            fechaInicio = sdf.parse((String) fechaIniObj);
                        } catch (Exception e) {
                            fechaInicio = null;
                        }
                    }
                }
                if (solicitud.getParametros().get("fechaFin") != null) {
                    Object fechaFinObj = solicitud.getParametros().get("fechaFin");
                    if (fechaFinObj instanceof java.util.Date) {
                        fechaFin = (java.util.Date) fechaFinObj;
                    } else if (fechaFinObj instanceof String) {
                        try {
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                            fechaFin = sdf.parse((String) fechaFinObj);
                        } catch (Exception e) {
                            fechaFin = null;
                        }
                    }
                }
            }

            // Soportar tanto 'tipo' como 'tipoMedicion' como clave
            String tipo = "";
            if (solicitud.getParametros() != null) {
                if (solicitud.getParametros().get("tipo") != null) {
                    tipo = solicitud.getParametros().get("tipo").toString();
                } else if (solicitud.getParametros().get("tipoMedicion") != null) {
                    tipo = solicitud.getParametros().get("tipoMedicion").toString();
                }
            }
            double montoSolicitud = 0.0;
            String nombreProceso = "";
            String resultadoMedicion = null;
            // Detectar tipo de informe por el nombre del proceso
            String nombreOriginal = solicitud.getProceso() != null ? solicitud.getProceso().getNombre() : "";
            boolean esMax = nombreOriginal != null && nombreOriginal.toUpperCase().contains("MAX");
            boolean esMin = nombreOriginal != null && nombreOriginal.toUpperCase().contains("MIN");
            boolean esProm = nombreOriginal != null && nombreOriginal.toUpperCase().contains("PROMEDIO");

            List<MedicionesPorCiudad> medicionesCiudad = null;
            List<MedicionesPorZona> medicionesZona = null;
            List<MedicionesPorPais> medicionesPais = null;

            if (ciudad != null && fechaInicio != null && fechaFin != null) {
                medicionesCiudad = medicionesService.obtenerMedicionesPorCiudadYRango(ciudad, tipo, fechaInicio, fechaFin);
                montoSolicitud = medicionesCiudad.stream().mapToDouble(m -> m.monto).sum();
                if (esMax) {
                    Double max = medicionesService.obtenerMaximoPorCiudad(ciudad, tipo, fechaInicio, fechaFin);
                    resultadoMedicion = "Máximo: " + (max.isNaN() ? "Sin datos" : max);
                } else if (esMin) {
                    Double min = medicionesService.obtenerMinimoPorCiudad(ciudad, tipo, fechaInicio, fechaFin);
                    resultadoMedicion = "Mínimo: " + (min.isNaN() ? "Sin datos" : min);
                } else if (esProm) {
                    double prom = medicionesCiudad.isEmpty() ? 0.0 : medicionesCiudad.stream().mapToDouble(m -> m.valor).average().orElse(0.0);
                    resultadoMedicion = "Promedio: " + (medicionesCiudad.isEmpty() ? "Sin datos" : prom);
                } else {
                    resultadoMedicion = "Total: " + montoSolicitud;
                }
                nombreProceso = "MEDICIONES_CIUDAD_" + ciudad + (!tipo.isEmpty() ? ("_" + tipo) : "");
            } else if (zona != null && fechaInicio != null && fechaFin != null) {
                medicionesZona = medicionesService.obtenerMedicionesPorZonaYRango(zona, tipo, fechaInicio, fechaFin);
                montoSolicitud = medicionesZona.stream().mapToDouble(m -> m.monto).sum();
                if (esMax) {
                    Double max = medicionesService.obtenerMaximoPorZona(zona, tipo, fechaInicio, fechaFin);
                    resultadoMedicion = "Máximo: " + (max.isNaN() ? "Sin datos" : max);
                } else if (esMin) {
                    Double min = medicionesService.obtenerMinimoPorZona(zona, tipo, fechaInicio, fechaFin);
                    resultadoMedicion = "Mínimo: " + (min.isNaN() ? "Sin datos" : min);
                } else if (esProm) {
                    double prom = medicionesZona.isEmpty() ? 0.0 : medicionesZona.stream().mapToDouble(m -> m.valor).average().orElse(0.0);
                    resultadoMedicion = "Promedio: " + (medicionesZona.isEmpty() ? "Sin datos" : prom);
                } else {
                    resultadoMedicion = "Total: " + montoSolicitud;
                }
                nombreProceso = "MEDICIONES_ZONA_" + zona + (!tipo.isEmpty() ? ("_" + tipo) : "");
            } else if (pais != null && fechaInicio != null && fechaFin != null) {
                medicionesPais = medicionesService.obtenerMedicionesPorPaisYRango(pais, tipo, fechaInicio, fechaFin);
                montoSolicitud = medicionesPais.stream().mapToDouble(m -> m.monto).sum();
                if (esMax) {
                    Double max = medicionesService.obtenerMaximoPorPais(pais, tipo, fechaInicio, fechaFin);
                    resultadoMedicion = "Máximo: " + (max.isNaN() ? "Sin datos" : max);
                } else if (esMin) {
                    Double min = medicionesService.obtenerMinimoPorPais(pais, tipo, fechaInicio, fechaFin);
                    resultadoMedicion = "Mínimo: " + (min.isNaN() ? "Sin datos" : min);
                } else if (esProm) {
                    double prom = medicionesPais.isEmpty() ? 0.0 : medicionesPais.stream().mapToDouble(m -> m.valor).average().orElse(0.0);
                    resultadoMedicion = "Promedio: " + (medicionesPais.isEmpty() ? "Sin datos" : prom);
                } else {
                    resultadoMedicion = "Total: " + montoSolicitud;
                }
                nombreProceso = "MEDICIONES_PAIS_" + pais + (!tipo.isEmpty() ? ("_" + tipo) : "");
            } else {
                nombreProceso = "PROCESO_PERSONALIZADO";
            }
            montoTotal += montoSolicitud;
            if (solicitud.getProceso() != null) {
                solicitud.getProceso().setNombre(nombreProceso);
                solicitud.getProceso().setCosto((int) Math.round(montoSolicitud));
            }
            // Guardar el resultado de la medición en la solicitud
            if (resultadoMedicion != null) {
                solicitud.setResultado(resultadoMedicion);
            }
            // Agregar monto y valor de medición a los parámetros para mostrar en UI y factura
            if (solicitud.getParametros() != null) {
                solicitud.getParametros().put("monto", montoSolicitud);
                // Si es promedio, min o max, guardar el valor numérico también
                if (resultadoMedicion != null && (resultadoMedicion.startsWith("Promedio") || resultadoMedicion.startsWith("Máximo") || resultadoMedicion.startsWith("Mínimo"))) {
                    String[] partes = resultadoMedicion.split(":");
                    if (partes.length == 2) {
                        try {
                            double valor = Double.parseDouble(partes[1].trim());
                            solicitud.getParametros().put("valorMedicion", valor);
                        } catch (NumberFormatException e) {
                            // No es un número, no guardar
                        }
                    }
                }
            }
            // Cambiar estado a COMPLETADO y actualizar en base
            solicitud.setEstado(EstadoSolicitud.COMPLETADO);
            solicitudProcesoService.updateSolicitud(solicitud);
        }

        // Antes de guardar la factura, asegurarse de que el resultado de cada solicitud esté presente
        for (SolicitudProceso solicitud : solicitudesPendientes) {
            if (solicitud.getResultado() == null || solicitud.getResultado().isEmpty()) {
                solicitud.setResultado("Sin resultado de medición");
            }
        }

        Factura factura = new Factura();
        factura.setUsuarioId(usuarioId);
        factura.setFechaEmision(LocalDate.now());
        factura.setSolicitudesFacturadas(solicitudesPendientes);
        factura.setEstado(EstadoFactura.PENDIENTE);
        factura.setMontoTotal(montoTotal);
        factura.setFechaVencimiento(LocalDate.now().plusDays(diasVencimiento));

        facturaRepository.save(factura);
        return factura;
    }

    public Factura generarFacturaParaUsuarioPorPeriodo(String usuarioId, LocalDate fechaInicio,
            LocalDate fechaFin, int diasVencimiento) {

        java.time.LocalDateTime fechaInicioTime = fechaInicio.atStartOfDay();
        java.time.LocalDateTime fechaFinTime = fechaFin.atTime(23, 59, 59);

        List<SolicitudProceso> solicitudes = solicitudProcesoService
                .obtenerSolicitudesPorUsuarioYRango(usuarioId, fechaInicioTime, fechaFinTime);

        List<SolicitudProceso> solicitudesCompletadas = solicitudes.stream()
                .filter(s -> s.getEstado() == EstadoSolicitud.COMPLETADO)
                .toList();

        if (solicitudesCompletadas.isEmpty()) {
            System.out.println("No hay solicitudes completadas en el período para facturar.");
            return null;
        }

        double montoTotal = 0.0;

        for (SolicitudProceso solicitud : solicitudesCompletadas) {

            if (solicitud.getProceso() != null) {
                montoTotal += solicitud.getProceso().getCosto();
            }
        }

        Factura factura = new Factura();
        factura.setUsuarioId(usuarioId);
        factura.setFechaEmision(LocalDate.now());
        factura.setSolicitudesFacturadas(solicitudesCompletadas);
        factura.setEstado(EstadoFactura.PENDIENTE);
        factura.setMontoTotal(montoTotal);
        factura.setFechaVencimiento(LocalDate.now().plusDays(diasVencimiento));

        facturaRepository.save(factura);
        return factura;
    }

    public void marcarFacturaComoPagada(String facturaId) {
        Factura factura = facturaRepository.findById(facturaId);
        if (factura != null) {
            factura.marcarComoPagada();
            facturaRepository.update(factura);
        }
    }

    public void actualizarFacturasVencidas() {
        List<Factura> facturasVencidas = facturaRepository.findFacturasVencidas();
        for (Factura factura : facturasVencidas) {
            factura.marcarComoVencida();
            facturaRepository.update(factura);
        }
        System.out.println("Se actualizaron " + facturasVencidas.size() + " facturas vencidas.");
    }

    public List<Factura> obtenerFacturasPorUsuario(String usuarioId) {
        return facturaRepository.findByUsuarioId(usuarioId);
    }

    public List<Factura> obtenerFacturasPorEstado(EstadoFactura estado) {
        return facturaRepository.findByEstado(estado);
    }

    public List<Factura> obtenerFacturasPendientesPorUsuario(String usuarioId) {
        return facturaRepository.findByUsuarioIdYEstado(usuarioId, EstadoFactura.PENDIENTE);
    }

    public List<Factura> obtenerFacturasPagadasPorUsuario(String usuarioId) {
        return facturaRepository.findByUsuarioIdYEstado(usuarioId, EstadoFactura.PAGADA);
    }

    public List<Factura> obtenerFacturasVencidasPorUsuario(String usuarioId) {
        return facturaRepository.findByUsuarioIdYEstado(usuarioId, EstadoFactura.VENCIDA);
    }

    public Factura obtenerFacturaPorId(String facturaId) {
        return facturaRepository.findById(facturaId);
    }

    public double calcularMontoAdeudadoPorUsuario(String usuarioId) {
        List<Factura> pendientes = obtenerFacturasPendientesPorUsuario(usuarioId);
        List<Factura> vencidas = obtenerFacturasVencidasPorUsuario(usuarioId);

        double montoPendiente = pendientes.stream().mapToDouble(Factura::getMontoTotal).sum();
        double montoVencido = vencidas.stream().mapToDouble(Factura::getMontoTotal).sum();

        return montoPendiente + montoVencido;
    }

    public double[] obtenerEstadisticasFacturacionPorUsuario(String usuarioId) {
        List<Factura> todasFacturas = obtenerFacturasPorUsuario(usuarioId);

        long totalFacturas = todasFacturas.size();
        long pendientes = todasFacturas.stream().filter(f -> f.getEstado() == EstadoFactura.PENDIENTE).count();
        long pagadas = todasFacturas.stream().filter(f -> f.getEstado() == EstadoFactura.PAGADA).count();
        long vencidas = todasFacturas.stream().filter(f -> f.getEstado() == EstadoFactura.VENCIDA).count();

        double montoTotalFacturado = todasFacturas.stream().mapToDouble(Factura::getMontoTotal).sum();
        double montoAdeudado = calcularMontoAdeudadoPorUsuario(usuarioId);

        return new double[]{totalFacturas, pendientes, pagadas, vencidas, montoTotalFacturado, montoAdeudado};
    }

    public List<SolicitudProceso> obtenerDetalleProcesosDeLaFactura(String facturaId) {
        Factura factura = facturaRepository.findById(facturaId);
        if (factura == null) {
            return new ArrayList<>();
        }

        return factura.getSolicitudesFacturadas();
    }

    public void eliminarFactura(String facturaId) {
        facturaRepository.delete(facturaId);
    }
}
