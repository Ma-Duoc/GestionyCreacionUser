package com.GestionyCreacionUser.GestionyCreacionUser.Service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.GestionyCreacionUser.GestionyCreacionUser.Model.Paciente;
import com.GestionyCreacionUser.GestionyCreacionUser.Model.Rol;
import com.GestionyCreacionUser.GestionyCreacionUser.Repository.PacienteRepository;

@SpringBootTest
public class PacienteServiceTest {

    @Autowired
    private PacienteService pacienteService;

    @MockBean
    private PacienteRepository pacienteRepository;

    @Test
    public void testListarPacientes(){
        Paciente paciente = new Paciente("11111111-1", "Juan", "juan@mail.com", "912345678", "Sano", new Rol(1,"Paciente"));
        when(pacienteRepository.findAll()).thenReturn(List.of(paciente));

        List<Paciente> pacientes = pacienteService.listarPacientes();
        assertNotNull(pacientes);
        assertEquals(1, pacientes.size());
    }

    @Test
    public void testGuardarPaciente() {
        Paciente paciente = new Paciente("11111111-1", "Juan", "juan@mail.com", "912345678", "Sano", new Rol(1, "Paciente"));
        when(pacienteRepository.save(paciente)).thenReturn(paciente);

        String mensaje= pacienteService.guardarPaciente(paciente);
        assertEquals("Paciente 11111111-1 guardado con éxito", mensaje);
    }

    @Test
    public void testGuardarPacientes() {
        Paciente paciente1 = new Paciente("11111111-1", "Juan", "juan@mail.com", "912345678", "Sano", new Rol(1, "Paciente"));
        Paciente paciente2 = new Paciente("22222222-2", "Ana", "ana@mail.com", "912345679", "Asma", new Rol(1, "Paciente"));

        List<Paciente> lista = List.of(paciente1, paciente2);

        when(pacienteRepository.saveAll(lista)).thenReturn(lista);

        String mensaje = pacienteService.guardarPacientes(lista);
        assertEquals("2 Pacientes guardados con exito", mensaje);
    }

    @Test
    public void testActualizarPaciente() {
        Paciente existente = new Paciente("11111111-1", "Juan", "juan@mail.com", "912345678", "Sano", new Rol(1, "Paciente"));
        Paciente actualizado = new Paciente("11111111-1", "Juan Pérez", "jp@mail.com", "987654321", "Asma", new Rol(1, "Paciente"));

        when(pacienteRepository.findById("11111111-1")).thenReturn(Optional.of(existente));
        when(pacienteRepository.save(existente)).thenReturn(actualizado);

        String mensaje = pacienteService.actualizarPaciente(actualizado);
        assertEquals("Paciente 11111111-1 actualizado con éxito", mensaje);
    }

    @Test
    public void testEliminarPaciente() {
        String rut = "11111111-1";
        doNothing().when(pacienteRepository).deleteById(rut);

        String mensaje = pacienteService.eliminarPaciente(rut);
        assertEquals("Paciente " + rut + " eliminado con éxito", mensaje);
        verify(pacienteRepository, times(1)).deleteById(rut);
    }

    @Test
    public void testBuscarPorRut() {
        Paciente paciente = new Paciente("11111111-1", "Juan", "juan@mail.com", "912345678", "Sano", new Rol(1, "Paciente"));
        when(pacienteRepository.findById("11111111-1")).thenReturn(Optional.of(paciente));

        Paciente encontrado = pacienteService.buscarPorRut("11111111-1");
        assertNotNull(encontrado);
        assertEquals("Juan", encontrado.getNombre());
    }

    @Test
    public void testBuscarPorNombre() {
        Paciente paciente = new Paciente("11111111-1", "Juan", "juan@mail.com", "912345678", "Sano", new Rol(1, "Paciente"));
        when(pacienteRepository.buscarPorNombre("Juan")).thenReturn(List.of(paciente));

        List<Paciente> resultado = pacienteService.buscarPorNombre("Juan");
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testBuscarPorNombreParcial() {
        Paciente paciente = new Paciente("11111111-1", "Juan", "juan@mail.com", "912345678", "Sano", new Rol(1, "Paciente"));
        when(pacienteRepository.buscarPorNombreParcial("Ju")).thenReturn(List.of(paciente));

        List<Paciente> resultado = pacienteService.buscarPorNombreParcial("Ju");
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

}
