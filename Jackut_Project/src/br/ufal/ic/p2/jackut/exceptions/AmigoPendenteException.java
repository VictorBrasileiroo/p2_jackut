package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica tentativa de reenviar um convite de amizade ainda pendente.
 */
public class AmigoPendenteException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     */
    public AmigoPendenteException() {
        super(MensagensErro.doContrato("Usu\u00e1rio j\u00e1 est\u00e1 adicionado como amigo, esperando aceita\u00e7\u00e3o do convite."));
    }
}
