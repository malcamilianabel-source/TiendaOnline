package state;

import java.util.logging.Logger;

// Estado 2: Producto(s) agregados al carrito
public class Reservado implements EstadoPedido {

    private static final Logger LOG = Logger.getLogger(Reservado.class.getName());

    @Override
    public String getNombre() { return "reservado"; }

    @Override
    public String getDescripcion() {
        return "Producto(s) en el carrito. Listo para proceder al pago.";
    }

    @Override
    public void manejar(Pedido pedido) {
        if (pedido.getCarrito().getPrecio() == 0) {
            LOG.info("El carrito esta vacio. Agregue productos primero.");
        } else {
            LOG.info(String.format("Carrito con total S/ %.2f. Procediendo al pago.",
                    pedido.getCarrito().getPrecio()));
            pedido.setEstado(new RegistroDatosPago());
        }
    }
}
