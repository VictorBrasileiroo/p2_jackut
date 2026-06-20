package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica tentativa de ler mensagem quando nao ha mensagens pendentes.
 */
public class SemMensagensException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     */
    public SemMensagensException() {
        super(MensagensErro.doContrato("N\u00e3o h\u00e1 mensagens."));
    }
}
