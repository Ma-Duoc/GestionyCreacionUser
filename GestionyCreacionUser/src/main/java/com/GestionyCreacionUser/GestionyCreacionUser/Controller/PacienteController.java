package com.GestionyCreacionUser.GestionyCreacionUser.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.GestionyCreacionUser.GestionyCreacionUser.Model.Rol;
import com.GestionyCreacionUser.GestionyCreacionUser.Service.PacienteService;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    @Autowired
    private PacienteService pacienteService;


    @GetMapping("/listar")
    public ResponseEntity<List<Paciente>> listarPacientes() {
        List<Paciente> pacientes = pacienteService.listarPacientes();

        if (pacientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pacientes);
    }


    @PostMapping("/guardar")
    public ResponseEntity<String> guardarPaciente(@RequestBody Paciente paciente) {
        if (paciente.getRut().isEmpty() ||
            paciente.getNombre().isEmpty() ||
            paciente.getCorreo().isEmpty() ||
            paciente.getFono().isEmpty() ||
            paciente.getHistorialMedico().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Todos los campos son obligatorios.");
        }

        Paciente pacienteExistente = pacienteService.buscarPorRut(paciente.getRut());
        if (pacienteExistente != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El paciente con rut " + paciente.getRut() + " ya existe.");
        }


        Rol rolPorDefecto = new Rol();
        rolPorDefecto.setRol_id(1);
        paciente.setRol_id(rolPorDefecto);

        String mensaje = pacienteService.guardarPaciente(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }


    @PostMapping("/guardar-multiple")
    public ResponseEntity<String> guardarPacientes(@RequestBody List<Paciente> pacientes) {
        if (pacientes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La lista de pacientes está vacía.");
        }

        for (Paciente paciente : pacientes) {
            if (paciente.getRut().isEmpty() ||
                paciente.getNombre().isEmpty() ||
                paciente.getCorreo().isEmpty() ||
                paciente.getFono().isEmpty() ||
                paciente.getHistorialMedico().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos incompletos de algún paciente.");
            }

            Paciente pacienteExistente = pacienteService.buscarPorRut(paciente.getRut());
            if (pacienteExistente != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ya existe un paciente con el RUT " + paciente.getRut());
            }

    
            Rol rolPorDefecto = new Rol();
            rolPorDefecto.setRol_id(1);
            paciente.setRol_id(rolPorDefecto);
        }

        String mensaje = pacienteService.guardarPacientes(pacientes);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }


    @PutMapping("/actualizar")
    public ResponseEntity<String> actualizarPaciente(@RequestBody Paciente paciente) {
        Paciente pacienteExistente = pacienteService.buscarPorRut(paciente.getRut());
        if (pacienteExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente con RUT " + paciente.getRut() + " no encontrado.");
        }

        if (paciente.getNombre().isEmpty() ||
            paciente.getCorreo().isEmpty() ||
            paciente.getFono().isEmpty() ||
            paciente.getHistorialMedico().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Correo, fono, nombre e historial médico son obligatorios.");
        }


        pacienteExistente.setNombre(paciente.getNombre());
        pacienteExistente.setCorreo(paciente.getCorreo());
        pacienteExistente.setFono(paciente.getFono());
        pacienteExistente.setHistorialMedico(paciente.getHistorialMedico());


        String mensaje = pacienteService.actualizarPaciente(pacienteExistente);
        return ResponseEntity.ok(mensaje);
    }


    @DeleteMapping("/eliminar/{rut}")
    public ResponseEntity<String> eliminarPaciente(@PathVariable String rut) {
        Paciente pacienteExistente = pacienteService.buscarPorRut(rut);
        if (pacienteExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente con RUT " + rut + " no encontrado.");
        }

        String mensaje = pacienteService.eliminarPaciente(rut);
        return ResponseEntity.ok(mensaje);  // 200 OK con el mensaje de éxito
    }

    @GetMapping("/buscar/rut/{rut}")
    public ResponseEntity<Paciente> buscarPorRut(@PathVariable String rut) {
        Paciente paciente = pacienteService.buscarPorRut(rut);
        if (paciente == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 Not Found si no se encuentra el paciente
        }
        
        return ResponseEntity.ok(paciente);  // 200 OK con los detalles del paciente
    }

    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<Paciente>> buscarPorNombre(@PathVariable String nombre) {
        List<Paciente> pacientes = pacienteService.buscarPorNombre(nombre);
        return ResponseEntity.ok(pacientes);  // 200 OK con la lista de pacientes
    }

    @GetMapping("/buscar/nombre-parcial/{nombre}")
    public ResponseEntity<List<Paciente>> buscarPorNombreParcial(@PathVariable String nombre) {
        List<Paciente> pacientes = pacienteService.buscarPorNombreParcial(nombre);
        return ResponseEntity.ok(pacientes);  // 200 OK con la lista de pacientes que coinciden parcialmente
    }


}
