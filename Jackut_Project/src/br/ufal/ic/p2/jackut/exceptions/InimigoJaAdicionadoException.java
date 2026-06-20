package br.ufal.ic.p2.jackut.exceptions;

/**
 * Indica tentativa de adicionar novamente um inimigo ja registrado.
 */
public class InimigoJaAdicionadoException extends JackutException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a excecao com a mensagem exigida pelo contrato do EasyAccept.
     */
    public InimigoJaAdicionadoException() {
        super(MensagensErro.doContrato("Usu\u00e1rio j\u00e1 est\u00e1 adicionado como inimigo."));
    }
}
