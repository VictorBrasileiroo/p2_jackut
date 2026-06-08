package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Representa um usuario cadastrado no Jackut.
 *
 * <p>O usuario controla seu login, senha, perfil, amizades, convites enviados
 * e fila de recados recebidos. As colecoes internas sao protegidas contra
 * modificacao externa.</p>
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Login unico do usuario.
     */
    private final String login;

    /**
     * Senha usada para autenticacao do usuario.
     */
    private final String senha;

    /**
     * Atributos dinamicos do perfil do usuario.
     */
    private final Map<String, String> perfil;

    /**
     * Logins dos amigos confirmados.
     */
    private Set<String> amigos;

    /**
     * Logins dos usuarios que receberam convite de amizade pendente.
     */
    private Set<String> convitesEnviados;

    /**
     * Fila de recados recebidos e ainda nao lidos.
     */
    private Queue<Recado> recadosRecebidos;

    /**
     * Cria um usuario com login, senha e nome inicial.
     *
     * @param login login unico do usuario
     * @param senha senha usada para autenticacao
     * @param nome nome inicial armazenado no perfil
     */
    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.perfil = new LinkedHashMap<String, String>();
        this.perfil.put("nome", nome == null ? "" : nome);
        this.amigos = new LinkedHashSet<String>();
        this.convitesEnviados = new LinkedHashSet<String>();
        this.recadosRecebidos = new LinkedList<Recado>();
    }

    /**
     * Retorna o login do usuario.
     *
     * @return login unico do usuario
     */
    public String getLogin() {
        return login;
    }

    /**
     * Verifica se a senha informada corresponde a senha do usuario.
     *
     * @param senhaInformada senha recebida para autenticacao
     * @return {@code true} quando a senha estiver correta, ou {@code false} caso contrario
     */
    public boolean senhaConfere(String senhaInformada) {
        return senha.equals(senhaInformada);
    }

    /**
     * Verifica se o perfil possui um atributo preenchido.
     *
     * @param atributo nome do atributo consultado
     * @return {@code true} quando o atributo existir no perfil, ou {@code false} caso contrario
     */
    public boolean possuiAtributo(String atributo) {
        return perfil.containsKey(atributo);
    }

    /**
     * Retorna o valor de um atributo de perfil.
     *
     * @param atributo nome do atributo consultado
     * @return valor do atributo, ou string vazia quando o valor armazenado for nulo
     */
    public String getAtributo(String atributo) {
        String valor = perfil.get(atributo);
        if (valor == null) {
            return "";
        }

        return valor;
    }

    /**
     * Altera ou cria um atributo no perfil do usuario.
     *
     * @param atributo nome do atributo alterado
     * @param valor novo valor do atributo
     */
    public void editarPerfil(String atributo, String valor) {
        perfil.put(atributo, valor == null ? "" : valor);
    }

    /**
     * Registra um convite de amizade enviado para outro usuario.
     *
     * @param loginAmigo login do usuario convidado
     */
    public void solicitarAmizade(String loginAmigo) {
        garantirRelacionamentos();
        convitesEnviados.add(loginAmigo);
    }

    /**
     * Verifica se ja existe convite enviado para um usuario.
     *
     * @param loginAmigo login do usuario consultado
     * @return {@code true} quando existir convite pendente, ou {@code false} caso contrario
     */
    public boolean possuiConviteEnviado(String loginAmigo) {
        garantirRelacionamentos();
        return convitesEnviados.contains(loginAmigo);
    }

    /**
     * Remove um convite de amizade enviado.
     *
     * @param loginAmigo login do usuario cujo convite sera removido
     */
    public void removerConviteEnviado(String loginAmigo) {
        garantirRelacionamentos();
        convitesEnviados.remove(loginAmigo);
    }

    /**
     * Adiciona um usuario a lista de amigos confirmados.
     *
     * @param loginAmigo login do amigo confirmado
     */
    public void adicionarAmigoConfirmado(String loginAmigo) {
        garantirRelacionamentos();
        amigos.add(loginAmigo);
    }

    /**
     * Verifica se ha amizade confirmada com outro usuario.
     *
     * @param loginAmigo login do usuario consultado
     * @return {@code true} quando o usuario for amigo confirmado, ou {@code false} caso contrario
     */
    public boolean ehAmigo(String loginAmigo) {
        garantirRelacionamentos();
        return amigos.contains(loginAmigo);
    }

    /**
     * Retorna os amigos confirmados em ordem de confirmacao.
     *
     * @return lista imutavel contendo os logins dos amigos
     */
    public List<String> getAmigos() {
        garantirRelacionamentos();
        return Collections.unmodifiableList(new ArrayList<String>(amigos));
    }

    /**
     * Adiciona um recado a fila de recados recebidos.
     *
     * @param recado recado recebido pelo usuario
     */
    public void receberRecado(Recado recado) {
        garantirRecados();
        recadosRecebidos.add(recado);
    }

    /**
     * Verifica se existe recado pendente para leitura.
     *
     * @return {@code true} quando houver recado pendente, ou {@code false} caso contrario
     */
    public boolean possuiRecado() {
        garantirRecados();
        return !recadosRecebidos.isEmpty();
    }

    /**
     * Remove e retorna o primeiro recado da fila.
     *
     * @return primeiro recado recebido, ou {@code null} quando a fila estiver vazia
     */
    public Recado lerProximoRecado() {
        garantirRecados();
        return recadosRecebidos.poll();
    }

    /**
     * Garante que as colecoes de relacionamento existam apos desserializacao.
     */
    private void garantirRelacionamentos() {
        if (amigos == null) {
            amigos = new LinkedHashSet<String>();
        }

        if (convitesEnviados == null) {
            convitesEnviados = new LinkedHashSet<String>();
        }
    }

    /**
     * Garante que a fila de recados exista apos desserializacao.
     */
    private void garantirRecados() {
        if (recadosRecebidos == null) {
            recadosRecebidos = new LinkedList<Recado>();
        }
    }
}
