package proxy;

import clase.Articulo;
import state.Pedido;
import state.PendienteRegistro;
import java.util.List;
import java.util.logging.Logger;

// PATRÓN PROXY - controla el acceso según el estado del pedido
public class CatalogoProxy implements ServicioCatalogo {

    private static final Logger LOG = Logger.getLogger(CatalogoProxy.class.getName());
    private final CatalogoReal catalogoReal;

    public CatalogoProxy() {
        this.catalogoReal = new CatalogoReal();
    }

    @Override
    public List<Articulo> verProductos() {
        LOG.info("Acceso al catalogo permitido.");
        return catalogoReal.verProductos();
    }

    @Override
    public boolean agregarAlCarrito(Articulo articulo, Pedido pedido) {
        String estado = pedido.getNombreEstado();

        if (estado.equals("sin_registro") || estado.equals("pendiente_registro")) {
            LOG.warning("ACCESO DENEGADO: debe iniciar sesion para agregar productos.");
            pedido.setEstado(new PendienteRegistro());
            return false;
        }

        LOG.info("Acceso permitido. Agregando al carrito...");
        return catalogoReal.agregarAlCarrito(articulo, pedido);
    }

    @Override
    public boolean procesarPago(Pedido pedido, String metodoPago) {
        String estado = pedido.getNombreEstado();

        if (!estado.equals("registro_datos_pago")) {
            LOG.warning("ACCESO DENEGADO: no esta en la etapa de pago. Estado actual: " + estado);
            return false;
        }

        if (metodoPago == null || metodoPago.isBlank()) {
            LOG.warning("ACCESO DENEGADO: metodo de pago no especificado.");
            return false;
        }

        LOG.info("Validacion superada. Procesando pago con: " + metodoPago);
        boolean resultado = catalogoReal.procesarPago(pedido, metodoPago);

        if (!resultado) {
            LOG.warning("El pago fue rechazado por el sistema.");
        }
        return resultado;
    }
}
