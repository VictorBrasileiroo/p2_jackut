package br.ufal.ic.p2.jackut.exceptions;

public class AmigoJaAdicionadoException extends JackutException {

    private static final long serialVersionUID = 1L;

    public AmigoJaAdicionadoException() {
        super(MensagensErro.doContrato("Usu\u00e1rio j\u00e1 est\u00e1 adicionado como amigo."));
    }
}
