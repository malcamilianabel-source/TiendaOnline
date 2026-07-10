package state;

import java.util.logging.Logger;

// Estado 8: Usuario ingresa la dirección de envío
public class DatosEnvio implements EstadoPedido {

    private static final Logger LOG = Logger.getLogger(DatosEnvio.class.getName());

    @Override
    public String getNombre() { return "datos_envio"; }

    @Override
    public String getDescripcion() {
        return "Ingresando direccion de envio.";
    }

    @Override
    public void manejar(Pedido pedido) {
        if (pedido.getDireccionEnvio() == null || pedido.getDireccionEnvio().isBlank()) {
            LOG.warning("Debe ingresar una direccion de envio valida.");
        } else {
            LOG.info("Direccion registrada: " + pedido.getDireccionEnvio());
            pedido.setEstado(new PendienteEnvio());
        }
    }
}
