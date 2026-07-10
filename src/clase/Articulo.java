package clase;

public class Articulo implements Producto {

    private int id;
    private String codigo;
    private String nombre;
    private double precio;
    private int stock;
    private int id_categorias;

    public Articulo(int id, String codigo, String nombre,
            double precio, int stock, int id_categorias) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.id_categorias = id_categorias;
    }

    public int getId() { return id; }
    public String getCodigo() { return codigo; }
    public int getStock() { return stock; }
    public int getCategoria() { return id_categorias; }

    @Override public String getNombre() { return nombre; }
    @Override public double getPrecio() { return precio; }
}
