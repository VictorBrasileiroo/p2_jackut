package br.ufal.ic.p2.jackut.repositories;

import br.ufal.ic.p2.jackut.models.EstadoJackut;
import br.ufal.ic.p2.jackut.models.Usuario;

public class UsuarioRepository {

    private final EstadoJackut estado;

    public UsuarioRepository(EstadoJackut estado) {
        this.estado = estado;
    }

    public boolean existe(String login) {
        return estado.contemUsuario(login);
    }

    public void adicionar(Usuario usuario) {
        estado.adicionarUsuario(usuario);
    }

    public Usuario buscarPorLogin(String login) {
        return estado.buscarUsuario(login);
    }
}
