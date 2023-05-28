package com.pago.uap.model.service;

import java.util.List;

import com.pago.uap.model.entity.EstadoPago;

public interface IEstadoPagoService {
    
    public List<EstadoPago> listaEstadoPagosAll();
    public void guardarEstadoPago(EstadoPago estadoPago);
    public EstadoPago sacarIdEstadoPago(Long id_estadoPago);
    public void eliminarIdEstadoPago(Long id_estadoPago);
}
