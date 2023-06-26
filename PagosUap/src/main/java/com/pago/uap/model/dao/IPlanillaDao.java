package com.pago.uap.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pago.uap.model.entity.Planilla;

public interface IPlanillaDao extends JpaRepository <Planilla, Long>{
    
}
