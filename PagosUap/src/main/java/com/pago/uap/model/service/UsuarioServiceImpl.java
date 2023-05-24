package com.pago.uap.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pago.uap.model.dao.IUsuarioDao;
import com.pago.uap.model.entity.Usuario;

@Service
public class UsuarioServiceImpl implements IUsuarioService{

    @Autowired
    private IUsuarioDao usuarioDao;

    @Override
    public List<Usuario> listaPersonasAll() {
        return usuarioDao.findAll();
    }

    @Override
    public void guardarUsuario(Usuario usuario) {
        usuarioDao.save(usuario);
    }

    @Override
    public Usuario sacarIdUsuario(Long id_usuario) {
        return usuarioDao.findById(id_usuario).orElse(null);
    }

    @Override
    public void eliminarIdUsuario(Long id_usuario) {
        usuarioDao.deleteById(id_usuario);
    }

    @Override
    public Usuario loginUsuario(String usuario, String password) {
        return usuarioDao.loginUsuario(usuario, password);
    }
    
}
