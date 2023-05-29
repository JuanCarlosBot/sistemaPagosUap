package com.pago.uap.model.service;

import com.pago.uap.model.entity.Contrato;

public interface IContratoService {

    public void guardarContrato(Contrato contrato);
    public Contrato sacarIdContrato(Long id_contrato);
    public void eliminarIdContrato(Long id_contrato);
}
