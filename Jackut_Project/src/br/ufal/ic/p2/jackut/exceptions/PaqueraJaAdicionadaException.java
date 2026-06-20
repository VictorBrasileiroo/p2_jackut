package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica tentativa de adicionar novamente uma paquera ja registrada.
 */
public class PaqueraJaAdicionadaException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     */
    public PaqueraJaAdicionadaException() {
        super(MensagensErro.doContrato("Usu\u00e1rio j\u00e1 est\u00e1 adicionado como paquera."));
    }
}
