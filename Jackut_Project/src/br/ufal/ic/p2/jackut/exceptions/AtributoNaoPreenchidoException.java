package br.ufal.ic.p2.jackut.exceptions;

public class AtributoNaoPreenchidoException extends JackutException {

    private static final long serialVersionUID = 1L;

    public AtributoNaoPreenchidoException() {
        super(MensagensErro.doContrato("Atributo n\u00e3o preenchido."));
    }
}
