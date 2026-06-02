package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String login;
    private final String senha;
    private final Map<String, String> perfil;
    private Set<String> amigos;
    private Set<String> convitesEnviados;

    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.perfil = new LinkedHashMap<String, String>();
        this.perfil.put("nome", nome == null ? "" : nome);
        this.amigos = new LinkedHashSet<String>();
        this.convitesEnviados = new LinkedHashSet<String>();
    }

    public String getLogin() {
        return login;
    }

    public boolean senhaConfere(String senhaInformada) {
        return senha.equals(senhaInformada);
    }

    public boolean possuiAtributo(String atributo) {
        return perfil.containsKey(atributo);
    }

    public String getAtributo(String atributo) {
        String valor = perfil.get(atributo);
        if (valor == null) {
            return "";
        }

        return valor;
    }

    public void editarPerfil(String atributo, String valor) {
        perfil.put(atributo, valor == null ? "" : valor);
    }

    public void solicitarAmizade(String loginAmigo) {
        garantirRelacionamentos();
        convitesEnviados.add(loginAmigo);
    }

    public boolean possuiConviteEnviado(String loginAmigo) {
        garantirRelacionamentos();
        return convitesEnviados.contains(loginAmigo);
    }

    public void removerConviteEnviado(String loginAmigo) {
        garantirRelacionamentos();
        convitesEnviados.remove(loginAmigo);
    }

    public void adicionarAmigoConfirmado(String loginAmigo) {
        garantirRelacionamentos();
        amigos.add(loginAmigo);
    }

    public boolean ehAmigo(String loginAmigo) {
        garantirRelacionamentos();
        return amigos.contains(loginAmigo);
    }

    public List<String> getAmigos() {
        garantirRelacionamentos();
        return Collections.unmodifiableList(new ArrayList<String>(amigos));
    }

    private void garantirRelacionamentos() {
        if (amigos == null) {
            amigos = new LinkedHashSet<String>();
        }

        if (convitesEnviados == null) {
            convitesEnviados = new LinkedHashSet<String>();
        }
    }
}
