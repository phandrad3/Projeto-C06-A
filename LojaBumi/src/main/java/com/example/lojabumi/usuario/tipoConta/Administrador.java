package com.example.lojabumi.usuario.tipoConta;

import com.example.lojabumi.usuario.Usuario;
import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;

public class Administrador extends Usuario {

    public Administrador(int idUsuario, String nome, String dataNasc, String email, String senha) {
        super(idUsuario, nome, dataNasc, email, senha);
    }

    public void alterarPrecoProduto(Produto produto, double novoPreco) {
        produto.atualizarPreco(novoPreco, this);
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
