package com.GestionyCreacionUser.GestionyCreacionUser.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.GestionyCreacionUser.GestionyCreacionUser.Model.Medico;
import com.GestionyCreacionUser.GestionyCreacionUser.Model.Rol;
import com.GestionyCreacionUser.GestionyCreacionUser.Service.MedicoService;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping("/listar")
    public ResponseEntity<List<Medico>> listarMedicos() {
        List<Medico> medicos = medicoService.listarMedicos();
        if (medicos.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(medicos);
    }

    @PostMapping("/guardar")
    public ResponseEntity<String> guardarMedico(@RequestBody Medico medico) {
        if (medico.getRut().isEmpty() ||
            medico.getNombre().isEmpty() ||
            medico.getCorreo().isEmpty() ||
            medico.getFono().isEmpty() ||
            medico.getEspecialidad().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Todos los campos son obligatorios.");
        }

        Medico medicoExistente = medicoService.buscarPorRut(medico.getRut());
        if (medicoExistente != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El médico con RUT " + medico.getRut() + " ya existe.");
        }

        // Asignar rol_id = 2 directamente (ejemplo)
        Rol rolPorDefecto = new Rol();
        rolPorDefecto.setRol_id(2);
        medico.setRol_id(rolPorDefecto);

        String mensaje = medicoService.guardarMedico(medico);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }

    @PostMapping("/guardar-multiple")
    public ResponseEntity<String> guardarMedicos(@RequestBody List<Medico> medicos) {
        if (medicos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La lista de médicos está vacía.");
        }

        for (Medico medico : medicos) {
            if (medico.getRut().isEmpty() ||
                medico.getNombre().isEmpty() ||
                medico.getCorreo().isEmpty() ||
                medico.getFono().isEmpty() ||
                medico.getEspecialidad().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos incompletos de algún médico.");
            }

            Medico medicoExistente = medicoService.buscarPorRut(medico.getRut());
            if (medicoExistente != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Ya existe un médico con el RUT " + medico.getRut());
            }

            // Asignar rol_id = 2 a cada médico
            Rol rolPorDefecto = new Rol();
            rolPorDefecto.setRol_id(2);
            medico.setRol_id(rolPorDefecto);
        }

        String mensaje = medicoService.guardarMedicos(medicos);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<String> actualizarMedico(@RequestBody Medico medico) {
        Medico medicoExistente = medicoService.buscarPorRut(medico.getRut());
        if (medicoExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Médico con RUT " + medico.getRut() + " no encontrado.");
        }

        if (medico.getCorreo().isEmpty() ||
            medico.getFono().isEmpty() ||
            medico.getEspecialidad().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Correo, fono y especialidad son obligatorios.");
        }

        // Solo actualizar campos permitidos
        medicoExistente.setNombre(medico.getNombre());
        medicoExistente.setCorreo(medico.getCorreo());
        medicoExistente.setFono(medico.getFono());
        medicoExistente.setEspecialidad(medico.getEspecialidad());


        String mensaje = medicoService.actualizarMedico(medicoExistente);
        return ResponseEntity.ok(mensaje);
    }

    @DeleteMapping("/eliminar/{rut}")
    public ResponseEntity<String> eliminarMedico(@PathVariable String rut) {
        Medico medicoExistente = medicoService.buscarPorRut(rut);
        if (medicoExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Médico con RUT " + rut + " no encontrado.");
        }

        String mensaje = medicoService.eliminarMedico(rut);
        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/buscar/rut/{rut}")
    public ResponseEntity<Medico> buscarPorRut(@PathVariable String rut) {
        Medico medico = medicoService.buscarPorRut(rut);
        if (medico == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(medico);
    }

    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<Medico>> buscarPorNombre(@PathVariable String nombre) {
        List<Medico> medicos = medicoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(medicos);
    }

    @GetMapping("/buscar/nombre-parcial/{nombre}")
    public ResponseEntity<List<Medico>> buscarPorNombreParcial(@PathVariable String nombre) {
        List<Medico> medicos = medicoService.buscarPorNombreParcial(nombre);
        return ResponseEntity.ok(medicos);
    }
}


