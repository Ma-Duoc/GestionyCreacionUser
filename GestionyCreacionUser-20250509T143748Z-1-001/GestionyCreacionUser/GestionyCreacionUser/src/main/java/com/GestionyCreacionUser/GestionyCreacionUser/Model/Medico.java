package com.GestionyCreacionUser.GestionyCreacionUser.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Medico {

    private String rut;
    private String nombre;
    private String correo;
    private int numero;
    private String especialidad;
    private Rol rol;

}
