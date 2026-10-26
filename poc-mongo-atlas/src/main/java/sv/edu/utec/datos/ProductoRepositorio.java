package sv.edu.utec.datos;
import sv.edu.utec.modelo.Producto;
import java.util.List;
import java.util.Optional;
/**
 * Contrato de acceso a datos. MongoDB, Firestore o PostgreSQL
 * solo cambian la clase que lo implementa; el resto del programa no se entera.
 */
public interface ProductoRepositorio {
    void insertar(Producto producto);
    Optional<Producto> buscarPorCodigo(String codigo);
    List<Producto> listarPorCategoria(String categoria);
    List<Producto> listarTodos();
    boolean actualizarPrecio(String codigo, double nuevoPrecio);
    boolean eliminar(String codigo);
}
