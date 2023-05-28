package com.pago.uap.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pago.uap.model.dao.ITipoLocalidadDao;
import com.pago.uap.model.entity.TipoLocalidad;
@Service
public class TipoLocalidadServiceImpl implements ITipoLocalidadService{

    @Autowired
    private ITipoLocalidadDao tipoLocalidadDao;

    @Override
    public List<TipoLocalidad> listaTipoLocalidadesAll() {
        return tipoLocalidadDao.findAll();
    }

    @Override
    public void guardarTipoLocalidad(TipoLocalidad tipoLocalidad) {
        tipoLocalidadDao.save(tipoLocalidad);
    }

    @Override
    public TipoLocalidad sacarIdTipoLocalidad(Long id_tipoLocalidad) {
        return tipoLocalidadDao.findById(id_tipoLocalidad).orElse(null);
    }

    @Override
    public void eliminarIdTipoLocalidad(Long id_tipoLocalidad) {
        tipoLocalidadDao.deleteById(id_tipoLocalidad);
    }
    
}
