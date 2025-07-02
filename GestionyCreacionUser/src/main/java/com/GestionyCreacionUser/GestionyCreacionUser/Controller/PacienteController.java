package com.GestionyCreacionUser.GestionyCreacionUser.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.GestionyCreacionUser.GestionyCreacionUser.Model.Paciente;
import com.GestionyCreacionUser.GestionyCreacionUser.Model.Rol;
import com.GestionyCreacionUser.GestionyCreacionUser.Service.PacienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;

@RestController
@RequestMapping("/pacientes")
@Tag(name = "Pacientes", description = "Operaciones relacionadas con los pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/listar")
    @Operation(summary = "Obtener todos los pacientes",description = "Retorna una lista con todos los pacientes registrados en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consulta exitosa",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Paciente.class)))),
        @ApiResponse(responseCode = "204", description = "No hay pacientes registrados", content = @Content)
    })
    public ResponseEntity<List<Paciente>> listarPacientes() {
        List<Paciente> pacientes = pacienteService.listarPacientes();
        if (pacientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pacientes);
    }


    @PostMapping("/guardar")
    @Operation(summary = "Registrar nuevo paciente",
        description = "Registra un nuevo paciente en el sistema. Se deben enviar todos los datos requeridos del paciente en el cuerpo de la solicitud."
    )
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Paciente creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Campos requeridos faltantes o RUT repetido")
    })
    public ResponseEntity<String> guardarPaciente(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Paciente a crear",required = true,content = @Content(
                mediaType = "application/json",schema = @Schema(implementation = Paciente.class))
        )
        @RequestBody Paciente paciente) {

        if (paciente.getRutPaciente().isEmpty() ||
            paciente.getNombre().isEmpty() ||
            paciente.getCorreo().isEmpty() ||
            paciente.getFono().isEmpty() ||
            paciente.getHistorialMedico().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Todos los campos son obligatorios.");
        }

        Paciente pacienteExistente = pacienteService.buscarPorRut(paciente.getRutPaciente());
        if (pacienteExistente != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("El paciente con rut " + paciente.getRutPaciente() + " ya existe.");
        }

        Rol rolPorDefecto = new Rol();
        rolPorDefecto.setRol_id(1);
        paciente.setRol_id(rolPorDefecto);

        String mensaje = pacienteService.guardarPaciente(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }


    @PostMapping("/guardar-multiple")
    @Operation(summary = "Registrar múltiples pacientes",
        description = "Permite registrar múltiples pacientes en una sola solicitud. Se envía un arreglo JSON con los datos de cada paciente."
    )
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Lista de pacientes creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Algún paciente tiene datos inválidos o uno o más RUT ya existen")
    })
    public ResponseEntity<String> guardarPacientes(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Lista de pacientes a crear",required = true,content = @Content(
                mediaType = "application/json",array = @ArraySchema(schema = @Schema(implementation = Paciente.class)))
        )@RequestBody List<Paciente> pacientes) {

        if (pacientes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La lista de pacientes está vacía.");
        }

        for (Paciente paciente : pacientes) {
            if (paciente.getRutPaciente().isEmpty() ||
                paciente.getNombre().isEmpty() ||
                paciente.getCorreo().isEmpty() ||
                paciente.getFono().isEmpty() ||
                paciente.getHistorialMedico().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos incompletos de algún paciente.");
            }

            Paciente pacienteExistente = pacienteService.buscarPorRut(paciente.getRutPaciente());
            if (pacienteExistente != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ya existe un paciente con el RUT " + paciente.getRutPaciente());
            }
            Rol rolPorDefecto = new Rol();
            rolPorDefecto.setRol_id(1);
            paciente.setRol_id(rolPorDefecto);
        }
        String mensaje = pacienteService.guardarPacientes(pacientes);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }


    @PutMapping("/actualizar")
    @Operation(
        summary = "Actualizar paciente",
        description = "Actualiza la información de un paciente existente en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Actualización exitosa"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    public ResponseEntity<String> actualizarPaciente(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Paciente con datos actualizados",required = true,
            content = @Content(mediaType = "application/json",schema = @Schema(implementation = Paciente.class))
        )
        @RequestBody Paciente paciente) {

        Paciente pacienteExistente = pacienteService.buscarPorRut(paciente.getRutPaciente());
        if (pacienteExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente con RUT " + paciente.getRutPaciente() + " no encontrado.");
        }

        if (paciente.getNombre().isEmpty() ||
            paciente.getCorreo().isEmpty() ||
            paciente.getFono().isEmpty() ||
            paciente.getHistorialMedico().isEmpty()) {return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Correo, fono, nombre e historial médico son obligatorios.");}

        pacienteExistente.setNombre(paciente.getNombre());
        pacienteExistente.setCorreo(paciente.getCorreo());
        pacienteExistente.setFono(paciente.getFono());
        pacienteExistente.setHistorialMedico(paciente.getHistorialMedico());
        String mensaje = pacienteService.actualizarPaciente(pacienteExistente);
        return ResponseEntity.ok(mensaje);
    }


    @DeleteMapping("/eliminar/{rut}")
    @Operation(
        summary = "Eliminar paciente",
        description = "Elimina del sistema al paciente cuyo RUT coincide con el valor proporcionado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "El paciente fue eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró un paciente con el RUT proporcionado")
    })
    public ResponseEntity<String> eliminarPaciente(
        @Parameter(description = "RUT del paciente a eliminar", required = true)
        @PathVariable String rut) {

        Paciente pacienteExistente = pacienteService.buscarPorRut(rut);
        if (pacienteExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente con RUT " + rut + " no encontrado.");
        }

        String mensaje = pacienteService.eliminarPaciente(rut);
        return ResponseEntity.ok(mensaje);
    }


    @GetMapping("/buscar/rut/{rut}")
    @Operation(
        summary = "Buscar paciente por RUT",
        description = "Retorna la información de un paciente específico según su RUT."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Paciente.class))),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado", content = @Content)
    })
    public ResponseEntity<Paciente> buscarPorRut(
        @Parameter(description = "RUT del paciente a buscar", required = true)
        @PathVariable String rut) {

        Paciente paciente = pacienteService.buscarPorRut(rut);
        if (paciente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(paciente);
    }


    @GetMapping("/buscar/nombre/{nombre}")
    @Operation(
        summary = "Buscar pacientes por nombre",
        description = "Busca y retorna una lista de pacientes cuyo nombre coincida con el valor entregado."
    )
    @ApiResponse(responseCode = "200", description = "Lista de pacientes encontrada (puede estar vacía)",
            content = @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = Paciente.class))))
    public ResponseEntity<List<Paciente>> buscarPorNombre(
        @Parameter(description = "Nombre del paciente a buscar", required = true)
        @PathVariable String nombre) {

        List<Paciente> pacientes = pacienteService.buscarPorNombre(nombre);
        return ResponseEntity.ok(pacientes);
    }


    @GetMapping("/buscar/nombre-parcial/{nombre}")
    @Operation(
        summary = "Buscar pacientes por nombre parcial",
        description = "Busca pacientes cuyo nombre contenga la cadena proporcionada. Útil para búsquedas parciales."
    )
    @ApiResponse(responseCode = "200", description = "Lista de pacientes encontrada (puede estar vacía)",
            content = @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = Paciente.class))))
    public ResponseEntity<List<Paciente>> buscarPorNombreParcial(
        @Parameter(description = "Parte del nombre del paciente a buscar", required = true)
        @PathVariable String nombre) {

        List<Paciente> pacientes = pacienteService.buscarPorNombreParcial(nombre);
        return ResponseEntity.ok(pacientes);
    }
}

