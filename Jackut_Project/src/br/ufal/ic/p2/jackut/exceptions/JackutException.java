package br.ufal.ic.p2.jackut.exceptions;

/**
 * Excecao base para erros de dominio do Jackut.
 *
 * <p>As excecoes especificas do sistema herdam desta classe para manter um
 * tipo comum de falha de negocio.</p>
 */
public class JackutException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria uma excecao de dominio com mensagem definida pelo contrato.
     *
     * @param mensagem mensagem que sera entregue ao EasyAccept
     */
    public JackutException(String mensagem) {
        super(mensagem);
    }
}
