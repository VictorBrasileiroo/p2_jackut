package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica tentativa de criar conta com login ja cadastrado.
 */
public class ContaExistenteException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     */
    public ContaExistenteException() {
        super(MensagensErro.doContrato("Conta com esse nome j\u00e1 existe."));
    }
}
