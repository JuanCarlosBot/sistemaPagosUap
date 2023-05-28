package com.pago.uap.model.service;

import java.util.List;

import com.pago.uap.model.entity.Municipio;

public interface IMunicipioService {

    public List<Municipio> listaMunicipiosAll();
    public void guardarMunicipio(Municipio municipio);
    public Municipio sacarIdMunicipio(Long id_municipio);
    public void eliminarIdMunicipio(Long id_municipio);
}
