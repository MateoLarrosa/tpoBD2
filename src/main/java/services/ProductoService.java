package services;
import modelo.Producto;
import repositories.MongoRepository;

public class ProductoService {

	private static ProductoService instance;
	private ProductoService() {}
	
	public static ProductoService getInstance() {
		if(instance == null)
			instance = new ProductoService();
		return instance;
	}
	
	public void createProduct(String nombre, String categoria) {
		Producto p = new Producto(nombre, categoria);
		MongoRepository.getInstance().insertProduct(p);
	}
	
}
