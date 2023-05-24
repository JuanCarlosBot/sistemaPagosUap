package com.pago.uap.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pago.uap.model.entity.Usuario;

public interface IUsuarioDao extends JpaRepository<Usuario, Long>{
    @Query("select u from Usuario u where u.usuario=?1 and u.password=?2")
    public Usuario loginUsuario(String usuario, String password);
}
