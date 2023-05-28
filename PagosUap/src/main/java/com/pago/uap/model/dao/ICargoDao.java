package com.pago.uap.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pago.uap.model.entity.Cargo;

public interface ICargoDao extends JpaRepository<Cargo, Long>{
    
}
