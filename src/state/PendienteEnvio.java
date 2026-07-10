package state;

import java.util.logging.Logger;

// Estado 9: Compra realizada, solicitud de envío enviada al almacén
public class PendienteEnvio implements EstadoPedido {

    private static final Logger LOG = Logger.getLogger(PendienteEnvio.class.getName());

    @Override
    public String getNombre() { return "pendiente_envio"; }

    @Override
    public String getDescripcion() {
        return "Compra confirmada. Solicitud enviada al almacen. En espera de revision.";
    }

    @Override
    public void manejar(Pedido pedido) {
        LOG.info("El almacen recibio la solicitud. Iniciando revision de stock.");
        pedido.setEstado(new RevisionAlmacen());
    }
}
