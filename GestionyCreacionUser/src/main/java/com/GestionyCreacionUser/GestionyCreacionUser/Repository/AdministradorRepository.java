package com.GestionyCreacionUser.GestionyCreacionUser.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.GestionyCreacionUser.GestionyCreacionUser.Model.Administrador;


@Repository
public interface AdministradorRepository extends JpaRepository <Administrador, String> {

    @Query("SELECT a FROM Administrador a WHERE a.nombre = :nombre")
        List<Administrador> buscarPorNombre(@Param("nombre") String nombre);

   
    @Query("SELECT a FROM Administrador a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
        List<Administrador> buscarPorNombreParcial(@Param("nombre") String nombre);
}
