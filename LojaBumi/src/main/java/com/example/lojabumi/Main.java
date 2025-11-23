package com.example.lojabumi;

import com.example.lojabumi.config.SupabaseConfig;
import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;
import com.example.lojabumi.produtos.tipo.*;
import com.example.lojabumi.usuario.tipoConta.Administrador;
import com.example.lojabumi.usuario.tipoConta.Cliente;
import org.controlsfx.control.ListActionView;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        Administrador adm = new Administrador(1, "Carlos", "10/10/1985", "carlos@admin.com", "admin123");
        Cliente cliente1 = new Cliente(101, "João", "01/01/1990", "joao@email.com", "12345");

        UserDatabase.adicionarUsuario(adm);
        UserDatabase.adicionarUsuario(cliente1);

        // Inicializando produtos/estoque
        List<Map<String, Object>> produtos = SupabaseConfig.getData("produtos", "idProduto", true);

        for (Map<String, Object> produto : produtos) {
            String nome = SupabaseConfig.getString(produto, "nome");
            Double preco = SupabaseConfig.getDouble(produto, "preco");
            Integer quantidade = SupabaseConfig.getInt(produto, "quantidade");
            String tipo = SupabaseConfig.getString(produto, "tipoProduto");

            Produto novoProduto;
            switch (tipo) {
                case "Eletrônicos":
                    novoProduto = new Eletronico(nome, preco);
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

            Estoque.adicionarEstoque(novoProduto, quantidade, adm);
        }



        com.example.lojabumi.HelloApplication.main(args);
    }
}