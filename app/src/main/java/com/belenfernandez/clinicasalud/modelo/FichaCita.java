package com.belenfernandez.clinicasalud.modelo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class FichaCita implements Serializable  {

    private String observaciones;
    private String recomendaciones;

    public FichaCita() {
    }

    public FichaCita(String observaciones, String recomendaciones) {
        this.observaciones = observaciones;
        this.recomendaciones = recomendaciones;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    // metodo que recibe un JSON que representa una cita y devuelve
    // un objeto cita (ojo, no se extrae de la BBDD, sino que viene directo de una llamada
    // a PHP)
    public static FichaCita fichaCitaFromJSON(JSONObject object) {
        FichaCita fichaCita = new FichaCita();

        try {
            String observaciones = object.getString("observaciones");
            String recomendaciones = object.getString("recomendaciones");

            fichaCita.setObservaciones(observaciones);
            fichaCita.setRecomendaciones(recomendaciones);
        } catch (JSONException je) {

        }

        return fichaCita;
    }
}
