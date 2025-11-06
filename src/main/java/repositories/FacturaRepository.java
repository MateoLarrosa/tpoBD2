package repositories;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;

import modelo.EstadoFactura;
import modelo.EstadoSolicitud;
import modelo.Factura;
import modelo.Proceso;
import modelo.SolicitudProceso;

public class FacturaRepository implements IRepository<Factura> {

    private final MongoDatabase database;
    private static final String COLLECTION_NAME = "facturas";
    private static FacturaRepository instance;

    private FacturaRepository() {
        this.database = MongoConnectionManager.getInstance().getDatabase();
    }

    public static FacturaRepository getInstance() {
        if (instance == null) {
            instance = new FacturaRepository();
        }
        return instance;
    }

    private Factura mapDocumentToFactura(Document doc) {
        Factura factura = new Factura();
        factura.setId(doc.getObjectId("_id").toString());
        factura.setUsuarioId(doc.getString("usuarioId"));

        Date fechaEmisionDate = doc.getDate("fechaEmision");
        if (fechaEmisionDate != null) {
            factura.setFechaEmision(fechaEmisionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }

        List<Document> solicitudesDocs = doc.getList("solicitudesFacturadas", Document.class);
        List<SolicitudProceso> solicitudes = new ArrayList<>();
        if (solicitudesDocs != null) {
            for (Document solDoc : solicitudesDocs) {
                SolicitudProceso solicitud = mapDocumentToSolicitud(solDoc);
                solicitudes.add(solicitud);
            }
        }
        factura.setSolicitudesFacturadas(solicitudes);

        String estadoStr = doc.getString("estado");
        factura.setEstado(estadoStr != null ? EstadoFactura.valueOf(estadoStr) : EstadoFactura.PENDIENTE);

        factura.setMontoTotal(doc.getDouble("montoTotal"));

        Date fechaVencimientoDate = doc.getDate("fechaVencimiento");
        if (fechaVencimientoDate != null) {
            factura.setFechaVencimiento(fechaVencimientoDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }

        return factura;
    }

    private SolicitudProceso mapDocumentToSolicitud(Document doc) {
        SolicitudProceso solicitud = new SolicitudProceso();
        solicitud.setId(doc.getString("id"));
        solicitud.setUsuarioId(doc.getString("usuarioId"));

        Document procesoDoc = doc.get("proceso", Document.class);
        if (procesoDoc != null) {
            Proceso proceso = new Proceso();
            proceso.setId(procesoDoc.getString("id"));
            proceso.setNombre(procesoDoc.getString("nombre"));
            proceso.setDescripcion(procesoDoc.getString("descripcion"));
            proceso.setTipoProceso(procesoDoc.getString("tipoProceso"));
            proceso.setCosto(procesoDoc.getInteger("costo", 0));
            solicitud.setProceso(proceso);
        }

        Date fechaSolicitud = doc.getDate("fechaSolicitud");
        if (fechaSolicitud != null) {
            solicitud.setFechaSolicitud(fechaSolicitud.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }

        String estadoStr = doc.getString("estado");
        if (estadoStr != null) {
            solicitud.setEstado(EstadoSolicitud.valueOf(estadoStr));
        }

        Document parametrosDoc = doc.get("parametros", Document.class);
        if (parametrosDoc != null) {
            solicitud.setParametros(new java.util.HashMap<>(parametrosDoc));
        }

        solicitud.setResultado(doc.getString("resultado"));

        Long tiempoEjecucion = doc.getLong("tiempoEjecucionMs");
        if (tiempoEjecucion != null) {
            solicitud.setTiempoEjecucionMs(tiempoEjecucion);
        }

        Date fechaCompletado = doc.getDate("fechaCompletado");
        if (fechaCompletado != null) {
            solicitud.setFechaCompletado(fechaCompletado.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }

        return solicitud;
    }

    @Override
    public InsertOneResult save(Factura factura) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        List<Document> solicitudesDocs = new ArrayList<>();
        if (factura.getSolicitudesFacturadas() != null) {
            for (SolicitudProceso solicitud : factura.getSolicitudesFacturadas()) {
                solicitudesDocs.add(mapSolicitudToDocument(solicitud));
            }
        }

        Document doc = new Document("usuarioId", factura.getUsuarioId())
                .append("fechaEmision", Date.from(factura.getFechaEmision().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .append("solicitudesFacturadas", solicitudesDocs)
                .append("estado", factura.getEstado().name())
                .append("montoTotal", factura.getMontoTotal())
                .append("fechaVencimiento", Date.from(factura.getFechaVencimiento().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        InsertOneResult result = collection.insertOne(doc);
        if (result != null && result.getInsertedId() != null && result.getInsertedId().isObjectId()) {
            factura.setId(result.getInsertedId().asObjectId().getValue().toString());
        }

        return result;
    }

    private Document mapSolicitudToDocument(SolicitudProceso solicitud) {
        Document procesoDoc = null;
        if (solicitud.getProceso() != null) {
            Proceso proceso = solicitud.getProceso();
            procesoDoc = new Document("id", proceso.getId())
                    .append("nombre", proceso.getNombre())
                    .append("descripcion", proceso.getDescripcion())
                    .append("tipoProceso", proceso.getTipoProceso())
                    .append("costo", proceso.getCosto());
        }

        Date fechaSolicitud = null;
        if (solicitud.getFechaSolicitud() != null) {
            fechaSolicitud = Date.from(solicitud.getFechaSolicitud().atZone(ZoneId.systemDefault()).toInstant());
        }

        Date fechaCompletado = null;
        if (solicitud.getFechaCompletado() != null) {
            fechaCompletado = Date.from(solicitud.getFechaCompletado().atZone(ZoneId.systemDefault()).toInstant());
        }

        return new Document("id", solicitud.getId())
                .append("usuarioId", solicitud.getUsuarioId())
                .append("proceso", procesoDoc)
                .append("fechaSolicitud", fechaSolicitud)
                .append("estado", solicitud.getEstado().name())
                .append("parametros", solicitud.getParametros() != null ? new Document(solicitud.getParametros()) : null)
                .append("resultado", solicitud.getResultado())
                .append("tiempoEjecucionMs", solicitud.getTiempoEjecucionMs())
                .append("fechaCompletado", fechaCompletado);
    }

    @Override
    public Factura findById(String id) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? mapDocumentToFactura(doc) : null;
    }

    @Override
    public List<Factura> findAll() {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<Factura> facturas = new ArrayList<>();
        collection.find().forEach(doc -> facturas.add(mapDocumentToFactura(doc)));
        return facturas;
    }

    public void update(Factura factura) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        List<Document> solicitudesDocs = new ArrayList<>();
        if (factura.getSolicitudesFacturadas() != null) {
            for (SolicitudProceso solicitud : factura.getSolicitudesFacturadas()) {
                solicitudesDocs.add(mapSolicitudToDocument(solicitud));
            }
        }

        Document updateDoc = new Document("usuarioId", factura.getUsuarioId())
                .append("fechaEmision", Date.from(factura.getFechaEmision().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .append("solicitudesFacturadas", solicitudesDocs)
                .append("estado", factura.getEstado().name())
                .append("montoTotal", factura.getMontoTotal())
                .append("fechaVencimiento", Date.from(factura.getFechaVencimiento().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        collection.updateOne(
                Filters.eq("_id", new ObjectId(factura.getId())),
                new Document("$set", updateDoc)
        );
    }

    @Override
    public void delete(String id) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }

    public List<Factura> findByUsuarioId(String usuarioId) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<Factura> facturas = new ArrayList<>();
        collection.find(Filters.eq("usuarioId", usuarioId))
                .sort(new Document("fechaEmision", -1))
                .forEach(doc -> facturas.add(mapDocumentToFactura(doc)));
        return facturas;
    }

    public List<Factura> findByEstado(EstadoFactura estado) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<Factura> facturas = new ArrayList<>();
        collection.find(Filters.eq("estado", estado.name()))
                .sort(new Document("fechaEmision", -1))
                .forEach(doc -> facturas.add(mapDocumentToFactura(doc)));
        return facturas;
    }

    public List<Factura> findByUsuarioIdYEstado(String usuarioId, EstadoFactura estado) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<Factura> facturas = new ArrayList<>();
        collection.find(Filters.and(
                Filters.eq("usuarioId", usuarioId),
                Filters.eq("estado", estado.name())
        ))
                .sort(new Document("fechaEmision", -1))
                .forEach(doc -> facturas.add(mapDocumentToFactura(doc)));
        return facturas;
    }

    public List<Factura> findFacturasVencidas() {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<Factura> facturas = new ArrayList<>();
        Date hoy = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        collection.find(Filters.and(
                Filters.eq("estado", EstadoFactura.PENDIENTE.name()),
                Filters.lt("fechaVencimiento", hoy)
        ))
                .sort(new Document("fechaEmision", -1))
                .forEach(doc -> facturas.add(mapDocumentToFactura(doc)));
        return facturas;
    }

    public List<Factura> findByUsuarioIdYRangoFechas(String usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<Factura> facturas = new ArrayList<>();

        Date inicio = Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date fin = Date.from(fechaFin.atStartOfDay(ZoneId.systemDefault()).toInstant());

        collection.find(Filters.and(
                Filters.eq("usuarioId", usuarioId),
                Filters.gte("fechaEmision", inicio),
                Filters.lte("fechaEmision", fin)
        ))
                .sort(new Document("fechaEmision", -1))
                .forEach(doc -> facturas.add(mapDocumentToFactura(doc)));
        return facturas;
    }
}
