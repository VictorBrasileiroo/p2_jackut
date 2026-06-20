package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.ComunidadeExistenteException;
import br.ufal.ic.p2.jackut.exceptions.ComunidadeNaoExisteException;
import br.ufal.ic.p2.jackut.exceptions.SemMensagensException;
import br.ufal.ic.p2.jackut.exceptions.UsuarioJaNaComunidadeException;
import br.ufal.ic.p2.jackut.exceptions.UsuarioNaoCadastradoException;
import br.ufal.ic.p2.jackut.models.Comunidade;
import br.ufal.ic.p2.jackut.models.Mensagem;
import br.ufal.ic.p2.jackut.models.Usuario;
import br.ufal.ic.p2.jackut.repositories.ComunidadeRepository;
import br.ufal.ic.p2.jackut.repositories.UsuarioRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Servico de aplicacao responsavel por comunidades e mensagens coletivas.
 */
public class ComunidadeService {

    private final ComunidadeRepository comunidadeRepository;
    private final UsuarioRepository usuarioRepository;
    private final SessaoService sessaoService;

    /**
     * Cria o servico de comunidades.
     *
     * @param comunidadeRepository repository de comunidades
     * @param usuarioRepository repository de usuarios
     * @param sessaoService servico de sessoes
     */
    public ComunidadeService(
            ComunidadeRepository comunidadeRepository,
            UsuarioRepository usuarioRepository,
            SessaoService sessaoService) {
        this.comunidadeRepository = comunidadeRepository;
        this.usuarioRepository = usuarioRepository;
        this.sessaoService = sessaoService;
    }

    /**
     * Cria uma comunidade para o usuario autenticado.
     *
     * @param sessao identificador da sessao
     * @param nome nome unico da comunidade
     * @param descricao descricao da comunidade
     * @throws UsuarioNaoCadastradoException quando a sessao nao identificar usuario valido
     * @throws ComunidadeExistenteException quando ja existir comunidade com o nome informado
     */
    public void criarComunidade(String sessao, String nome, String descricao)
            throws UsuarioNaoCadastradoException, ComunidadeExistenteException {
        Usuario dono = sessaoService.buscarUsuarioPorSessao(sessao);
        if (comunidadeRepository.existe(nome)) {
            throw new ComunidadeExistenteException();
        }

        Comunidade comunidade = new Comunidade(nome, descricao, dono.getLogin());
        comunidadeRepository.adicionar(comunidade);
        dono.adicionarComunidade(nome);
    }

    /**
     * Retorna a descricao de uma comunidade.
     *
     * @param nome nome da comunidade
     * @return descricao da comunidade
     * @throws ComunidadeNaoExisteException quando a comunidade nao existir
     */
    public String getDescricaoComunidade(String nome) throws ComunidadeNaoExisteException {
        return buscarComunidade(nome).getDescricao();
    }

    /**
     * Retorna o dono de uma comunidade.
     *
     * @param nome nome da comunidade
     * @return login do dono da comunidade
     * @throws ComunidadeNaoExisteException quando a comunidade nao existir
     */
    public String getDonoComunidade(String nome) throws ComunidadeNaoExisteException {
        return buscarComunidade(nome).getDono();
    }

    /**
     * Lista os membros de uma comunidade.
     *
     * @param nome nome da comunidade
     * @return membros formatados para o EasyAccept
     * @throws ComunidadeNaoExisteException quando a comunidade nao existir
     */
    public String getMembrosComunidade(String nome) throws ComunidadeNaoExisteException {
        return formatarLista(buscarComunidade(nome).getMembros());
    }

    /**
     * Adiciona o usuario autenticado a uma comunidade.
     *
     * @param sessao identificador da sessao
     * @param nome nome da comunidade
     * @throws UsuarioNaoCadastradoException quando a sessao nao identificar usuario valido
     * @throws ComunidadeNaoExisteException quando a comunidade nao existir
     * @throws UsuarioJaNaComunidadeException quando o usuario ja participar da comunidade
     */
    public void adicionarComunidade(String sessao, String nome)
            throws UsuarioNaoCadastradoException, ComunidadeNaoExisteException, UsuarioJaNaComunidadeException {
        Usuario usuario = sessaoService.buscarUsuarioPorSessao(sessao);
        Comunidade comunidade = buscarComunidade(nome);

        if (comunidade.possuiMembro(usuario.getLogin())) {
            throw new UsuarioJaNaComunidadeException();
        }

        comunidade.adicionarMembro(usuario.getLogin());
        usuario.adicionarComunidade(nome);
    }

    /**
     * Lista as comunidades de um usuario.
     *
     * @param login login do usuario consultado
     * @return comunidades formatadas para o EasyAccept
     * @throws UsuarioNaoCadastradoException quando o usuario nao existir
     */
    public String getComunidades(String login) throws UsuarioNaoCadastradoException {
        Usuario usuario = usuarioRepository.buscarPorLogin(login);
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException();
        }

        return formatarLista(usuario.getComunidades());
    }

    /**
     * Envia mensagem para todos os membros de uma comunidade.
     *
     * @param id identificador da sessao do remetente
     * @param nomeComunidade nome da comunidade
     * @param texto texto da mensagem
     * @throws UsuarioNaoCadastradoException quando a sessao nao identificar usuario valido
     * @throws ComunidadeNaoExisteException quando a comunidade nao existir
     */
    public void enviarMensagem(String id, String nomeComunidade, String texto)
            throws UsuarioNaoCadastradoException, ComunidadeNaoExisteException {
        Usuario remetente = sessaoService.buscarUsuarioPorSessao(id);
        Comunidade comunidade = buscarComunidade(nomeComunidade);
        Mensagem mensagem = new Mensagem(remetente.getLogin(), texto);

        for (String loginMembro : comunidade.getMembros()) {
            Usuario membro = usuarioRepository.buscarPorLogin(loginMembro);
            if (membro != null && !membro.possuiInimigo(remetente.getLogin())) {
                membro.receberMensagem(mensagem);
            }
        }
    }

    /**
     * Le a primeira mensagem recebida pelo usuario autenticado.
     *
     * @param id identificador da sessao
     * @return texto da primeira mensagem pendente
     * @throws UsuarioNaoCadastradoException quando a sessao nao identificar usuario valido
     * @throws SemMensagensException quando o usuario nao possuir mensagens pendentes
     */
    public String lerMensagem(String id) throws UsuarioNaoCadastradoException, SemMensagensException {
        Usuario usuario = sessaoService.buscarUsuarioPorSessao(id);
        if (!usuario.possuiMensagem()) {
            throw new SemMensagensException();
        }

        return usuario.lerProximaMensagem().getTexto();
    }

    /**
     * Remove um usuario das comunidades e apaga comunidades das quais ele e dono.
     *
     * @param login login do usuario removido
     */
    public void removerParticipacaoDeUsuario(String login) {
        List<String> comunidadesRemovidas = new ArrayList<String>();
        for (Comunidade comunidade : comunidadeRepository.listar()) {
            if (login.equals(comunidade.getDono())) {
                comunidadesRemovidas.add(comunidade.getNome());
                comunidadeRepository.remover(comunidade.getNome());
            } else if (comunidade.possuiMembro(login)) {
                comunidade.removerMembro(login);
            }
        }

        for (Usuario usuario : usuarioRepository.listar()) {
            for (String nomeComunidade : comunidadesRemovidas) {
                usuario.removerComunidade(nomeComunidade);
            }
        }
    }

    private Comunidade buscarComunidade(String nome) throws ComunidadeNaoExisteException {
        Comunidade comunidade = comunidadeRepository.buscarPorNome(nome);
        if (comunidade == null) {
            throw new ComunidadeNaoExisteException();
        }

        return comunidade;
    }

    private String formatarLista(List<String> itens) {
        return "{" + String.join(",", itens) + "}";
    }
}
