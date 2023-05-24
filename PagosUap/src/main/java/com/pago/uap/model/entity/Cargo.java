package com.pago.uap.model.entity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name="cargo")
@Getter
@Setter
public class Cargo implements Serializable{
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cargo;
    private String nombre_cargo;
    private String descripcion;
    @Column
    @Temporal(TemporalType.DATE)
    private Date fecha_inicio;
    @Column
    @Temporal(TemporalType.DATE)
    private Date fecha_fin;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cargo", fetch = FetchType.LAZY)
	private List<Persona> personas;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_gestion")
    private Gestion gestion;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_pago")
    private EstadoPago estadoPago;
}
