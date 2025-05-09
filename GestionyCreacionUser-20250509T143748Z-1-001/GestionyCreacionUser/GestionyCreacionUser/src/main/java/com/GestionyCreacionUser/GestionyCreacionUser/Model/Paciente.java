package com.GestionyCreacionUser.GestionyCreacionUser.Model;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "paciente")
public class Paciente {

    @Id
    @Column(unique = true, length = 13, nullable = false)
    private String rut;  // Clave primaria, único e indispensable

    @Column(nullable = false, length = 50, unique = true)
    private String nombre;  // Nombre único

    @Column(nullable = false)
    private String correo;  // Puede repetirse

    @Column(nullable = false, length = 9)
    private String fono;  // Puede repetirse

    @Column(nullable = false, columnDefinition = "TEXT")
    private String historialMedico;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;  // Puede repetirse entre varios pacientes

}
