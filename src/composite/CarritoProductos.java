package composite;
import clase.Producto;
import java.util.ArrayList;
import java.util.List;

//PATRÓN COMPOSITE
public class CarritoProductos implements Producto {

    private final List<Producto> items = new ArrayList<>();

    public void agregar(Producto p) {
        items.add(p);
    }

    public void remover(Producto p) {
        items.remove(p);
    }

    public void vaciar() {
        items.clear();
    }

    public List<Producto> getItems() {
        return items;
    }

    @Override
    public String getNombre() {
        if (items.isEmpty()) return "Carrito vacío";
        StringBuilder sb = new StringBuilder("Carrito:\n");
        for (Producto p : items) {
            sb.append("    - ").append(p.getNombre())
              .append(" (S/ ").append(String.format("%.2f", p.getPrecio())).append(")\n");
        }
        return sb.toString();
    }

    @Override
    public double getPrecio() {
        double total = 0;
        for (Producto p : items) {
            total += p.getPrecio();
        }
        return total;
    }
}
