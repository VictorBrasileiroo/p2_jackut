package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica tentativa de criar comunidade com nome ja cadastrado.
 */
public class ComunidadeExistenteException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     */
    public ComunidadeExistenteException() {
        super(MensagensErro.doContrato("Comunidade com esse nome j\u00e1 existe."));
    }
}
