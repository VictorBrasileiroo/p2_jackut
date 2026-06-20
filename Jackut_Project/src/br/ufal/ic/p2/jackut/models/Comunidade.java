package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Representa uma comunidade do Jackut.
 *
 * <p>A comunidade possui nome unico, descricao, dono e membros em ordem de
 * entrada. A colecao interna de membros nao e exposta para modificacao.</p>
 */
public class Comunidade implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Nome unico da comunidade.
     */
    private final String nome;

    /**
     * Descricao publica da comunidade.
     */
    private final String descricao;

    /**
     * Login do usuario dono da comunidade.
     */
    private final String dono;

    /**
     * Logins dos membros da comunidade.
     */
    private Set<String> membros;

    /**
     * Cria uma comunidade com seu dono como primeiro membro.
     *
     * @param nome nome unico da comunidade
     * @param descricao descricao da comunidade
     * @param dono login do usuario dono
     */
    public Comunidade(String nome, String descricao, String dono) {
        this.nome = nome;
        this.descricao = descricao == null ? "" : descricao;
        this.dono = dono;
        this.membros = new LinkedHashSet<String>();
        this.membros.add(dono);
    }

    /**
     * Retorna o nome da comunidade.
     *
     * @return nome unico da comunidade
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna a descricao da comunidade.
     *
     * @return descricao da comunidade
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Retorna o login do dono da comunidade.
     *
     * @return login do usuario dono
     */
    public String getDono() {
        return dono;
    }

    /**
     * Verifica se um usuario participa da comunidade.
     *
     * @param login login do usuario consultado
     * @return {@code true} quando o usuario for membro, ou {@code false} caso contrario
     */
    public boolean possuiMembro(String login) {
        garantirMembros();
        return membros.contains(login);
    }

    /**
     * Adiciona um usuario como membro da comunidade.
     *
     * @param login login do novo membro
     */
    public void adicionarMembro(String login) {
        garantirMembros();
        membros.add(login);
    }

    /**
     * Remove um usuario da comunidade.
     *
     * @param login login do membro removido
     */
    public void removerMembro(String login) {
        garantirMembros();
        membros.remove(login);
    }

    /**
     * Retorna os membros em ordem de entrada.
     *
     * @return lista imutavel de logins dos membros
     */
    public List<String> getMembros() {
        garantirMembros();
        return Collections.unmodifiableList(new ArrayList<String>(membros));
    }

    /**
     * Garante que a colecao de membros exista apos desserializacao.
     */
    private void garantirMembros() {
        if (membros == null) {
            membros = new LinkedHashSet<String>();
        }
    }
}
