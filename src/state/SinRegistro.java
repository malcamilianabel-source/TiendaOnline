package state;

import java.util.logging.Logger;

// Estado 1: Usuario navega el catálogo sin haber iniciado sesión
public class SinRegistro implements EstadoPedido {

    private static final Logger LOG = Logger.getLogger(SinRegistro.class.getName());

    @Override
    public String getNombre() { return "sin_registro"; }

    @Override
    public String getDescripcion() {
        return "Navegando el catalogo sin sesion iniciada.";
    }

    @Override
    public void manejar(Pedido pedido) {
        LOG.info("Para comprar debe iniciar sesion o registrarse.");
        pedido.setEstado(new PendienteRegistro());
    }
}
