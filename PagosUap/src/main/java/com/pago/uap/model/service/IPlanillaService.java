package com.pago.uap.model.service;

import java.util.List;

import com.pago.uap.model.entity.Planilla;

public interface IPlanillaService {
    
    public List<Planilla> listaPlanillasAll();
    public Planilla guardarPlanilla(Planilla planilla);
    public void sacarIdPlanilla(Long id_planilla);
    public void eliminarIdPlanilla(Long id_planilla);

}
