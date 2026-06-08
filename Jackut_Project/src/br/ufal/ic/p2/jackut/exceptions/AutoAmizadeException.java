package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica tentativa de um usuario adicionar a si mesmo como amigo.
 */
public class AutoAmizadeException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     */
    public AutoAmizadeException() {
        super(MensagensErro.doContrato("Usu\u00e1rio n\u00e3o pode adicionar a si mesmo como amigo."));
    }
}
