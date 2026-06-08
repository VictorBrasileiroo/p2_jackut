package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.AmigoJaAdicionadoException;
import br.ufal.ic.p2.jackut.exceptions.AmigoPendenteException;
import br.ufal.ic.p2.jackut.exceptions.AtributoNaoPreenchidoException;
import br.ufal.ic.p2.jackut.exceptions.AutoAmizadeException;
import br.ufal.ic.p2.jackut.exceptions.AutoRecadoException;
import br.ufal.ic.p2.jackut.exceptions.ContaExistenteException;
import br.ufal.ic.p2.jackut.exceptions.LoginInvalidoException;
import br.ufal.ic.p2.jackut.exceptions.LoginOuSenhaInvalidosException;
import br.ufal.ic.p2.jackut.exceptions.SemRecadosException;
import br.ufal.ic.p2.jackut.exceptions.SenhaInvalidaException;
import br.ufal.ic.p2.jackut.exceptions.UsuarioNaoCadastradoException;
import br.ufal.ic.p2.jackut.models.EstadoJackut;
import br.ufal.ic.p2.jackut.models.Recado;
import br.ufal.ic.p2.jackut.models.Sessao;
import br.ufal.ic.p2.jackut.models.Usuario;
import br.ufal.ic.p2.jackut.persistence.PersistenciaService;
import br.ufal.ic.p2.jackut.repositories.SessaoRepository;
import br.ufal.ic.p2.jackut.repositories.UsuarioRepository;
import java.util.List;

/**
 * Servico de aplicacao que coordena os casos de uso do Jackut.
 *
 * <p>A classe valida fluxos de negocio, consulta repositories, aciona modelos
 * de dominio e delega a persistencia do estado do sistema.</p>
 */
public class JackutService {

    private final EstadoJackut estado;
    private final UsuarioRepository usuarioRepository;
    private final SessaoRepository sessaoRepository;
    private final PersistenciaService persistenciaService;

    /**
     * Cria o servico carregando o estado persistido e preparando os repositories.
     *
     * @throws IllegalStateException quando o estado persistido nao puder ser carregado
     */
    public JackutService() {
        this.persistenciaService = new PersistenciaService();
        this.estado = persistenciaService.carregar();
        this.usuarioRepository = new UsuarioRepository(estado);
        this.sessaoRepository = new SessaoRepository();
    }

    /**
     * Limpa todos os dados do sistema e remove o arquivo persistido.
     *
     * @throws IllegalStateException quando os dados persistidos nao puderem ser apagados
     */
    public void zerarSistema() {
        estado.limpar();
        sessaoRepository.limpar();
        persistenciaService.apagar();
    }

    /**
     * Cria uma conta de usuario com login unico, senha e nome.
     *
     * @param login login unico do usuario
     * @param senha senha usada para autenticacao
     * @param nome nome inicial do perfil
     * @throws LoginInvalidoException quando o login estiver vazio
     * @throws SenhaInvalidaException quando a senha estiver vazia
     * @throws ContaExistenteException quando ja existir usuario com o login informado
     */
    public void criarUsuario(String login, String senha, String nome) {
        validarLogin(login);
        validarSenha(senha);

        if (usuarioRepository.existe(login)) {
            throw new ContaExistenteException();
        }

        usuarioRepository.adicionar(new Usuario(login, senha, nome));
    }

    /**
     * Consulta um atributo de perfil de um usuario cadastrado.
     *
     * @param login login do usuario consultado
     * @param atributo nome do atributo desejado
     * @return valor do atributo armazenado no perfil
     * @throws UsuarioNaoCadastradoException quando o usuario nao existir
     * @throws AtributoNaoPreenchidoException quando o atributo nao estiver preenchido
     */
    public String getAtributoUsuario(String login, String atributo) {
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
     * Autentica um usuario e cria uma sessao de uso.
     *
     * @param login login do usuario
     * @param senha senha informada para autenticacao
     * @return identificador da sessao criada
     * @throws LoginOuSenhaInvalidosException quando login ou senha forem invalidos
     */
    public String abrirSessao(String login, String senha) {
        Usuario usuario = usuarioRepository.buscarPorLogin(login);
        if (usuario == null || !usuario.senhaConfere(senha)) {
            throw new LoginOuSenhaInvalidosException();
        }

        return sessaoRepository.criarSessao(usuario).getId();
    }

    /**
     * Salva o estado persistente do sistema.
     *
     * @throws IllegalStateException quando o estado nao puder ser salvo
     */
    public void encerrarSistema() {
        persistenciaService.salvar(estado);
    }

    /**
     * Edita ou cria um atributo de perfil do usuario autenticado.
     *
     * @param id identificador da sessao aberta
     * @param atributo nome do atributo alterado
     * @param valor novo valor do atributo
     * @throws UsuarioNaoCadastradoException quando a sessao nao identificar usuario valido
     */
    public void editarPerfil(String id, String atributo, String valor) {
        Usuario usuario = buscarUsuarioPorSessao(id);
        usuario.editarPerfil(atributo, valor);
    }

    /**
     * Solicita ou confirma amizade para o usuario autenticado.
     *
     * @param id identificador da sessao aberta
     * @param loginAmigo login do usuario a ser adicionado
     * @throws UsuarioNaoCadastradoException quando a sessao ou o amigo forem invalidos
     * @throws AutoAmizadeException quando o usuario tentar adicionar a si mesmo
     * @throws AmigoJaAdicionadoException quando a amizade ja estiver confirmada
     * @throws AmigoPendenteException quando ja houver convite pendente
     */
    public void adicionarAmigo(String id, String loginAmigo) {
        Usuario usuario = buscarUsuarioPorSessao(id);
        Usuario amigo = usuarioRepository.buscarPorLogin(loginAmigo);

        if (amigo == null) {
            throw new UsuarioNaoCadastradoException();
        }

        if (usuario.getLogin().equals(loginAmigo)) {
            throw new AutoAmizadeException();
        }

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
    public boolean ehAmigo(String login, String loginAmigo) {
        Usuario usuario = usuarioRepository.buscarPorLogin(login);
        Usuario amigo = usuarioRepository.buscarPorLogin(loginAmigo);

        if (usuario == null || amigo == null) {
            throw new UsuarioNaoCadastradoException();
        }

        return usuario.ehAmigo(loginAmigo);
    }

    /**
     * Retorna a lista de amigos no formato esperado pelo EasyAccept.
     *
     * @param login login do usuario consultado
     * @return representacao textual dos amigos confirmados
     * @throws UsuarioNaoCadastradoException quando o usuario nao existir
     */
    public String getAmigos(String login) {
        Usuario usuario = usuarioRepository.buscarPorLogin(login);
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException();
        }

        List<String> amigos = usuario.getAmigos();
        return "{" + String.join(",", amigos) + "}";
    }

    /**
     * Envia um recado do usuario autenticado para outro usuario cadastrado.
     *
     * @param id identificador da sessao aberta
     * @param loginDestinatario login do usuario destinatario
     * @param mensagem texto do recado enviado
     * @throws UsuarioNaoCadastradoException quando a sessao ou o destinatario forem invalidos
     * @throws AutoRecadoException quando o remetente tentar enviar recado para si mesmo
     */
    public void enviarRecado(String id, String loginDestinatario, String mensagem) {
        Usuario remetente = buscarUsuarioPorSessao(id);
        Usuario destinatario = usuarioRepository.buscarPorLogin(loginDestinatario);

        if (destinatario == null) {
            throw new UsuarioNaoCadastradoException();
        }

        if (remetente.getLogin().equals(loginDestinatario)) {
            throw new AutoRecadoException();
        }

        destinatario.receberRecado(new Recado(remetente.getLogin(), mensagem));
    }

    /**
     * Le e remove o primeiro recado recebido pelo usuario autenticado.
     *
     * @param id identificador da sessao aberta
     * @return texto do primeiro recado pendente
     * @throws UsuarioNaoCadastradoException quando a sessao nao identificar usuario valido
     * @throws SemRecadosException quando o usuario nao possuir recados pendentes
     */
    public String lerRecado(String id) {
        Usuario usuario = buscarUsuarioPorSessao(id);

        if (!usuario.possuiRecado()) {
            throw new SemRecadosException();
        }

        return usuario.lerProximoRecado().getMensagem();
    }

    /**
     * Valida o login informado para criacao de conta.
     *
     * @param login login que sera validado
     * @throws LoginInvalidoException quando o login estiver vazio
     */
    private void validarLogin(String login) {
        if (login == null || login.isEmpty()) {
            throw new LoginInvalidoException();
        }
    }

    /**
     * Valida a senha informada para criacao de conta.
     *
     * @param senha senha que sera validada
     * @throws SenhaInvalidaException quando a senha estiver vazia
     */
    private void validarSenha(String senha) {
        if (senha == null || senha.isEmpty()) {
            throw new SenhaInvalidaException();
        }
    }

    /**
     * Localiza o usuario autenticado por uma sessao.
     *
     * @param id identificador da sessao aberta
     * @return usuario associado a sessao
     * @throws UsuarioNaoCadastradoException quando a sessao nao existir ou apontar para usuario invalido
     */
    private Usuario buscarUsuarioPorSessao(String id) {
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
}
