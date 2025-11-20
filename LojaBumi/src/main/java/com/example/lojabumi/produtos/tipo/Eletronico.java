package com.example.lojabumi.produtos.tipo;

import com.example.lojabumi.config.SupabaseConfig;
import com.example.lojabumi.produtos.Produto;
import java.util.Locale;

public class Eletronico extends Produto {

    public Eletronico(String nome, double preco) {
        super(nome, preco);

        String tableName = "produtos";
        String precoFormatado = String.format(Locale.US, "%.2f", preco);

        String jsonInputString = String.format(
                Locale.US,
                "{\"idProduto\": %d, \"nome\": \"%s\", \"quantidade\": %d, \"preco\": %s, \"tipoProduto\": \"%s\"}",
                getId(), nome, 0, precoFormatado, "Eletronico"
        );

        System.out.println("DEBUG JSON: " + jsonInputString);

        SupabaseConfig.testInsertData(tableName, jsonInputString);
    }
}





//String tableName = "produtos";
//            String jsonInputString = "{\"idProduto\": \"%d\", \"nome\": \"%s\",\"quantidade\": \"%d\",\"preco\": \"%d\",\"tipoProduto\": \"%s\"}";
//            jsonInputString = String.format(jsonInputString, getId(), nome, 0, preco, "Eletronico");
//            SupabaseConfig.testInsertData(tableName, jsonInputString);
