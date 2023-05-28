package com.pago.uap.model.entity;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name="persona")
@Getter
@Setter
public class Persona implements Serializable{
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_persona;
    
    @NotNull(message = "El campo no puede estar vacio")
    @NotBlank(message = "Favor introducir CI")
    @Size(min = 1, max = 12, message = "El ci debe medir entre 1 y 10 digitos")
    @Column(unique = true)
    @Pattern(regexp = "^[0-9]+(?:-[0-9][A-Z])?", message = "El ci solo debe tener numeros mas complemento en MAYUSCULA si se requiere")
    private String ci_persona;
    @NotNull(message = "El campo no puede estar vacio")
    @NotBlank(message = "Favor introducir Nombre")
    @Size(min = 1, max = 50, message = "El nombre debe medir entre 1 y 50 digitos")
    @Column
    @Pattern(regexp = "^[A-Z]+(\\s?[A-Z]+)?+(\\s?[A-Z]+)?", message = "El Nombre tiene que estar en MAYUSCULA(S)")
    private String nombre_persona;
    @NotNull(message = "El campo no puede estar vacio")
    @NotBlank(message = "Favor introducir Ap paterno")
    @Size(min = 1, max = 50, message = "El Ap paterno debe medir entre 1 y 50 digitos")
    @Column
    @Pattern(regexp = "^[A-Z]+(\\s?[A-Z]+)?+(\\s?[A-Z]+)?", message = "El Ap. paterno tiene que estar en MAYUSCULA(S)")
    private String ap_paterno_persona;
    @NotNull(message = "El campo no puede estar vacio")
    @NotBlank(message = "Favor introducir Ap materno")
    @Size(min = 1, max = 50, message = "El Ap materno debe medir entre 1 y 50 digitos")
    @Column
    @Pattern(regexp = "^[A-Z]+(\\s?[A-Z]+)?+(\\s?[A-Z]+)?", message = "El Ap. materno tiene que estar en MAYUSCULA(S)")
    private String ap_materno_persona;
    @Column
    private String nombre_completo_persona;
    @Column
    private String estado_persona;
    @Column
    @Temporal(TemporalType.DATE)
    private Date fecha_registro_persona;
    @Column
    @Temporal(TemporalType.DATE)
    private Date fecha_mod_persona;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_localidad")
    private Localidad localidad;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cargo")
    private Cargo cargo;
}
