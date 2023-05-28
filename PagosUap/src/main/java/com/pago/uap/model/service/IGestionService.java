package com.pago.uap.model.service;

import java.util.List;

import com.pago.uap.model.entity.Gestion;

public interface IGestionService {
    
    public List<Gestion> listaGestionesAll();
    public void guardarGestion(Gestion gestion);
    public Gestion sacarIdGestion(Long id_gestion);
    public Gestion sacarGestionPorNombre(String gestion);
}
