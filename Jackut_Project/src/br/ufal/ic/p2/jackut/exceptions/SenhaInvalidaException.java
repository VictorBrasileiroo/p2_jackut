package br.ufal.ic.p2.jackut.exceptions;

public class SenhaInvalidaException extends JackutException {

    private static final long serialVersionUID = 1L;

    public SenhaInvalidaException() {
        super(MensagensErro.doContrato("Senha inv\u00e1lida."));
    }
}
