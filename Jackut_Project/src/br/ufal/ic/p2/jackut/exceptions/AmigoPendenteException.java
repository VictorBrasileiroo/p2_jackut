package br.ufal.ic.p2.jackut.exceptions;

public class AmigoPendenteException extends JackutException {

    private static final long serialVersionUID = 1L;

    public AmigoPendenteException() {
        super(MensagensErro.doContrato("Usu\u00e1rio j\u00e1 est\u00e1 adicionado como amigo, esperando aceita\u00e7\u00e3o do convite."));
    }
}
