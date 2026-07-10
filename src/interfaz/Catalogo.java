package interfaz;

import proxy.CatalogoProxy;

/**
 *
 * @author Abel
 */
public class Catalogo extends javax.swing.JPanel {

    private final CatalogoProxy proxy = new CatalogoProxy();
    private Menu menuPadre;
    private java.util.List<clase.Articulo> articulos = new java.util.ArrayList<>();

    public Catalogo() {
        initComponents();
        centrar();
    }

    public Catalogo(Menu menu) {
        initComponents();
        centrar();
        this.menuPadre = menu;
        cargarProductos();
    }

    private void centrar() {
        setLayout(new java.awt.BorderLayout());
        removeAll();
        add(jPanel1, java.awt.BorderLayout.CENTER);
    }

    private void cargarProductos() {
        articulos = proxy.verProductos();

        javax.swing.JButton[] botones = {btnDetalle1, btnDetalle3, btnDetalle2, btnDetalle4,
                                         btnDetalle5, btnDetalle6, btnDetalle7, btnDetalle8};
        javax.swing.JLabel[]  imgs    = {jLabel3,  jLabel29, jLabel28, jLabel26,
                                         jLabel30, jLabel31, jLabel32, jLabel27};
        javax.swing.JLabel[]  descs   = {jLabel24, jLabel20, jLabel22, jLabel18,
                                         jLabel10, jLabel12, jLabel14, jLabel16};
        javax.swing.JLabel[]  precios = {jLabel25, jLabel21, jLabel23, jLabel19,
                                         jLabel11, jLabel13, jLabel15, jLabel17};

        for (int i = 0; i < botones.length; i++) {
            if (i < articulos.size()) {
                clase.Articulo art = articulos.get(i);
                descs[i].setText(art.getNombre());
                precios[i].setText("S/ " + String.format("%.2f", art.getPrecio()));

                java.awt.Dimension dim = new java.awt.Dimension(120, 120);
                imgs[i].setPreferredSize(dim);
                imgs[i].setMinimumSize(dim);
                imgs[i].setMaximumSize(dim);
                imgs[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

                javax.swing.ImageIcon icono = cargarImagen(art.getNombre(), 120, 120);
                if (icono != null) {
                    imgs[i].setIcon(icono);
                    imgs[i].setText("");
                } else {
                    imgs[i].setIcon(null);
                    imgs[i].setText("[ " + art.getNombre() + " ]");
                }

                if (art.getStock() <= 0) {
                    botones[i].setText("AGOTADO");
                    botones[i].setEnabled(false);
                    botones[i].setForeground(java.awt.Color.RED);
                } else {
                    botones[i].setText("Ver detalle");
                    botones[i].setEnabled(true);
                    botones[i].setForeground(null);
                }
                botones[i].setVisible(true);
                imgs[i].setVisible(true);
                descs[i].setVisible(true);
                precios[i].setVisible(true);
            } else {
                botones[i].setVisible(false);
                imgs[i].setVisible(false);
                descs[i].setVisible(false);
                precios[i].setVisible(false);
            }
        }
    }

    /** Carga y escala una imagen desde imagenes/productos/ */
    private javax.swing.ImageIcon cargarImagen(String nombre, int ancho, int alto) {
        try {
            String nombreLimpio = nombre.replace("\"", "");
            java.net.URL url = getClass().getResource("/imagenes/productos/" + nombreLimpio + ".jpg");
            if (url == null) return null;
            java.awt.Image img = new javax.swing.ImageIcon(url).getImage()
                .getScaledInstance(ancho, alto, java.awt.Image.SCALE_SMOOTH);
            return new javax.swing.ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }

    /** Navega al detalle del artículo en el slot indicado */
    private void mostrarDetalle(int indice) {
        if (menuPadre != null && indice < articulos.size()) {
            menuPadre.mostrarDetalle(articulos.get(indice));
        }
    }

    // ── Código generado por NetBeans (no modificar) ───────────────
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnDetalle1 = new javax.swing.JButton();
        btnDetalle2 = new javax.swing.JButton();
        btnDetalle4 = new javax.swing.JButton();
        btnDetalle3 = new javax.swing.JButton();
        btnDetalle5 = new javax.swing.JButton();
        btnDetalle6 = new javax.swing.JButton();
        btnDetalle7 = new javax.swing.JButton();
        btnDetalle8 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("PRODUCTOS");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 50, -1, -1));

        btnDetalle1.setText("Ver detalle");
        btnDetalle1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalle1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnDetalle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 121, -1, -1));

        btnDetalle2.setText("Ver detalle");
        btnDetalle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalle2ActionPerformed(evt);
            }
        });
        jPanel1.add(btnDetalle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 120, -1, -1));

        btnDetalle4.setText("Ver detalle");
        btnDetalle4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalle4ActionPerformed(evt);
            }
        });
        jPanel1.add(btnDetalle4, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 120, -1, -1));

        btnDetalle3.setText("Ver detalle");
        btnDetalle3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalle3ActionPerformed(evt);
            }
        });
        jPanel1.add(btnDetalle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 120, -1, -1));

        btnDetalle5.setText("Ver detalle");
        btnDetalle5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalle5ActionPerformed(evt);
            }
        });
        jPanel1.add(btnDetalle5, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 410, -1, -1));

        btnDetalle6.setText("Ver detalle");
        btnDetalle6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalle6ActionPerformed(evt);
            }
        });
        jPanel1.add(btnDetalle6, new org.netbeans.lib.awtextra.AbsoluteConstraints(245, 410, -1, -1));

        btnDetalle7.setText("Ver detalle");
        btnDetalle7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalle7ActionPerformed(evt);
            }
        });
        jPanel1.add(btnDetalle7, new org.netbeans.lib.awtextra.AbsoluteConstraints(484, 410, -1, -1));

        btnDetalle8.setText("Ver detalle");
        btnDetalle8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalle8ActionPerformed(evt);
            }
        });
        jPanel1.add(btnDetalle8, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 410, -1, -1));

        jLabel3.setPreferredSize(new java.awt.Dimension(150, 150));
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, -1, -1));

        jLabel10.setText("Descripcion");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 620, -1, -1));

        jLabel11.setText("Precio");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 660, -1, -1));

        jLabel12.setText("Descripcion");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 620, -1, -1));

        jLabel13.setText("Precio");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 660, -1, -1));

        jLabel14.setText("Descripcion");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 620, -1, -1));

        jLabel15.setText("Precio");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 660, -1, -1));

        jLabel16.setText("Descripcion");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 620, -1, -1));

        jLabel17.setText("Precio");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 660, -1, -1));

        jLabel18.setText("Descripcion");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 320, -1, -1));

        jLabel19.setText("Precio");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 360, -1, -1));

        jLabel20.setText("Descripcion");
        jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 320, -1, -1));

        jLabel21.setText("Precio");
        jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 360, -1, -1));

        jLabel22.setText("Descripcion");
        jPanel1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 320, -1, -1));

        jLabel23.setText("Precio");
        jPanel1.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 360, -1, -1));

        jLabel24.setText("Descripcion");
        jPanel1.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(39, 324, -1, -1));

        jLabel25.setText("Precio");
        jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(39, 358, -1, -1));

        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/productos/Laptop Pro 15.jpg"))); // NOI18N
        jLabel26.setPreferredSize(new java.awt.Dimension(150, 150));
        jPanel1.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 160, -1, -1));

        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/productos/Webcam Full HD.jpg"))); // NOI18N
        jLabel27.setPreferredSize(new java.awt.Dimension(150, 150));
        jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 460, -1, -1));

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/productos/Auriculares Bluetooth.jpg"))); // NOI18N
        jLabel28.setPreferredSize(new java.awt.Dimension(150, 150));
        jPanel1.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 160, -1, -1));

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/productos/SSD 1TB.jpg"))); // NOI18N
        jLabel29.setPreferredSize(new java.awt.Dimension(150, 150));
        jPanel1.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 160, -1, -1));

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/productos/Monitor 27 4K.jpg"))); // NOI18N
        jLabel30.setPreferredSize(new java.awt.Dimension(150, 150));
        jPanel1.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 460, -1, -1));

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/productos/Mouse Inalambrico.jpg"))); // NOI18N
        jLabel31.setPreferredSize(new java.awt.Dimension(150, 150));
        jPanel1.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 460, -1, -1));

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/productos/Teclado Mecanico.jpg"))); // NOI18N
        jLabel32.setPreferredSize(new java.awt.Dimension(150, 150));
        jPanel1.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 460, -1, -1));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 950, 730));
    }// </editor-fold>//GEN-END:initComponents

    private void btnDetalle1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalle1ActionPerformed
        mostrarDetalle(0);
    }//GEN-LAST:event_btnDetalle1ActionPerformed

    private void btnDetalle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalle3ActionPerformed
        mostrarDetalle(1);
    }//GEN-LAST:event_btnDetalle3ActionPerformed

    private void btnDetalle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalle2ActionPerformed
        mostrarDetalle(2);
    }//GEN-LAST:event_btnDetalle2ActionPerformed

    private void btnDetalle4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalle4ActionPerformed
        mostrarDetalle(3);
    }//GEN-LAST:event_btnDetalle4ActionPerformed

    private void btnDetalle5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalle5ActionPerformed
        mostrarDetalle(4);
    }//GEN-LAST:event_btnDetalle5ActionPerformed

    private void btnDetalle6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalle6ActionPerformed
        mostrarDetalle(5);
    }//GEN-LAST:event_btnDetalle6ActionPerformed

    private void btnDetalle7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalle7ActionPerformed
        mostrarDetalle(6);
    }//GEN-LAST:event_btnDetalle7ActionPerformed

    private void btnDetalle8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalle8ActionPerformed
        mostrarDetalle(7);
    }//GEN-LAST:event_btnDetalle8ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDetalle1;
    private javax.swing.JButton btnDetalle2;
    private javax.swing.JButton btnDetalle3;
    private javax.swing.JButton btnDetalle4;
    private javax.swing.JButton btnDetalle5;
    private javax.swing.JButton btnDetalle6;
    private javax.swing.JButton btnDetalle7;
    private javax.swing.JButton btnDetalle8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
