package conexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

// PATRÓN SINGLETON - Conexión a PostgreSQL
public class Conexion {

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%4$s] %2$s: %5$s%6$s%n");
    }

    private static final Logger LOG = Logger.getLogger(Conexion.class.getName());

    // Cambia estos valores según tu configuración de PostgreSQL
    private static final String URL      = "jdbc:postgresql://localhost:5432/BD_tiendaonline";
    private static final String USER     = "postgres";
    private static final String PASSWORD = "root";

    private Connection conexion;
    private static Conexion instancia;

    private Conexion() {
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            LOG.info("Conexion a PostgreSQL establecida correctamente.");
        } catch (Exception e) {
            LOG.severe("ERROR al conectar con PostgreSQL: " + e.getMessage());
        }
    }

    public static synchronized Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                LOG.info("Reconectando a PostgreSQL...");
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            LOG.warning("Error al verificar conexion: " + e.getMessage());
        }
        return conexion;
    }

    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                LOG.info("Conexion cerrada correctamente.");
            }
        } catch (SQLException e) {
            LOG.warning("Error al cerrar conexion: " + e.getMessage());
        }
    }
}
