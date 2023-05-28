package com.pago.uap.model.service;

import java.util.List;

import com.pago.uap.model.entity.Cargo;

public interface ICargoService {
    
    public List<Cargo> listaCargosAll();
    public void guardarCargo(Cargo cargo);
    public Cargo sacarIdCargo(Long id_cargo);
    public void eliminarIdCargo(Long id_cargo);
}
