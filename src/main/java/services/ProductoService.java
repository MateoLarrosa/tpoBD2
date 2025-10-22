package services;
import repositories.MongoRepository;

public class ProductoService {

	private static ProductoService instance;
	private ProductoService() {}
	
	public static ProductoService getInstance() {
		if(instance == null)
			instance = new ProductoService();
		return instance;
	}
	
}
