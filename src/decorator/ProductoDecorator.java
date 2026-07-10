package decorator;
import clase.Producto;
/**
 *
 * @author LAB-USR-LSUR
 */
public abstract class ProductoDecorator implements Producto {

    protected Producto producto; 

    public ProductoDecorator(Producto producto) {
        this.producto = producto;
    }

    @Override
    public String getNombre() { return producto.getNombre(); }

    @Override
    public double getPrecio() { return producto.getPrecio(); }

    @Override
    public int getId() { return producto.getId(); }
}
