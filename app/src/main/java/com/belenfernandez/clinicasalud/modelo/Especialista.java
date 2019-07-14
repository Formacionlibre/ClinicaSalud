package com.belenfernandez.clinicasalud.modelo;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Especialista implements Usuario, Comparable<Especialista> {

    private int id;
    private String nombre;
    private String apellidos;
    private String especialidad;
    private double salario;
    private String tipo;

    public Especialista() {
    }

    public Especialista(int id, String nombre, String apellidos, String especialidad, double salario, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.especialidad = especialidad;
        this.salario = salario;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return nombre + " " + apellidos;
    }

    // metodo que recibe un JSON que representa un especialista y devuelve
    // un objeto especialista (ojo, no se extrae de la BBDD, sino que viene directo de una llamada
    // a PHP)
    public static Especialista especialistaFromJSON(JSONObject object) {
        Especialista especialista = null;

        try {
            int id = Integer.parseInt(object.getString("id"));
            String nombre = object.getString("nombre");
            String apellidos = object.getString("apellidos");
            String especialidad = object.getString("especialidad");
            double salario = Double.parseDouble(object.getString("salario"));
            String tipo = object.getString("tipo");

            especialista = new Especialista(id,nombre,apellidos,especialidad,salario,tipo);
        } catch (JSONException e) {

        }

        return especialista;
    }

    // lo mismo que la anterior, pero para el caso que se me devuelva un array de especialistas
    public static List<Especialista> listaEspecialistaFromJSON(JSONArray array) {
        List<Especialista> lista = new ArrayList<>();
        try {
            for(int i=0;i<array.length();i++) {
                lista.add(especialistaFromJSON(array.getJSONObject(i)));
            }
        } catch (JSONException e) {

        }
        return lista;
    }

    @Override
    public int compareTo(@NonNull Especialista o) {
        return this.nombre.compareTo(o.nombre);
    }
}
