package com.GestionyCreacionUser.GestionyCreacionUser.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.GestionyCreacionUser.GestionyCreacionUser.Model.Administrador;
import com.GestionyCreacionUser.GestionyCreacionUser.Model.Rol;
import com.GestionyCreacionUser.GestionyCreacionUser.Service.AdministradorService;

@RestController
@RequestMapping("/administradores")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @GetMapping("/listar")
    public ResponseEntity<List<Administrador>> listarAdministradores() {
        List<Administrador> administradores = administradorService.listarAdministradores();
        if (administradores.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(administradores);
    }

    @PostMapping("/guardar")
    public ResponseEntity<String> guardarAdministrador(@RequestBody Administrador administrador) {
        if (administrador.getRut().isEmpty() ||
            administrador.getNombre().isEmpty() ||
            administrador.getCorreo().isEmpty() ||
            administrador.getFono().isEmpty() ||
            administrador.getArea().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Todos los campos son obligatorios.");
        }

        Administrador administradorExistente = administradorService.buscarPorRut(administrador.getRut());
        if (administradorExistente != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El administrador con RUT " + administrador.getRut() + " ya existe.");
        }

        // Asignar rol_id = 1 por defecto
        Rol rolPorDefecto = new Rol();
        rolPorDefecto.setRol_id(3);
        administrador.setRol_id(rolPorDefecto);

        String mensaje = administradorService.guardarAdministrador(administrador);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }

    @PostMapping("/guardar-multiple")
    public ResponseEntity<String> guardarAdministradores(@RequestBody List<Administrador> administradores) {
        if (administradores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La lista de administradores está vacía.");
        }

        for (Administrador administrador : administradores) {
            if (administrador.getRut().isEmpty() ||
                administrador.getNombre().isEmpty() ||
                administrador.getCorreo().isEmpty() ||
                administrador.getFono().isEmpty() ||
                administrador.getArea().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Datos incompletos de algún administrador.");
            }

            Administrador administradorExistente = administradorService.buscarPorRut(administrador.getRut());
            if (administradorExistente != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Ya existe un administrador con el RUT " + administrador.getRut());
            }

            // Asignar rol_id = 1 por defecto
            Rol rolPorDefecto = new Rol();
            rolPorDefecto.setRol_id(3);
            administrador.setRol_id(rolPorDefecto);
        }

        String mensaje = administradorService.guardarAdministradores(administradores);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<String> actualizarAdministrador(@RequestBody Administrador administrador) {
        Administrador administradorExistente = administradorService.buscarPorRut(administrador.getRut());
        if (administradorExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Administrador con RUT " + administrador.getRut() + " no encontrado.");
        }

        if (administrador.getCorreo().isEmpty() ||
            administrador.getFono().isEmpty() ||
            administrador.getArea().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Correo, fono y área son obligatorios.");
        }

        // Solo actualizamos campos permitidos
        administradorExistente.setNombre(administrador.getNombre());
        administradorExistente.setCorreo(administrador.getCorreo());
        administradorExistente.setFono(administrador.getFono());
        administradorExistente.setArea(administrador.getArea());



        String mensaje = administradorService.actualizarAdministrador(administradorExistente);
        return ResponseEntity.ok(mensaje);
    }

    @DeleteMapping("/eliminar/{rut}")
    public ResponseEntity<String> eliminarAdministrador(@PathVariable String rut) {
        Administrador administradorExistente = administradorService.buscarPorRut(rut);
        if (administradorExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Administrador con RUT " + rut + " no encontrado.");
        }

        String mensaje = administradorService.eliminarAdministrador(rut);
        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/buscar/rut/{rut}")
    public ResponseEntity<Administrador> buscarPorRut(@PathVariable String rut) {
        Administrador administrador = administradorService.buscarPorRut(rut);
        if (administrador == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(administrador);
    }

    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<Administrador>> buscarPorNombre(@PathVariable String nombre) {
        List<Administrador> administradores = administradorService.buscarPorNombre(nombre);
        return ResponseEntity.ok(administradores);
    }

    @GetMapping("/buscar/nombre-parcial/{nombre}")
    public ResponseEntity<List<Administrador>> buscarPorNombreParcial(@PathVariable String nombre) {
        List<Administrador> administradores = administradorService.buscarPorNombreParcial(nombre);
        return ResponseEntity.ok(administradores);
    }
}


