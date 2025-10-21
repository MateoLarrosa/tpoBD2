package controlador;

import services.ProductoService;

public class ProductController {
	
	public void agregarProducto(String nombre, String categoria) {
		ProductoService.getInstance().createProduct(nombre, categoria);
	}

}
