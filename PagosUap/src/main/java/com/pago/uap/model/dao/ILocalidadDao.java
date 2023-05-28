package com.pago.uap.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pago.uap.model.entity.Localidad;

public interface ILocalidadDao extends JpaRepository<Localidad, Long>{

    @Query(value="select * from localidad as l where l.id_municipio=?1", nativeQuery = true)
    public List<Localidad> localidadesPorIdMunicipio(Long id_municipio);
    
}
