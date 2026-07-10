package decorator;
import clase.Producto;
/**
 *
 * @author LAB-USR-LSUR
 */
public class EnvioExpress extends ProductoDecorator {

    private static final double COSTO = 30.0;

    public EnvioExpress(Producto producto) {
        super(producto);
    }

    @Override
    public String getNombre() {
        return producto.getNombre() + " + Envio Express";
    }

    @Override
    public double getPrecio() {
        return producto.getPrecio() + COSTO;
    }
}