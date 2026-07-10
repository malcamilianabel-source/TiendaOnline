package factory;

import java.util.logging.Logger;

class PagoPaypal implements MetodoPago {

    private static final Logger LOG = Logger.getLogger(PagoPaypal.class.getName());

    @Override
    public void procesarPago(double monto) {
        LOG.info(String.format("Pago con PAYPAL procesado. Monto: S/ %.2f", monto));
    }
}
