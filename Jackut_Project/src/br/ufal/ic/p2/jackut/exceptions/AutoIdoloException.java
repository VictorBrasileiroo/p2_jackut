package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica tentativa de um usuario ser fa de si mesmo.
 */
public class AutoIdoloException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     */
    public AutoIdoloException() {
        super(MensagensErro.doContrato("Usu\u00e1rio n\u00e3o pode ser f\u00e3 de si mesmo."));
    }
}
