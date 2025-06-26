package com.GestionyCreacionUser.GestionyCreacionUser.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medico")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Medico {

    @Id
    @Column(name = "rut_medico", unique = true, length = 13)
    private String rutMedico;

    @Column(nullable = false, length = 50,unique = false)
    private String nombre;

    @Column(nullable = false)
    private String correo;

    @Column(nullable = false, length = 9)
    private String fono;

    @Column(nullable = false, length = 30)
    private String especialidad;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol_id;

}
