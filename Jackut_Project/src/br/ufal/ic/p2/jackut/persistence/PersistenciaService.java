package br.ufal.ic.p2.jackut.persistence;

import br.ufal.ic.p2.jackut.models.EstadoJackut;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Servico de infraestrutura responsavel pela persistencia do estado do Jackut.
 *
 * <p>A classe carrega, salva e remove o arquivo serializado do sistema usando
 * caminhos relativos ao diretorio de execucao.</p>
 */
public class PersistenciaService {

    private final Path arquivoEstado;

    /**
     * Cria o servico de persistencia e localiza o arquivo de estado.
     */
    public PersistenciaService() {
        this.arquivoEstado = localizarArquivoEstado();
    }

    /**
     * Carrega o estado persistido do sistema.
     *
     * @return estado carregado, ou um estado vazio quando nao houver arquivo
     * @throws IllegalStateException quando o arquivo existir e nao puder ser carregado
     */
    public EstadoJackut carregar() {
        if (!Files.exists(arquivoEstado)) {
            return new EstadoJackut();
        }

        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(arquivoEstado))) {
            return (EstadoJackut) input.readObject();
        } catch (IOException e) {
            throw new IllegalStateException("Nao foi possivel carregar os dados do sistema.", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Nao foi possivel carregar os dados do sistema.", e);
        }
    }

    /**
     * Salva o estado do sistema em arquivo.
     *
     * @param estado estado persistente que sera salvo
     * @throws IllegalStateException quando o estado nao puder ser gravado
     */
    public void salvar(EstadoJackut estado) {
        try {
            Files.createDirectories(arquivoEstado.getParent());

            try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(arquivoEstado))) {
                output.writeObject(estado);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Nao foi possivel salvar os dados do sistema.", e);
        }
    }

    /**
     * Remove o arquivo persistido, caso ele exista.
     *
     * @throws IllegalStateException quando o arquivo nao puder ser apagado
     */
    public void apagar() {
        try {
            Files.deleteIfExists(arquivoEstado);
        } catch (IOException e) {
            throw new IllegalStateException("Nao foi possivel apagar os dados do sistema.", e);
        }
    }

    /**
     * Localiza o caminho relativo usado para salvar o estado do sistema.
     *
     * @return caminho do arquivo de estado
     */
    private Path localizarArquivoEstado() {
        Path[] diretorios = {
                Paths.get("..", "dados"),
                Paths.get("dados"),
                Paths.get("P2-2023.1-JACKUT", "dados")
        };

        for (Path diretorio : diretorios) {
            if (Files.exists(diretorio)) {
                return diretorio.resolve("jackut.ser");
            }
        }

        return Paths.get("..", "dados", "jackut.ser");
    }
}
