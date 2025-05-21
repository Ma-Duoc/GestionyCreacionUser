package com.GestionyCreacionUser.GestionyCreacionUser.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.GestionyCreacionUser.GestionyCreacionUser.Model.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, String>{

      @Query("SELECT p FROM Paciente p WHERE p.nombre = :nombre")
        List<Paciente> buscarPorNombre(@Param("nombre") String nombre);

   
    @Query("SELECT p FROM Paciente p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
        List<Paciente> buscarPorNombreParcial(@Param("nombre") String nombre);

} 


