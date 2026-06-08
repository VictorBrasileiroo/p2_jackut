package br.ufal.ic.p2.jackut.repositories;

import br.ufal.ic.p2.jackut.models.EstadoJackut;
import br.ufal.ic.p2.jackut.models.Usuario;

/**
 * Repository responsavel pelo acesso controlado aos usuarios do estado.
 *
 * <p>A classe encapsula consultas e registros de usuarios, evitando que outras
 * camadas manipulem diretamente a estrutura interna de {@link EstadoJackut}.</p>
 */
public class UsuarioRepository {

    private final EstadoJackut estado;

    /**
     * Cria um repository baseado em um estado persistente.
     *
     * @param estado estado do sistema que contem os usuarios cadastrados
     */
    public UsuarioRepository(EstadoJackut estado) {
        this.estado = estado;
    }

    /**
     * Verifica se existe usuario com um login.
     *
     * @param login login pesquisado
     * @return {@code true} quando o usuario existir, ou {@code false} caso contrario
     */
    public boolean existe(String login) {
        return estado.contemUsuario(login);
    }

    /**
     * Registra um usuario no estado.
     *
     * @param usuario usuario que sera cadastrado
     */
    public void adicionar(Usuario usuario) {
        estado.adicionarUsuario(usuario);
    }

    /**
     * Busca um usuario por login.
     *
     * @param login login pesquisado
     * @return usuario encontrado, ou {@code null} quando nao houver cadastro
     */
    public Usuario buscarPorLogin(String login) {
        return estado.buscarUsuario(login);
    }
}
