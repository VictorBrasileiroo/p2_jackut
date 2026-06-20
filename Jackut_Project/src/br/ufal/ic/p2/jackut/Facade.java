package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.exceptions.JackutException;
import br.ufal.ic.p2.jackut.services.JackutApplication;

/**
 * Fachada publica usada pelo EasyAccept para acessar as operacoes do Jackut.
 *
 * <p>A classe mantem o contrato esperado pelos scripts de aceitacao e delega a
 * execucao das regras de negocio para a aplicacao do sistema.</p>
 */
public class Facade {

    private final JackutApplication application;

    /**
     * Cria uma fachada conectada a uma nova aplicacao do Jackut.
     */
    public Facade() {
        this.application = new JackutApplication();
    }

    /**
     * Remove dados persistidos, usuarios em memoria e sessoes abertas.
     */
    public void zerarSistema() {
        application.zerarSistema();
    }

    /**
     * Cria um usuario no Jackut.
     *
     * @param login login unico do usuario
     * @param senha senha usada para autenticacao
     * @param nome nome inicial do usuario no perfil
     * @throws JackutException quando alguma regra de criacao de usuario for violada
     */
    public void criarUsuario(String login, String senha, String nome) throws JackutException {
        application.criarUsuario(login, senha, nome);
    }

    /**
     * Abre uma sessao para um usuario autenticado.
     *
     * @param login login do usuario
     * @param senha senha informada para autenticacao
     * @return identificador da sessao aberta
     * @throws JackutException quando login ou senha forem invalidos
     */
    public String abrirSessao(String login, String senha) throws JackutException {
        return application.abrirSessao(login, senha);
    }

    /**
     * Retorna um atributo de perfil de um usuario cadastrado.
     *
     * @param login login do usuario consultado
     * @param atributo nome do atributo desejado
     * @return valor armazenado para o atributo
     * @throws JackutException quando o usuario ou atributo nao existir
     */
    public String getAtributoUsuario(String login, String atributo) throws JackutException {
        return application.getAtributoUsuario(login, atributo);
    }

    /**
     * Altera ou cria um atributo no perfil do usuario autenticado.
     *
     * @param id identificador da sessao aberta
     * @param atributo nome do atributo que sera alterado
     * @param valor novo valor do atributo
     * @throws JackutException quando a sessao nao identificar usuario valido
     */
    public void editarPerfil(String id, String atributo, String valor) throws JackutException {
        application.editarPerfil(id, atributo, valor);
    }

    /**
     * Solicita ou confirma amizade entre o usuario autenticado e outro usuario.
     *
     * @param id identificador da sessao aberta
     * @param amigo login do usuario a ser adicionado
     * @throws JackutException quando alguma regra de amizade for violada
     */
    public void adicionarAmigo(String id, String amigo) throws JackutException {
        application.adicionarAmigo(id, amigo);
    }

    /**
     * Verifica se dois usuarios possuem amizade confirmada.
     *
     * @param login login de um usuario
     * @param amigo login do outro usuario
     * @return {@code true} se os usuarios forem amigos, ou {@code false} caso contrario
     * @throws JackutException quando algum usuario nao existir
     */
    public boolean ehAmigo(String login, String amigo) throws JackutException {
        return application.ehAmigo(login, amigo);
    }

    /**
     * Lista os amigos confirmados de um usuario.
     *
     * @param login login do usuario consultado
     * @return lista de amigos no formato exigido pelo EasyAccept
     * @throws JackutException quando o usuario nao existir
     */
    public String getAmigos(String login) throws JackutException {
        return application.getAmigos(login);
    }

    /**
     * Envia um recado do usuario autenticado para outro usuario cadastrado.
     *
     * @param id identificador da sessao aberta
     * @param destinatario login do usuario que recebera o recado
     * @param recado texto do recado enviado
     * @throws JackutException quando alguma regra de recado for violada
     */
    public void enviarRecado(String id, String destinatario, String recado) throws JackutException {
        application.enviarRecado(id, destinatario, recado);
    }

    /**
     * Le e remove o primeiro recado recebido pelo usuario autenticado.
     *
     * @param id identificador da sessao aberta
     * @return texto do primeiro recado da fila
     * @throws JackutException quando nao houver usuario valido ou recados pendentes
     */
    public String lerRecado(String id) throws JackutException {
        return application.lerRecado(id);
    }

    /**
     * Cria uma comunidade cujo dono e o usuario autenticado.
     *
     * @param sessao identificador da sessao aberta
     * @param nome nome unico da comunidade
     * @param descricao descricao da comunidade
     * @throws JackutException quando alguma regra de comunidade for violada
     */
    public void criarComunidade(String sessao, String nome, String descricao) throws JackutException {
        application.criarComunidade(sessao, nome, descricao);
    }

    /**
     * Retorna a descricao de uma comunidade.
     *
     * @param nome nome da comunidade
     * @return descricao da comunidade
     * @throws JackutException quando a comunidade nao existir
     */
    public String getDescricaoComunidade(String nome) throws JackutException {
        return application.getDescricaoComunidade(nome);
    }

    /**
     * Retorna o dono de uma comunidade.
     *
     * @param nome nome da comunidade
     * @return login do dono da comunidade
     * @throws JackutException quando a comunidade nao existir
     */
    public String getDonoComunidade(String nome) throws JackutException {
        return application.getDonoComunidade(nome);
    }

    /**
     * Lista os membros de uma comunidade.
     *
     * @param nome nome da comunidade
     * @return membros no formato exigido pelo EasyAccept
     * @throws JackutException quando a comunidade nao existir
     */
    public String getMembrosComunidade(String nome) throws JackutException {
        return application.getMembrosComunidade(nome);
    }

    /**
     * Adiciona o usuario autenticado a uma comunidade.
     *
     * @param sessao identificador da sessao aberta
     * @param nome nome da comunidade
     * @throws JackutException quando alguma regra de entrada em comunidade for violada
     */
    public void adicionarComunidade(String sessao, String nome) throws JackutException {
        application.adicionarComunidade(sessao, nome);
    }

    /**
     * Lista as comunidades de um usuario.
     *
     * @param login login do usuario consultado
     * @return comunidades no formato exigido pelo EasyAccept
     * @throws JackutException quando o usuario nao existir
     */
    public String getComunidades(String login) throws JackutException {
        return application.getComunidades(login);
    }

    /**
     * Envia mensagem para uma comunidade.
     *
     * @param id identificador da sessao aberta
     * @param comunidade nome da comunidade
     * @param mensagem texto da mensagem
     * @throws JackutException quando alguma regra de mensagem for violada
     */
    public void enviarMensagem(String id, String comunidade, String mensagem) throws JackutException {
        application.enviarMensagem(id, comunidade, mensagem);
    }

    /**
     * Le e remove a primeira mensagem de comunidade recebida.
     *
     * @param id identificador da sessao aberta
     * @return texto da primeira mensagem da fila
     * @throws JackutException quando nao houver usuario valido ou mensagens pendentes
     */
    public String lerMensagem(String id) throws JackutException {
        return application.lerMensagem(id);
    }

    /**
     * Adiciona um idolo ao usuario autenticado.
     *
     * @param id identificador da sessao aberta
     * @param idolo login do usuario idolo
     * @throws JackutException quando alguma regra de idolo for violada
     */
    public void adicionarIdolo(String id, String idolo) throws JackutException {
        application.adicionarIdolo(id, idolo);
    }

    /**
     * Verifica se um usuario e fa de outro.
     *
     * @param login login do fa
     * @param idolo login do idolo
     * @return {@code true} quando a relacao existir, ou {@code false} caso contrario
     * @throws JackutException quando algum usuario nao existir
     */
    public boolean ehFa(String login, String idolo) throws JackutException {
        return application.ehFa(login, idolo);
    }

    /**
     * Lista os fas de um usuario.
     *
     * @param login login do usuario consultado
     * @return fas no formato exigido pelo EasyAccept
     * @throws JackutException quando o usuario nao existir
     */
    public String getFas(String login) throws JackutException {
        return application.getFas(login);
    }

    /**
     * Adiciona uma paquera ao usuario autenticado.
     *
     * @param id identificador da sessao aberta
     * @param paquera login da paquera
     * @throws JackutException quando alguma regra de paquera for violada
     */
    public void adicionarPaquera(String id, String paquera) throws JackutException {
        application.adicionarPaquera(id, paquera);
    }

    /**
     * Verifica se o usuario autenticado marcou outro como paquera.
     *
     * @param id identificador da sessao aberta
     * @param paquera login da paquera
     * @return {@code true} quando a relacao existir, ou {@code false} caso contrario
     * @throws JackutException quando algum usuario nao existir
     */
    public boolean ehPaquera(String id, String paquera) throws JackutException {
        return application.ehPaquera(id, paquera);
    }

    /**
     * Lista as paqueras do usuario autenticado.
     *
     * @param id identificador da sessao aberta
     * @return paqueras no formato exigido pelo EasyAccept
     * @throws JackutException quando a sessao nao identificar usuario valido
     */
    public String getPaqueras(String id) throws JackutException {
        return application.getPaqueras(id);
    }

    /**
     * Adiciona um inimigo ao usuario autenticado.
     *
     * @param id identificador da sessao aberta
     * @param inimigo login do inimigo
     * @throws JackutException quando alguma regra de inimizade for violada
     */
    public void adicionarInimigo(String id, String inimigo) throws JackutException {
        application.adicionarInimigo(id, inimigo);
    }

    /**
     * Remove a conta associada a sessao informada.
     *
     * @param id identificador da sessao aberta
     * @throws JackutException quando a sessao nao identificar usuario valido
     */
    public void removerUsuario(String id) throws JackutException {
        application.removerUsuario(id);
    }

    /**
     * Salva o estado persistente do sistema.
     */
    public void encerrarSistema() {
        application.encerrarSistema();
    }
}
