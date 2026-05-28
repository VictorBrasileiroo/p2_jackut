package br.ufal.ic.p2.jackut.exceptions;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

final class MensagensErro {

    private MensagensErro() {
    }

    static String doContrato(String mensagem) {
        byte[] bytesIso = mensagem.getBytes(StandardCharsets.ISO_8859_1);
        return new String(bytesIso, Charset.defaultCharset());
    }
}
