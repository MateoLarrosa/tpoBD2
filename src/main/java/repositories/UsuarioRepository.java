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
import modelo.Usuario;

public class UsuarioRepository implements IRepository<Usuario> {

    private final MongoDatabase database;
    private static final String COLLECTION_NAME = "usuarios";

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
                doc.getString("rol"),
                new CuentaCorriente(doc.getDouble("cuentaCorriente"))
        );
    }

    @Override
    public InsertOneResult save(Usuario usuario) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        System.out.println("DEBUG: Preparing to save Usuario:");
        System.out.println("  nombre: " + usuario.getNombre());
        System.out.println("  email: " + usuario.getEmail());
        System.out.println("  password: " + (usuario.getPassword() != null ? "[REDACTED]" : null));
        System.out.println("  rol: " + usuario.getRol());
        System.out.println("  fechaRegistro: " + usuario.getFechaRegistro());
        System.out.println("  cuentaCorriente.saldo: " + (usuario.getCuentaCorriente() != null ? usuario.getCuentaCorriente().getSaldo() : null));
        System.out.println("  estado: " + (usuario.getEstado() != null ? usuario.getEstado().name() : null));

        Document doc = new Document("nombre", usuario.getNombre())
                .append("email", usuario.getEmail())
                .append("password", usuario.getPassword())
                .append("rol", usuario.getRol() != null ? usuario.getRol() : null)
                .append("fechaRegistro", usuario.getFechaRegistro())
                .append("cuentaCorriente", usuario.getCuentaCorriente() != null ? usuario.getCuentaCorriente().getSaldo() : null)
                .append("estado", usuario.getEstado() != null ? usuario.getEstado().name() : null);
                

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
