/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package interfaz;

/**
 *
 * @author Abel
 */
public class PanelAdmin extends javax.swing.JPanel {

    private Menu menuPadre;
    /** true = tabla mostrando usuarios, false = mostrando pedidos */
    private boolean mostrandoUsuarios = true;

    public PanelAdmin() {
        initComponents();
    }

    public PanelAdmin(Menu menu) {
        initComponents();
        this.menuPadre = menu;
        jLabel1.setText("PANEL ADMINISTRADOR");

        btnVerUsuarios.addActionListener(e -> cargarUsuarios());
        btnVerPedidos.addActionListener(e -> cargarPedidos());

        btnEliminarUsuario.addActionListener(e -> eliminarUsuario());

        // Clic en fila de pedidos → llenar tablaDetalle automáticamente
        tablaADMIN.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !mostrandoUsuarios) {
                int fila = tablaADMIN.getSelectedRow();
                if (fila >= 0) {
                    int idPedido = (int) tablaADMIN.getValueAt(fila, 0);
                    cargarDetalle(idPedido);
                }
            }
        });
        limpiarDetalle();
    }

    /** Al entrar al panel muestra usuarios por defecto */
    public void cargar() {
        cargarUsuarios();
    }

    private void limpiarDetalle() {
        tablaDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new String[]{"Producto", "Cantidad", "Precio Unit.", "Subtotal", "Garantia", "Express"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
    }

    private void cargarDetalle(int idPedido) {
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
            new String[]{"Producto", "Cantidad", "Precio Unit.", "Subtotal", "Garantia", "Express"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaDetalle.setModel(model);
        java.sql.Connection cn = conexionBD.Conexion.getInstancia().getConexion();
        if (cn == null) return;
        try (java.sql.PreparedStatement ps = cn.prepareStatement(
                "SELECT nombre_producto, cantidad, precio_unitario, garantia, envio_express " +
                "FROM detalle_pedido WHERE id_pedidos = ?")) {
            ps.setInt(1, idPedido);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String prod   = rs.getString("nombre_producto");
                    int cant      = rs.getInt("cantidad");
                    double precio = rs.getDouble("precio_unitario");
                    boolean gar   = rs.getBoolean("garantia");
                    boolean exp   = rs.getBoolean("envio_express");
                    model.addRow(new Object[]{
                        prod, cant,
                        String.format("S/ %.2f", precio),
                        String.format("S/ %.2f", precio * cant),
                        gar ? "Sí" : "No",
                        exp ? "Sí" : "No"
                    });
                }
            }
        } catch (java.sql.SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void cargarUsuarios() {
        mostrandoUsuarios = true;
        limpiarDetalle();
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
            new String[]{"ID", "Nombre", "Email", "DNI", "Telefono", "Rol"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaADMIN.setModel(model);
        java.sql.Connection cn = conexionBD.Conexion.getInstancia().getConexion();
        if (cn == null) return;
        try (java.sql.Statement st = cn.createStatement();
             java.sql.ResultSet rs = st.executeQuery(
                "SELECT id, nombre || ' ' || apellido AS nombre, email, dni, telefono, rol " +
                "FROM usuarios ORDER BY id")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("nombre"), rs.getString("email"),
                    rs.getString("dni"), rs.getString("telefono"), rs.getString("rol")
                });
            }
        } catch (java.sql.SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void cargarPedidos() {
        mostrandoUsuarios = false;
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
            new String[]{"ID", "Cliente", "Total", "Estado", "Metodo Pago", "Fecha"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaADMIN.setModel(model);
        java.sql.Connection cn = conexionBD.Conexion.getInstancia().getConexion();
        if (cn == null) return;
        try (java.sql.Statement st = cn.createStatement();
             java.sql.ResultSet rs = st.executeQuery(
                "SELECT p.id, u.nombre || ' ' || u.apellido AS cliente, p.monto_total, " +
                "p.estado, p.metodo_pago, p.fecha_registro " +
                "FROM pedidos p JOIN usuarios u ON p.id_usuarios = u.id ORDER BY p.id DESC")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("cliente"),
                    "S/ " + rs.getString("monto_total"),
                    rs.getString("estado"), rs.getString("metodo_pago"),
                    rs.getString("fecha_registro")
                });
            }
        } catch (java.sql.SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void eliminarUsuario() {
        if (!mostrandoUsuarios) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Cambia a la vista de Usuarios primero.", "Aviso",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        int fila = tablaADMIN.getSelectedRow();
        if (fila < 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Selecciona un usuario primero.", "Aviso",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id     = (int) tablaADMIN.getValueAt(fila, 0);
        String nombre = (String) tablaADMIN.getValueAt(fila, 1);
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
            "Eliminar a " + nombre + "?", "Confirmar",
            javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm != javax.swing.JOptionPane.YES_OPTION) return;
        java.sql.Connection cn = conexionBD.Conexion.getInstancia().getConexion();
        if (cn == null) return;
        try (java.sql.PreparedStatement ps = cn.prepareStatement(
                "DELETE FROM usuarios WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            cargarUsuarios();
            javax.swing.JOptionPane.showMessageDialog(this, "Usuario eliminado.");
        } catch (java.sql.SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "No se puede eliminar: el usuario tiene pedidos asociados.", "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaADMIN = new javax.swing.JTable();
        btnVerUsuarios = new javax.swing.JButton();
        btnVerPedidos = new javax.swing.JButton();
        btnEliminarUsuario = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaDetalle = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("PANEL ADMINISTRADOR");

        tablaADMIN.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Email", "DNI", "Telefono", "Rol"
            }
        ));
        jScrollPane1.setViewportView(tablaADMIN);

        btnVerUsuarios.setText("Ver Usuarios");

        btnVerPedidos.setText("Ver Pedidos");

        btnEliminarUsuario.setText("Eliminar Usuario");

        tablaDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Producto", "Cantidad", "Precio Unit.", "Subtotal", "Garantia", "Express"
            }
        ));
        jScrollPane2.setViewportView(tablaDetalle);

        jLabel2.setText("Detalles:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane1)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(btnVerUsuarios)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnVerPedidos)
                                    .addGap(26, 26, 26)
                                    .addComponent(btnEliminarUsuario))
                                .addComponent(jScrollPane2)))))
                .addContainerGap(260, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel1)
                .addGap(56, 56, 56)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVerUsuarios)
                    .addComponent(btnVerPedidos)
                    .addComponent(btnEliminarUsuario))
                .addContainerGap(112, Short.MAX_VALUE))
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 660));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminarUsuario;
    private javax.swing.JButton btnVerPedidos;
    private javax.swing.JButton btnVerUsuarios;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tablaADMIN;
    private javax.swing.JTable tablaDetalle;
    // End of variables declaration//GEN-END:variables
}
