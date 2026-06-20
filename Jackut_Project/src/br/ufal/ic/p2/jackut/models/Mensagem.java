package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;

/**
 * Representa uma mensagem enviada para uma comunidade.
 *
 * <p>A mensagem guarda o remetente para permitir remocoes e filtros de
 * inimizade, mas o contrato publico retorna apenas o texto.</p>
 */
public class Mensagem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Login do usuario remetente.
     */
    private final String remetente;

    /**
     * Texto da mensagem.
     */
    private final String texto;

    /**
     * Cria uma mensagem de comunidade.
     *
     * @param remetente login do usuario que enviou a mensagem
     * @param texto texto enviado para a comunidade
     */
    public Mensagem(String remetente, String texto) {
        this.remetente = remetente;
        this.texto = texto;
    }

    /**
     * Retorna o login do remetente.
     *
     * @return login do remetente
     */
    public String getRemetente() {
        return remetente;
    }

    /**
     * Retorna o texto da mensagem.
     *
     * @return texto enviado para a comunidade
     */
    public String getTexto() {
        return texto;
    }
}
