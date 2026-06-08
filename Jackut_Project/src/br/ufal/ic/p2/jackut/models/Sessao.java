package br.ufal.ic.p2.jackut.models;

/**
 * Representa uma sessao aberta para um usuario autenticado.
 *
 * <p>A sessao guarda apenas um identificador temporario e o login do usuario
 * associado. Sessoes nao fazem parte do estado persistente.</p>
 */
public class Sessao {

    private final String id;
    private final String loginUsuario;

    /**
     * Cria uma sessao para um usuario.
     *
     * @param id identificador da sessao
     * @param loginUsuario login do usuario autenticado
     */
    public Sessao(String id, String loginUsuario) {
        this.id = id;
        this.loginUsuario = loginUsuario;
    }

    /**
     * Retorna o identificador da sessao.
     *
     * @return id da sessao
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna o login do usuario associado a sessao.
     *
     * @return login do usuario autenticado
     */
    public String getLoginUsuario() {
        return loginUsuario;
    }
}
