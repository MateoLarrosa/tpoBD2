package repositories;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;

import modelo.CuentaCorriente;
import modelo.EstadoUsuario;
import modelo.Rol;
import modelo.Usuario;

public class UsuarioRepository implements IRepository<Usuario> {

    private final MongoDatabase database;
    private static final String COLLECTION_NAME = "Usuario";

    public UsuarioRepository() {
        this.database = MongoConnectionManager.getInstance().getDatabase();
    }

    private Usuario mapDocumentToUsuario(Document doc) {
        return new Usuario(
                doc.getObjectId("_id").toString(),
                doc.getString("nombre"),
                doc.getString("email"),
                doc.getString("password"),
                EstadoUsuario.valueOf(doc.getString("estado")),
                new Rol(null, doc.getString("rol")),
                new CuentaCorriente(doc.getDouble("cuentaCorriente"))
        );
    }

    @Override
    public InsertOneResult save(Usuario usuario) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        
        // Debug: Verificar valores antes de crear el documento
        System.out.println("=== DEBUG USUARIO ===");
        System.out.println("Nombre: " + usuario.getNombre());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Password: " + usuario.getPassword());
        System.out.println("Rol: " + (usuario.getRol() != null ? usuario.getRol().getNombre() : "NULL"));
        System.out.println("Estado: " + (usuario.getEstado() != null ? usuario.getEstado().name() : "NULL"));
        System.out.println("FechaRegistro: " + (usuario.getFechaRegistro() != null ? usuario.getFechaRegistro().toString() : "NULL"));
        System.out.println("CuentaCorriente: " + (usuario.getCuentaCorriente() != null ? usuario.getCuentaCorriente().getSaldo() : "NULL"));
        System.out.println("===================");
        
        Document doc = new Document("nombre", usuario.getNombre())
                .append("email", usuario.getEmail())
                .append("password", usuario.getPassword());
                
        // Agregar campos solo si no son null
        if (usuario.getRol() != null && usuario.getRol().getNombre() != null) {
            doc.append("rol", usuario.getRol().getNombre());
        }
        if (usuario.getEstado() != null) {
            doc.append("estado", usuario.getEstado().name());
        }
        if (usuario.getFechaRegistro() != null) {
            doc.append("fechaRegistro", usuario.getFechaRegistro().toString());
        }
        if (usuario.getCuentaCorriente() != null) {
            doc.append("cuentaCorriente", usuario.getCuentaCorriente().getSaldo());
        }
        
        System.out.println("Documento a guardar: " + doc.toJson());
        
        InsertOneResult result = collection.insertOne(doc);
        if (result != null && result.getInsertedId() != null && result.getInsertedId().isObjectId()) {
            usuario.setId(result.getInsertedId().asObjectId().getValue().toString());
        }
        return result;
    }

    @Override
    public Usuario findById(String id) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? mapDocumentToUsuario(doc) : null;
    }

    @Override
    public List<Usuario> findAll() {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        List<Usuario> usuarios = new ArrayList<>();
        for (Document doc : collection.find()) {
            usuarios.add(mapDocumentToUsuario(doc));
        }
        return usuarios;
    }

    @Override
    public void delete(String id) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }
}