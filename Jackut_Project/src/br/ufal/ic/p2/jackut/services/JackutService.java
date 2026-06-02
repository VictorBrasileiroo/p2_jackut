package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.AmigoJaAdicionadoException;
import br.ufal.ic.p2.jackut.exceptions.AmigoPendenteException;
import br.ufal.ic.p2.jackut.exceptions.AtributoNaoPreenchidoException;
import br.ufal.ic.p2.jackut.exceptions.AutoAmizadeException;
import br.ufal.ic.p2.jackut.exceptions.ContaExistenteException;
import br.ufal.ic.p2.jackut.exceptions.LoginInvalidoException;
import br.ufal.ic.p2.jackut.exceptions.LoginOuSenhaInvalidosException;
import br.ufal.ic.p2.jackut.exceptions.SenhaInvalidaException;
import br.ufal.ic.p2.jackut.exceptions.UsuarioNaoCadastradoException;
import br.ufal.ic.p2.jackut.models.EstadoJackut;
import br.ufal.ic.p2.jackut.models.Sessao;
import br.ufal.ic.p2.jackut.models.Usuario;
import br.ufal.ic.p2.jackut.persistence.PersistenciaService;
import br.ufal.ic.p2.jackut.repositories.SessaoRepository;
import br.ufal.ic.p2.jackut.repositories.UsuarioRepository;
import java.util.List;

public class JackutService {

    private final EstadoJackut estado;
    private final UsuarioRepository usuarioRepository;
    private final SessaoRepository sessaoRepository;
    private final PersistenciaService persistenciaService;

    public JackutService() {
        this.persistenciaService = new PersistenciaService();
        this.estado = persistenciaService.carregar();
        this.usuarioRepository = new UsuarioRepository(estado);
        this.sessaoRepository = new SessaoRepository();
    }

    public void zerarSistema() {
        estado.limpar();
        sessaoRepository.limpar();
        persistenciaService.apagar();
    }

    public void criarUsuario(String login, String senha, String nome) {
        validarLogin(login);
        validarSenha(senha);

        if (usuarioRepository.existe(login)) {
            throw new ContaExistenteException();
        }

        usuarioRepository.adicionar(new Usuario(login, senha, nome));
    }

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

    public String abrirSessao(String login, String senha) {
        Usuario usuario = usuarioRepository.buscarPorLogin(login);
        if (usuario == null || !usuario.senhaConfere(senha)) {
            throw new LoginOuSenhaInvalidosException();
        }

        return sessaoRepository.criarSessao(usuario).getId();
    }

    public void encerrarSistema() {
        persistenciaService.salvar(estado);
    }

    public void editarPerfil(String id, String atributo, String valor) {
        Usuario usuario = buscarUsuarioPorSessao(id);
        usuario.editarPerfil(atributo, valor);
    }

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

    public boolean ehAmigo(String login, String loginAmigo) {
        Usuario usuario = usuarioRepository.buscarPorLogin(login);
        Usuario amigo = usuarioRepository.buscarPorLogin(loginAmigo);

        if (usuario == null || amigo == null) {
            throw new UsuarioNaoCadastradoException();
        }

        return usuario.ehAmigo(loginAmigo);
    }

    public String getAmigos(String login) {
        Usuario usuario = usuarioRepository.buscarPorLogin(login);
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException();
        }

        List<String> amigos = usuario.getAmigos();
        return "{" + String.join(",", amigos) + "}";
    }

    private void validarLogin(String login) {
        if (login == null || login.isEmpty()) {
            throw new LoginInvalidoException();
        }
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.isEmpty()) {
            throw new SenhaInvalidaException();
        }
    }

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
