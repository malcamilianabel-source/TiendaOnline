package decorator;
import clase.Producto;
/**
 *
 * @author LAB-USR-LSUR
 */
public class Garantia extends ProductoDecorator {

    private static final double COSTO = 200.0;

    public Garantia(Producto producto) {
        super(producto);
    }

    @Override
    public String getNombre() {
        return producto.getNombre() + " + Garantia";
    }

    @Override
    public double getPrecio() {
        return producto.getPrecio() + COSTO;
    }
}