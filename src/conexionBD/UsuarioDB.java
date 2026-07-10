package conexionBD;

import java.sql.*;
import java.util.logging.Logger;
import clase.Usuario;

public class UsuarioDB {

    private static final Logger LOG = Logger.getLogger(UsuarioDB.class.getName());
    private final Connection cn;

    public UsuarioDB() {
        this.cn = Conexion.getInstancia().getConexion();
    }

    public Usuario login(String email, String password) {
        if (cn == null) {
            LOG.warning("Sin conexion a BD.");
            return null;
        }
        String sql = "SELECT id, nombre, apellido, email, dni, telefono, password, rol, puntos "
                   + "FROM usuarios WHERE email = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (!PasswordUtil.verify(password, storedHash)) {
                        return null;
                    }
                    Usuario u = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        storedHash,
                        rs.getString("rol")
                    );
                    u.setPuntos(rs.getInt("puntos"));
                    return u;
                }
            }
        } catch (SQLException e) {
            LOG.warning("Error en login: " + e.getMessage());
        }
        return null;
    }

    public boolean registrar(String nombre, String apellido, String email, String dni,
            String telefono, String password) {
        if (cn == null) {
            LOG.warning("Sin conexion a BD.");
            return false;
        }
        String sql = "INSERT INTO usuarios (nombre, apellido, email, dni, telefono, password, rol) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, email);
            ps.setString(4, dni);
            ps.setString(5, telefono);
            ps.setString(6, PasswordUtil.hash(password));
            ps.setString(7, "CLIENTE");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.warning("Error en registrar: " + e.getMessage());
            return false;
        }
    }
}
