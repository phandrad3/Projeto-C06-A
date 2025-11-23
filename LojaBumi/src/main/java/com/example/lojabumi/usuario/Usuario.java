package com.example.lojabumi.usuario;

import com.example.lojabumi.UserDatabase;
import com.example.lojabumi.usuario.Permissao;
import com.example.lojabumi.usuario.tipoConta.Cliente;
import com.example.lojabumi.usuario.tipoConta.Administrador;
import com.example.lojabumi.config.SupabaseConfig;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Usuario implements Permissao {
    protected int idUsuario;
    protected String nome;
    protected String dataNasc;
    protected String email;
    protected String senha;

    public Usuario(int idUsuario, String nome, String dataNasc, String email, String senha) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.email = email;
        this.senha = senha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public static String converterDataParaISO(String dataBrasil) {
        String[] partes = dataBrasil.split("/");
        if (partes.length != 3) {
            throw new IllegalArgumentException("Formato de data inválido. Use dd/MM/yyyy.");
        }
        try {
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);
            if (dia < 1 || dia > 31 || mes < 1 || mes > 12 || ano < 1900 || ano > 2100) {
                throw new IllegalArgumentException("Data inválida.");
            }
            return String.format("%04d-%02d-%02d", ano, mes, dia);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de data inválido. Use dd/MM/yyyy.");
        }
    }

    public static String converterDataParaBrasil(String dataISO) {
        String[] partes = dataISO.split("-");
        if (partes.length != 3) return dataISO;
        return partes[2] + "/" + partes[1] + "/" + partes[0];
    }

    public static Usuario cadastrarUsuario(int idUsuario, String nome, String dataNasc,
                                           String email, String senha, String tipoUsuario) {
        Usuario usuario;
        if (tipoUsuario.equalsIgnoreCase("Cliente")) {
            usuario = new Cliente(idUsuario, nome, dataNasc, email, senha, true);
        } else if (tipoUsuario.equalsIgnoreCase("Administrador")) {
            usuario = new Administrador(idUsuario, nome, dataNasc, email, senha, true);
        } else {
            throw new IllegalArgumentException("Tipo de usuário inválido: " + tipoUsuario);
        }

        UserDatabase.adicionarUsuario(usuario);

        return usuario;
    }

    public static Usuario buscarUsuarioPorEmail(String email) {
        String resposta = SupabaseConfig.testSelectUserByEmail(email);

        if (resposta == null || resposta.isEmpty() || resposta.equals("[]")) {
            return null;
        }

        try {
            JSONArray jsonArray = new JSONArray(resposta);

            if (jsonArray.length() > 0) {
                JSONObject jsonUsuario = jsonArray.getJSONObject(0);

                int idUsuario = jsonUsuario.getInt("idUsuario");
                String nome = jsonUsuario.getString("nomeUsuario");
                String dataNasc = jsonUsuario.getString("dataNasc");
                String emailBD = jsonUsuario.getString("email");
                String senha = jsonUsuario.getString("senha");
                String tipoUsuario = jsonUsuario.getString("tipoUsuario");

                String dataBrasil = converterDataParaBrasil(dataNasc);

                if (tipoUsuario.equals("Cliente")) {
                    return new Cliente(idUsuario, nome, dataBrasil, emailBD, senha, false);
                } else if (tipoUsuario.equals("Administrador")) {
                    return new Administrador(idUsuario, nome, dataBrasil, emailBD, senha, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
