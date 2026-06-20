package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.AmigoJaAdicionadoException;
import br.ufal.ic.p2.jackut.exceptions.AmigoPendenteException;
import br.ufal.ic.p2.jackut.exceptions.AutoAmizadeException;
import br.ufal.ic.p2.jackut.exceptions.FuncaoInvalidaException;
import br.ufal.ic.p2.jackut.exceptions.UsuarioNaoCadastradoException;
import br.ufal.ic.p2.jackut.models.Usuario;
import br.ufal.ic.p2.jackut.repositories.UsuarioRepository;
import java.util.List;

/**
 * Servico de aplicacao responsavel por amizades.
 */
public class AmizadeService {

    private final UsuarioRepository usuarioRepository;
    private final SessaoService sessaoService;

    /**
     * Cria o servico de amizades.
     *
     * @param usuarioRepository repository de usuarios
     * @param sessaoService servico de sessoes
     */
    public AmizadeService(UsuarioRepository usuarioRepository, SessaoService sessaoService) {
        this.usuarioRepository = usuarioRepository;
        this.sessaoService = sessaoService;
    }

    /**
     * Solicita ou confirma amizade para o usuario autenticado.
     *
     * @param id identificador da sessao
     * @param loginAmigo login do usuario a ser adicionado
     * @throws UsuarioNaoCadastradoException quando a sessao ou o amigo forem invalidos
     * @throws AutoAmizadeException quando o usuario tentar adicionar a si mesmo
     * @throws AmigoJaAdicionadoException quando a amizade ja estiver confirmada
     * @throws AmigoPendenteException quando ja houver convite pendente
     * @throws FuncaoInvalidaException quando o alvo tiver marcado o usuario como inimigo
     */
    public void adicionarAmigo(String id, String loginAmigo)
            throws UsuarioNaoCadastradoException, AutoAmizadeException, AmigoJaAdicionadoException,
            AmigoPendenteException, FuncaoInvalidaException {
        Usuario usuario = sessaoService.buscarUsuarioPorSessao(id);
        Usuario amigo = usuarioRepository.buscarPorLogin(loginAmigo);

        if (amigo == null) {
            throw new UsuarioNaoCadastradoException();
        }

        if (usuario.getLogin().equals(loginAmigo)) {
            throw new AutoAmizadeException();
        }

        validarBloqueioPorInimigo(usuario, amigo);

        if (usuario.ehAmigo(loginAmigo)) {
            throw new AmigoJaAdicionadoException();
        }

        if (usuario.possuiConviteEnviado(loginAmigo)) {
            throw new AmigoPendenteException();
        }

        if (amigo.possuiConviteEnviado(usuario.getLogin())) {
            amigo.removerConviteEnviado(usuario.getLogin());
            usuario.adicionarAmigoConfirmado(loginAmigo);
            amigo.adicionarAmigoConfirmado(usuario.getLogin());
            return;
        }

        usuario.solicitarAmizade(loginAmigo);
    }

    /**
     * Verifica se dois usuarios sao amigos.
     *
     * @param login login de um usuario
     * @param loginAmigo login do outro usuario
     * @return {@code true} se houver amizade confirmada, ou {@code false} caso contrario
     * @throws UsuarioNaoCadastradoException quando algum usuario nao existir
     */
    public boolean ehAmigo(String login, String loginAmigo) throws UsuarioNaoCadastradoException {
        Usuario usuario = usuarioRepository.buscarPorLogin(login);
        Usuario amigo = usuarioRepository.buscarPorLogin(loginAmigo);

        if (usuario == null || amigo == null) {
            throw new UsuarioNaoCadastradoException();
        }

        return usuario.ehAmigo(loginAmigo);
    }

    /**
     * Lista os amigos de um usuario.
     *
     * @param login login do usuario consultado
     * @return amigos formatados para o EasyAccept
     * @throws UsuarioNaoCadastradoException quando o usuario nao existir
     */
    public String getAmigos(String login) throws UsuarioNaoCadastradoException {
        Usuario usuario = usuarioRepository.buscarPorLogin(login);
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException();
        }

        return formatarLista(usuario.getAmigos());
    }

    private void validarBloqueioPorInimigo(Usuario origem, Usuario destino) throws FuncaoInvalidaException {
        if (destino.possuiInimigo(origem.getLogin())) {
            throw new FuncaoInvalidaException(destino.getNome());
        }
    }

    private String formatarLista(List<String> itens) {
        return "{" + String.join(",", itens) + "}";
    }
}
