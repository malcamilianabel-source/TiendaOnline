package proxy;

import clase.Articulo;
import conexionBD.PedidoDB;
import factory.Fabrica;
import factory.MetodoPago;
import state.Pedido;
import tiendaonline.TiendaVirtual;
import java.util.List;
import java.util.logging.Logger;

// PATRÓN PROXY - Objeto real: ejecuta las operaciones verdaderas
public class CatalogoReal implements ServicioCatalogo {

    private static final Logger LOG = Logger.getLogger(CatalogoReal.class.getName());
    private final TiendaVirtual tienda = TiendaVirtual.getInstancia();

    @Override
    public List<Articulo> verProductos() {
        return tienda.getCatalogo();
    }

    @Override
    public boolean agregarAlCarrito(Articulo articulo, Pedido pedido) {
        pedido.agregarAlCarrito(articulo);
        LOG.info("Producto agregado: " + articulo.getNombre()
                + " (S/ " + String.format("%.2f", articulo.getPrecio()) + ")");
        return true;
    }

    @Override
    public boolean procesarPago(Pedido pedido, String metodoPago) {
        MetodoPago pago = Fabrica.crearMetodoPago(metodoPago);
        if (pago != null) {
            pago.procesarPago(pedido.getMontoTotal());
            pedido.setPagoExitoso(true);

            PedidoDB pedidoDB = new PedidoDB();
            int idGenerado = pedidoDB.guardarPedido(pedido);
            if (idGenerado > 0) {
                pedido.setIdPedidoBD(idGenerado);
            } else {
                LOG.warning("El pago fue exitoso pero no se pudo guardar en la BD.");
            }
            return true;
        }
        pedido.setPagoExitoso(false);
        return false;
    }
}
