package com.pago.uap.model.service;

import java.util.List;

import com.pago.uap.model.entity.TipoCargo;

public interface ITipoCargoService {
    
    public List<TipoCargo> listaTipoCargosAll();
    public void guardarTipoCargo(TipoCargo tipoCargo);
    public TipoCargo sacarIdTipoCargo(Long id_tipoCargo);
    public void eliminarIdTipoCargo(Long id_tipoCargo);
}
