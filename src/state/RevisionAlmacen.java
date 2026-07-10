package state;

import java.util.logging.Logger;

// Estado 10: El almacenero revisa el pedido y modifica el stock
public class RevisionAlmacen implements EstadoPedido {

    private static final Logger LOG = Logger.getLogger(RevisionAlmacen.class.getName());

    @Override
    public String getNombre() { return "revision_almacen"; }

    @Override
    public String getDescripcion() {
        return "Almacenero revisando stock y preparando el pedido.";
    }

    @Override
    public void manejar(Pedido pedido) {
        LOG.info("Stock verificado y descontado. Programando fecha de envio...");
        java.time.LocalDate fechaEntrega = java.time.LocalDate.now().plusDays(3);
        pedido.setFechaEstimadaEntrega(fechaEntrega.toString());
        pedido.setEstado(new EnvioProgramado());
    }
}
