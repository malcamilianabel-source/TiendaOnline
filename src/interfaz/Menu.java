
package interfaz;

import java.awt.CardLayout;
import tiendaonline.TiendaVirtual;
import clase.Usuario;

/**
 *
 * @author LAB-USR-LSUR
 */
public class Menu extends javax.swing.JFrame {

    private final TiendaVirtual tienda = TiendaVirtual.getInstancia();

    // Paneles del CardLayout
    private CardLayout        cardLayout;
    private Catalogo          panelCatalogo;
    private Carrito           panelCarrito;
    private Login             panelLogin;
    private Registro          panelRegistro;
    private DetalleProducto   panelDetalle;
    private TipoPago          panelPago;
    private EnvioProducto     panelEnvio;
    private DetallePago       panelConfirmacion;
    private PanelAdmin        panelAdmin;
    private PanelAtencion     panelAtencion;
    private PanelAlmacen      panelAlmacen;

    /** Panel al que volver después del login */
    private String panelAnterior = "CATALOGO";

    /**
     * Creates new form Menu
     */
    public Menu() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Tienda Virtual");
        inicializarPaneles();
        agregarListeners();
    }

    // ── CardLayout ───────────────────────────────────────────────
    private void inicializarPaneles() {
        cardLayout        = new CardLayout();
        pnlContenido.setLayout(cardLayout);

        panelCatalogo     = new Catalogo(this);
        panelCarrito      = new Carrito(this);
        panelLogin        = new Login(this);
        panelRegistro     = new Registro(this);
        panelDetalle      = new DetalleProducto(this);
        panelPago         = new TipoPago(this);
        panelEnvio        = new EnvioProducto(this);
        panelConfirmacion = new DetallePago(this);
        panelAdmin        = new PanelAdmin(this);
        panelAtencion     = new PanelAtencion(this);
        panelAlmacen      = new PanelAlmacen(this);

        pnlContenido.add(panelCatalogo,     "CATALOGO");
        pnlContenido.add(panelCarrito,      "CARRITO");
        pnlContenido.add(panelLogin,        "LOGIN");
        pnlContenido.add(panelRegistro,     "REGISTRO");
        pnlContenido.add(panelDetalle,      "DETALLE");
        pnlContenido.add(panelPago,         "PAGO");
        pnlContenido.add(panelEnvio,        "ENVIO");
        pnlContenido.add(panelConfirmacion, "CONFIRMACION");
        pnlContenido.add(panelAdmin,        "ADMIN");
        pnlContenido.add(panelAtencion,     "ATENCION");
        pnlContenido.add(panelAlmacen,      "ALMACEN");

        cardLayout.show(pnlContenido, "CATALOGO");
    }

    /** Cambia el panel visible dentro de pnlContenido */
    public void mostrarPanel(String nombre) {
        cardLayout.show(pnlContenido, nombre);
    }

    /** Abre el login y recuerda a qué panel volver después */
    public void mostrarLogin(String origen) {
        panelAnterior = origen;
        panelLogin.limpiar();
        mostrarPanel("LOGIN");
    }

    /** Devuelve el panel al que volver tras el login */
    public String getPanelAnterior() { return panelAnterior; }

    /** Redirige al panel correspondiente según el rol del usuario */
    public void mostrarPanelPorRol(String rol) {
        switch (rol.toUpperCase()) {
            case "ADMINISTRADOR":
                panelAdmin.cargar();
                mostrarPanel("ADMIN");
                break;
            case "ATENCION":
                panelAtencion.cargar();
                mostrarPanel("ATENCION");
                break;
            case "ALMACEN":
                panelAlmacen.cargar();
                mostrarPanel("ALMACEN");
                break;
            default:
                mostrarPanel("CATALOGO");
                break;
        }
    }

    /** Muestra el detalle de un artículo */
    public void mostrarDetalle(clase.Articulo art) {
        panelDetalle.setArticulo(art);
        mostrarPanel("DETALLE");
    }

    /** Muestra el panel de pago (actualiza total y estado) */
    public void mostrarPago() {
        panelPago.actualizar();
        mostrarPanel("PAGO");
    }

    /** Muestra el panel de envío */
    public void mostrarEnvio() {
        panelEnvio.actualizar();
        mostrarPanel("ENVIO");
    }

    /** Muestra la confirmación final del pedido */
    public void mostrarConfirmacion() {
        panelConfirmacion.actualizar();
        mostrarPanel("CONFIRMACION");
        actualizarUI(); // refresca puntos en el header tras la compra
    }

    // ── Listeners de botones del header ──────────────────────────
    private void agregarListeners() {

        btnCarrito.addActionListener(e -> {
            panelCarrito.cargarCarrito();
            mostrarPanel("CARRITO");
        });

        btnIniciarSesion.addActionListener(e -> {
            if (tienda.getUsuarioActual() == null) {
                mostrarLogin("CATALOGO");
            }
        });

        btnRegistro.addActionListener(e -> {
            panelRegistro.limpiar();
            mostrarPanel("REGISTRO");
        });

        btnCerrarSesion.addActionListener(e -> {
            tienda.logout();
            actualizarUI();
            mostrarPanel("CATALOGO");
        });

        btnCerrar.addActionListener(e -> {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this, "¿Deseas cerrar la aplicación?", "Salir",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE);
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    // ── Métodos públicos para otros paneles ───────────────────────

    /** Actualiza el texto del botón Carrito con el número de items */
    public void actualizarCarritoBtn() {
        int n = tienda.getPedidoActual().getCarrito().getItems().size();
        btnCarrito.setText(n > 0 ? "Carrito (" + n + ")" : "Carrito");
    }

    /** Actualiza el header tras login / logout / compra */
    public void actualizarUI() {
        Usuario u = tienda.getUsuarioActual();
        if (u != null) {
            // Refrescar puntos desde BD para que se vea el saldo actualizado
            int pts = consultarPuntos(u.getId());
            u.setPuntos(pts);
            btnIniciarSesion.setText(u.getNombre() + " (" + pts + " pts)");
            btnIniciarSesion.setEnabled(false);
        } else {
            btnIniciarSesion.setText("Iniciar Sesion");
            btnIniciarSesion.setEnabled(true);
        }
    }

    private int consultarPuntos(int idUsuario) {
        java.sql.Connection cn = conexionBD.Conexion.getInstancia().getConexion();
        if (cn == null) return 0;
        try (java.sql.PreparedStatement ps = cn.prepareStatement(
                "SELECT puntos FROM usuarios WHERE id = ?")) {
            ps.setInt(1, idUsuario);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("puntos");
            }
        } catch (java.sql.SQLException e) { /* ignorar */ }
        return 0;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlContenido = new javax.swing.JPanel();
        pnlEncabezado = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnCarrito = new javax.swing.JButton();
        btnIniciarSesion = new javax.swing.JButton();
        btnRegistro = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnCerrarSesion = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlContenido.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(pnlContenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 1080, 720));

        pnlEncabezado.setBackground(new java.awt.Color(102, 102, 255));

        jLabel1.setText("TIENDA VIRTUAL");

        btnCarrito.setText("CARRITO");

        btnIniciarSesion.setText("INICIAR SESION");

        btnRegistro.setText("REGISTRARSE");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/logo_tiendapro.jpg"))); // NOI18N

        btnCerrarSesion.setText("CERRAR SESION");

        btnCerrar.setText("X");

        javax.swing.GroupLayout pnlEncabezadoLayout = new javax.swing.GroupLayout(pnlEncabezado);
        pnlEncabezado.setLayout(pnlEncabezadoLayout);
        pnlEncabezadoLayout.setHorizontalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel2)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(jLabel1)
                        .addGap(157, 157, 157)
                        .addComponent(btnCarrito)
                        .addGap(54, 54, 54)
                        .addComponent(btnIniciarSesion)
                        .addGap(39, 39, 39)
                        .addComponent(btnRegistro)
                        .addGap(39, 39, 39)
                        .addComponent(btnCerrarSesion)
                        .addContainerGap(81, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEncabezadoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCerrar))))
        );
        pnlEncabezadoLayout.setVerticalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(btnCarrito)
                            .addComponent(btnIniciarSesion)
                            .addComponent(btnRegistro)
                            .addComponent(btnCerrarSesion)))
                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCerrar))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        getContentPane().add(pnlEncabezado, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1080, 130));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.setErr(System.out);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCarrito;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnIniciarSesion;
    private javax.swing.JButton btnRegistro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel pnlContenido;
    private javax.swing.JPanel pnlEncabezado;
    // End of variables declaration//GEN-END:variables
}
