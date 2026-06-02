package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.services.JackutService;

public class Facade {

    private final JackutService service;

    public Facade() {
        this.service = new JackutService();
    }

    public void zerarSistema() {
        service.zerarSistema();
    }

    public void criarUsuario(String login, String senha, String nome) {
        service.criarUsuario(login, senha, nome);
    }

    public String abrirSessao(String login, String senha) {
        return service.abrirSessao(login, senha);
    }

    public String getAtributoUsuario(String login, String atributo) {
        return service.getAtributoUsuario(login, atributo);
    }

    public void editarPerfil(String id, String atributo, String valor) {
        service.editarPerfil(id, atributo, valor);
    }

    public void encerrarSistema() {
        service.encerrarSistema();
    }
}
