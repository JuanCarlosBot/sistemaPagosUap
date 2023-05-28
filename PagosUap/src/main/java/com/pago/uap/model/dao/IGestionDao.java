package com.pago.uap.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pago.uap.model.entity.Gestion;

public interface IGestionDao extends JpaRepository<Gestion, Long>{

    @Query("select g from Gestion g where g.nombre_gestion=?1")
    public Gestion sacarGestionPorNombre(String gestion);
}
