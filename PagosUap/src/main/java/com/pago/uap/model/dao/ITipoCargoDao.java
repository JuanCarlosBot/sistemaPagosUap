package com.pago.uap.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pago.uap.model.entity.TipoCargo;

public interface ITipoCargoDao extends JpaRepository<TipoCargo, Long>{
    
}
