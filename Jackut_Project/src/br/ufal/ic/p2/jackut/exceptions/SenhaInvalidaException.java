package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica tentativa de criar usuario com senha invalida.
 */
public class SenhaInvalidaException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     */
    public SenhaInvalidaException() {
        super(MensagensErro.doContrato("Senha inv\u00e1lida."));
    }
}
