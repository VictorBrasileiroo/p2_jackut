package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.services.JackutService;

/**
 * Fachada publica usada pelo EasyAccept para acessar as operacoes do Jackut.
 *
 * <p>A classe mantem o contrato esperado pelos scripts de aceitacao e delega a
 * execucao das regras de negocio para {@link JackutService}.</p>
 */
public class Facade {

    private final JackutService service;

    /**
     * Cria uma fachada conectada a um novo servico de aplicacao.
     */
    public Facade() {
        this.service = new JackutService();
    }

    /**
     * Remove dados persistidos, usuarios em memoria e sessoes abertas.
     */
    public void zerarSistema() {
        service.zerarSistema();
    }

    /**
     * Cria um usuario no Jackut.
     *
     * @param login login unico do usuario
     * @param senha senha usada para autenticacao
     * @param nome nome inicial do usuario no perfil
     * @throws br.ufal.ic.p2.jackut.exceptions.LoginInvalidoException quando o login estiver vazio
     * @throws br.ufal.ic.p2.jackut.exceptions.SenhaInvalidaException quando a senha estiver vazia
     * @throws br.ufal.ic.p2.jackut.exceptions.ContaExistenteException quando ja existir usuario com o login informado
     */
    public void criarUsuario(String login, String senha, String nome) {
        service.criarUsuario(login, senha, nome);
    }

    /**
     * Abre uma sessao para um usuario autenticado.
     *
     * @param login login do usuario
     * @param senha senha informada para autenticacao
     * @return identificador da sessao aberta
     * @throws br.ufal.ic.p2.jackut.exceptions.LoginOuSenhaInvalidosException quando login ou senha forem invalidos
     */
    public String abrirSessao(String login, String senha) {
        return service.abrirSessao(login, senha);
    }

    /**
     * Retorna um atributo de perfil de um usuario cadastrado.
     *
     * @param login login do usuario consultado
     * @param atributo nome do atributo desejado
     * @return valor armazenado para o atributo
     * @throws br.ufal.ic.p2.jackut.exceptions.UsuarioNaoCadastradoException quando o usuario nao existir
     * @throws br.ufal.ic.p2.jackut.exceptions.AtributoNaoPreenchidoException quando o atributo nao estiver preenchido
     */
    public String getAtributoUsuario(String login, String atributo) {
        return service.getAtributoUsuario(login, atributo);
    }

    /**
     * Altera ou cria um atributo no perfil do usuario autenticado.
     *
     * @param id identificador da sessao aberta
     * @param atributo nome do atributo que sera alterado
     * @param valor novo valor do atributo
     * @throws br.ufal.ic.p2.jackut.exceptions.UsuarioNaoCadastradoException quando a sessao nao identificar usuario valido
     */
    public void editarPerfil(String id, String atributo, String valor) {
        service.editarPerfil(id, atributo, valor);
    }

    /**
     * Solicita ou confirma amizade entre o usuario autenticado e outro usuario.
     *
     * @param id identificador da sessao aberta
     * @param amigo login do usuario a ser adicionado
     * @throws br.ufal.ic.p2.jackut.exceptions.UsuarioNaoCadastradoException quando a sessao ou o amigo forem invalidos
     * @throws br.ufal.ic.p2.jackut.exceptions.AutoAmizadeException quando o usuario tentar adicionar a si mesmo
     * @throws br.ufal.ic.p2.jackut.exceptions.AmigoJaAdicionadoException quando a amizade ja estiver confirmada
     * @throws br.ufal.ic.p2.jackut.exceptions.AmigoPendenteException quando ja existir convite pendente
     */
    public void adicionarAmigo(String id, String amigo) {
        service.adicionarAmigo(id, amigo);
    }

    /**
     * Verifica se dois usuarios possuem amizade confirmada.
     *
     * @param login login de um usuario
     * @param amigo login do outro usuario
     * @return {@code true} se os usuarios forem amigos, ou {@code false} caso contrario
     * @throws br.ufal.ic.p2.jackut.exceptions.UsuarioNaoCadastradoException quando algum usuario nao existir
     */
    public boolean ehAmigo(String login, String amigo) {
        return service.ehAmigo(login, amigo);
    }

    /**
     * Lista os amigos confirmados de um usuario.
     *
     * @param login login do usuario consultado
     * @return lista de amigos no formato exigido pelo EasyAccept
     * @throws br.ufal.ic.p2.jackut.exceptions.UsuarioNaoCadastradoException quando o usuario nao existir
     */
    public String getAmigos(String login) {
        return service.getAmigos(login);
    }

    /**
     * Envia um recado do usuario autenticado para outro usuario cadastrado.
     *
     * @param id identificador da sessao aberta
     * @param destinatario login do usuario que recebera o recado
     * @param recado texto do recado enviado
     * @throws br.ufal.ic.p2.jackut.exceptions.UsuarioNaoCadastradoException quando a sessao ou o destinatario forem invalidos
     * @throws br.ufal.ic.p2.jackut.exceptions.AutoRecadoException quando o usuario tentar enviar recado para si mesmo
     */
    public void enviarRecado(String id, String destinatario, String recado) {
        service.enviarRecado(id, destinatario, recado);
    }

    /**
     * Le e remove o primeiro recado recebido pelo usuario autenticado.
     *
     * @param id identificador da sessao aberta
     * @return texto do primeiro recado da fila
     * @throws br.ufal.ic.p2.jackut.exceptions.UsuarioNaoCadastradoException quando a sessao nao identificar usuario valido
     * @throws br.ufal.ic.p2.jackut.exceptions.SemRecadosException quando o usuario nao possuir recados pendentes
     */
    public String lerRecado(String id) {
        return service.lerRecado(id);
    }

    /**
     * Salva o estado persistente do sistema.
     *
     * @throws IllegalStateException quando os dados nao puderem ser salvos
     */
    public void encerrarSistema() {
        service.encerrarSistema();
    }
}
