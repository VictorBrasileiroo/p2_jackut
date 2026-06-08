package br.ufal.ic.p2.jackut.repositories;

import br.ufal.ic.p2.jackut.models.Sessao;
import br.ufal.ic.p2.jackut.models.Usuario;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Repository responsavel por sessoes abertas em memoria.
 *
 * <p>As sessoes sao temporarias e nao fazem parte do estado persistido do
 * Jackut.</p>
 */
public class SessaoRepository {

    private final Map<String, Sessao> sessoes;
    private int proximoId;

    /**
     * Cria um repository de sessoes vazio.
     */
    public SessaoRepository() {
        this.sessoes = new LinkedHashMap<String, Sessao>();
        this.proximoId = 1;
    }

    /**
     * Cria uma sessao para um usuario autenticado.
     *
     * @param usuario usuario autenticado
     * @return sessao criada para o usuario
     */
    public Sessao criarSessao(Usuario usuario) {
        String id = String.valueOf(proximoId);
        proximoId++;

        Sessao sessao = new Sessao(id, usuario.getLogin());
        sessoes.put(id, sessao);
        return sessao;
    }

    /**
     * Busca uma sessao aberta por id.
     *
     * @param id identificador da sessao
     * @return sessao encontrada, ou {@code null} quando a sessao nao existir
     */
    public Sessao buscarPorId(String id) {
        return sessoes.get(id);
    }

    /**
     * Remove todas as sessoes abertas e reinicia o contador de ids.
     */
    public void limpar() {
        sessoes.clear();
        proximoId = 1;
    }
}
