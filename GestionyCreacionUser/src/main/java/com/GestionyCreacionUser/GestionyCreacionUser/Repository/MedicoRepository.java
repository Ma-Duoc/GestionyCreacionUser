package com.GestionyCreacionUser.GestionyCreacionUser.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.GestionyCreacionUser.GestionyCreacionUser.Model.Medico;


public interface MedicoRepository extends JpaRepository<Medico, String>{

    @Query("SELECT m FROM Medico m WHERE m.nombre = :nombre")
        List<Medico> buscarPorNombre(@Param("nombre") String nombre);

   
    @Query("SELECT m FROM Medico m WHERE LOWER(m.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
        List<Medico> buscarPorNombreParcial(@Param("nombre") String nombre);

}
