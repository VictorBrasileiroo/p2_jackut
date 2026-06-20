package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.JackutException;
import br.ufal.ic.p2.jackut.models.EstadoJackut;
import br.ufal.ic.p2.jackut.models.Usuario;
import br.ufal.ic.p2.jackut.persistence.PersistenciaService;
import br.ufal.ic.p2.jackut.repositories.ComunidadeRepository;
import br.ufal.ic.p2.jackut.repositories.SessaoRepository;
import br.ufal.ic.p2.jackut.repositories.UsuarioRepository;

/**
 * Aplicacao do Jackut que compoe os servicos por area funcional.
 *
 * <p>Esta classe monta dependencias e delega comandos da fachada para servicos
 * menores de usuario, sessao, amizade, recado, comunidade e relacionamentos.</p>
 */
public class JackutApplication {

    private final SistemaService sistemaService;
    private final UsuarioService usuarioService;
    private final SessaoService sessaoService;
    private final AmizadeService amizadeService;
    private final RecadoService recadoService;
    private final ComunidadeService comunidadeService;
    private final RelacionamentoService relacionamentoService;

    /**
     * Cria a aplicacao carregando o estado persistido e preparando os servicos.
     */
    public JackutApplication() {
        PersistenciaService persistenciaService = new PersistenciaService();
        EstadoJackut estado = persistenciaService.carregar();
        UsuarioRepository usuarioRepository = new UsuarioRepository(estado);
        ComunidadeRepository comunidadeRepository = new ComunidadeRepository(estado);
        SessaoRepository sessaoRepository = new SessaoRepository();

        this.sessaoService = new SessaoService(usuarioRepository, sessaoRepository);
        this.usuarioService = new UsuarioService(usuarioRepository, sessaoService);
        this.amizadeService = new AmizadeService(usuarioRepository, sessaoService);
        this.recadoService = new RecadoService(usuarioRepository, sessaoService);
        this.comunidadeService = new ComunidadeService(comunidadeRepository, usuarioRepository, sessaoService);
        this.relacionamentoService = new RelacionamentoService(usuarioRepository, sessaoService, recadoService);
        this.sistemaService = new SistemaService(estado, persistenciaService, sessaoService);
    }

    /**
     * Remove dados persistidos, usuarios em memoria e sessoes abertas.
     */
    public void zerarSistema() {
        sistemaService.zerarSistema();
    }

    /**
     * Cria um usuario.
     *
     * @param login login unico
     * @param senha senha de acesso
     * @param nome nome inicial
     * @throws JackutException quando alguma regra de criacao for violada
     */
    public void criarUsuario(String login, String senha, String nome) throws JackutException {
        usuarioService.criarUsuario(login, senha, nome);
    }

    /**
     * Abre sessao para um usuario.
     *
     * @param login login do usuario
     * @param senha senha informada
     * @return identificador da sessao
     * @throws JackutException quando a autenticacao falhar
     */
    public String abrirSessao(String login, String senha) throws JackutException {
        return sessaoService.abrirSessao(login, senha);
    }

    /**
     * Consulta atributo de perfil.
     *
     * @param login login consultado
     * @param atributo atributo desejado
     * @return valor do atributo
     * @throws JackutException quando o usuario ou o atributo nao existir
     */
    public String getAtributoUsuario(String login, String atributo) throws JackutException {
        return usuarioService.getAtributoUsuario(login, atributo);
    }

    /**
     * Edita perfil do usuario autenticado.
     *
     * @param id identificador da sessao
     * @param atributo atributo alterado
     * @param valor novo valor
     * @throws JackutException quando a sessao nao identificar usuario valido
     */
    public void editarPerfil(String id, String atributo, String valor) throws JackutException {
        usuarioService.editarPerfil(id, atributo, valor);
    }

    /**
     * Solicita ou confirma amizade.
     *
     * @param id identificador da sessao
     * @param amigo login do amigo
     * @throws JackutException quando alguma regra de amizade for violada
     */
    public void adicionarAmigo(String id, String amigo) throws JackutException {
        amizadeService.adicionarAmigo(id, amigo);
    }

    /**
     * Verifica amizade.
     *
     * @param login login de um usuario
     * @param amigo login do outro usuario
     * @return {@code true} quando forem amigos
     * @throws JackutException quando algum usuario nao existir
     */
    public boolean ehAmigo(String login, String amigo) throws JackutException {
        return amizadeService.ehAmigo(login, amigo);
    }

    /**
     * Lista amigos de um usuario.
     *
     * @param login login consultado
     * @return amigos formatados
     * @throws JackutException quando o usuario nao existir
     */
    public String getAmigos(String login) throws JackutException {
        return amizadeService.getAmigos(login);
    }

    /**
     * Envia recado privado.
     *
     * @param id identificador da sessao
     * @param destinatario login do destinatario
     * @param recado texto do recado
     * @throws JackutException quando alguma regra de recado for violada
     */
    public void enviarRecado(String id, String destinatario, String recado) throws JackutException {
        recadoService.enviarRecado(id, destinatario, recado);
    }

    /**
     * Le recado privado.
     *
     * @param id identificador da sessao
     * @return texto do recado
     * @throws JackutException quando nao houver usuario valido ou recados
     */
    public String lerRecado(String id) throws JackutException {
        return recadoService.lerRecado(id);
    }

    /**
     * Cria uma comunidade.
     *
     * @param sessao identificador da sessao
     * @param nome nome da comunidade
     * @param descricao descricao da comunidade
     * @throws JackutException quando alguma regra de comunidade for violada
     */
    public void criarComunidade(String sessao, String nome, String descricao) throws JackutException {
        comunidadeService.criarComunidade(sessao, nome, descricao);
    }

    /**
     * Retorna descricao de comunidade.
     *
     * @param nome nome da comunidade
     * @return descricao da comunidade
     * @throws JackutException quando a comunidade nao existir
     */
    public String getDescricaoComunidade(String nome) throws JackutException {
        return comunidadeService.getDescricaoComunidade(nome);
    }

    /**
     * Retorna dono de comunidade.
     *
     * @param nome nome da comunidade
     * @return login do dono
     * @throws JackutException quando a comunidade nao existir
     */
    public String getDonoComunidade(String nome) throws JackutException {
        return comunidadeService.getDonoComunidade(nome);
    }

    /**
     * Lista membros de comunidade.
     *
     * @param nome nome da comunidade
     * @return membros formatados
     * @throws JackutException quando a comunidade nao existir
     */
    public String getMembrosComunidade(String nome) throws JackutException {
        return comunidadeService.getMembrosComunidade(nome);
    }

    /**
     * Adiciona usuario autenticado a uma comunidade.
     *
     * @param sessao identificador da sessao
     * @param nome nome da comunidade
     * @throws JackutException quando alguma regra de entrada for violada
     */
    public void adicionarComunidade(String sessao, String nome) throws JackutException {
        comunidadeService.adicionarComunidade(sessao, nome);
    }

    /**
     * Lista comunidades de um usuario.
     *
     * @param login login do usuario
     * @return comunidades formatadas
     * @throws JackutException quando o usuario nao existir
     */
    public String getComunidades(String login) throws JackutException {
        return comunidadeService.getComunidades(login);
    }

    /**
     * Envia mensagem para uma comunidade.
     *
     * @param id identificador da sessao
     * @param comunidade nome da comunidade
     * @param mensagem texto da mensagem
     * @throws JackutException quando alguma regra de envio for violada
     */
    public void enviarMensagem(String id, String comunidade, String mensagem) throws JackutException {
        comunidadeService.enviarMensagem(id, comunidade, mensagem);
    }

    /**
     * Le mensagem de comunidade.
     *
     * @param id identificador da sessao
     * @return texto da mensagem
     * @throws JackutException quando nao houver usuario valido ou mensagens
     */
    public String lerMensagem(String id) throws JackutException {
        return comunidadeService.lerMensagem(id);
    }

    /**
     * Adiciona idolo ao usuario autenticado.
     *
     * @param id identificador da sessao
     * @param idolo login do idolo
     * @throws JackutException quando alguma regra de idolo for violada
     */
    public void adicionarIdolo(String id, String idolo) throws JackutException {
        relacionamentoService.adicionarIdolo(id, idolo);
    }

    /**
     * Verifica relacao fa-idolo.
     *
     * @param login login do fa
     * @param idolo login do idolo
     * @return {@code true} quando a relacao existir
     * @throws JackutException quando algum usuario nao existir
     */
    public boolean ehFa(String login, String idolo) throws JackutException {
        return relacionamentoService.ehFa(login, idolo);
    }

    /**
     * Lista fas de um usuario.
     *
     * @param login login do usuario consultado
     * @return fas formatados
     * @throws JackutException quando o usuario nao existir
     */
    public String getFas(String login) throws JackutException {
        return relacionamentoService.getFas(login);
    }

    /**
     * Adiciona paquera ao usuario autenticado.
     *
     * @param id identificador da sessao
     * @param paquera login da paquera
     * @throws JackutException quando alguma regra de paquera for violada
     */
    public void adicionarPaquera(String id, String paquera) throws JackutException {
        relacionamentoService.adicionarPaquera(id, paquera);
    }

    /**
     * Verifica se o usuario autenticado marcou outro como paquera.
     *
     * @param id identificador da sessao
     * @param paquera login da paquera
     * @return {@code true} quando a relacao existir
     * @throws JackutException quando algum usuario nao existir
     */
    public boolean ehPaquera(String id, String paquera) throws JackutException {
        return relacionamentoService.ehPaquera(id, paquera);
    }

    /**
     * Lista paqueras do usuario autenticado.
     *
     * @param id identificador da sessao
     * @return paqueras formatadas
     * @throws JackutException quando a sessao nao identificar usuario valido
     */
    public String getPaqueras(String id) throws JackutException {
        return relacionamentoService.getPaqueras(id);
    }

    /**
     * Adiciona inimigo ao usuario autenticado.
     *
     * @param id identificador da sessao
     * @param inimigo login do inimigo
     * @throws JackutException quando alguma regra de inimizade for violada
     */
    public void adicionarInimigo(String id, String inimigo) throws JackutException {
        relacionamentoService.adicionarInimigo(id, inimigo);
    }

    /**
     * Remove o usuario autenticado e suas referencias no sistema.
     *
     * @param id identificador da sessao
     * @throws JackutException quando a sessao nao identificar usuario valido
     */
    public void removerUsuario(String id) throws JackutException {
        Usuario usuario = sessaoService.buscarUsuarioPorSessao(id);
        String login = usuario.getLogin();
        comunidadeService.removerParticipacaoDeUsuario(login);
        relacionamentoService.removerReferenciasDoUsuario(login);
        recadoService.removerConteudosDoUsuario(login);
        usuarioService.removerUsuario(login);
        sessaoService.removerSessoesDoUsuario(login);
    }

    /**
     * Salva o estado persistente do sistema.
     */
    public void encerrarSistema() {
        sistemaService.encerrarSistema();
    }
}
