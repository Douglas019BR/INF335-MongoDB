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
            Integer codigo = doc.getInteger("Codigo");
            String nome = doc.getString("Nome");
            String descricao = doc.getString("Descrição");
            Double valor = null;
            String estado = doc.getString("Estado");

            if (codigo == null) codigo = doc.getInteger("produto_id");
            if (nome == null) nome = doc.getString("nome");
            if (descricao == null) descricao = doc.getString("descricao");

            if (doc.get("Valor") instanceof Double) {
                valor = doc.getDouble("Valor");
            } else if (doc.get("Valor") instanceof Integer) {
                valor = ((Integer) doc.get("Valor")).doubleValue();
            } else if (doc.get("preco") instanceof Double) {
                valor = doc.getDouble("preco");
            } else if (doc.get("preco") instanceof Integer) {
                valor = ((Integer) doc.get("preco")).doubleValue();
            }

            if (codigo == null || nome == null || descricao == null || valor == null) {
                System.err.println("Documento inválido encontrado: " + doc.toJson());
                continue;
            }

            Produto produto = new Produto(codigo, nome, descricao, valor, estado);
            produtos.add(produto);
        }
        return produtos;
    }

    public void inserirProduto(Produto produto) {
        Document doc = new Document("Codigo", produto.getCodigo())
                .append("Nome", produto.getNome())
                .append("Descrição", produto.getDescricao())
                .append("Valor", produto.getValor())
                .append("Estado", produto.getEstado());
        collection.insertOne(doc);
    }

    public void atualizarValorProduto(int codigo, double novoValor) {
        Document query = new Document("Codigo", codigo);
        Document update = new Document("$set", new Document("Valor", novoValor));
        collection.updateOne(query, update);
    }

    public void removerProduto(int codigo) {
        Document query = new Document("Codigo", codigo);
        collection.deleteOne(query);
    }
}
