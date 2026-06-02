package br.ufal.ic.p2.jackut.repositories;

import br.ufal.ic.p2.jackut.models.Sessao;
import br.ufal.ic.p2.jackut.models.Usuario;
import java.util.LinkedHashMap;
import java.util.Map;

public class SessaoRepository {

    private final Map<String, Sessao> sessoes;
    private int proximoId;

    public SessaoRepository() {
        this.sessoes = new LinkedHashMap<String, Sessao>();
        this.proximoId = 1;
    }

    public Sessao criarSessao(Usuario usuario) {
        String id = String.valueOf(proximoId);
        proximoId++;

        Sessao sessao = new Sessao(id, usuario.getLogin());
        sessoes.put(id, sessao);
        return sessao;
    }

    public Sessao buscarPorId(String id) {
        return sessoes.get(id);
    }

    public void limpar() {
        sessoes.clear();
        proximoId = 1;
    }
}
