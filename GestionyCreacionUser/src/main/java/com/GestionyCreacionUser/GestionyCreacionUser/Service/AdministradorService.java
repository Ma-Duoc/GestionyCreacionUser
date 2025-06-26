package com.GestionyCreacionUser.GestionyCreacionUser.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GestionyCreacionUser.GestionyCreacionUser.Model.Administrador;
import com.GestionyCreacionUser.GestionyCreacionUser.Repository.AdministradorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    public List<Administrador> listarAdministradores() {
        return administradorRepository.findAll();
    }

    public String guardarAdministrador(Administrador administrador) {
        administradorRepository.save(administrador);
        return "Administrador " + administrador.getRutAdministrador() + " guardado con éxito";
    }

    public String guardarAdministradores(List<Administrador> administradores) {
        administradorRepository.saveAll(administradores);
        return administradores.size() + " administradores guardados con éxito";
    }

    public String actualizarAdministrador(Administrador administrador) {
        administradorRepository.save(administrador);
        return "Administrador " + administrador.getRutAdministrador() + " actualizado con éxito";
    }

    public String eliminarAdministrador(String rut) {
        administradorRepository.deleteById(rut);
        return "Administrador " + rut + " eliminado con éxito";
    }

    public Administrador buscarPorRut(String rut) {
        return administradorRepository.findById(rut).orElse(null);
    }

    public List<Administrador> buscarPorNombre(String nombre) {
        return administradorRepository.buscarPorNombre(nombre);
    }

    public List<Administrador> buscarPorNombreParcial(String nombre) {
        return administradorRepository.buscarPorNombreParcial(nombre);
    }
}

