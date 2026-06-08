package br.ufal.ic.p2.jackut.exceptions;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Utilitario interno para adaptar mensagens de erro ao contrato dos scripts.
 *
 * <p>Os testes fornecidos podem ser lidos com codificacao diferente da fonte
 * Java. Este utilitario preserva a compatibilidade das mensagens esperadas
 * pelo EasyAccept.</p>
 */
final class MensagensErro {

    /**
     * Impede instanciacao do utilitario.
     */
    private MensagensErro() {
    }

    /**
     * Converte uma mensagem para a codificacao esperada no ambiente de execucao.
     *
     * @param mensagem mensagem original do contrato
     * @return mensagem ajustada para a codificacao padrao da JVM
     */
    static String doContrato(String mensagem) {
        byte[] bytesIso = mensagem.getBytes(StandardCharsets.ISO_8859_1);
        return new String(bytesIso, Charset.defaultCharset());
    }
}
