package tiendaonline;

import clase.Usuario;
import clase.Articulo;
import conexionBD.ArticuloDB;
import conexionBD.UsuarioDB;
import state.Pedido;
import state.ProcesadoRegistro;
import java.util.List;
import java.util.logging.Logger;

// PATRÓN SINGLETON
public class TiendaVirtual {

    private static final Logger LOG = Logger.getLogger(TiendaVirtual.class.getName());
    private static TiendaVirtual instancia;

    private final UsuarioDB usuarioDB;
    private final ArticuloDB articuloDB;
    private Usuario usuarioActual;
    private Pedido pedidoActual;

    private TiendaVirtual() {
        usuarioDB  = new UsuarioDB();
        articuloDB = new ArticuloDB();
    }

    public static synchronized TiendaVirtual getInstancia() {
        if (instancia == null) {
            instancia = new TiendaVirtual();
        }
        return instancia;
    }

    public Usuario login(String email, String password) {
        usuarioActual = usuarioDB.login(email, password);
        if (usuarioActual != null) {
            Pedido p = getPedidoActual();
            p.setUsuario(usuarioActual);
            String estado = p.getNombreEstado();
            if (estado.equals("sin_registro") || estado.equals("pendiente_registro")) {
                p.setEstado(new ProcesadoRegistro());
            }
            LOG.info("Login exitoso: " + usuarioActual.getEmail());
        }
        return usuarioActual;
    }

    public void logout() {
        LOG.info("Sesion cerrada.");
        usuarioActual = null;
        pedidoActual  = null;
    }

    public List<Articulo> getCatalogo() {
        return articuloDB.obtenerTodos();
    }

    public Pedido getPedidoActual() {
        if (pedidoActual == null) {
            pedidoActual = new Pedido();
        }
        return pedidoActual;
    }

    public void nuevoPedido() {
        pedidoActual = new Pedido();
        if (usuarioActual != null) {
            pedidoActual.setUsuario(usuarioActual);
            pedidoActual.setEstado(new ProcesadoRegistro());
        }
    }

    public Usuario getUsuarioActual() { return usuarioActual; }
}
