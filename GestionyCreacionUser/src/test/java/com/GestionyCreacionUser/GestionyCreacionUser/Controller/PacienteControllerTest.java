package com.GestionyCreacionUser.GestionyCreacionUser.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.GestionyCreacionUser.GestionyCreacionUser.Model.Paciente;
import com.GestionyCreacionUser.GestionyCreacionUser.Model.Rol;
import com.GestionyCreacionUser.GestionyCreacionUser.Service.PacienteService;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(PacienteController.class)
public class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Paciente paciente;


    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setRut("12345678-9");
        paciente.setNombre("Juan Pérez");
        paciente.setCorreo("juan@example.com");
        paciente.setFono("912345678");
        paciente.setHistorialMedico("Sin alergias");
        paciente.setRol_id(new Rol(1, "Paciente"));
    }

    @Test
    public void testListarPacientes() throws Exception {
        when(pacienteService.listarPacientes()).thenReturn(List.of(paciente));

        mockMvc.perform(get("/pacientes/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rut").value("12345678-9"));
    }

    @Test
    public void testGuardarPaciente() throws Exception {
        when(pacienteService.buscarPorRut(anyString())).thenReturn(null);
        when(pacienteService.guardarPaciente(any(Paciente.class))).thenReturn("Paciente guardado");

        mockMvc.perform(post("/pacientes/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paciente)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Paciente guardado"));
    }

    @Test
    public void testGuardarPacientes() throws Exception {
        when(pacienteService.guardarPacientes(anyList())).thenReturn("Pacientes guardados");
        when(pacienteService.buscarPorRut(anyString())).thenReturn(null);

        mockMvc.perform(post("/pacientes/guardar-multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(paciente))))
                .andExpect(status().isCreated())
                .andExpect(content().string("Pacientes guardados"));
    }

    @Test
    public void testActualizarPaciente() throws Exception {
        when(pacienteService.buscarPorRut("12345678-9")).thenReturn(paciente);
        when(pacienteService.actualizarPaciente(any(Paciente.class))).thenReturn("Paciente actualizado");

        paciente.setCorreo("nuevo@email.com");

        mockMvc.perform(put("/pacientes/actualizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paciente)))
                .andExpect(status().isOk())
                .andExpect(content().string("Paciente actualizado"));
    }

    @Test
    public void testEliminarPaciente() throws Exception {
        when(pacienteService.buscarPorRut("12345678-9")).thenReturn(paciente);
        when(pacienteService.eliminarPaciente("12345678-9")).thenReturn("Paciente eliminado");

        mockMvc.perform(delete("/pacientes/eliminar/12345678-9"))
                .andExpect(status().isOk())
                .andExpect(content().string("Paciente eliminado"));
    }

    @Test
    public void testBuscarPorRut() throws Exception {
        when(pacienteService.buscarPorRut("12345678-9")).thenReturn(paciente);

        mockMvc.perform(get("/pacientes/buscar/rut/12345678-9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"));
    }

    @Test
    public void testBuscarPorNombre() throws Exception {
        when(pacienteService.buscarPorNombre("Juan")).thenReturn(List.of(paciente));

        mockMvc.perform(get("/pacientes/buscar/nombre/Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"));
    }

    @Test
    public void testBuscarPorNombreParcial() throws Exception {
        when(pacienteService.buscarPorNombreParcial("Juan")).thenReturn(List.of(paciente));

        mockMvc.perform(get("/pacientes/buscar/nombre-parcial/Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"));
    }
    
}

