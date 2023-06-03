package com.pago.uap.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pago.uap.model.entity.Persona;

public interface IPersonaDao extends JpaRepository<Persona, Long>{
    
    @Query(value="select * "+
    "from persona as p left join cargo as c "+
    "on p.id_cargo=c.id_cargo "+
    "left join gestion as g "+
    "on c.id_gestion=g.id_gestion "+
    "where g.id_gestion=?1", nativeQuery = true)
    public List<Persona> listarPersonasPorGestion(Long id_gestion);

    @Query(value = "select * from v_todo", nativeQuery = true)
    public List<Persona> listaPersonasAllView();
}
