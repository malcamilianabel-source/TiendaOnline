package state;

import java.util.logging.Logger;

// Estado 5: Usuario ingresa los datos de pago
public class RegistroDatosPago implements EstadoPedido {

    private static final Logger LOG = Logger.getLogger(RegistroDatosPago.class.getName());

    @Override
    public String getNombre() { return "registro_datos_pago"; }

    @Override
    public String getDescripcion() {
        return "Ingresando datos de pago.";
    }

    @Override
    public void manejar(Pedido pedido) {
        if (pedido.isPagoExitoso()) {
            LOG.info("Pago procesado correctamente.");
            pedido.setEstado(new ProcesadoPago());
        } else {
            LOG.warning("Datos de pago incorrectos o saldo insuficiente.");
            pedido.setEstado(new IncorrectoPago());
        }
    }
}
