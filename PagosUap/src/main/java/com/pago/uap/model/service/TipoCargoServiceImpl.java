package com.pago.uap.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pago.uap.model.dao.ITipoCargoDao;
import com.pago.uap.model.entity.TipoCargo;
@Service
public class TipoCargoServiceImpl implements ITipoCargoService{

    @Autowired
    private ITipoCargoDao tipoCargoDao;
    @Override
    public List<TipoCargo> listaTipoCargosAll() {
        return tipoCargoDao.findAll();
    }

    @Override
    public void guardarTipoCargo(TipoCargo tipoCargo) {
        tipoCargoDao.save(tipoCargo);
    }

    @Override
    public TipoCargo sacarIdTipoCargo(Long id_tipoCargo) {
        return tipoCargoDao.findById(id_tipoCargo).orElse(null);
    }

    @Override
    public void eliminarIdTipoCargo(Long id_tipoCargo) {
        tipoCargoDao.deleteById(id_tipoCargo);
    }
    
}
