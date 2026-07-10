package state;

import clase.Usuario;
import clase.Articulo;
import composite.CarritoProductos;
import java.util.logging.Logger;

// PATRÓN STATE - Contexto: representa una sesión de compra completa
public class Pedido {

    private static final Logger LOG = Logger.getLogger(Pedido.class.getName());

    private EstadoPedido estado;
    private Usuario usuario;
    private final CarritoProductos carrito;
    private String metodoPago;
    private String direccionEnvio;
    private String fechaEstimadaEntrega;
    private boolean pagoExitoso;
    private String numero;
    private int idPedidoBD;
    private int descuentoPuntos  = 0;   // S/ a descontar del total
    private int puntosCanjeados  = 0;   // puntos que se gastarán

    public Pedido() {
        this.estado  = new SinRegistro();
        this.carrito = new CarritoProductos();
        this.pagoExitoso = false;
    }

    public void avanzar() {
        estado.manejar(this);
    }

    public void setEstado(EstadoPedido nuevoEstado) {
        LOG.info(estado.getNombre() + " --> " + nuevoEstado.getNombre());
        this.estado = nuevoEstado;
    }

    public EstadoPedido getEstado()                   { return estado; }
    public String getNombreEstado()                   { return estado.getNombre(); }
    public String getDescripcionEstado()              { return estado.getDescripcion(); }

    public Usuario getUsuario()                       { return usuario; }
    public void setUsuario(Usuario usuario)           { this.usuario = usuario; }

    public CarritoProductos getCarrito()              { return carrito; }
    public void agregarAlCarrito(Articulo a)          { carrito.agregar(a); }

    public String getMetodoPago()                     { return metodoPago; }
    public void setMetodoPago(String metodoPago)      { this.metodoPago = metodoPago; }
    public double getMontoTotal()                     { return Math.max(0, carrito.getPrecio() - descuentoPuntos); }

    public int getDescuentoPuntos()                   { return descuentoPuntos; }
    public void setDescuentoPuntos(int d)             { this.descuentoPuntos = d; }
    public int getPuntosCanjeados()                   { return puntosCanjeados; }
    public void setPuntosCanjeados(int p)             { this.puntosCanjeados = p; }
    public boolean isPagoExitoso()                    { return pagoExitoso; }
    public void setPagoExitoso(boolean pagoExitoso)   { this.pagoExitoso = pagoExitoso; }

    public String getDireccionEnvio()                 { return direccionEnvio; }
    public void setDireccionEnvio(String dir)         { this.direccionEnvio = dir; }
    public String getFechaEstimadaEntrega()           { return fechaEstimadaEntrega; }
    public void setFechaEstimadaEntrega(String fecha) { this.fechaEstimadaEntrega = fecha; }
    public String getNumero()                         { return numero; }
    public void setNumero(String numero)              { this.numero = numero; }

    public int getIdPedidoBD()                        { return idPedidoBD; }
    public void setIdPedidoBD(int idPedidoBD)         { this.idPedidoBD = idPedidoBD; }
}
