package com.example.lojabumi.usuario.tipoConta;

import com.example.lojabumi.config.SupabaseConfig;
import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;
import com.example.lojabumi.usuario.Usuario;
import javafx.scene.control.Alert;

import java.util.HashMap;
import java.util.Map;

public class Cliente extends Usuario {
    private Map<Produto, Integer> carrinho = new HashMap<>();

    public Cliente(int idUsuario, String nome, String dataNasc, String email, String senha, boolean inserirNoBanco) {
        super(idUsuario, nome, dataNasc, email, senha);
        if (inserirNoBanco) {
            String tableName = "usuario";
            String dataISO = Usuario.converterDataParaISO(dataNasc);
            String jsonInputString = "{\"idUsuario\": \"%d\", \"nomeUsuario\": \"%s\",\"dataNasc\": \"%s\",\"email\": \"%s\",\"senha\": \"%s\",\"tipoUsuario\": \"%s\"}";
            jsonInputString = String.format(jsonInputString, idUsuario, nome, dataISO, email, senha, "Cliente");
            SupabaseConfig.insertData(tableName, jsonInputString);
        }
    }

    public Map<Produto, Integer> getCarrinho() {
        return carrinho;
    }

    public void addProduto(Produto produto) {
        int quantidadeCarrinho = carrinho.getOrDefault(produto, 0);

        if (quantidadeCarrinho >= Estoque.getEstoque(produto)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Não temos mais estoque desse produto!");
            alert.showAndWait();
            System.out.println("Estoque insuficiente");
            return;
        }
        new Alert(Alert.AlertType.INFORMATION, "Produto adicionado no carrinho!").showAndWait();
        carrinho.put(produto, quantidadeCarrinho + 1);
        System.out.println("Produto '" + produto.getNome() + "' adicionado ao carrinho.");
    }

    @Override
    public boolean alterarEstoque() {
        return false;
    }

    @Override
    public boolean alterarPreco() {
        return false;
    }

    @Override
    public boolean addEstoque() {
        return false;
    }

    @Override
    public boolean removerProduto() {
        return false;
    }


}
