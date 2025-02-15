package inf335mongodb;

import inf335mongodb.entidades.Produto;
import inf335mongodb.repositorios.ProdutoRepositorio;

public class App {
    public static void main(String[] args) {
        ProdutoRepositorio repositorio = new ProdutoRepositorio();
        repositorio.removerProduto(7);
        System.out.println("Lista de Produtos:");
        for (Produto produto : repositorio.listarProdutos()) {
            System.out.println(produto);
        }

        Produto novoProduto = new Produto(7, "Prod7", "Descrição do produto 7", 2000.0, "Novo");
        repositorio.inserirProduto(novoProduto);
        System.out.println("Produto inserido: " + novoProduto);

        repositorio.atualizarValorProduto(7, 1300.0);
        System.out.println("Valor do produto 7 atualizado.");
        System.out.println("Lista de Produtos com produto 7 atualizado:");
        for (Produto produto : repositorio.listarProdutos()) {
            System.out.println(produto);
        }
        repositorio.removerProduto(7);
        System.out.println("Produto com código 7 removido.");

        System.out.println("Lista de Produtos atualizada:");
        for (Produto produto : repositorio.listarProdutos()) {
            System.out.println(produto);
        }
    }
}
