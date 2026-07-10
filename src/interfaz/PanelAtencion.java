/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package interfaz;

/**
 *
 * @author Abel
 */
public class PanelAtencion extends javax.swing.JPanel {

    private Menu menuPadre;

    public PanelAtencion() {
        initComponents();
    }

    public PanelAtencion(Menu menu) {
        initComponents();
        this.menuPadre = menu;
        btnCambiarEstado.addActionListener(e -> cambiarEstado());
        

        // Clic en fila → llenar tablaDetalle automáticamente
        tablaATENCION.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaATENCION.getSelectedRow();
                if (fila >= 0) {
                    int idPedido = (int) tablaATENCION.getValueAt(fila, 0);
                    cargarDetalle(idPedido);
                }
            }
        });
        limpiarDetalle();
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

    /** Carga todos los pedidos con datos del cliente */
    public void cargar() {
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
            new String[]{"ID", "Cliente", "Total", "Estado", "Metodo Pago", "Direccion", "Fecha"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaATENCION.setModel(model);
        limpiarDetalle();
        java.sql.Connection cn = conexionBD.Conexion.getInstancia().getConexion();
        if (cn == null) return;
        try (java.sql.Statement st = cn.createStatement();
             java.sql.ResultSet rs = st.executeQuery(
                "SELECT p.id, u.nombre || ' ' || u.apellido AS cliente, p.monto_total, " +
                "p.estado, p.metodo_pago, p.direccion_envio, p.fecha_registro " +
                "FROM pedidos p JOIN usuarios u ON p.id_usuarios = u.id ORDER BY p.id DESC")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("cliente"),
                    "S/ " + rs.getString("monto_total"), rs.getString("estado"),
                    rs.getString("metodo_pago"), rs.getString("direccion_envio"),
                    rs.getString("fecha_registro")
                });
            }
        } catch (java.sql.SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void cambiarEstado() {
        int fila = tablaATENCION.getSelectedRow();
        if (fila < 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Selecciona un pedido primero.", "Aviso",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idPedido = (int) tablaATENCION.getValueAt(fila, 0);
        String estadoActual = (String) tablaATENCION.getValueAt(fila, 3);
        String[] estados = {"procesado_registro", "pendiente_pago", "pago_exitoso",
                            "envio_programado", "en_camino", "entregado", "cancelado"};
        String nuevo = (String) javax.swing.JOptionPane.showInputDialog(this,
            "Estado actual: " + estadoActual + "\nSelecciona el nuevo estado:",
            "Cambiar Estado", javax.swing.JOptionPane.PLAIN_MESSAGE,
            null, estados, estadoActual);
        if (nuevo == null || nuevo.equals(estadoActual)) return;
        java.sql.Connection cn = conexionBD.Conexion.getInstancia().getConexion();
        if (cn == null) return;
        try (java.sql.PreparedStatement ps = cn.prepareStatement(
                "UPDATE pedidos SET estado = ? WHERE id = ?")) {
            ps.setString(1, nuevo);
            ps.setInt(2, idPedido);
            ps.executeUpdate();
            cargar();
            javax.swing.JOptionPane.showMessageDialog(this, "Estado actualizado a: " + nuevo);
        } catch (java.sql.SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void verDetalle() {
        int fila = tablaATENCION.getSelectedRow();
        if (fila < 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Selecciona un pedido primero.", "Aviso",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idPedido = (int) tablaATENCION.getValueAt(fila, 0);
        cargarDetalle(idPedido);
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
        tablaATENCION = new javax.swing.JTable();
        btnCambiarEstado = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaDetalle = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("PANEL ATENCIÓN AL CLIENTE");

        tablaATENCION.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Cliente", "Total", "Estado", "Metodo de Pago", "Direccion", "Fecha"
            }
        ));
        jScrollPane1.setViewportView(tablaATENCION);

        btnCambiarEstado.setText("Cambiar Estado");

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
                .addGap(62, 62, 62)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCambiarEstado)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1)
                        .addComponent(jScrollPane1)
                        .addComponent(jScrollPane2)))
                .addContainerGap(316, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1)
                .addGap(61, 61, 61)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addComponent(btnCambiarEstado)
                .addGap(114, 114, 114))
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 830, 660));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCambiarEstado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tablaATENCION;
    private javax.swing.JTable tablaDetalle;
    // End of variables declaration//GEN-END:variables
}
