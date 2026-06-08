package br.ufal.ic.p2.jackut.exceptions;

public class AutoRecadoException extends JackutException {

    private static final long serialVersionUID = 1L;

    public AutoRecadoException() {
        super(MensagensErro.doContrato("Usu\u00e1rio n\u00e3o pode enviar recado para si mesmo."));
    }
}
