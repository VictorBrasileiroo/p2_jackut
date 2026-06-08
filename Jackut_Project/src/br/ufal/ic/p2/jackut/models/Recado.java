package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;

/**
 * Representa um recado recebido por um usuario do Jackut.
 *
 * <p>O recado guarda o remetente e a mensagem enviada. A leitura dos recados
 * e controlada pela fila mantida em {@link Usuario}.</p>
 */
public class Recado implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Login do usuario remetente.
     */
    private final String remetente;

    /**
     * Texto enviado no recado.
     */
    private final String mensagem;

    /**
     * Cria um recado.
     *
     * @param remetente login do usuario que enviou o recado
     * @param mensagem texto do recado
     */
    public Recado(String remetente, String mensagem) {
        this.remetente = remetente;
        this.mensagem = mensagem;
    }

    /**
     * Retorna o login do remetente.
     *
     * @return login do usuario que enviou o recado
     */
    public String getRemetente() {
        return remetente;
    }

    /**
     * Retorna a mensagem do recado.
     *
     * @return texto do recado
     */
    public String getMensagem() {
        return mensagem;
    }
}
