package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.AutoIdoloException;
import br.ufal.ic.p2.jackut.exceptions.AutoInimigoException;
import br.ufal.ic.p2.jackut.exceptions.AutoPaqueraException;
import br.ufal.ic.p2.jackut.exceptions.FuncaoInvalidaException;
import br.ufal.ic.p2.jackut.exceptions.IdoloJaAdicionadoException;
import br.ufal.ic.p2.jackut.exceptions.InimigoJaAdicionadoException;
import br.ufal.ic.p2.jackut.exceptions.PaqueraJaAdicionadaException;
import br.ufal.ic.p2.jackut.exceptions.UsuarioNaoCadastradoException;
import br.ufal.ic.p2.jackut.models.Usuario;
import br.ufal.ic.p2.jackut.repositories.UsuarioRepository;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Servico de aplicacao responsavel por idolos, paqueras e inimigos.
 */
public class RelacionamentoService {

    private final UsuarioRepository usuarioRepository;
    private final SessaoService sessaoService;
    private final RecadoService recadoService;

    /**
     * Cria o servico de relacionamentos.
     *
     * @param usuarioRepository repository de usuarios
     * @param sessaoService servico de sessoes
     * @param recadoService servico de recados
     */
    public RelacionamentoService(
            UsuarioRepository usuarioRepository,
            SessaoService sessaoService,
            RecadoService recadoService) {
        this.usuarioRepository = usuarioRepository;
        this.sessaoService = sessaoService;
        this.recadoService = recadoService;
    }

    /**
     * Adiciona um idolo ao usuario autenticado.
     *
     * @param id identificador da sessao
     * @param loginIdolo login do idolo
     * @throws UsuarioNaoCadastradoException quando a sessao ou o idolo forem invalidos
     * @throws AutoIdoloException quando o usuario tentar ser fa de si mesmo
     * @throws IdoloJaAdicionadoException quando o idolo ja estiver registrado
     * @throws FuncaoInvalidaException quando o idolo tiver marcado o usuario como inimigo
     */
    public void adicionarIdolo(String id, String loginIdolo)
            throws UsuarioNaoCadastradoException, AutoIdoloException, IdoloJaAdicionadoException,
            FuncaoInvalidaException {
        Usuario usuario = sessaoService.buscarUsuarioPorSessao(id);
        Usuario idolo = usuarioRepository.buscarPorLogin(loginIdolo);

        if (idolo == null) {
            throw new UsuarioNaoCadastradoException();
        }
        if (usuario.getLogin().equals(loginIdolo)) {
            throw new AutoIdoloException();
        }
        validarBloqueioPorInimigo(usuario, idolo);
        if (usuario.ehFaDe(loginIdolo)) {
            throw new IdoloJaAdicionadoException();
        }

        usuario.adicionarIdolo(loginIdolo);
    }

    /**
     * Verifica se um usuario e fa de outro.
     *
     * @param login login do fa
     * @param idolo login do idolo
     * @return {@code true} quando a relacao existir, ou {@code false} caso contrario
     * @throws UsuarioNaoCadastradoException quando algum usuario nao existir
     */
    public boolean ehFa(String login, String idolo) throws UsuarioNaoCadastradoException {
        Usuario usuario = usuarioRepository.buscarPorLogin(login);
        Usuario usuarioIdolo = usuarioRepository.buscarPorLogin(idolo);
        if (usuario == null || usuarioIdolo == null) {
            throw new UsuarioNaoCadastradoException();
        }

        return usuario.ehFaDe(idolo);
    }

    /**
     * Lista os fas de um usuario.
     *
     * @param login login do usuario consultado
     * @return fas formatados para o EasyAccept
     * @throws UsuarioNaoCadastradoException quando o usuario nao existir
     */
    public String getFas(String login) throws UsuarioNaoCadastradoException {
        if (usuarioRepository.buscarPorLogin(login) == null) {
            throw new UsuarioNaoCadastradoException();
        }

        List<String> fas = new ArrayList<String>();
        for (Usuario usuario : usuarioRepository.listar()) {
            if (usuario.ehFaDe(login)) {
                fas.add(usuario.getLogin());
            }
        }

        return formatarLista(fas);
    }

    /**
     * Adiciona uma paquera ao usuario autenticado.
     *
     * @param id identificador da sessao
     * @param loginPaquera login da paquera
     * @throws UsuarioNaoCadastradoException quando a sessao ou a paquera forem invalidas
     * @throws AutoPaqueraException quando o usuario tentar ser paquera de si mesmo
     * @throws PaqueraJaAdicionadaException quando a paquera ja estiver registrada
     * @throws FuncaoInvalidaException quando a paquera tiver marcado o usuario como inimigo
     */
    public void adicionarPaquera(String id, String loginPaquera)
            throws UsuarioNaoCadastradoException, AutoPaqueraException, PaqueraJaAdicionadaException,
            FuncaoInvalidaException {
        Usuario usuario = sessaoService.buscarUsuarioPorSessao(id);
        Usuario paquera = usuarioRepository.buscarPorLogin(loginPaquera);

        if (paquera == null) {
            throw new UsuarioNaoCadastradoException();
        }
        if (usuario.getLogin().equals(loginPaquera)) {
            throw new AutoPaqueraException();
        }
        validarBloqueioPorInimigo(usuario, paquera);
        if (usuario.ehPaqueraDe(loginPaquera)) {
            throw new PaqueraJaAdicionadaException();
        }

        usuario.adicionarPaquera(loginPaquera);
        if (paquera.ehPaqueraDe(usuario.getLogin())) {
            recadoService.enviarRecadoSistema(
                    usuario,
                    textoContrato(paquera.getNome() + " \u00e9 seu paquera - Recado do Jackut.")
            );
            recadoService.enviarRecadoSistema(
                    paquera,
                    textoContrato(usuario.getNome() + " \u00e9 seu paquera - Recado do Jackut.")
            );
        }
    }

    /**
     * Verifica se o usuario autenticado marcou outro como paquera.
     *
     * @param id identificador da sessao
     * @param loginPaquera login da paquera
     * @return {@code true} quando a relacao existir, ou {@code false} caso contrario
     * @throws UsuarioNaoCadastradoException quando a sessao ou a paquera forem invalidas
     */
    public boolean ehPaquera(String id, String loginPaquera) throws UsuarioNaoCadastradoException {
        Usuario usuario = sessaoService.buscarUsuarioPorSessao(id);
        if (usuarioRepository.buscarPorLogin(loginPaquera) == null) {
            throw new UsuarioNaoCadastradoException();
        }

        return usuario.ehPaqueraDe(loginPaquera);
    }

    /**
     * Lista as paqueras do usuario autenticado.
     *
     * @param id identificador da sessao
     * @return paqueras formatadas para o EasyAccept
     * @throws UsuarioNaoCadastradoException quando a sessao nao identificar usuario valido
     */
    public String getPaqueras(String id) throws UsuarioNaoCadastradoException {
        Usuario usuario = sessaoService.buscarUsuarioPorSessao(id);
        return formatarLista(usuario.getPaqueras());
    }

    /**
     * Adiciona um inimigo ao usuario autenticado.
     *
     * @param id identificador da sessao
     * @param loginInimigo login do inimigo
     * @throws UsuarioNaoCadastradoException quando a sessao ou o inimigo forem invalidos
     * @throws AutoInimigoException quando o usuario tentar ser inimigo de si mesmo
     * @throws InimigoJaAdicionadoException quando o inimigo ja estiver registrado
     */
    public void adicionarInimigo(String id, String loginInimigo)
            throws UsuarioNaoCadastradoException, AutoInimigoException, InimigoJaAdicionadoException {
        Usuario usuario = sessaoService.buscarUsuarioPorSessao(id);
        Usuario inimigo = usuarioRepository.buscarPorLogin(loginInimigo);

        if (inimigo == null) {
            throw new UsuarioNaoCadastradoException();
        }
        if (usuario.getLogin().equals(loginInimigo)) {
            throw new AutoInimigoException();
        }
        if (usuario.possuiInimigo(loginInimigo)) {
            throw new InimigoJaAdicionadoException();
        }

        usuario.adicionarInimigo(loginInimigo);
    }

    /**
     * Remove referencias de todos os usuarios a uma conta removida.
     *
     * @param login login removido
     */
    public void removerReferenciasDoUsuario(String login) {
        for (Usuario usuario : usuarioRepository.listar()) {
            usuario.removerReferenciasAoUsuario(login);
        }
    }

    private void validarBloqueioPorInimigo(Usuario origem, Usuario destino) throws FuncaoInvalidaException {
        if (destino.possuiInimigo(origem.getLogin())) {
            throw new FuncaoInvalidaException(destino.getNome());
        }
    }

    private String formatarLista(List<String> itens) {
        return "{" + String.join(",", itens) + "}";
    }

    private String textoContrato(String texto) {
        byte[] bytesIso = texto.getBytes(StandardCharsets.ISO_8859_1);
        return new String(bytesIso, Charset.defaultCharset());
    }
}
