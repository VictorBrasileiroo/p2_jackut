package br.ufal.ic.p2.jackut.models;

public class Sessao {

    private final String id;
    private final String loginUsuario;

    public Sessao(String id, String loginUsuario) {
        this.id = id;
        this.loginUsuario = loginUsuario;
    }

    public String getId() {
        return id;
    }

    public String getLoginUsuario() {
        return loginUsuario;
    }
}
