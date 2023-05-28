package com.pago.uap.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pago.uap.model.dao.IMunicipioDao;
import com.pago.uap.model.entity.Municipio;

@Service
public class MunicipioServiceImpl implements IMunicipioService{

    @Autowired
    private IMunicipioDao municipioDao;

    @Override
    public List<Municipio> listaMunicipiosAll() {
        return municipioDao.findAll();
    }

    @Override
    public void guardarMunicipio(Municipio municipio) {
        municipioDao.save(municipio);
    }

    @Override
    public Municipio sacarIdMunicipio(Long id_municipio) {
        return municipioDao.findById(id_municipio).orElse(null);
    }

    @Override
    public void eliminarIdMunicipio(Long id_municipio) {
        municipioDao.deleteById(id_municipio);
    }
    
}
