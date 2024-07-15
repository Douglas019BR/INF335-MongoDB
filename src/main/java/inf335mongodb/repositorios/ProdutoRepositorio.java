package inf335mongodb.repositorios;

import inf335mongodb.entidades.Produto;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ProdutoRepositorio {

    private final MongoCollection<Document> collection;

    public ProdutoRepositorio() {
        String username = "root";
        String password = "example";
        String databaseName = "admin";
        String connectionString = "mongodb://"+ username + ":" + password + "@localhost:27017/" + databaseName;

        ConnectionString connString = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .build();

        MongoClient mongoClient = MongoClients.create(settings);

        MongoDatabase database = mongoClient.getDatabase("inf335-trab6");
        collection = database.getCollection("produtos");
    }

    public List<Produto> listarProdutos() {
        List<Produto> produtos = new ArrayList<>();
        for (Document doc : collection.find()) {
            Produto produto = new Produto(
                    doc.getInteger("codigo"),
                    doc.getString("nome"),
                    doc.getString("descricao"),
                    doc.getDouble("valor"),
                    doc.getString("estado")
            );
            produtos.add(produto);
        }
        return produtos;
    }

    public void inserirProduto(Produto produto) {
        Document doc = new Document("codigo", produto.getCodigo())
                .append("nome", produto.getNome())
                .append("descricao", produto.getDescricao())
                .append("valor", produto.getValor())
                .append("estado", produto.getEstado());
        collection.insertOne(doc);
    }

    public void atualizarValorProduto(int codigo, double novoValor) {
        Document query = new Document("codigo", codigo);
        Document update = new Document("$set", new Document("valor", novoValor));
        collection.updateOne(query, update);
    }

    public void removerProduto(int codigo) {
        Document query = new Document("codigo", codigo);
        collection.deleteOne(query);
    }
}
