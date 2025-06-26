package com.GestionyCreacionUser.GestionyCreacionUser.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.GestionyCreacionUser.GestionyCreacionUser.Model.Medico;
import com.GestionyCreacionUser.GestionyCreacionUser.Model.Rol;
import com.GestionyCreacionUser.GestionyCreacionUser.Service.MedicoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;

@RestController
@RequestMapping("/medicos")
@Tag(name = "Médicos", description = "Operaciones relacionadas con los médicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping("/listar")
    @Operation(summary = "Obtener todos los médicos", description = "Retorna una lista con todos los médicos registrados en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consulta exitosa",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Medico.class)))),
        @ApiResponse(responseCode = "204", description = "No hay médicos registrados", content = @Content)
    })
    public ResponseEntity<List<Medico>> listarMedicos() {
        List<Medico> medicos = medicoService.listarMedicos();
        if (medicos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(medicos);
    }

    @PostMapping("/guardar")
    @Operation(
        summary = "Registrar nuevo médico",
        description = "Registra un nuevo médico en el sistema. Se deben enviar todos los datos requeridos del médico en el cuerpo de la solicitud."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Médico creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Campos requeridos faltantes o RUT repetido")
    })
    public ResponseEntity<String> guardarMedico(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Médico a crear",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Medico.class)
            )
        )
        @RequestBody Medico medico) {

        if (medico.getRutMedico().isEmpty() ||
            medico.getNombre().isEmpty() ||
            medico.getCorreo().isEmpty() ||
            medico.getFono().isEmpty() ||
            medico.getEspecialidad().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Todos los campos son obligatorios.");
        }

        Medico medicoExistente = medicoService.buscarPorRut(medico.getRutMedico());
        if (medicoExistente != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("El médico con RUT " + medico.getRutMedico() + " ya existe.");
        }

        Rol rolPorDefecto = new Rol();
        rolPorDefecto.setRol_id(2); // Rol médico
        medico.setRol_id(rolPorDefecto);

        String mensaje = medicoService.guardarMedico(medico);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }

    @PostMapping("/guardar-multiple")
    @Operation(
        summary = "Registrar múltiples médicos",
        description = "Permite registrar múltiples médicos en una sola solicitud. Se envía un arreglo JSON con los datos de cada médico."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Lista de médicos creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Algún médico tiene datos inválidos o uno o más RUT ya existen")
    })
    public ResponseEntity<String> guardarMedicos(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Lista de médicos a crear",
            required = true,
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Medico.class))
            )
        )
        @RequestBody List<Medico> medicos) {

        if (medicos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La lista de médicos está vacía.");
        }

        for (Medico medico : medicos) {
            if (medico.getRutMedico().isEmpty() ||
                medico.getNombre().isEmpty() ||
                medico.getCorreo().isEmpty() ||
                medico.getFono().isEmpty() ||
                medico.getEspecialidad().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos incompletos de algún médico.");
            }

            Medico medicoExistente = medicoService.buscarPorRut(medico.getRutMedico());
            if (medicoExistente != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ya existe un médico con el RUT " + medico.getRutMedico());
            }

            Rol rolPorDefecto = new Rol();
            rolPorDefecto.setRol_id(2);
            medico.setRol_id(rolPorDefecto);
        }

        String mensaje = medicoService.guardarMedicos(medicos);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }

    @PutMapping("/actualizar")
    @Operation(
        summary = "Actualizar médico",
        description = "Actualiza la información de un médico existente en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Actualización exitosa"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
        @ApiResponse(responseCode = "404", description = "Médico no encontrado")
    })
    public ResponseEntity<String> actualizarMedico(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Médico con datos actualizados",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Medico.class)
            )
        )
        @RequestBody Medico medico) {

        Medico medicoExistente = medicoService.buscarPorRut(medico.getRutMedico());
        if (medicoExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Médico con RUT " + medico.getRutMedico() + " no encontrado.");
        }

        if (medico.getNombre().isEmpty() ||
            medico.getCorreo().isEmpty() ||
            medico.getFono().isEmpty() ||
            medico.getEspecialidad().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Correo, fono, nombre y especialidad son obligatorios.");
        }

        medicoExistente.setNombre(medico.getNombre());
        medicoExistente.setCorreo(medico.getCorreo());
        medicoExistente.setFono(medico.getFono());
        medicoExistente.setEspecialidad(medico.getEspecialidad());

        String mensaje = medicoService.actualizarMedico(medicoExistente);
        return ResponseEntity.ok(mensaje);
    }

    @DeleteMapping("/eliminar/{rut}")
    @Operation(
        summary = "Eliminar médico",
        description = "Elimina del sistema al médico cuyo RUT coincide con el valor proporcionado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "El médico fue eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró un médico con el RUT proporcionado")
    })
    public ResponseEntity<String> eliminarMedico(
        @Parameter(description = "RUT del médico a eliminar", required = true)
        @PathVariable String rut) {

        Medico medicoExistente = medicoService.buscarPorRut(rut);
        if (medicoExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Médico con RUT " + rut + " no encontrado.");
        }

        String mensaje = medicoService.eliminarMedico(rut);
        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/buscar/rut/{rut}")
    @Operation(
        summary = "Buscar médico por RUT",
        description = "Retorna la información de un médico específico según su RUT."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Médico encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Medico.class))),
        @ApiResponse(responseCode = "404", description = "Médico no encontrado", content = @Content)
    })
    public ResponseEntity<Medico> buscarPorRut(
        @Parameter(description = "RUT del médico a buscar", required = true)
        @PathVariable String rut) {

        Medico medico = medicoService.buscarPorRut(rut);
        if (medico == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(medico);
    }

    @GetMapping("/buscar/nombre/{nombre}")
    @Operation(
        summary = "Buscar médicos por nombre",
        description = "Busca y retorna una lista de médicos cuyo nombre coincida con el valor entregado."
    )
    @ApiResponse(responseCode = "200", description = "Lista de médicos encontrada (puede estar vacía)",
        content = @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = Medico.class))))
    public ResponseEntity<List<Medico>> buscarPorNombre(
        @Parameter(description = "Nombre del médico a buscar", required = true)
        @PathVariable String nombre) {

        List<Medico> medicos = medicoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(medicos);
    }

    @GetMapping("/buscar/nombre-parcial/{nombre}")
    @Operation(
        summary = "Buscar médicos por nombre parcial",
        description = "Busca médicos cuyo nombre contenga la cadena proporcionada. Útil para búsquedas parciales."
    )
    @ApiResponse(responseCode = "200", description = "Lista de médicos encontrada (puede estar vacía)",
        content = @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = Medico.class))))
    public ResponseEntity<List<Medico>> buscarPorNombreParcial(
        @Parameter(description = "Parte del nombre del médico a buscar", required = true)
        @PathVariable String nombre) {

        List<Medico> medicos = medicoService.buscarPorNombreParcial(nombre);
        return ResponseEntity.ok(medicos);
    }
}
