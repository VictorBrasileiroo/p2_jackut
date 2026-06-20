package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.AutoRecadoException;
import br.ufal.ic.p2.jackut.exceptions.FuncaoInvalidaException;
import br.ufal.ic.p2.jackut.exceptions.SemRecadosException;
import br.ufal.ic.p2.jackut.exceptions.UsuarioNaoCadastradoException;
import br.ufal.ic.p2.jackut.models.Recado;
import br.ufal.ic.p2.jackut.models.Usuario;
import br.ufal.ic.p2.jackut.repositories.UsuarioRepository;

/**
 * Servico de aplicacao responsavel por recados privados.
 */
public class RecadoService {

    private final UsuarioRepository usuarioRepository;
    private final SessaoService sessaoService;

    /**
     * Cria o servico de recados.
     *
     * @param usuarioRepository repository de usuarios
     * @param sessaoService servico de sessoes
     */
    public RecadoService(UsuarioRepository usuarioRepository, SessaoService sessaoService) {
        this.usuarioRepository = usuarioRepository;
        this.sessaoService = sessaoService;
    }

    /**
     * Envia um recado privado para outro usuario.
     *
     * @param id identificador da sessao
     * @param loginDestinatario login do destinatario
     * @param mensagem texto do recado
     * @throws UsuarioNaoCadastradoException quando a sessao ou o destinatario forem invalidos
     * @throws AutoRecadoException quando o usuario tentar enviar recado para si mesmo
     * @throws FuncaoInvalidaException quando o destinatario tiver marcado o remetente como inimigo
     */
    public void enviarRecado(String id, String loginDestinatario, String mensagem)
            throws UsuarioNaoCadastradoException, AutoRecadoException, FuncaoInvalidaException {
        Usuario remetente = sessaoService.buscarUsuarioPorSessao(id);
        Usuario destinatario = usuarioRepository.buscarPorLogin(loginDestinatario);

        if (destinatario == null) {
            throw new UsuarioNaoCadastradoException();
        }

        if (remetente.getLogin().equals(loginDestinatario)) {
            throw new AutoRecadoException();
        }

        if (destinatario.possuiInimigo(remetente.getLogin())) {
            throw new FuncaoInvalidaException(destinatario.getNome());
        }

        destinatario.receberRecado(new Recado(remetente.getLogin(), mensagem));
    }

    /**
     * Envia recado automatico do sistema para um usuario.
     *
     * @param destinatario usuario que recebera o recado
     * @param mensagem texto do recado automatico
     */
    public void enviarRecadoSistema(Usuario destinatario, String mensagem) {
        destinatario.receberRecado(new Recado("jackut", mensagem));
    }

    /**
     * Le e remove o primeiro recado recebido pelo usuario autenticado.
     *
     * @param id identificador da sessao
     * @return texto do primeiro recado pendente
     * @throws UsuarioNaoCadastradoException quando a sessao nao identificar usuario valido
     * @throws SemRecadosException quando o usuario nao possuir recados pendentes
     */
    public String lerRecado(String id) throws UsuarioNaoCadastradoException, SemRecadosException {
        Usuario usuario = sessaoService.buscarUsuarioPorSessao(id);

        if (!usuario.possuiRecado()) {
            throw new SemRecadosException();
        }

        return usuario.lerProximoRecado().getMensagem();
    }

    /**
     * Remove recados e mensagens enviados por um usuario.
     *
     * @param login login do usuario removido
     */
    public void removerConteudosDoUsuario(String login) {
        for (Usuario usuario : usuarioRepository.listar()) {
            usuario.removerRecadosDe(login);
            usuario.removerMensagensDe(login);
        }
    }
}
