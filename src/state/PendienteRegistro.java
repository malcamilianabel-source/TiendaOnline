package state;

import java.util.logging.Logger;

// Estado 3: Usuario quiere pagar pero aún no ha iniciado sesión
public class PendienteRegistro implements EstadoPedido {

    private static final Logger LOG = Logger.getLogger(PendienteRegistro.class.getName());

    @Override
    public String getNombre() { return "pendiente_registro"; }

    @Override
    public String getDescripcion() {
        return "Esperando que el usuario inicie sesion o se registre.";
    }

    @Override
    public void manejar(Pedido pedido) {
        if (pedido.getUsuario() != null) {
            LOG.info("Sesion iniciada. Bienvenido, " + pedido.getUsuario().getNombreCompleto());
            pedido.setEstado(new ProcesadoRegistro());
        } else {
            LOG.info("Aun no hay usuario. Ingrese sus credenciales.");
        }
    }
}
