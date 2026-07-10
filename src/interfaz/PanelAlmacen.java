/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package interfaz;

/**
 *
 * @author Abel
 */
public class PanelAlmacen extends javax.swing.JPanel {

    private Menu menuPadre;

    public PanelAlmacen() {
        initComponents();
    }

    public PanelAlmacen(Menu menu) {
        initComponents();
        this.menuPadre = menu;
        btnActualizarStock.addActionListener(e -> actualizarStock());
        btnAgregarProducto.addActionListener(e -> agregarProducto());
        btnEliminarProducto.addActionListener(e -> eliminarProducto());
        btnStockBajo.addActionListener(e -> filtrarStockBajo());
        btnActualizar.addActionListener(e -> cargar());
    }

    /** Carga los productos con su stock en la tabla */
    public void cargar() {
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
            new String[]{"Codigo", "Producto", "Precio", "Stock"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaAlmacen.setModel(model);

        java.sql.Connection cn = conexionBD.Conexion.getInstancia().getConexion();
        if (cn == null) return;
        try (java.sql.Statement st = cn.createStatement();
             java.sql.ResultSet rs = st.executeQuery(
                 "SELECT codigo, nombre, precio, stock FROM articulos ORDER BY nombre")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                });
            }
        } catch (java.sql.SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error cargando productos: " + e.getMessage());
        }
    }

    private void actualizarStock() {
        int fila = tablaAlmacen.getSelectedRow();
        if (fila < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un producto primero.");
            return;
        }
        String codigo = (String) tablaAlmacen.getValueAt(fila, 0);
        String nombre = (String) tablaAlmacen.getValueAt(fila, 1);
        String input = javax.swing.JOptionPane.showInputDialog(this,
            "Nuevo stock para " + nombre + ":", tablaAlmacen.getValueAt(fila, 3));
        if (input == null) return;
        try {
            int nuevoStock = Integer.parseInt(input.trim());
            java.sql.Connection cn = conexionBD.Conexion.getInstancia().getConexion();
            try (java.sql.PreparedStatement ps = cn.prepareStatement(
                    "UPDATE articulos SET stock = ? WHERE codigo = ?")) {
                ps.setInt(1, nuevoStock);
                ps.setString(2, codigo);
                ps.executeUpdate();
            }
            cargar();
            javax.swing.JOptionPane.showMessageDialog(this, "Stock actualizado correctamente.");
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ingresa un numero valido.");
        } catch (java.sql.SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void agregarProducto() {
        javax.swing.JTextField fCodigo  = new javax.swing.JTextField();
        javax.swing.JTextField fNombre  = new javax.swing.JTextField();
        javax.swing.JTextField fPrecio  = new javax.swing.JTextField();
        javax.swing.JTextField fStock   = new javax.swing.JTextField();
        Object[] campos = {"Codigo:", fCodigo, "Nombre:", fNombre,
                           "Precio:", fPrecio, "Stock inicial:", fStock};
        int op = javax.swing.JOptionPane.showConfirmDialog(this, campos,
            "Agregar Producto", javax.swing.JOptionPane.OK_CANCEL_OPTION);
        if (op != javax.swing.JOptionPane.OK_OPTION) return;
        try {
            String cod  = fCodigo.getText().trim();
            String nom  = fNombre.getText().trim();
            double prec = Double.parseDouble(fPrecio.getText().trim());
            int stock   = Integer.parseInt(fStock.getText().trim());
            if (cod.isEmpty() || nom.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Codigo y nombre son obligatorios.");
                return;
            }
            java.sql.Connection cn = conexionBD.Conexion.getInstancia().getConexion();
            try (java.sql.PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO articulos (codigo, nombre, precio, stock) VALUES (?,?,?,?)")) {
                ps.setString(1, cod);
                ps.setString(2, nom);
                ps.setDouble(3, prec);
                ps.setInt(4, stock);
                ps.executeUpdate();
            }
            cargar();
            javax.swing.JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Precio y stock deben ser numeros.");
        } catch (java.sql.SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminarProducto() {
        int fila = tablaAlmacen.getSelectedRow();
        if (fila < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un producto primero.");
            return;
        }
        String codigo = (String) tablaAlmacen.getValueAt(fila, 0);
        String nombre = (String) tablaAlmacen.getValueAt(fila, 1);
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
            "Eliminar " + nombre + "?", "Confirmar",
            javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm != javax.swing.JOptionPane.YES_OPTION) return;
        java.sql.Connection cn = conexionBD.Conexion.getInstancia().getConexion();
        try (java.sql.PreparedStatement ps = cn.prepareStatement(
                "DELETE FROM articulos WHERE codigo = ?")) {
            ps.setString(1, codigo);
            ps.executeUpdate();
            cargar();
            javax.swing.JOptionPane.showMessageDialog(this, "Producto eliminado.");
        } catch (java.sql.SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "No se puede eliminar: el producto tiene pedidos asociados.", "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrarStockBajo() {
        String input = javax.swing.JOptionPane.showInputDialog(this,
            "Mostrar productos con stock menor a:", "10");
        if (input == null) return;
        try {
            int limite = Integer.parseInt(input.trim());
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
                new String[]{"Codigo", "Producto", "Precio", "Stock"}, 0
            ) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            tablaAlmacen.setModel(model);
            java.sql.Connection cn = conexionBD.Conexion.getInstancia().getConexion();
            try (java.sql.PreparedStatement ps = cn.prepareStatement(
                    "SELECT codigo, nombre, precio, stock FROM articulos " +
                    "WHERE stock < ? ORDER BY stock ASC")) {
                ps.setInt(1, limite);
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        model.addRow(new Object[]{
                            rs.getString("codigo"), rs.getString("nombre"),
                            rs.getDouble("precio"), rs.getInt("stock")
                        });
                    }
                }
            }
            if (model.getRowCount() == 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "No hay productos con stock menor a " + limite + ".");
                cargar();
            }
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ingresa un numero valido.");
        } catch (java.sql.SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
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
        tablaAlmacen = new javax.swing.JTable();
        btnActualizarStock = new javax.swing.JButton();
        btnAgregarProducto = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        btnStockBajo = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("PANEL ALMACÉN");

        tablaAlmacen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Codigo", "Producto", "Precio", "Stock"
            }
        ));
        jScrollPane1.setViewportView(tablaAlmacen);

        btnActualizarStock.setText("Actualizar Stock");

        btnAgregarProducto.setText("Agregar Producto");

        btnEliminarProducto.setText("Eliminar Producto");

        btnStockBajo.setText("Stock Bajo");

        jLabel2.setText("Filtrar por Stock:");

        btnActualizar.setText("Actualizar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 574, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnActualizarStock)
                                .addGap(26, 26, 26)
                                .addComponent(btnAgregarProducto)
                                .addGap(27, 27, 27)
                                .addComponent(btnEliminarProducto)
                                .addGap(38, 38, 38)
                                .addComponent(btnActualizar)))))
                .addContainerGap(265, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnStockBajo)
                .addGap(276, 276, 276))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStockBajo)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnActualizarStock)
                    .addComponent(btnAgregarProducto)
                    .addComponent(btnEliminarProducto)
                    .addComponent(btnActualizar))
                .addContainerGap(133, Short.MAX_VALUE))
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnActualizarStock;
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnStockBajo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaAlmacen;
    // End of variables declaration//GEN-END:variables
}
