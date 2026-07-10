package factory;

import java.util.logging.Logger;

class PagoTarjeta implements MetodoPago {

    private static final Logger LOG = Logger.getLogger(PagoTarjeta.class.getName());

    @Override
    public void procesarPago(double monto) {
        LOG.info(String.format("Pago con TARJETA procesado. Monto: S/ %.2f", monto));
    }
}
