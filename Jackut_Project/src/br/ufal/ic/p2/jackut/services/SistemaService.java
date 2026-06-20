package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.models.EstadoJackut;
import br.ufal.ic.p2.jackut.persistence.PersistenciaService;

/**
 * Servico de aplicacao responsavel por ciclo de vida e persistencia.
 */
public class SistemaService {

    private final EstadoJackut estado;
    private final PersistenciaService persistenciaService;
    private final SessaoService sessaoService;

    /**
     * Cria o servico de sistema.
     *
     * @param estado estado persistente
     * @param persistenciaService servico de persistencia
     * @param sessaoService servico de sessoes
     */
    public SistemaService(
            EstadoJackut estado,
            PersistenciaService persistenciaService,
            SessaoService sessaoService) {
        this.estado = estado;
        this.persistenciaService = persistenciaService;
        this.sessaoService = sessaoService;
    }

    /**
     * Limpa estado, sessoes e dados persistidos.
     */
    public void zerarSistema() {
        estado.limpar();
        sessaoService.limpar();
        persistenciaService.apagar();
    }

    /**
     * Salva o estado persistente.
     */
    public void encerrarSistema() {
        persistenciaService.salvar(estado);
    }
}
