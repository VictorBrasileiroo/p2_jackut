package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica tentativa de ler recado quando nao ha recados pendentes.
 */
public class SemRecadosException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     */
    public SemRecadosException() {
        super(MensagensErro.doContrato("N\u00e3o h\u00e1 recados."));
    }
}
