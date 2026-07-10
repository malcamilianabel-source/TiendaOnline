package interfaz;

import javax.swing.table.DefaultTableModel;
import clase.Producto;
import composite.CarritoProductos;
import tiendaonline.TiendaVirtual;

/**
 * Panel del carrito de compras (diseño visual).
 * @author Abel
 */
public class Carrito extends javax.swing.JPanel {

    private final TiendaVirtual tienda = TiendaVirtual.getInstancia();
    private Menu menuPadre;

    public Carrito() {
        initComponents();
        configurar();
    }

    public Carrito(Menu menu) {
        initComponents();
        this.menuPadre = menu;
        configurar();
    }

    /** Configuración post-initComponents: modelo de tabla y listener de jButton3 */
    private void configurar() {
        jTable1.setModel(new DefaultTableModel(
            new Object[][]{}, new String[]{"#", "PRODUCTO", "PRECIO"}
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
        jTable1.getColumnModel().getColumn(0).setMaxWidth(40);
        jTable1.getColumnModel().getColumn(2).setMaxWidth(100);

        btnPagar.addActionListener(e -> procederAlPago());
    }

    /** Recarga la tabla con los items actuales del carrito */
    public void cargarCarrito() {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0);
        CarritoProductos carrito = tienda.getPedidoActual().getCarrito();
        int i = 1;
        for (Producto p : carrito.getItems()) {
            modelo.addRow(new Object[]{i++, p.getNombre(), String.format("%.2f", p.getPrecio())});
        }
        jLabel2.setText("TOTAL: S/ " + String.format("%.2f", carrito.getPrecio()));
    }

    private void eliminarSeleccionado() {
        int fila = jTable1.getSelectedRow();
        if (fila < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un producto.", "Aviso",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        java.util.List<Producto> items = tienda.getPedidoActual().getCarrito().getItems();
        if (fila < items.size()) {
            tienda.getPedidoActual().getCarrito().remover(items.get(fila));
            cargarCarrito();
            if (menuPadre != null) menuPadre.actualizarCarritoBtn();
        }
    }

    private void procederAlPago() {
        CarritoProductos carrito = tienda.getPedidoActual().getCarrito();
        if (carrito.getItems().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "El carrito esta vacio.", "Aviso",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tienda.getUsuarioActual() == null) {
            if (menuPadre != null) menuPadre.mostrarLogin("CARRITO");
            return;
        }
        state.Pedido pedido = tienda.getPedidoActual();
        // Avanzar desde procesado_registro → reservado si hace falta
        if (pedido.getNombreEstado().equals("procesado_registro")) {
            pedido.avanzar();
        }
        // Avanzar desde reservado → registro_datos_pago
        if (pedido.getNombreEstado().equals("reservado")) {
            pedido.avanzar();
        }
        if (menuPadre != null) menuPadre.mostrarPago();
    }

    // ── Código generado por NetBeans (no modificar) ───────────────
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        TablaCarrito = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnEliminar = new javax.swing.JButton();
        btnVaciarCarro = new javax.swing.JButton();
        btnPagar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnVolver = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("MI CARRITO");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 59, -1, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "#", "PRODUCTO", "PRECIO"
            }
        ));
        TablaCarrito.setViewportView(jTable1);

        add(TablaCarrito, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 134, 765, 303));

        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(388, 549, -1, -1));

        btnVaciarCarro.setText("VACIAR CARRO");
        btnVaciarCarro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVaciarCarroActionPerformed(evt);
            }
        });
        add(btnVaciarCarro, new org.netbeans.lib.awtextra.AbsoluteConstraints(503, 549, -1, -1));

        btnPagar.setText("PROCEDER AL PAGO");
        add(btnPagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(658, 549, -1, -1));

        jLabel2.setText("TOTAL");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(707, 504, -1, -1));

        btnVolver.setText("VOLVER");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });
        add(btnVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 549, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminarSeleccionado();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnVaciarCarroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVaciarCarroActionPerformed
        tienda.getPedidoActual().getCarrito().vaciar();
        cargarCarrito();
        if (menuPadre != null) menuPadre.actualizarCarritoBtn();
    }//GEN-LAST:event_btnVaciarCarroActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        if (menuPadre != null) menuPadre.mostrarPanel("CATALOGO");
    }//GEN-LAST:event_btnVolverActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane TablaCarrito;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnPagar;
    private javax.swing.JButton btnVaciarCarro;
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
