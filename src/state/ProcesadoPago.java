package state;

import java.util.logging.Logger;

// Estado 7: Pago realizado con éxito
public class ProcesadoPago implements EstadoPedido {

    private static final Logger LOG = Logger.getLogger(ProcesadoPago.class.getName());

    @Override
    public String getNombre() { return "procesado_pago"; }

    @Override
    public String getDescripcion() {
        return "Pago realizado con exito. Ingrese los datos de envio.";
    }

    @Override
    public void manejar(Pedido pedido) {
        LOG.info("Pago confirmado. Por favor ingrese la direccion de envio.");
        pedido.setEstado(new DatosEnvio());
    }
}
