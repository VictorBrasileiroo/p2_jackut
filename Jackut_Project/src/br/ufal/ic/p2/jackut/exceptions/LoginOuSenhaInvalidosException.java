package br.ufal.ic.p2.jackut.exceptions;

public class LoginOuSenhaInvalidosException extends JackutException {

    private static final long serialVersionUID = 1L;

    public LoginOuSenhaInvalidosException() {
        super(MensagensErro.doContrato("Login ou senha inv\u00e1lidos."));
    }
}
