package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.LoginOuSenhaInvalidosException;
import br.ufal.ic.p2.jackut.exceptions.UsuarioNaoCadastradoException;
import br.ufal.ic.p2.jackut.models.Sessao;
import br.ufal.ic.p2.jackut.models.Usuario;
import br.ufal.ic.p2.jackut.repositories.SessaoRepository;
import br.ufal.ic.p2.jackut.repositories.UsuarioRepository;

/**
 * Servico de aplicacao responsavel por autenticacao e sessoes.
 */
public class SessaoService {

    private final UsuarioRepository usuarioRepository;
    private final SessaoRepository sessaoRepository;

    /**
     * Cria o servico de sessoes.
     *
     * @param usuarioRepository repository de usuarios
     * @param sessaoRepository repository de sessoes
     */
    public SessaoService(UsuarioRepository usuarioRepository, SessaoRepository sessaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.sessaoRepository = sessaoRepository;
    }

    /**
     * Autentica um usuario e cria uma sessao.
     *
     * @param login login do usuario
     * @param senha senha informada
     * @return identificador da sessao criada
     * @throws LoginOuSenhaInvalidosException quando login ou senha forem invalidos
     */
    public String abrirSessao(String login, String senha) throws LoginOuSenhaInvalidosException {
        Usuario usuario = usuarioRepository.buscarPorLogin(login);
        if (usuario == null || !usuario.senhaConfere(senha)) {
            throw new LoginOuSenhaInvalidosException();
        }

        return sessaoRepository.criarSessao(usuario).getId();
    }

    /**
     * Busca o usuario autenticado por uma sessao.
     *
     * @param id identificador da sessao
     * @return usuario associado a sessao
     * @throws UsuarioNaoCadastradoException quando a sessao nao existir ou apontar para usuario removido
     */
    public Usuario buscarUsuarioPorSessao(String id) throws UsuarioNaoCadastradoException {
        Sessao sessao = sessaoRepository.buscarPorId(id);
        if (sessao == null) {
            throw new UsuarioNaoCadastradoException();
        }

        Usuario usuario = usuarioRepository.buscarPorLogin(sessao.getLoginUsuario());
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException();
        }

        return usuario;
    }

    /**
     * Remove todas as sessoes abertas.
     */
    public void limpar() {
        sessaoRepository.limpar();
    }

    /**
     * Remove sessoes associadas a um usuario.
     *
     * @param login login do usuario
     */
    public void removerSessoesDoUsuario(String login) {
        sessaoRepository.removerPorUsuario(login);
    }
}
