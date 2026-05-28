package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.ContaExistenteException;
import br.ufal.ic.p2.jackut.exceptions.LoginInvalidoException;
import br.ufal.ic.p2.jackut.exceptions.LoginOuSenhaInvalidosException;
import br.ufal.ic.p2.jackut.exceptions.SenhaInvalidaException;
import br.ufal.ic.p2.jackut.exceptions.UsuarioNaoCadastradoException;
import br.ufal.ic.p2.jackut.models.EstadoJackut;
import br.ufal.ic.p2.jackut.models.Usuario;
import br.ufal.ic.p2.jackut.persistence.PersistenciaService;
import br.ufal.ic.p2.jackut.repositories.SessaoRepository;
import br.ufal.ic.p2.jackut.repositories.UsuarioRepository;

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
}
