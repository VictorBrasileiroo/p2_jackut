package br.ufal.ic.p2.jackut.persistence;

import br.ufal.ic.p2.jackut.models.EstadoJackut;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PersistenciaService {

    private final Path arquivoEstado;

    public PersistenciaService() {
        this.arquivoEstado = localizarArquivoEstado();
    }

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

    public void apagar() {
        try {
            Files.deleteIfExists(arquivoEstado);
        } catch (IOException e) {
            throw new IllegalStateException("Nao foi possivel apagar os dados do sistema.", e);
        }
    }

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
