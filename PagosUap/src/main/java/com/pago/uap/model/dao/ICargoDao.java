package com.pago.uap.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pago.uap.model.entity.Cargo;

public interface ICargoDao extends JpaRepository<Cargo, Long>{
    
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM cargo"+
            " INNER JOIN persona ON cargo.id_cargo = persona.id_cargo"+
            " INNER JOIN gestion ON gestion.id_gestion = cargo.id_gestion"+
            " WHERE gestion.id_gestion =?1")
    public int numerarPorGestion(Long id_gestion);
}
