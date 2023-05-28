package com.pago.uap.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pago.uap.model.dao.ICargoDao;
import com.pago.uap.model.entity.Cargo;

@Service
public class CargoServiceImpl implements ICargoService{

    @Autowired
    private ICargoDao cargoDao;

    @Override
    public List<Cargo> listaCargosAll() {
        return cargoDao.findAll();
    }

    @Override
    public void guardarCargo(Cargo cargo) {
        cargoDao.save(cargo);
    }

    @Override
    public Cargo sacarIdCargo(Long id_cargo) {
        return cargoDao.findById(id_cargo).orElse(null);
    }

    @Override
    public void eliminarIdCargo(Long id_cargo) {
        cargoDao.deleteById(id_cargo);
    }
    
}
