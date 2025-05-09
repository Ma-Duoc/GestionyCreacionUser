package com.GestionyCreacionUser.GestionyCreacionUser.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GestionyCreacionUser.GestionyCreacionUser.Model.Paciente;
import com.GestionyCreacionUser.GestionyCreacionUser.Service.PacienteService;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    @Autowired
    private PacienteService pacienteService;

    
    @GetMapping("/listar")
    public ResponseEntity<List<Paciente>> listarPacientes() {
        List<Paciente> pacientes = pacienteService.listarPacientes();
        return ResponseEntity.ok(pacientes);  
    }

    @PostMapping("/guardar")
    public ResponseEntity<String> guardarPaciente(@RequestBody Paciente paciente) {
        String mensaje = pacienteService.guardarPaciente(paciente);
        return ResponseEntity.ok(mensaje);  
    }

    @PostMapping("/guardar-multiple")
    public ResponseEntity<String> guardarPacientes(@RequestBody List<Paciente> pacientes) {
        String mensaje = pacienteService.guardarPacientes(pacientes);
        return ResponseEntity.ok(mensaje);  
    }

    @PutMapping("/actualizar")
    public ResponseEntity<String> actualizarPaciente(@RequestBody Paciente paciente) {
        String mensaje = pacienteService.ActualizarPaciente(paciente);
        return ResponseEntity.ok(mensaje);  
    }

    @DeleteMapping("/eliminar/{rut}")
    public ResponseEntity<String> eliminarPaciente(@PathVariable String rut) {
        String mensaje = pacienteService.eliminarPaciente(rut);
        return ResponseEntity.ok(mensaje);  
    }

    @GetMapping("/buscar/rut/{rut}")
    public ResponseEntity<Paciente> buscarPorRut(@PathVariable String rut) {
        Paciente paciente = pacienteService.buscarPorRut(rut);
        return ResponseEntity.ok(paciente);  
    }

    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<Paciente>> buscarPorNombre(@PathVariable String nombre) {
        List<Paciente> pacientes = pacienteService.buscarPorNombre(nombre);
        return ResponseEntity.ok(pacientes);  
    }

    @GetMapping("/buscar/nombre-parcial/{nombre}")
    public ResponseEntity<List<Paciente>> buscarPorNombreParcial(@PathVariable String nombre) {
        List<Paciente> pacientes = pacienteService.buscarPorNombreParcial(nombre);
        return ResponseEntity.ok(pacientes);  
    }

}
