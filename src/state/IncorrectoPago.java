package state;

import java.util.logging.Logger;

// Estado 6: Falló el pago
public class IncorrectoPago implements EstadoPedido {

    private static final Logger LOG = Logger.getLogger(IncorrectoPago.class.getName());

    @Override
    public String getNombre() { return "incorrecto_pago"; }

    @Override
    public String getDescripcion() {
        return "El pago fallo. Puede reintentar con otro metodo o corregir los datos.";
    }

    @Override
    public void manejar(Pedido pedido) {
        LOG.warning("Reintentando pago. Ingrese nuevos datos.");
        pedido.setPagoExitoso(false);
        pedido.setEstado(new RegistroDatosPago());
    }
}
