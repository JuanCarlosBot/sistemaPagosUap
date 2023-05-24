package com.pago.uap.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pago.uap.model.dao.IPersonaDao;
import com.pago.uap.model.entity.Persona;

@Service
public class PersonaServiceImpl implements IPersonaService{

    @Autowired
    private IPersonaDao personaDao;

    @Override
    public List<Persona> listaPersonasAll() {
        return personaDao.findAll();
    }

    @Override
    public void guardarPersona(Persona persona) {
        personaDao.save(persona);
    }

    @Override
    public Persona sacarIdPersona(Long id_persona) {
        return personaDao.findById(id_persona).orElse(null);
    }

    @Override
    public void eliminarIdPersona(Long id_persona) {
        personaDao.deleteById(id_persona);
    }
    
}
