package com.pago.uap.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pago.uap.model.entity.v_todo;

public interface V_todo extends JpaRepository<v_todo, Long>{
    
    @Query(value = "select * from v_todo", nativeQuery = true)
    public List<v_todo> listaPersonasAllVtodo();
}
