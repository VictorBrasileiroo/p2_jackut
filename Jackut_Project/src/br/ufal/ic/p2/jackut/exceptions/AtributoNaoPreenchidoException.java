package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica consulta a atributo de perfil que nao esta preenchido.
 */
public class AtributoNaoPreenchidoException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     */
    public AtributoNaoPreenchidoException() {
        super(MensagensErro.doContrato("Atributo n\u00e3o preenchido."));
    }
}
