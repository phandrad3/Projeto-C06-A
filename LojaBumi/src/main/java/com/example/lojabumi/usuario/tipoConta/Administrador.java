package com.example.lojabumi.usuario.tipoConta;

import com.example.lojabumi.config.SupabaseConfig;
import com.example.lojabumi.usuario.Usuario;
import com.example.lojabumi.utilitarios.Verificacoes;

public class Administrador extends Usuario {

    public Administrador(int idUsuario, String nome, String dataNasc, String email, String senha, boolean inserirNoBanco) {
        super(idUsuario, nome, dataNasc, email, senha);
        if (inserirNoBanco) {
            String tableName = "usuario";
            String dataISO = Verificacoes.converterDataParaISO(dataNasc);
            String jsonInputString = "{\"idUsuario\": \"%d\", \"nomeUsuario\": \"%s\",\"dataNasc\": \"%s\",\"email\": \"%s\",\"senha\": \"%s\",\"tipoUsuario\": \"%s\"}";
            jsonInputString = String.format(jsonInputString, idUsuario, nome, dataISO, email, senha, "Administrador");
            SupabaseConfig.insertData(tableName, jsonInputString);
        }
    }

    @Override
    public boolean alterarEstoque() {
        return true;
    }

    @Override
    public boolean alterarPreco() {
        return true;
    }

    @Override
    public boolean addEstoque() {
        return true;
    }

    @Override
    public boolean removerProduto() {
        return true;
    }
}