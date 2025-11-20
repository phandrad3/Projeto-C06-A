package com.example.lojabumi.produtos.tipo;

import com.example.lojabumi.config.SupabaseConfig;
import com.example.lojabumi.produtos.Produto;

import java.util.Locale;

public class Verduras extends Produto {

    public Verduras(String nome, double preco) {
        super(nome, preco);

        String tableName = "produtos";
        String precoFormatado = String.format(Locale.US, "%.2f", preco);

        String jsonInputString = String.format(
                Locale.US,
                "{\"idProduto\": %d, \"nome\": \"%s\", \"quantidade\": %d, \"preco\": %s, \"tipoProduto\": \"%s\"}",
                getId(), nome, 0, precoFormatado, "Verduras"
        );

        System.out.println("DEBUG JSON: " + jsonInputString);

        SupabaseConfig.testInsertData(tableName, jsonInputString);
    }

    }
