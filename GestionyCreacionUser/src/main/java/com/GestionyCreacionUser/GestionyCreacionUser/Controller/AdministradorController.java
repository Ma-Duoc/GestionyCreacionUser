package com.GestionyCreacionUser.GestionyCreacionUser.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.GestionyCreacionUser.GestionyCreacionUser.Model.Administrador;
import com.GestionyCreacionUser.GestionyCreacionUser.Model.Rol;
import com.GestionyCreacionUser.GestionyCreacionUser.Service.AdministradorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/administradores")
@Tag(name = "Administradores", description = "Operaciones relacionadas con los administradores")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @GetMapping("/listar")
    @Operation(summary = "Obtener todos los administradores", description = "Retorna una lista con todos los administradores registrados en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consulta exitosa",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Administrador.class)))),
        @ApiResponse(responseCode = "204", description = "No hay administradores registrados", content = @Content)
    })
    public ResponseEntity<List<Administrador>> listarAdministradores() {
        List<Administrador> administradores = administradorService.listarAdministradores();
        if (administradores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(administradores);
    }

    @PostMapping("/guardar")
    @Operation(summary = "Registrar nuevo administrador", description = "Registra un nuevo administrador en el sistema. Se deben enviar todos los datos requeridos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Administrador creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Campos requeridos faltantes o RUT repetido")
    })
    public ResponseEntity<String> guardarAdministrador(
        @RequestBody(
            description = "Administrador a crear",
            required = true,
            content = @Content(schema = @Schema(implementation = Administrador.class))
        )
        @org.springframework.web.bind.annotation.RequestBody Administrador administrador) {

        if (administrador.getRutAdministrador().isEmpty() ||
            administrador.getNombre().isEmpty() ||
            administrador.getCorreo().isEmpty() ||
            administrador.getFono().isEmpty() ||
            administrador.getArea().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Todos los campos son obligatorios.");
        }

        Administrador administradorExistente = administradorService.buscarPorRut(administrador.getRutAdministrador());
        if (administradorExistente != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("El administrador con RUT " + administrador.getRutAdministrador() + " ya existe.");
        }

        Rol rolPorDefecto = new Rol();
        rolPorDefecto.setRol_id(3);
        administrador.setRol_id(rolPorDefecto);

        String mensaje = administradorService.guardarAdministrador(administrador);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }

    @PostMapping("/guardar-multiple")
    @Operation(summary = "Registrar múltiples administradores", description = "Permite registrar múltiples administradores en una sola solicitud.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Lista de administradores creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Algún administrador tiene datos inválidos o RUT duplicado")
    })
    public ResponseEntity<String> guardarAdministradores(
        @RequestBody(
            description = "Lista de administradores a crear",
            required = true,
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Administrador.class)))
        )
        @org.springframework.web.bind.annotation.RequestBody List<Administrador> administradores) {

        if (administradores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La lista de administradores está vacía.");
        }

        for (Administrador administrador : administradores) {
            if (administrador.getRutAdministrador().isEmpty() ||
                administrador.getNombre().isEmpty() ||
                administrador.getCorreo().isEmpty() ||
                administrador.getFono().isEmpty() ||
                administrador.getArea().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Datos incompletos de algún administrador.");
            }

            Administrador administradorExistente = administradorService.buscarPorRut(administrador.getRutAdministrador());
            if (administradorExistente != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Ya existe un administrador con el RUT " + administrador.getRutAdministrador());
            }

            Rol rolPorDefecto = new Rol();
            rolPorDefecto.setRol_id(3);
            administrador.setRol_id(rolPorDefecto);
        }

        String mensaje = administradorService.guardarAdministradores(administradores);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }

    @PutMapping("/actualizar")
    @Operation(summary = "Actualizar administrador", description = "Actualiza los datos de un administrador existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Administrador actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
        @ApiResponse(responseCode = "404", description = "Administrador no encontrado")
    })
    public ResponseEntity<String> actualizarAdministrador(
        @RequestBody(
            description = "Administrador con datos actualizados",
            required = true,
            content = @Content(schema = @Schema(implementation = Administrador.class))
        )
        @org.springframework.web.bind.annotation.RequestBody Administrador administrador) {

        Administrador administradorExistente = administradorService.buscarPorRut(administrador.getRutAdministrador());
        if (administradorExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Administrador con RUT " + administrador.getRutAdministrador() + " no encontrado.");
        }

        if (administrador.getNombre().isEmpty() ||
            administrador.getCorreo().isEmpty() ||
            administrador.getFono().isEmpty() ||
            administrador.getArea().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Correo, fono y área son obligatorios.");
        }

        administradorExistente.setNombre(administrador.getNombre());
        administradorExistente.setCorreo(administrador.getCorreo());
        administradorExistente.setFono(administrador.getFono());
        administradorExistente.setArea(administrador.getArea());
        
        String mensaje = administradorService.actualizarAdministrador(administradorExistente);
        return ResponseEntity.ok(mensaje);
    }

    @DeleteMapping("/eliminar/{rut}")
    @Operation(summary = "Eliminar administrador", description = "Elimina al administrador cuyo RUT coincida con el proporcionado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Administrador eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Administrador no encontrado")
    })
    public ResponseEntity<String> eliminarAdministrador(
        @Parameter(description = "RUT del administrador a eliminar", required = true)
        @PathVariable String rut) {

        Administrador administradorExistente = administradorService.buscarPorRut(rut);
        if (administradorExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Administrador con RUT " + rut + " no encontrado.");
        }

        String mensaje = administradorService.eliminarAdministrador(rut);
        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/buscar/rut/{rut}")
    @Operation(summary = "Buscar administrador por RUT", description = "Retorna la información de un administrador según su RUT.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Administrador encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Administrador.class))),
        @ApiResponse(responseCode = "404", description = "Administrador no encontrado", content = @Content)
    })
    public ResponseEntity<Administrador> buscarPorRut(
        @Parameter(description = "RUT del administrador a buscar", required = true)
        @PathVariable String rut) {

        Administrador administrador = administradorService.buscarPorRut(rut);
        if (administrador == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(administrador);
    }

    @GetMapping("/buscar/nombre/{nombre}")
    @Operation(summary = "Buscar administradores por nombre", description = "Busca y retorna una lista de administradores con el nombre especificado.")
    @ApiResponse(responseCode = "200", description = "Lista de administradores encontrada",
        content = @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = Administrador.class))))
    public ResponseEntity<List<Administrador>> buscarPorNombre(
        @Parameter(description = "Nombre del administrador a buscar", required = true)
        @PathVariable String nombre) {

        List<Administrador> administradores = administradorService.buscarPorNombre(nombre);
        return ResponseEntity.ok(administradores);
    }

    @GetMapping("/buscar/nombre-parcial/{nombre}")
    @Operation(summary = "Buscar administradores por nombre parcial", description = "Busca administradores cuyo nombre contenga la cadena proporcionada.")
    @ApiResponse(responseCode = "200", description = "Lista de administradores encontrada",
        content = @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = Administrador.class))))
    public ResponseEntity<List<Administrador>> buscarPorNombreParcial(
        @Parameter(description = "Parte del nombre del administrador a buscar", required = true)
        @PathVariable String nombre) {

        List<Administrador> administradores = administradorService.buscarPorNombreParcial(nombre);
        return ResponseEntity.ok(administradores);
    }
}



