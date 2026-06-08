package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica tentativa de criar usuario com login invalido.
 */
public class LoginInvalidoException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     */
    public LoginInvalidoException() {
        super(MensagensErro.doContrato("Login inv\u00e1lido."));
    }
}
