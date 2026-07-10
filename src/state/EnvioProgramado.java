package state;

import java.util.logging.Logger;

// Estado 11: Estado final — envío programado
public class EnvioProgramado implements EstadoPedido {

    private static final Logger LOG = Logger.getLogger(EnvioProgramado.class.getName());

    @Override
    public String getNombre() { return "envio_programado"; }

    @Override
    public String getDescripcion() {
        return "Envio programado. Se notifico al usuario la fecha estimada de llegada.";
    }

    @Override
    public void manejar(Pedido pedido) {
        LOG.info("Su pedido llegara el: " + pedido.getFechaEstimadaEntrega());
        LOG.info("Gracias por su compra. Pedido completado.");
    }
}
