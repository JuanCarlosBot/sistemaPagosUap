package com.pago.uap.model.service;

import java.util.List;

import com.pago.uap.model.entity.Usuario;

public interface IUsuarioService {
    
    public List<Usuario> listaPersonasAll();
    public void guardarUsuario(Usuario usuario);
    public Usuario sacarIdUsuario(Long id_usuario);
    public void eliminarIdUsuario(Long id_usuario);
    public Usuario loginUsuario(String usuario, String password);
}
