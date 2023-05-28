package com.pago.uap.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pago.uap.model.dao.IEstadoPagoDao;

@Service
public class EstadoPagoServiceImpl implements IEstadoPagoService{

    @Autowired
    private IEstadoPagoDao estadoPagoDao;

    @Override
    public List<com.pago.uap.model.entity.EstadoPago> listaEstadoPagosAll() {
        return estadoPagoDao.findAll();
    }

    @Override
    public void guardarEstadoPago(com.pago.uap.model.entity.EstadoPago estadoPago) {
        estadoPagoDao.save(estadoPago);
    }

    @Override
    public com.pago.uap.model.entity.EstadoPago sacarIdEstadoPago(Long id_estadoPago) {
        return estadoPagoDao.findById(id_estadoPago).orElse(null);
    }

    @Override
    public void eliminarIdEstadoPago(Long id_estadoPago) {
        estadoPagoDao.deleteById(id_estadoPago);
    }
    
}
