package br.ufal.ic.p2.jackut.exceptions;

public class SemRecadosException extends JackutException {

    private static final long serialVersionUID = 1L;

    public SemRecadosException() {
        super(MensagensErro.doContrato("N\u00e3o h\u00e1 recados."));
    }
}
