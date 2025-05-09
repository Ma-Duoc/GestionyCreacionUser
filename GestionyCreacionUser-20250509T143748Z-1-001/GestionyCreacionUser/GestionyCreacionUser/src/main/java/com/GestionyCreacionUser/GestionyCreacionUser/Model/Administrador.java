package com.GestionyCreacionUser.GestionyCreacionUser.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Administrador {

    private String rut;
    private String nombre;
    private String correo;
    private int numero;
    private String area;
    private Rol rol;

}
