package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Representa o estado persistente do sistema Jackut.
 *
 * <p>O estado agrupa os usuarios cadastrados e oferece operacoes controladas
 * para consulta, registro e limpeza, sem expor o mapa interno.</p>
 */
public class EstadoJackut implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Usuarios cadastrados, indexados pelo login.
     */
    private final Map<String, Usuario> usuarios;

    /**
     * Cria um estado vazio para o sistema.
     */
    public EstadoJackut() {
        this.usuarios = new LinkedHashMap<String, Usuario>();
    }

    /**
     * Verifica se existe usuario cadastrado com um login.
     *
     * @param login login pesquisado
     * @return {@code true} quando o usuario existir, ou {@code false} caso contrario
     */
    public boolean contemUsuario(String login) {
        return usuarios.containsKey(login);
    }

    /**
     * Adiciona um usuario ao estado.
     *
     * @param usuario usuario que sera registrado
     */
    public void adicionarUsuario(Usuario usuario) {
        usuarios.put(usuario.getLogin(), usuario);
    }

    /**
     * Busca um usuario cadastrado por login.
     *
     * @param login login pesquisado
     * @return usuario encontrado, ou {@code null} quando nao existir cadastro
     */
    public Usuario buscarUsuario(String login) {
        return usuarios.get(login);
    }

    /**
     * Remove todos os usuarios cadastrados do estado.
     */
    public void limpar() {
        usuarios.clear();
    }
}
