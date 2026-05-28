package br.ufal.ic.p2.jackut.exceptions;

public class LoginInvalidoException extends JackutException {

    private static final long serialVersionUID = 1L;

    public LoginInvalidoException() {
        super(MensagensErro.doContrato("Login inv\u00e1lido."));
    }
}
