package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa o estado persistente do sistema Jackut.
 *
 * <p>O estado agrupa usuarios e comunidades cadastrados e oferece operacoes
 * controladas para consulta, registro, remocao e limpeza, sem expor mapas
 * internos para modificacao externa.</p>
 */
public class EstadoJackut implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Usuarios cadastrados, indexados pelo login.
     */
    private Map<String, Usuario> usuarios;

    /**
     * Comunidades cadastradas, indexadas pelo nome.
     */
    private Map<String, Comunidade> comunidades;

    /**
     * Cria um estado vazio para o sistema.
     */
    public EstadoJackut() {
        this.usuarios = new LinkedHashMap<String, Usuario>();
        this.comunidades = new LinkedHashMap<String, Comunidade>();
    }

    /**
     * Verifica se existe usuario cadastrado com um login.
     *
     * @param login login pesquisado
     * @return {@code true} quando o usuario existir, ou {@code false} caso contrario
     */
    public boolean contemUsuario(String login) {
        garantirUsuarios();
        return usuarios.containsKey(login);
    }

    /**
     * Adiciona um usuario ao estado.
     *
     * @param usuario usuario que sera registrado
     */
    public void adicionarUsuario(Usuario usuario) {
        garantirUsuarios();
        usuarios.put(usuario.getLogin(), usuario);
    }

    /**
     * Busca um usuario cadastrado por login.
     *
     * @param login login pesquisado
     * @return usuario encontrado, ou {@code null} quando nao existir cadastro
     */
    public Usuario buscarUsuario(String login) {
        garantirUsuarios();
        return usuarios.get(login);
    }

    /**
     * Lista os usuarios cadastrados em ordem de criacao.
     *
     * @return lista com os usuarios cadastrados
     */
    public List<Usuario> listarUsuarios() {
        garantirUsuarios();
        return new ArrayList<Usuario>(usuarios.values());
    }

    /**
     * Remove um usuario cadastrado.
     *
     * @param login login do usuario removido
     */
    public void removerUsuario(String login) {
        garantirUsuarios();
        usuarios.remove(login);
    }

    /**
     * Verifica se existe comunidade com um nome.
     *
     * @param nome nome pesquisado
     * @return {@code true} quando a comunidade existir, ou {@code false} caso contrario
     */
    public boolean contemComunidade(String nome) {
        garantirComunidades();
        return comunidades.containsKey(nome);
    }

    /**
     * Adiciona uma comunidade ao estado.
     *
     * @param comunidade comunidade que sera registrada
     */
    public void adicionarComunidade(Comunidade comunidade) {
        garantirComunidades();
        comunidades.put(comunidade.getNome(), comunidade);
    }

    /**
     * Busca uma comunidade cadastrada por nome.
     *
     * @param nome nome pesquisado
     * @return comunidade encontrada, ou {@code null} quando nao existir cadastro
     */
    public Comunidade buscarComunidade(String nome) {
        garantirComunidades();
        return comunidades.get(nome);
    }

    /**
     * Lista as comunidades cadastradas em ordem de criacao.
     *
     * @return lista com as comunidades cadastradas
     */
    public List<Comunidade> listarComunidades() {
        garantirComunidades();
        return new ArrayList<Comunidade>(comunidades.values());
    }

    /**
     * Remove uma comunidade cadastrada.
     *
     * @param nome nome da comunidade removida
     */
    public void removerComunidade(String nome) {
        garantirComunidades();
        comunidades.remove(nome);
    }

    /**
     * Remove todos os dados cadastrados do estado.
     */
    public void limpar() {
        garantirUsuarios();
        garantirComunidades();
        usuarios.clear();
        comunidades.clear();
    }

    /**
     * Garante que o mapa de usuarios exista apos desserializacao.
     */
    private void garantirUsuarios() {
        if (usuarios == null) {
            usuarios = new LinkedHashMap<String, Usuario>();
        }
    }

    /**
     * Garante que o mapa de comunidades exista apos desserializacao.
     */
    private void garantirComunidades() {
        if (comunidades == null) {
            comunidades = new LinkedHashMap<String, Comunidade>();
        }
    }
}
