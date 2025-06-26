package com.GestionyCreacionUser.GestionyCreacionUser.Controller;

import com.GestionyCreacionUser.GestionyCreacionUser.Model.Paciente;
import com.GestionyCreacionUser.GestionyCreacionUser.Service.PacienteService;
import com.GestionyCreacionUser.GestionyCreacionUser.assemblers.PacienteModelAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/pacientes")
public class PacienteControllerV2 {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private PacienteModelAssembler pacienteAssembler;

    @Operation(summary = "Listar todos los pacientes")
    @ApiResponse(
        responseCode = "200",
        description = "Listado completo de pacientes",
        content = @Content(
            mediaType = "application/hal+json",
            schema = @Schema(implementation = CollectionModel.class)
        )
    )
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Paciente>>> listarPacientes() {
        List<Paciente> pacientes = pacienteService.listarPacientes();
        List<EntityModel<Paciente>> pacientesModel = pacientes.stream()
                .map(pacienteAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(pacientesModel,
                        linkTo(methodOn(PacienteControllerV2.class).listarPacientes()).withSelfRel()));
    }

    @Operation(summary = "Buscar paciente por RUT")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Paciente encontrado",
            content = @Content(
                mediaType = "application/hal+json",
                schema = @Schema(implementation = EntityModel.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    @GetMapping(value = "/buscar/rut/{rut}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Paciente>> buscarPorRut(@PathVariable String rut) {
        Paciente paciente = pacienteService.buscarPorRut(rut);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pacienteAssembler.toModel(paciente));
    }

    @Operation(summary = "Buscar pacientes por nombre exacto")
    @ApiResponse(
        responseCode = "200",
        description = "Pacientes encontrados por nombre exacto",
        content = @Content(
            mediaType = "application/hal+json",
            schema = @Schema(implementation = CollectionModel.class)
        )
    )
    @GetMapping(value = "/buscar/nombre/{nombre}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Paciente>>> buscarPorNombre(@PathVariable String nombre) {
        List<Paciente> pacientes = pacienteService.buscarPorNombre(nombre);
        List<EntityModel<Paciente>> pacientesModel = pacientes.stream()
                .map(pacienteAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(pacientesModel,
                        linkTo(methodOn(PacienteControllerV2.class).buscarPorNombre(nombre)).withSelfRel()));
    }

    @Operation(summary = "Buscar pacientes por coincidencia parcial de nombre")
    @ApiResponse(
        responseCode = "200",
        description = "Pacientes encontrados por coincidencia parcial",
        content = @Content(
            mediaType = "application/hal+json",
            schema = @Schema(implementation = CollectionModel.class)
        )
    )
    @GetMapping(value = "/buscar/nombre-parcial/{nombre}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Paciente>>> buscarPorNombreParcial(@PathVariable String nombre) {
        List<Paciente> pacientes = pacienteService.buscarPorNombreParcial(nombre);
        List<EntityModel<Paciente>> pacientesModel = pacientes.stream()
                .map(pacienteAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(pacientesModel,
                        linkTo(methodOn(PacienteControllerV2.class).buscarPorNombreParcial(nombre)).withSelfRel()));
    }
}


