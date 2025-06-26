package com.GestionyCreacionUser.GestionyCreacionUser.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GestionyCreacionUser.GestionyCreacionUser.Model.Medico;
import com.GestionyCreacionUser.GestionyCreacionUser.Repository.MedicoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

    public String guardarMedico(Medico medico) {
        medicoRepository.save(medico);
        return "Médico " + medico.getRutMedico() + " guardado con éxito";
    }

    public String guardarMedicos(List<Medico> medicos) {
        medicoRepository.saveAll(medicos);
        return medicos.size() + " médicos guardados con éxito";
    }

    public String actualizarMedico(Medico medico) {
        medicoRepository.save(medico);
        return "Médico " + medico.getRutMedico() + " actualizado con éxito";
    }

    public String eliminarMedico(String rut) {
        medicoRepository.deleteById(rut);
        return "Médico " + rut + " eliminado con éxito";
    }

    public Medico buscarPorRut(String rut) {
        return medicoRepository.findById(rut).orElse(null);
    }
    

    public List<Medico> buscarPorNombre(String nombre) {
        return medicoRepository.buscarPorNombre(nombre);
    }

    public List<Medico> buscarPorNombreParcial(String nombre) {
        return medicoRepository.buscarPorNombreParcial(nombre);
    }
}

