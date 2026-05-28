package br.ufal.ic.p2.jackut.exceptions;

public class UsuarioNaoCadastradoException extends JackutException {

    private static final long serialVersionUID = 1L;

    public UsuarioNaoCadastradoException() {
        super(MensagensErro.doContrato("Usu\u00e1rio n\u00e3o cadastrado."));
    }
}
