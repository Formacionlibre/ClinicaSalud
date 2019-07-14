package com.belenfernandez.clinicasalud.controlador;

import com.belenfernandez.clinicasalud.modelo.Usuario;

//quiero saber que usuario está logueado en cada momento. Produce que solo hay una instancia de un objeto a la vez.
//creo clase con atributo estatico y donde lo invoque lo tendré
public class Sesion {

    private static Sesion instance;  //atributo estatico dentro del mismo

    private Usuario usuario;  //usuario serian el especialista y el paciente

    private Sesion() {

    }

    public static Sesion getInstance() {
        if(instance==null)
            instance = new Sesion(); //si tiene valor lo instancio

        return instance;//si si tiene valor lo devuelve
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
