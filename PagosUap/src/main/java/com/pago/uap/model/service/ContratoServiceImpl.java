package com.pago.uap.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pago.uap.model.dao.IContratoDao;
import com.pago.uap.model.entity.Cargo;
import com.pago.uap.model.entity.Contrato;

@Service
public class ContratoServiceImpl implements IContratoService{

    @Autowired
    private IContratoDao contratoDao;

    @Override
    public void guardarContrato(Contrato contrato) {
        contratoDao.save(contrato);
    }

    @Override
    public Contrato sacarIdContrato(Long id_contrato) {
        return contratoDao.findById(id_contrato).orElse(null);
    }

    @Override
    public void eliminarIdContrato(Long id_contrato) {
        contratoDao.deleteById(id_contrato);
    }

    
}
