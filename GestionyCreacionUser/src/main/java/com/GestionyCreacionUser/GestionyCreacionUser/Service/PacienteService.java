package com.GestionyCreacionUser.GestionyCreacionUser.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GestionyCreacionUser.GestionyCreacionUser.Model.Paciente;
import com.GestionyCreacionUser.GestionyCreacionUser.Repository.PacienteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PacienteService {
    
    @Autowired
    private PacienteRepository pacienteRepository;

     public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    public String guardarPaciente(Paciente paciente) {
        pacienteRepository.save(paciente);
        return "Paciente " + paciente.getRutPaciente() + " guardado con éxito";
    }

    public String guardarPacientes(List<Paciente> pacientes) {
        pacienteRepository.saveAll(pacientes);
        return pacientes.size() + " Pacientes guardados con exito";
    }

    public String actualizarPaciente(Paciente paciente) {
        pacienteRepository.save(paciente); 
        return "Paciente "+paciente.getRutPaciente()+" actualizado con éxito";
    }

    public String eliminarPaciente(String rut) {
        pacienteRepository.deleteById(rut);
        return "Paciente "+rut+" eliminado con éxito";
    }

   public Paciente buscarPorRut(String rut) {
    return pacienteRepository.findById(rut).orElse(null);
    }


    public List<Paciente> buscarPorNombre(String nombre) {
        return pacienteRepository.buscarPorNombre(nombre);
    }

    public List<Paciente> buscarPorNombreParcial(String nombre) {
        return pacienteRepository.buscarPorNombreParcial(nombre);
    }

}
