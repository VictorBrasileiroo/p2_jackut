package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica uso de comunidade inexistente.
 */
public class ComunidadeNaoExisteException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     */
    public ComunidadeNaoExisteException() {
        super(MensagensErro.doContrato("Comunidade n\u00e3o existe."));
    }
}
