package com.pago.uap.model.service;

import java.util.List;

import com.pago.uap.model.entity.Persona;

public interface IPersonaService {
    
    public List<Persona> listaPersonasAllView();

    public List<Persona> listaPersonasAll();
    public void guardarPersona(Persona persona);
    public Persona sacarIdPersona(Long id_persona);
    public void eliminarIdPersona(Long id_persona);
    public List<Persona> listarPersonasPorGestion(Long id_gestion);
}
