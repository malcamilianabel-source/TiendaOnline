package conexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import clase.Articulo;

public class ArticuloDB {

    private static final Logger LOG = Logger.getLogger(ArticuloDB.class.getName());
    private final Connection cn;

    public ArticuloDB() {
        this.cn = Conexion.getInstancia().getConexion();
    }

    public List<Articulo> obtenerTodos() {
        List<Articulo> lista = new ArrayList<>();
        if (cn == null) {
            LOG.warning("Sin conexion a BD.");
            return lista;
        }
        String sql = "SELECT id, codigo, nombre, precio, stock, id_categorias "
                   + "FROM articulos ORDER BY nombre";

        try (Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Articulo(
                    rs.getInt("id"),
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getInt("id_categorias")
                ));
            }
        } catch (SQLException e) {
            LOG.warning("Error al obtener catalogo: " + e.getMessage());
        }
        return lista;
    }
}
