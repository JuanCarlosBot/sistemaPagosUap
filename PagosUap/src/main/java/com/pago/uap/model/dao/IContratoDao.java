package com.pago.uap.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pago.uap.model.entity.Contrato;

public interface IContratoDao extends JpaRepository<Contrato, Long>{
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM contrato " +
            "INNER JOIN persona ON contrato.id_persona = persona.id_persona " +
            "INNER JOIN cargo ON cargo.id_cargo = persona.id_cargo " +
            "INNER JOIN gestion ON gestion.id_gestion = cargo.id_gestion " +
            "WHERE gestion.id_gestion = ?1")
    public int NumerarContrato(Long id_gestion);
}
