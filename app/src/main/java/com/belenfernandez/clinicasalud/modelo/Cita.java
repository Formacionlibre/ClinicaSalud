package com.belenfernandez.clinicasalud.modelo;

import android.util.Log;

import com.belenfernandez.clinicasalud.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

public class Cita implements Serializable {

    private int id;
    private Paciente paciente;
    private Especialista especialista;
    private Date fecha; // poner fecha+hora
    private String estado;
    private Mutua mutua;
    private FichaCita ficha;

    public Cita() {

    }

    public Cita(int id, Paciente paciente, Especialista especialista, Date fecha, String estado, Mutua mutua,FichaCita ficha) {
        this.id = id;
        this.paciente = paciente;
        this.especialista = especialista;
        this.fecha = fecha;
        this.estado = estado;
        this.mutua = mutua;
        this.ficha = ficha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Especialista getEspecialista() {
        return especialista;
    }

    public void setEspecialista(Especialista especialista) {
        this.especialista = especialista;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Mutua getMutua() {
        return mutua;
    }

    public void setMutua(Mutua mutua) {
        this.mutua = mutua;
    }

    public FichaCita getFicha() {
        return ficha;
    }

    public void setFicha(FichaCita ficha) {
        this.ficha = ficha;
    }

    @Override
    public String toString() {
        return paciente.getNombre() + " " + paciente.getApellidos() +  "( " + especialista.getNombre() + " " +
                especialista.getApellidos() + " ) - " + Util.dateToString(fecha);
    }

    // metodo que recibe un JSON que representa una cita y devuelve
    // un objeto cita (ojo, no se extrae de la BBDD, sino que viene directo de una llamada
    // a PHP)
    public static Cita citaFromJSON(JSONObject object) {
        Cita cita = null;

        try {
            int id = Integer.parseInt(object.getString("id"));
            JSONObject objPaciente = object.getJSONObject("paciente");
            Paciente paciente = Paciente.pacienteFromJSON(objPaciente);
            JSONObject objEspecialista = object.getJSONObject("especialista");
            Especialista especialista = Especialista.especialistaFromJSON(objEspecialista);

            String fecha = object.getString("fecha");
            String hora = object.getString("hora");

            String cadena_fecha = fecha + " " + hora;
            Log.d("XXX","cadena fecha: " + cadena_fecha);
            Date fecha_cita = Util.convertirFechaHoraBBDD(cadena_fecha);

            String estado = object.getString("estado");

            JSONObject objMutua = object.getJSONObject("mutua");
            Mutua mutua = Mutua.mutuaFromJSON(objMutua);

            FichaCita ficha = FichaCita.fichaCitaFromJSON(object.getJSONObject("ficha"));

            Log.d("XXX","Cita from json paciente: " + paciente.toString());

            cita = new Cita(id,paciente,especialista,fecha_cita,estado,mutua,ficha);
        } catch (JSONException e) {

        }

        return cita;
    }


}
