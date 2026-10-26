package sv.edu.utec.datos;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import sv.edu.utec.modelo.Producto;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
public class MongoProductoRepositorio implements ProductoRepositorio {
    private final MongoCollection<Document> coleccion;
    public MongoProductoRepositorio(MongoDatabase bd) {
        this.coleccion = bd.getCollection("productos");
        // Regla de negocio en la base de datos: el codigo no se repite
        coleccion.createIndex(Indexes.ascending("codigo"),
                new IndexOptions().unique(true));
    }
    @Override
    public void insertar(Producto p) {
        coleccion.insertOne(aDocumento(p));
    }
    @Override
    public Optional<Producto> buscarPorCodigo(String codigo) {
        Document d = coleccion.find(Filters.eq("codigo", codigo)).first();
        return Optional.ofNullable(d).map(this::aProducto);
    }
    @Override
    public List<Producto> listarPorCategoria(String categoria) {
        List<Producto> resultado = new ArrayList<>();
        for (Document d : coleccion.find(Filters.eq("categoria", categoria))
                .sort(Sorts.ascending("nombre"))) {
            resultado.add(aProducto(d));
        }
        return resultado;
    }
    @Override
    public List<Producto> listarTodos() {
        List<Producto> resultado = new ArrayList<>();
        for (Document d : coleccion.find().sort(Sorts.ascending("codigo"))) {
            resultado.add(aProducto(d));
        }
        return resultado;
    }
    @Override
    public boolean actualizarPrecio(String codigo, double nuevoPrecio) {
        UpdateResult r = coleccion.updateOne(
                Filters.eq("codigo", codigo),
                Updates.combine(
                        Updates.set("precio", nuevoPrecio),
                        Updates.currentDate("actualizadoEn")));
        return r.getModifiedCount() > 0;
    }
    @Override
    public boolean eliminar(String codigo) {
        DeleteResult r = coleccion.deleteOne(Filters.eq("codigo", codigo));
        return r.getDeletedCount() > 0;
    }
    // ---- Traduccion objeto <-> documento ----
    private Document aDocumento(Producto p) {
        return new Document("codigo", p.getCodigo())
                .append("nombre", p.getNombre())
                .append("categoria", p.getCategoria())
                .append("precio", p.getPrecio())
                .append("existencia", p.getExistencia())
                .append("creadoEn", new Date());
    }
    private Producto aProducto(Document d) {
        return new Producto(
                d.getString("codigo"),
                d.getString("nombre"),
                d.getString("categoria"),
                d.get("precio", Number.class).doubleValue(),
                d.get("existencia", Number.class).intValue());
    }
}
