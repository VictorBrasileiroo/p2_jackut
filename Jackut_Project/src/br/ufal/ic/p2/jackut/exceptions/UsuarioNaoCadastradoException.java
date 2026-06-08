package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica uso de usuario inexistente ou sessao que nao identifica usuario valido.
 */
public class UsuarioNaoCadastradoException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     */
    public UsuarioNaoCadastradoException() {
        super(MensagensErro.doContrato("Usu\u00e1rio n\u00e3o cadastrado."));
    }
}
