package com.pago.uap.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pago.uap.model.dao.IPlanillaDao;
import com.pago.uap.model.entity.Planilla;
@Service
public class PlanillaServiceImpl implements IPlanillaService{

    @Autowired
    private IPlanillaDao planillaDao;

    @Override
    public List<Planilla> listaPlanillasAll() {
        return (List<Planilla>) planillaDao.findAll();
    }

    @Override
    public Planilla guardarPlanilla(Planilla planilla) {
        return planillaDao.save(planilla);
    }

    @Override
    public void sacarIdPlanilla(Long id_planilla) {
        planillaDao.findById(id_planilla).orElse(null);
    }

    @Override
    public void eliminarIdPlanilla(Long id_planilla) {
        planillaDao.deleteById(id_planilla);
    }
    
}
