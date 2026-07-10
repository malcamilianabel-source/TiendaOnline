package conexionBD;

import clase.Producto;
import java.sql.*;
import java.util.logging.Logger;
import state.Pedido;

public class PedidoDB {

    private static final Logger LOG = Logger.getLogger(PedidoDB.class.getName());
    private final Connection cn;

    public PedidoDB() {
        this.cn = Conexion.getInstancia().getConexion();
    }

    public int guardarPedido(Pedido pedido) {
        if (cn == null) {
            LOG.warning("Sin conexion a BD.");
            return -1;
        }
        if (pedido.getUsuario() == null) {
            LOG.warning("No hay usuario en el pedido.");
            return -1;
        }

        try {
            cn.setAutoCommit(false);

            int idPedido = insertarPedido(pedido);
            if (idPedido < 0) {
                cn.rollback();
                cn.setAutoCommit(true);
                return -1;
            }

            for (Producto p : pedido.getCarrito().getItems()) {
                insertarDetalle(idPedido, p);
                descontarStock(p.getId(), 1);
            }

            insertarPago(idPedido, pedido);
            actualizarPuntos(idPedido, pedido);

            cn.commit();
            cn.setAutoCommit(true);
            LOG.info("Pedido #" + idPedido + " guardado correctamente.");
            return idPedido;

        } catch (SQLException e) {
            LOG.severe("Error al guardar pedido: " + e.getMessage());
            try {
                cn.rollback();
                cn.setAutoCommit(true);
            } catch (SQLException ex) {
                LOG.severe("Error al hacer rollback: " + ex.getMessage());
            }
            return -1;
        }
    }

    private int insertarPedido(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos "
                   + "(id_usuarios, estado, metodo_pago, monto_total, direccion_envio, telefono, fecha_estimada_entrega) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt   (1, pedido.getUsuario().getId());
            ps.setString(2, pedido.getNombreEstado());
            ps.setString(3, pedido.getMetodoPago() != null ? pedido.getMetodoPago() : "");
            ps.setDouble(4, pedido.getMontoTotal());
            ps.setString(5, pedido.getDireccionEnvio() != null ? pedido.getDireccionEnvio() : "");
            ps.setString(6, pedido.getNumero() != null ? pedido.getNumero() : "");

            if (pedido.getFechaEstimadaEntrega() != null) {
                ps.setDate(7, java.sql.Date.valueOf(pedido.getFechaEstimadaEntrega()));
            } else {
                ps.setDate(7, java.sql.Date.valueOf(java.time.LocalDate.now().plusDays(7)));
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    private void insertarDetalle(int idPedido, Producto p) throws SQLException {
        String sql = "INSERT INTO detalle_pedido "
                   + "(id_pedidos, id_articulos, nombre_producto, precio_unitario, cantidad, garantia, envio_express) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        boolean tieneGarantia     = p.getNombre().contains("Garantia");
        boolean tieneEnvioExpress = p.getNombre().contains("Envio Express");

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt    (1, idPedido);
            ps.setInt    (2, p.getId());
            ps.setString (3, p.getNombre());
            ps.setDouble (4, p.getPrecio());
            ps.setInt    (5, 1);
            ps.setBoolean(6, tieneGarantia);
            ps.setBoolean(7, tieneEnvioExpress);
            ps.executeUpdate();
        }
    }

    private void insertarPago(int idPedido, Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pagos (id_pedidos, metodo, monto, estado) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt   (1, idPedido);
            ps.setString(2, pedido.getMetodoPago() != null ? pedido.getMetodoPago() : "");
            ps.setDouble(3, pedido.getMontoTotal());
            ps.setString(4, pedido.isPagoExitoso() ? "exitoso" : "fallido");
            ps.executeUpdate();
        }
    }

    private void actualizarPuntos(int idPedido, Pedido pedido) throws SQLException {
        int idUsuario      = pedido.getUsuario().getId();
        int puntosGanados  = (int) pedido.getMontoTotal(); // 1 punto por cada sol gastado
        int puntosCanjeados = pedido.getPuntosCanjeados();

        // Actualizar saldo neto en usuarios
        try (PreparedStatement ps = cn.prepareStatement(
                "UPDATE usuarios SET puntos = puntos + ? - ? WHERE id = ?")) {
            ps.setInt(1, puntosGanados);
            ps.setInt(2, puntosCanjeados);
            ps.setInt(3, idUsuario);
            ps.executeUpdate();
        }

        // Registrar historial
        String sqlH = "INSERT INTO historial_puntos (id_usuario, puntos, motivo) VALUES (?, ?, ?)";
        if (puntosCanjeados > 0) {
            try (PreparedStatement ps = cn.prepareStatement(sqlH)) {
                ps.setInt   (1, idUsuario);
                ps.setInt   (2, -puntosCanjeados);
                ps.setString(3, "Canje descuento en pedido #" + idPedido);
                ps.executeUpdate();
            }
        }
        if (puntosGanados > 0) {
            try (PreparedStatement ps = cn.prepareStatement(sqlH)) {
                ps.setInt   (1, idUsuario);
                ps.setInt   (2, puntosGanados);
                ps.setString(3, "Compra #" + idPedido);
                ps.executeUpdate();
            }
        }
    }

    private void descontarStock(int idArticulo, int cantidad) throws SQLException {
        if (idArticulo <= 0) return;
        String sql = "UPDATE articulos SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idArticulo);
            ps.setInt(3, cantidad);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                LOG.warning("Stock insuficiente para articulo id=" + idArticulo);
            }
        }
    }
}
