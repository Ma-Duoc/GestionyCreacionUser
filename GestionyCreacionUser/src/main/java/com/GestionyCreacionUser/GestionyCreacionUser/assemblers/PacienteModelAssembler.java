package com.GestionyCreacionUser.GestionyCreacionUser.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.GestionyCreacionUser.GestionyCreacionUser.Controller.PacienteControllerV2;
import com.GestionyCreacionUser.GestionyCreacionUser.Model.Paciente;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PacienteModelAssembler implements RepresentationModelAssembler<Paciente, EntityModel<Paciente>> {

    @Override
    public EntityModel<Paciente> toModel(Paciente paciente) {
        return EntityModel.of(paciente,
            linkTo(methodOn(PacienteControllerV2.class).buscarPorRut(paciente.getRutPaciente())).withSelfRel(),
            linkTo(methodOn(PacienteControllerV2.class).listarPacientes()).withRel("todos"),
            linkTo(methodOn(PacienteControllerV2.class).buscarPorNombre(paciente.getNombre())).withRel("buscarPorNombre"),
            linkTo(methodOn(PacienteControllerV2.class).buscarPorNombreParcial(paciente.getNombre())).withRel("buscarPorNombreParcial")
        );
    }
}
