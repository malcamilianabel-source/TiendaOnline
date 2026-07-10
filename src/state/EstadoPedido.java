package state;

// PATRÓN STATE - Interfaz base para todos los estados del pedido
public interface EstadoPedido {
    String getNombre();
    String getDescripcion();
    void manejar(Pedido pedido);
}
