package com.pago.uap.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pago.uap.model.dao.IGestionDao;
import com.pago.uap.model.entity.Gestion;
@Service
public class GestionServiceImpl implements IGestionService{

    @Autowired
    private IGestionDao gestionDao;

    @Override
    public List<Gestion> listaGestionesAll() {
        return gestionDao.findAll();
    }

    @Override
    public void guardarGestion(Gestion gestion) {
        gestionDao.save(gestion);
    }

    @Override
    public Gestion sacarIdGestion(Long id_gestion) {
        return gestionDao.findById(id_gestion).orElse(null);
    }

    @Override
    public Gestion sacarGestionPorNombre(String gestion) {
        return gestionDao.sacarGestionPorNombre(gestion);
    }
    
}
