package com.example.lojabumi.usuario;

import com.example.lojabumi.usuario.tipoConta.Cliente;
import com.example.lojabumi.usuario.tipoConta.Administrador;
import com.example.lojabumi.config.SupabaseConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public abstract class Usuario implements Permissao {
    protected int idUsuario;
    protected String nome;
    protected String dataNasc;
    protected String email;
    protected String senha;

    private static ArrayList<Object> cacheUsuarios = new ArrayList<>();
    private static Usuario usuarioLogado;

    public Usuario(int idUsuario, String nome, String dataNasc, String email, String senha) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Usuario usuario) {
        usuarioLogado = usuario;
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

    public static Usuario registrarUsuario(int idUsuario, String nome, String dataNasc,
                                           String email, String senha, String tipoUsuario) {
        Usuario usuario;
        if (tipoUsuario.equalsIgnoreCase("Cliente")) {
            usuario = new Cliente(idUsuario, nome, dataNasc, email, senha, true);
        } else if (tipoUsuario.equalsIgnoreCase("Administrador")) {
            usuario = new Administrador(idUsuario, nome, dataNasc, email, senha, true);
        } else {
            throw new IllegalArgumentException("Tipo de usuário inválido: " + tipoUsuario);
        }

        cacheUsuarios.add(usuario);

        return usuario;
    }

    public static Usuario validarCredenciais(String email, String senha) {
        for (Object u : cacheUsuarios) {
            if (u instanceof Cliente) {
                Cliente c = (Cliente) u;
                if (c.getEmail().equals(email) && c.getSenha().equals(senha)) {
                    usuarioLogado = c;
                    return c;
                }
            }

            if (u instanceof Administrador) {
                Administrador a = (Administrador) u;
                if (a.getEmail().equals(email) && a.getSenha().equals(senha)) {
                    usuarioLogado = a;
                    return a;
                }
            }
        }

        Usuario usuarioBD = buscarUsuarioPorEmail(email, senha);

        if (usuarioBD != null) {
            cacheUsuarios.add(usuarioBD);
            usuarioLogado = usuarioBD;
            return usuarioBD;
        }

        return null;
    }

    public static Usuario buscarUsuarioPorEmail(String email, String senha) {
        String resposta = SupabaseConfig.getUserByEmail(email);

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
                String senhaArmazenada = jsonUsuario.getString("senha");
                String tipoUsuario = jsonUsuario.getString("tipoUsuario");

                String senhaInformadaCriptografada = SupabaseConfig.sha256(senha);

                if (senhaInformadaCriptografada == null || !senhaInformadaCriptografada.equals(senhaArmazenada)) {
                    return null;
                }

                String dataBrasil = converterDataParaBrasil(dataNasc);

                if (tipoUsuario.equals("Cliente")) {
                    return new Cliente(idUsuario, nome, dataBrasil, emailBD, senhaArmazenada, false);
                } else if (tipoUsuario.equals("Administrador")) {
                    return new Administrador(idUsuario, nome, dataBrasil, emailBD, senhaArmazenada, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}