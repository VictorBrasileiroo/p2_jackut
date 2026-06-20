package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica tentativa de adicionar usuario a uma comunidade da qual ja participa.
 */
public class UsuarioJaNaComunidadeException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     */
    public UsuarioJaNaComunidadeException() {
        super(MensagensErro.doContrato("Usuario j\u00e1 faz parte dessa comunidade."));
    }
}
