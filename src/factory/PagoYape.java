package factory;

import java.util.logging.Logger;

class PagoYape implements MetodoPago {

    private static final Logger LOG = Logger.getLogger(PagoYape.class.getName());

    @Override
    public void procesarPago(double monto) {
        LOG.info(String.format("Pago con YAPE procesado. Monto: S/ %.2f", monto));
    }
}
