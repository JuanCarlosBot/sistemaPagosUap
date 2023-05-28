package com.pago.uap.model.service;

import java.util.List;

import com.pago.uap.model.entity.TipoLocalidad;

public interface ITipoLocalidadService {
    
    public List<TipoLocalidad> listaTipoLocalidadesAll();
    public void guardarTipoLocalidad(TipoLocalidad tipoLocalidad);
    public TipoLocalidad sacarIdTipoLocalidad(Long id_tipoLocalidad);
    public void eliminarIdTipoLocalidad(Long id_tipoLocalidad);
}
