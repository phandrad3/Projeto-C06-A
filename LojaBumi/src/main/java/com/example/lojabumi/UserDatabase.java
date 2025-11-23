package com.example.lojabumi;

import com.example.lojabumi.usuario.Usuario;
import com.example.lojabumi.usuario.tipoConta.Cliente;
import com.example.lojabumi.usuario.tipoConta.Administrador;
import java.util.ArrayList;

public class UserDatabase {

    private static ArrayList<Object> usuarios = new ArrayList<>();

    public static void adicionarUsuario(Object usuario) {
        usuarios.add(usuario);
    }

    public static Object autenticar(String email, String senha) {
        for (Object u : usuarios) {
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

        Usuario usuarioBD = Usuario.buscarUsuarioPorEmail(email);

        if (usuarioBD != null && usuarioBD.getSenha().equals(senha)) {
            usuarios.add(usuarioBD);
            usuarioLogado = usuarioBD;
            return usuarioBD;
        }

        return null;
    }

    private static Usuario usuarioLogado;

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Usuario usuario) {
        usuarioLogado = usuario;
    }
}