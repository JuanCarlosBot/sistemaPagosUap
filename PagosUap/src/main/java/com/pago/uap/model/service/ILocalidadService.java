package com.pago.uap.model.service;

import java.util.List;

import com.pago.uap.model.entity.Localidad;

public interface ILocalidadService {
    
    public List<Localidad> listaLocalidadesAll();
    public void guardarLocalidad(Localidad localidad);
    public Localidad sacarIdLocalidad(Long id_localidad);
    public void eliminarIdLocalidad(Long id_localidad);
    public List<Localidad> localidadesPorIdMunicipio(Long id_municipio);
}
