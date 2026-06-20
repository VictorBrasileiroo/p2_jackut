package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.AtributoNaoPreenchidoException;
import br.ufal.ic.p2.jackut.exceptions.ContaExistenteException;
import br.ufal.ic.p2.jackut.exceptions.LoginInvalidoException;
import br.ufal.ic.p2.jackut.exceptions.SenhaInvalidaException;
import br.ufal.ic.p2.jackut.exceptions.UsuarioNaoCadastradoException;
import br.ufal.ic.p2.jackut.models.Usuario;
import br.ufal.ic.p2.jackut.repositories.UsuarioRepository;

/**
 * Servico de aplicacao responsavel por contas e perfis de usuario.
 */
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final SessaoService sessaoService;

    /**
     * Cria o servico de usuarios.
     *
     * @param usuarioRepository repository de usuarios
     * @param sessaoService servico de sessoes
     */
    public UsuarioService(UsuarioRepository usuarioRepository, SessaoService sessaoService) {
        this.usuarioRepository = usuarioRepository;
        this.sessaoService = sessaoService;
    }

    /**
     * Cria uma conta de usuario.
     *
     * @param login login unico do usuario
     * @param senha senha usada para autenticacao
     * @param nome nome inicial do perfil
     * @throws LoginInvalidoException quando o login estiver vazio
     * @throws SenhaInvalidaException quando a senha estiver vazia
     * @throws ContaExistenteException quando ja existir usuario com o login informado
     */
    public void criarUsuario(String login, String senha, String nome)
            throws LoginInvalidoException, SenhaInvalidaException, ContaExistenteException {
        validarLogin(login);
        validarSenha(senha);

        if (usuarioRepository.existe(login)) {
            throw new ContaExistenteException();
        }

        usuarioRepository.adicionar(new Usuario(login, senha, nome));
    }

    /**
     * Consulta um atributo de perfil.
     *
     * @param login login do usuario consultado
     * @param atributo nome do atributo desejado
     * @return valor do atributo
     * @throws UsuarioNaoCadastradoException quando o usuario nao existir
     * @throws AtributoNaoPreenchidoException quando o atributo nao estiver preenchido
     */
    public String getAtributoUsuario(String login, String atributo)
            throws UsuarioNaoCadastradoException, AtributoNaoPreenchidoException {
        Usuario usuario = usuarioRepository.buscarPorLogin(login);
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException();
        }

        if (!usuario.possuiAtributo(atributo)) {
            throw new AtributoNaoPreenchidoException();
        }

        return usuario.getAtributo(atributo);
    }

    /**
     * Edita ou cria um atributo no perfil do usuario autenticado.
     *
     * @param id identificador da sessao
     * @param atributo nome do atributo
     * @param valor novo valor
     * @throws UsuarioNaoCadastradoException quando a sessao nao identificar usuario valido
     */
    public void editarPerfil(String id, String atributo, String valor) throws UsuarioNaoCadastradoException {
        Usuario usuario = sessaoService.buscarUsuarioPorSessao(id);
        usuario.editarPerfil(atributo, valor);
    }

    /**
     * Remove uma conta do estado.
     *
     * @param login login do usuario removido
     */
    public void removerUsuario(String login) {
        usuarioRepository.remover(login);
    }

    private void validarLogin(String login) throws LoginInvalidoException {
        if (login == null || login.isEmpty()) {
            throw new LoginInvalidoException();
        }
    }

    private void validarSenha(String senha) throws SenhaInvalidaException {
        if (senha == null || senha.isEmpty()) {
            throw new SenhaInvalidaException();
        }
    }
}
