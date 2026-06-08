package br.ufal.ic.p2.jackut;

import easyaccept.EasyAccept;
import java.io.File;

/**
 * Ponto de entrada para execucao dos scripts de aceitacao do Jackut.
 *
 * <p>A classe localiza a pasta de testes por caminhos relativos e executa os
 * scripts do milestone por meio do EasyAccept.</p>
 */
public class Main {

    private static final String FACADE = "br.ufal.ic.p2.jackut.Facade";

    /**
     * Impede instanciacao da classe de execucao.
     */
    private Main() {
    }

    /**
     * Executa os scripts de aceitacao configurados para o milestone atual.
     *
     * @param args argumentos de linha de comando, nao utilizados pelo projeto
     * @throws IllegalStateException quando a pasta de testes nao puder ser localizada
     */
    public static void main(String[] args) {
        String base = findTestBase();
        EasyAccept.main(new String[] {FACADE, base + "us1_1.txt"});
        EasyAccept.main(new String[] {FACADE, base + "us1_2.txt"});
        EasyAccept.main(new String[] {FACADE, base + "us2_1.txt"});
        EasyAccept.main(new String[] {FACADE, base + "us2_2.txt"});
        EasyAccept.main(new String[] {FACADE, base + "us3_1.txt"});
        EasyAccept.main(new String[] {FACADE, base + "us3_2.txt"});
        EasyAccept.main(new String[] {FACADE, base + "us4_1.txt"});
        EasyAccept.main(new String[] {FACADE, base + "us4_2.txt"});
    }

    /**
     * Localiza a pasta dos scripts do EasyAccept a partir do diretorio de execucao.
     *
     * @return caminho relativo da pasta que contem os scripts de teste
     * @throws IllegalStateException quando nenhum caminho conhecido contiver os testes
     */
    private static String findTestBase() {
        if (new File("tests/us1_1.txt").exists()) {
            return "tests/";
        }

        if (new File("jackut_project/tests/us1_1.txt").exists()) {
            return "jackut_project/tests/";
        }

        if (new File("P2-2023.1-JACKUT/jackut_project/tests/us1_1.txt").exists()) {
            return "P2-2023.1-JACKUT/jackut_project/tests/";
        }

        File[] subdirs = new File(".").listFiles(File::isDirectory);
        if (subdirs != null) {
            for (File sub : subdirs) {
                String path = sub.getName() + "/P2-2023.1-JACKUT/jackut_project/tests/";
                if (new File(path + "us1_1.txt").exists()) {
                    return path;
                }
            }
        }

        throw new IllegalStateException(
                "Nao foi possivel localizar a pasta de testes. Diretorio atual: "
                        + new File(".").getAbsolutePath()
        );
    }
}
