package com.pago.uap.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "v_todo")
@Getter
@Setter
public class v_todo {
    //SELECT  , CONCAT( ca.fecha_inicio,' al ', ca.fecha_fin) AS tiempo, ep.nombre_estado_pago, ge.nombre_gestion
    @Id
    private Long id_persona;
    private String ci_persona;
    private String nombre_completo_persona;
    private String nombre_municipio;
    private String nombre_localidad;
    private String tipo_cargo;
    private String tiempo;
    private String nombre_estado_pago;
    private String nombre_gestion;

    
}
