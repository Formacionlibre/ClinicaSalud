package com.belenfernandez.clinicasalud.modelo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Mutua implements Serializable {

    private int id;
    private String nombre;
    private double precioCita;

    public Mutua() {
    }

    public Mutua(int id, String nombre, double precioCita) {
        this.id = id;
        this.nombre = nombre;
        this.precioCita = precioCita;
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

    public double getPrecioCita() {
        return precioCita;
    }

    public void setPrecioCita(double precioCita) {
        this.precioCita = precioCita;
    }

    @Override
    public String toString() {
        return nombre;
    }

    // metodo que recibe un JSON que representa un mutua y devuelve
    // un objeto mutua (ojo, no se extrae de la BBDD, sino que viene directo de una llamada
    // a PHP)
    public static Mutua mutuaFromJSON(JSONObject object) {
        Mutua mutua = null;

        try {
            int id = Integer.parseInt(object.getString("id"));
            String nombre = object.getString("nombre");
            double precio_cita = Double.parseDouble(object.getString("precio_cita"));

            mutua = new Mutua(id,nombre,precio_cita);
        } catch (JSONException e) {

        }

        return mutua;
    }
}

