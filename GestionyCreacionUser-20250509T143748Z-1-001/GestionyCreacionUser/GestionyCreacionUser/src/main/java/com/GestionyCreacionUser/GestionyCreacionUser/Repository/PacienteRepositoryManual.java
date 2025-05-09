package com.GestionyCreacionUser.GestionyCreacionUser.Repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.GestionyCreacionUser.GestionyCreacionUser.Model.Paciente;


@Repository
public class PacienteRepositoryManual {
    
    //Arreglo que guardara los pacientes
    private List<Paciente> pacientes = new ArrayList<>();

    //Metodo que retorna los pacientes
    public List<Paciente> listarPacientes() {
        return pacientes;
    }

    //Metodo que guardar (registra) un paciente
    public String guardarPaciente(Paciente paciente) {
        if (buscarPorRut(paciente.getRut()) != null) {
            return "El paciente con el RUT " + paciente.getRut() + " ya existe.";
        }
        pacientes.add(paciente);
        return "Paciente con el RUT " + paciente.getRut() + " ha sido registrado correctamente";
    }

    //Metodo que guarda (registra) varios pacientes
    public String guardarPacientes(List<Paciente> pacientesLista){
        for (Paciente paciente : pacientesLista) {
            if (buscarPorRut(paciente.getRut()) != null) {
                return "El paciente con el RUT " + paciente.getRut() + " ya existe.";
            }
        }
        pacientes.addAll(pacientesLista);
        return pacientesLista.size() + " pacientes han sido registrados correctamente.";
    }

    //Metodo para actualizar paciente
    public String actualizarPaciente(Paciente paciente){
        Paciente auxPaciente = buscarPorRut(paciente.getRut());
        if (auxPaciente == null) {
            return "El paciente con el RUT " + paciente.getRut() + " no existe.";
        }
        auxPaciente.setCorreo(paciente.getCorreo());
        auxPaciente.setFono(paciente.getFono());
        return "Paciente con el RUT " + paciente.getRut() + " ha sido actualizado correctamente.";

    }

     //Metodo para eliminar paciente
     public String eliminar(String rut) {
        Paciente paciente = buscarPorRut(rut);
        if (paciente == null) {
            return "El paciente con el RUT " + rut + " no existe.";
        }
        pacientes.remove(paciente);
            return "Paciente con el RUT " + rut + " ha sido eliminado correctamente.";
    }

    //Metodo para buscar paciente por rut
    public Paciente buscarPorRut(String rut) {
        for (Paciente auxPaciente : pacientes ){
            if (auxPaciente.getRut().equalsIgnoreCase(rut)){
                return auxPaciente;
            }
        }
        return null;
    }

     //Buscar paciente por nombre
     public String buscarPorNombre(String nombre) {
        for (Paciente auxPaciente : pacientes) {
            if (auxPaciente.getNombre().equalsIgnoreCase(nombre)) {
                return auxPaciente.toString(); 
            }
        }
        return "No se encontró un paciente con el nombre: " + nombre;
    }

    //Buscar paciente por nombre parcial
    public List<Paciente> buscarPorNombreParcial(String nombre){
        List<Paciente> nombresParciales = new ArrayList<>();
        for (Paciente auxPaciente : pacientes) {
            if (auxPaciente.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                nombresParciales.add(auxPaciente);
            }
        }
        return nombresParciales;
    }

}
