package sv.edu.utec.modelo;
public class Producto {
    private String codigo;
    private String nombre;
    private String categoria;
    private double precio;
    private int existencia;
    public Producto() { }
    public Producto(String codigo, String nombre, String categoria,
                    double precio, int existencia) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        setPrecio(precio);
        setExistencia(existencia);
    }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.precio = precio;
    }
    public int getExistencia() { return existencia; }
    public void setExistencia(int existencia) {
        if (existencia < 0) {
            throw new IllegalArgumentException("La existencia no puede ser negativa");
        }
        this.existencia = existencia;
    }
    @Override
    public String toString() {
        return String.format("%-6s %-24s %-10s $%7.2f %4d u.",
                codigo, nombre, categoria, precio, existencia);
    }
}
