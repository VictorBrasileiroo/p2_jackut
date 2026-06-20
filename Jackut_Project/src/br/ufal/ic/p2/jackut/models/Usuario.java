package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Representa um usuario cadastrado no Jackut.
 *
 * <p>O usuario controla seu login, senha, perfil, amizades, comunidades,
 * relacionamentos sociais e filas de recados e mensagens. As colecoes internas
 * sao protegidas contra modificacao externa.</p>
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Login unico do usuario.
     */
    private final String login;

    /**
     * Senha usada para autenticacao do usuario.
     */
    private final String senha;

    /**
     * Atributos dinamicos do perfil do usuario.
     */
    private final Map<String, String> perfil;

    /**
     * Logins dos amigos confirmados.
     */
    private Set<String> amigos;

    /**
     * Logins dos usuarios que receberam convite de amizade pendente.
     */
    private Set<String> convitesEnviados;

    /**
     * Nomes das comunidades das quais o usuario participa.
     */
    private Set<String> comunidades;

    /**
     * Logins dos usuarios marcados como idolos.
     */
    private Set<String> idolos;

    /**
     * Logins dos usuarios marcados como paqueras.
     */
    private Set<String> paqueras;

    /**
     * Logins dos usuarios marcados como inimigos.
     */
    private Set<String> inimigos;

    /**
     * Fila de recados recebidos e ainda nao lidos.
     */
    private Queue<Recado> recadosRecebidos;

    /**
     * Fila de mensagens de comunidade recebidas e ainda nao lidas.
     */
    private Queue<Mensagem> mensagensRecebidas;

    /**
     * Cria um usuario com login, senha e nome inicial.
     *
     * @param login login unico do usuario
     * @param senha senha usada para autenticacao
     * @param nome nome inicial armazenado no perfil
     */
    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.perfil = new LinkedHashMap<String, String>();
        this.perfil.put("nome", nome == null ? "" : nome);
        this.amigos = new LinkedHashSet<String>();
        this.convitesEnviados = new LinkedHashSet<String>();
        this.comunidades = new LinkedHashSet<String>();
        this.idolos = new LinkedHashSet<String>();
        this.paqueras = new LinkedHashSet<String>();
        this.inimigos = new LinkedHashSet<String>();
        this.recadosRecebidos = new LinkedList<Recado>();
        this.mensagensRecebidas = new LinkedList<Mensagem>();
    }

    /**
     * Retorna o login do usuario.
     *
     * @return login unico do usuario
     */
    public String getLogin() {
        return login;
    }

    /**
     * Retorna o nome publico armazenado no perfil.
     *
     * @return nome do usuario
     */
    public String getNome() {
        return getAtributo("nome");
    }

    /**
     * Verifica se a senha informada corresponde a senha do usuario.
     *
     * @param senhaInformada senha recebida para autenticacao
     * @return {@code true} quando a senha estiver correta, ou {@code false} caso contrario
     */
    public boolean senhaConfere(String senhaInformada) {
        return senha.equals(senhaInformada);
    }

    /**
     * Verifica se o perfil possui um atributo preenchido.
     *
     * @param atributo nome do atributo consultado
     * @return {@code true} quando o atributo existir no perfil, ou {@code false} caso contrario
     */
    public boolean possuiAtributo(String atributo) {
        return perfil.containsKey(atributo);
    }

    /**
     * Retorna o valor de um atributo de perfil.
     *
     * @param atributo nome do atributo consultado
     * @return valor do atributo, ou string vazia quando o valor armazenado for nulo
     */
    public String getAtributo(String atributo) {
        String valor = perfil.get(atributo);
        if (valor == null) {
            return "";
        }

        return valor;
    }

    /**
     * Altera ou cria um atributo no perfil do usuario.
     *
     * @param atributo nome do atributo alterado
     * @param valor novo valor do atributo
     */
    public void editarPerfil(String atributo, String valor) {
        perfil.put(atributo, valor == null ? "" : valor);
    }

    /**
     * Registra um convite de amizade enviado para outro usuario.
     *
     * @param loginAmigo login do usuario convidado
     */
    public void solicitarAmizade(String loginAmigo) {
        garantirRelacionamentos();
        convitesEnviados.add(loginAmigo);
    }

    /**
     * Verifica se ja existe convite enviado para um usuario.
     *
     * @param loginAmigo login do usuario consultado
     * @return {@code true} quando existir convite pendente, ou {@code false} caso contrario
     */
    public boolean possuiConviteEnviado(String loginAmigo) {
        garantirRelacionamentos();
        return convitesEnviados.contains(loginAmigo);
    }

    /**
     * Remove um convite de amizade enviado.
     *
     * @param loginAmigo login do usuario cujo convite sera removido
     */
    public void removerConviteEnviado(String loginAmigo) {
        garantirRelacionamentos();
        convitesEnviados.remove(loginAmigo);
    }

    /**
     * Adiciona um usuario a lista de amigos confirmados.
     *
     * @param loginAmigo login do amigo confirmado
     */
    public void adicionarAmigoConfirmado(String loginAmigo) {
        garantirRelacionamentos();
        amigos.add(loginAmigo);
    }

    /**
     * Remove uma amizade confirmada.
     *
     * @param loginAmigo login do amigo removido
     */
    public void removerAmigo(String loginAmigo) {
        garantirRelacionamentos();
        amigos.remove(loginAmigo);
    }

    /**
     * Verifica se ha amizade confirmada com outro usuario.
     *
     * @param loginAmigo login do usuario consultado
     * @return {@code true} quando o usuario for amigo confirmado, ou {@code false} caso contrario
     */
    public boolean ehAmigo(String loginAmigo) {
        garantirRelacionamentos();
        return amigos.contains(loginAmigo);
    }

    /**
     * Retorna os amigos confirmados em ordem de confirmacao.
     *
     * @return lista imutavel contendo os logins dos amigos
     */
    public List<String> getAmigos() {
        garantirRelacionamentos();
        return Collections.unmodifiableList(new ArrayList<String>(amigos));
    }

    /**
     * Adiciona uma comunidade a lista de comunidades do usuario.
     *
     * @param nomeComunidade nome da comunidade
     */
    public void adicionarComunidade(String nomeComunidade) {
        garantirComunidades();
        comunidades.add(nomeComunidade);
    }

    /**
     * Remove uma comunidade da lista de comunidades do usuario.
     *
     * @param nomeComunidade nome da comunidade removida
     */
    public void removerComunidade(String nomeComunidade) {
        garantirComunidades();
        comunidades.remove(nomeComunidade);
    }

    /**
     * Verifica se o usuario participa de uma comunidade.
     *
     * @param nomeComunidade nome da comunidade
     * @return {@code true} quando o usuario for membro, ou {@code false} caso contrario
     */
    public boolean participaDaComunidade(String nomeComunidade) {
        garantirComunidades();
        return comunidades.contains(nomeComunidade);
    }

    /**
     * Retorna as comunidades do usuario em ordem de entrada.
     *
     * @return lista imutavel com os nomes das comunidades
     */
    public List<String> getComunidades() {
        garantirComunidades();
        return Collections.unmodifiableList(new ArrayList<String>(comunidades));
    }

    /**
     * Adiciona um idolo ao usuario.
     *
     * @param loginIdolo login do idolo
     */
    public void adicionarIdolo(String loginIdolo) {
        garantirRelacionamentos();
        idolos.add(loginIdolo);
    }

    /**
     * Verifica se o usuario e fa de outro usuario.
     *
     * @param loginIdolo login do idolo
     * @return {@code true} quando o idolo estiver registrado, ou {@code false} caso contrario
     */
    public boolean ehFaDe(String loginIdolo) {
        garantirRelacionamentos();
        return idolos.contains(loginIdolo);
    }

    /**
     * Adiciona uma paquera ao usuario.
     *
     * @param loginPaquera login da paquera
     */
    public void adicionarPaquera(String loginPaquera) {
        garantirRelacionamentos();
        paqueras.add(loginPaquera);
    }

    /**
     * Verifica se o usuario marcou outro como paquera.
     *
     * @param loginPaquera login da paquera
     * @return {@code true} quando a paquera estiver registrada, ou {@code false} caso contrario
     */
    public boolean ehPaqueraDe(String loginPaquera) {
        garantirRelacionamentos();
        return paqueras.contains(loginPaquera);
    }

    /**
     * Retorna as paqueras do usuario em ordem de adicao.
     *
     * @return lista imutavel com os logins das paqueras
     */
    public List<String> getPaqueras() {
        garantirRelacionamentos();
        return Collections.unmodifiableList(new ArrayList<String>(paqueras));
    }

    /**
     * Adiciona um inimigo ao usuario.
     *
     * @param loginInimigo login do inimigo
     */
    public void adicionarInimigo(String loginInimigo) {
        garantirRelacionamentos();
        inimigos.add(loginInimigo);
    }

    /**
     * Verifica se o usuario marcou outro como inimigo.
     *
     * @param loginInimigo login do inimigo
     * @return {@code true} quando o inimigo estiver registrado, ou {@code false} caso contrario
     */
    public boolean possuiInimigo(String loginInimigo) {
        garantirRelacionamentos();
        return inimigos.contains(loginInimigo);
    }

    /**
     * Adiciona um recado a fila de recados recebidos.
     *
     * @param recado recado recebido pelo usuario
     */
    public void receberRecado(Recado recado) {
        garantirRecados();
        recadosRecebidos.add(recado);
    }

    /**
     * Verifica se existe recado pendente para leitura.
     *
     * @return {@code true} quando houver recado pendente, ou {@code false} caso contrario
     */
    public boolean possuiRecado() {
        garantirRecados();
        return !recadosRecebidos.isEmpty();
    }

    /**
     * Remove e retorna o primeiro recado da fila.
     *
     * @return primeiro recado recebido, ou {@code null} quando a fila estiver vazia
     */
    public Recado lerProximoRecado() {
        garantirRecados();
        return recadosRecebidos.poll();
    }

    /**
     * Adiciona uma mensagem de comunidade a fila do usuario.
     *
     * @param mensagem mensagem recebida
     */
    public void receberMensagem(Mensagem mensagem) {
        garantirMensagens();
        mensagensRecebidas.add(mensagem);
    }

    /**
     * Verifica se existe mensagem pendente para leitura.
     *
     * @return {@code true} quando houver mensagem pendente, ou {@code false} caso contrario
     */
    public boolean possuiMensagem() {
        garantirMensagens();
        return !mensagensRecebidas.isEmpty();
    }

    /**
     * Remove e retorna a primeira mensagem da fila.
     *
     * @return primeira mensagem recebida, ou {@code null} quando a fila estiver vazia
     */
    public Mensagem lerProximaMensagem() {
        garantirMensagens();
        return mensagensRecebidas.poll();
    }

    /**
     * Remove todas as referencias diretas a outro usuario.
     *
     * @param loginRemovido login removido do sistema
     */
    public void removerReferenciasAoUsuario(String loginRemovido) {
        garantirRelacionamentos();
        amigos.remove(loginRemovido);
        convitesEnviados.remove(loginRemovido);
        idolos.remove(loginRemovido);
        paqueras.remove(loginRemovido);
        inimigos.remove(loginRemovido);
    }

    /**
     * Remove recados enviados por um usuario.
     *
     * @param loginRemovido login do remetente removido
     */
    public void removerRecadosDe(String loginRemovido) {
        garantirRecados();
        Queue<Recado> preservados = new LinkedList<Recado>();
        while (!recadosRecebidos.isEmpty()) {
            Recado recado = recadosRecebidos.poll();
            if (!loginRemovido.equals(recado.getRemetente())) {
                preservados.add(recado);
            }
        }
        recadosRecebidos = preservados;
    }

    /**
     * Remove mensagens enviadas por um usuario.
     *
     * @param loginRemovido login do remetente removido
     */
    public void removerMensagensDe(String loginRemovido) {
        garantirMensagens();
        Queue<Mensagem> preservadas = new LinkedList<Mensagem>();
        while (!mensagensRecebidas.isEmpty()) {
            Mensagem mensagem = mensagensRecebidas.poll();
            if (!loginRemovido.equals(mensagem.getRemetente())) {
                preservadas.add(mensagem);
            }
        }
        mensagensRecebidas = preservadas;
    }

    /**
     * Remove comunidades inexistentes da lista do usuario.
     *
     * @param comunidadesAtivas nomes das comunidades ainda cadastradas
     */
    public void manterApenasComunidades(Set<String> comunidadesAtivas) {
        garantirComunidades();
        Iterator<String> iterator = comunidades.iterator();
        while (iterator.hasNext()) {
            String comunidade = iterator.next();
            if (!comunidadesAtivas.contains(comunidade)) {
                iterator.remove();
            }
        }
    }

    /**
     * Garante que as colecoes de relacionamento existam apos desserializacao.
     */
    private void garantirRelacionamentos() {
        if (amigos == null) {
            amigos = new LinkedHashSet<String>();
        }
        if (convitesEnviados == null) {
            convitesEnviados = new LinkedHashSet<String>();
        }
        if (idolos == null) {
            idolos = new LinkedHashSet<String>();
        }
        if (paqueras == null) {
            paqueras = new LinkedHashSet<String>();
        }
        if (inimigos == null) {
            inimigos = new LinkedHashSet<String>();
        }
    }

    /**
     * Garante que a colecao de comunidades exista apos desserializacao.
     */
    private void garantirComunidades() {
        if (comunidades == null) {
            comunidades = new LinkedHashSet<String>();
        }
    }

    /**
     * Garante que a fila de recados exista apos desserializacao.
     */
    private void garantirRecados() {
        if (recadosRecebidos == null) {
            recadosRecebidos = new LinkedList<Recado>();
        }
    }

    /**
     * Garante que a fila de mensagens exista apos desserializacao.
     */
    private void garantirMensagens() {
        if (mensagensRecebidas == null) {
            mensagensRecebidas = new LinkedList<Mensagem>();
        }
    }
}
