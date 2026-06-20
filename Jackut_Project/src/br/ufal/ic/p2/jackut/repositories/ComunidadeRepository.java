package br.ufal.ic.p2.jackut.repositories;

import br.ufal.ic.p2.jackut.models.Comunidade;
import br.ufal.ic.p2.jackut.models.EstadoJackut;
import java.util.List;

/**
 * Repository responsavel pelo acesso controlado as comunidades do estado.
 */
public class ComunidadeRepository {

    private final EstadoJackut estado;

    /**
     * Cria um repository baseado em um estado persistente.
     *
     * @param estado estado do sistema que contem as comunidades cadastradas
     */
    public ComunidadeRepository(EstadoJackut estado) {
        this.estado = estado;
    }

    /**
     * Verifica se existe comunidade com um nome.
     *
     * @param nome nome pesquisado
     * @return {@code true} quando a comunidade existir, ou {@code false} caso contrario
     */
    public boolean existe(String nome) {
        return estado.contemComunidade(nome);
    }

    /**
     * Registra uma comunidade no estado.
     *
     * @param comunidade comunidade que sera cadastrada
     */
    public void adicionar(Comunidade comunidade) {
        estado.adicionarComunidade(comunidade);
    }

    /**
     * Busca uma comunidade por nome.
     *
     * @param nome nome pesquisado
     * @return comunidade encontrada, ou {@code null} quando nao houver cadastro
     */
    public Comunidade buscarPorNome(String nome) {
        return estado.buscarComunidade(nome);
    }

    /**
     * Lista todas as comunidades cadastradas em ordem de criacao.
     *
     * @return lista com as comunidades cadastradas
     */
    public List<Comunidade> listar() {
        return estado.listarComunidades();
    }

    /**
     * Remove uma comunidade cadastrada.
     *
     * @param nome nome da comunidade removida
     */
    public void remover(String nome) {
        estado.removerComunidade(nome);
    }
}
