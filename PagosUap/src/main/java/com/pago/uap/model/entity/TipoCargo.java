package com.pago.uap.model.entity;
import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name="tipo_cargo")
@Getter
@Setter
public class TipoCargo implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_tipo_cargo;
    private String nombre_tipo_cargo;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoCargo", fetch = FetchType.LAZY)
	private List<Cargo> cargos;
}
