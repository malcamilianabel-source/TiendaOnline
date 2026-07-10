package state;

import java.util.logging.Logger;

// Estado 4: Usuario se registró o inició sesión correctamente
public class ProcesadoRegistro implements EstadoPedido {

    private static final Logger LOG = Logger.getLogger(ProcesadoRegistro.class.getName());

    @Override
    public String getNombre() { return "procesado_registro"; }

    @Override
    public String getDescripcion() {
        return "Sesion activa. El usuario puede agregar productos al carrito.";
    }

    @Override
    public void manejar(Pedido pedido) {
        LOG.info("Sesion activa. Puede agregar productos al carrito.");
        pedido.setEstado(new Reservado());
    }
}
