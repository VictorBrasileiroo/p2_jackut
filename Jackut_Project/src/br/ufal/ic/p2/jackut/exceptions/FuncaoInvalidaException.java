package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica tentativa de interagir com um usuario que marcou o remetente como inimigo.
 */
public class FuncaoInvalidaException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     *
     * @param nomeInimigo nome do usuario que considera o remetente inimigo
     */
    public FuncaoInvalidaException(String nomeInimigo) {
        super(MensagensErro.doContrato("Fun\u00e7\u00e3o inv\u00e1lida: " + nomeInimigo + " \u00e9 seu inimigo."));
    }
}
