package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica tentativa de adicionar amizade que ja esta confirmada.
 */
public class AmigoJaAdicionadoException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     */
    public AmigoJaAdicionadoException() {
        super(MensagensErro.doContrato("Usu\u00e1rio j\u00e1 est\u00e1 adicionado como amigo."));
    }
}
