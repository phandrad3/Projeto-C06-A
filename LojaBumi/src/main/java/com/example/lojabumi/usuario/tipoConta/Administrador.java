package com.example.lojabumi.usuario.tipoConta;

import com.example.lojabumi.usuario.Usuario;
import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;

public class Administrador extends Usuario {

    public Administrador(int idUsuario, String nome, String dataNasc, String email, String senha) {
        super(idUsuario, nome, dataNasc, email, senha);
    }

    public void alterarPrecoProduto(Produto produto, double novoPreco) {
        if (produto == null) {
            System.err.println("Erro: Produto inválido.");
            return;
        }

        if (novoPreco <= 0) {
            System.err.println("Erro: preço inválido.");
            return;
        }

        produto.atualizarPreco(novoPreco);
        System.out.println("Preço do produto '" + produto.getNome() + "' alterado para R$ " + String.format("%.2f", novoPreco));
    }

    public void adicionarProdutoAoEstoque(Produto produto, int quantidade) {
        Estoque.adicionarEstoque(produto, quantidade, this);
    }

    public void atualizarQuantidadeEstoque(Produto produto, int novaQuantidade) {
        Estoque.atualizarEstoque(produto, novaQuantidade, this);
    }

    public void removerProdutoDoEstoque(Produto produto) {
        Estoque.removerProduto(produto, this);
    }

    public void listarEstoque() {
        Estoque.listarProdutosEmEstoque(this);
    }

    @Override
    public boolean isAdministrador() {
        return true;
    }
}
