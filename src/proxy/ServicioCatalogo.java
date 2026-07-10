package proxy;

import clase.Articulo;
import state.Pedido;
import java.util.List;

// PATRÓN PROXY - Interfaz común para el real y el proxy
public interface ServicioCatalogo {
    List<Articulo> verProductos();
    boolean agregarAlCarrito(Articulo articulo, Pedido pedido);
    boolean procesarPago(Pedido pedido, String metodoPago);
}
