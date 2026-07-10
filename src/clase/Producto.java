package clase;

/**
 *
 * @author LAB-USR-LSUR
 */
public interface Producto {
    String getNombre();
    double getPrecio();
    // Retorna el id del Articulo base (0 si no aplica)
    default int getId() { return 0; }
}

