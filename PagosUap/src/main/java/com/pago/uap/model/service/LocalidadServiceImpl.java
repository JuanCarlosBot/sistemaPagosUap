package com.pago.uap.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pago.uap.model.dao.ILocalidadDao;
import com.pago.uap.model.entity.Localidad;
@Service
public class LocalidadServiceImpl implements ILocalidadService{

    @Autowired
    private ILocalidadDao localidadDao;

    @Override
    public List<Localidad> listaLocalidadesAll() {
        return localidadDao.findAll();
    }

    @Override
    public void guardarLocalidad(Localidad localidad) {
        localidadDao.save(localidad);
    }

    @Override
    public Localidad sacarIdLocalidad(Long id_localidad) {
        return localidadDao.findById(id_localidad).orElse(null);
    }

    @Override
    public void eliminarIdLocalidad(Long id_localidad) {
        localidadDao.deleteById(id_localidad);
    }

    @Override
    public List<Localidad> localidadesPorIdMunicipio(Long id_municipio) {
        return localidadDao.localidadesPorIdMunicipio(id_municipio);
    }
    
}
