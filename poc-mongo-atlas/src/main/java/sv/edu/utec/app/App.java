package sv.edu.utec.app;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;
import sv.edu.utec.datos.ConexionMongo;
import sv.edu.utec.datos.MongoProductoRepositorio;
import sv.edu.utec.datos.ProductoRepositorio;
import sv.edu.utec.modelo.Producto;

public class App {
    public static void main(String[] args) {
        try {
            ProductoRepositorio repo =
                    new MongoProductoRepositorio(ConexionMongo.getBaseDeDatos());
            // 0. Limpieza: la demo se puede ejecutar varias veces
            repo.eliminar("P-001");
            repo.eliminar("P-002");
            repo.eliminar("P-003");
            // 1. CREATE
            repo.insertar(new Producto("P-001", "Cafe molido 400 g", "Abarrotes", 4.75, 120));
            repo.insertar(new Producto("P-002", "Frijol rojo 1 lb", "Abarrotes", 1.35, 300));
            repo.insertar(new Producto("P-003", "Detergente 1 kg", "Limpieza", 3.10, 80));
            System.out.println("1) Insertados 3 productos");
            // 2. READ (uno y varios)
            repo.buscarPorCodigo("P-002")
                    .ifPresent(p -> System.out.println("2) Encontrado: " + p));
            System.out.println(" Categoria Abarrotes:");
            repo.listarPorCategoria("Abarrotes")
                    .forEach(p -> System.out.println(" " + p));
            // 3. UPDATE
            System.out.println("3) Precio P-001 actualizado: "
                    + repo.actualizarPrecio("P-001", 4.95));
            // 4. DELETE
            System.out.println("4) P-003 eliminado: " + repo.eliminar("P-003"));
            System.out.println("--- Inventario final ---");
            repo.listarTodos().forEach(System.out::println);
            // 5. La base de datos protege la integridad: codigo duplicado
            try {
                repo.insertar(new Producto("P-001", "Duplicado", "Abarrotes", 1.00, 1));
            } catch (MongoWriteException e) {
                if (e.getError().getCategory() == ErrorCategory.DUPLICATE_KEY) {
                    System.out.println("5) Rechazado por el indice unico: "
                            + "P-001 ya existe");
                } else {
                    throw e;
                }
            }
        } catch (MongoException e) {
            System.err.println("Error de MongoDB: " + e.getMessage());
        } finally {
            ConexionMongo.cerrar();
        }
    }
}