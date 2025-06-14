package com.GestionyCreacionUser.GestionyCreacionUser;

import com.GestionyCreacionUser.GestionyCreacionUser.Model.*;
import com.GestionyCreacionUser.GestionyCreacionUser.Repository.*;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired AdministradorRepository administradorRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        Rol rolPaciente = new Rol();
        rolPaciente.setRol_id(1); 

        for (int i = 0; i < 5; i++) {
            Paciente paciente = new Paciente();
            paciente.setRut(faker.idNumber().valid());
            paciente.setNombre(faker.name().fullName());
            paciente.setCorreo(faker.internet().emailAddress());
            paciente.setFono(String.valueOf(faker.number().numberBetween(900000000, 999999999)));
            paciente.setHistorialMedico("Historial de paciente generado automáticamente.");
            paciente.setRol_id(rolPaciente);

            pacienteRepository.save(paciente);
        }

        System.out.println(" Pacientes insertados correctamente ");

        Rol rolMedico= new Rol();
        rolMedico.setRol_id(2);

        for (int i=0; i<5; i++) {
            Medico medico = new Medico();
            medico.setRut(faker.idNumber().valid());
            medico.setNombre(faker.name().fullName());
            medico.setCorreo(faker.internet().emailAddress());
            medico.setFono(String.valueOf(faker.number().numberBetween(900000000, 999999999)));
            medico.setEspecialidad("Especialidad generada");
            medico.setRol_id(rolMedico); 

            medicoRepository.save(medico);
        }

        System.out.println("Medicos insertados correctamente");

        Rol rolAdministrador = new Rol();
        rolAdministrador.setRol_id(3);

        for (int i=0; i<5; i++) {
            Administrador administrador= new Administrador();
            administrador.setRut(faker.idNumber().valid());
            administrador.setNombre(faker.name().fullName());
            administrador.setCorreo(faker.internet().emailAddress());
            administrador.setFono(String.valueOf((faker.number().numberBetween(900000000, 999999999))));
            administrador.setArea("Area generada");
            administrador.setRol_id(rolAdministrador);
            
            administradorRepository.save(administrador);
        }

        System.out.println("Administradores insertados correctamente");
    }
}
