package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String login;
    private final String senha;
    private final Map<String, String> perfil;

    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.perfil = new LinkedHashMap<String, String>();
        this.perfil.put("nome", nome == null ? "" : nome);
    }

    public String getLogin() {
        return login;
    }

    public boolean senhaConfere(String senhaInformada) {
        return senha.equals(senhaInformada);
    }

    public String getAtributo(String atributo) {
        String valor = perfil.get(atributo);
        if (valor == null) {
            return "";
        }

        return valor;
    }
}
