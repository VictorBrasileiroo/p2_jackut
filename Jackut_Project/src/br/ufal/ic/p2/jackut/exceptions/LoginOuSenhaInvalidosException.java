package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica falha de autenticacao por login ou senha invalidos.
 */
public class LoginOuSenhaInvalidosException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     */
    public LoginOuSenhaInvalidosException() {
        super(MensagensErro.doContrato("Login ou senha inv\u00e1lidos."));
    }
}
