package com.example.lojabumi;

import com.example.lojabumi.config.SupabaseConfig;
import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;
import com.example.lojabumi.produtos.tipo.*;
import com.example.lojabumi.usuario.tipoConta.Sistema;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        Sistema sistema = new Sistema(1, "Sistema", "", "", "sys2025");

        List<Map<String, Object>> produtos = SupabaseConfig.getData("produtos", "idProduto", true);

        for (Map<String, Object> produto : produtos) {
            String nome = SupabaseConfig.getString(produto, "nome");
            Double preco = SupabaseConfig.getDouble(produto, "preco");
            Integer quantidade = SupabaseConfig.getInt(produto, "quantidade");
            String tipo = SupabaseConfig.getString(produto, "tipoProduto");

            Produto novoProduto;
            switch (tipo) {
                case "Eletrônicos":
                    novoProduto = new Eletronicos(nome, preco);
                    break;
                case "Frutas":
                    novoProduto = new Frutas(nome, preco);
                    break;
                case "Frios":
                    novoProduto = new Frios(nome, preco);
                    break;
                case "Verduras":
                    novoProduto = new Verduras(nome, preco);
                    break;
                default:
                    novoProduto = new NaoPereciveis(nome, preco);
            }

            Estoque.adicionarEstoque(novoProduto, quantidade, sistema);
        }
        com.example.lojabumi.HelloApplication.main(args);
    }
}