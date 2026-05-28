package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class EstadoJackut implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, Usuario> usuarios;

    public EstadoJackut() {
        this.usuarios = new LinkedHashMap<String, Usuario>();
    }

    public boolean contemUsuario(String login) {
        return usuarios.containsKey(login);
    }

    public void adicionarUsuario(Usuario usuario) {
        usuarios.put(usuario.getLogin(), usuario);
    }

    public Usuario buscarUsuario(String login) {
        return usuarios.get(login);
    }

    public void limpar() {
        usuarios.clear();
    }
}
