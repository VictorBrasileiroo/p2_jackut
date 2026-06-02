package br.ufal.ic.p2.jackut.exceptions;

public class AutoAmizadeException extends JackutException {

    private static final long serialVersionUID = 1L;

    public AutoAmizadeException() {
        super(MensagensErro.doContrato("Usu\u00e1rio n\u00e3o pode adicionar a si mesmo como amigo."));
    }
}
