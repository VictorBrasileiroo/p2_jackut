package br.ufal.ic.p2.jackut.exceptions;

public class ContaExistenteException extends JackutException {

    private static final long serialVersionUID = 1L;

    public ContaExistenteException() {
        super(MensagensErro.doContrato("Conta com esse nome j\u00e1 existe."));
    }
}
