package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica tentativa de um usuario enviar recado para si mesmo.
 */
public class AutoRecadoException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     */
    public AutoRecadoException() {
        super(MensagensErro.doContrato("Usu\u00e1rio n\u00e3o pode enviar recado para si mesmo."));
    }
}
