package com.belenfernandez.clinicasalud.modelo;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Paciente implements Usuario {

    private int id;
    private String nombre;
    private String apellidos;
    private String tipoDocumento;
    private String valorDocumento;
    private String telefono;
    private String email;
    private Mutua mutua;
    private String password;

    public Paciente() {
    }

    public Paciente(int id, String nombre, String apellidos, String tipoDocumento, String valorDocumento, String telefono, String email, Mutua mutua, String password) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.tipoDocumento = tipoDocumento;
        this.valorDocumento = valorDocumento;
        this.telefono = telefono;
        this.email = email;
        this.mutua = mutua;
        this.password = password;
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

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getValorDocumento() {
        return valorDocumento;
    }

    public void setValorDocumento(String valorDocumento) {
        this.valorDocumento = valorDocumento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Mutua getMutua() {
        return mutua;
    }

    public void setMutua(Mutua mutua) {
        this.mutua = mutua;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return nombre + " " + apellidos;
    }

    // metodo que recibe un JSON que representa un especialista y devuelve
    // un objeto especialista (ojo, no se extrae de la BBDD, sino que viene directo de una llamada
    // a PHP)
    public static Paciente pacienteFromJSON(JSONObject object) {
        Paciente paciente = null;

        try {
            int id = Integer.parseInt(object.getString("id"));
            String nombre = object.getString("nombre");
            String apellidos = object.getString("apellidos");
            String tipo_documento = object.getString("tipo_documento");
            String valor_documento = object.getString("valor_documento");
            String telefono = object.getString("telefono");
            String email = object.getString("email");

            JSONObject sociedad_medica = object.getJSONObject("sociedad_medica");
            Mutua mutua = Mutua.mutuaFromJSON(sociedad_medica);

            String password = object.getString("password");

            paciente = new Paciente(id,nombre,apellidos,tipo_documento,valor_documento,telefono,email,mutua,password);
        } catch (JSONException e) {
            Log.d("XXX", "Excepcion: " + e.getMessage());
        }

        return paciente;
    }

    public static List<Paciente> listaPacienteFromJSON(JSONArray array) {
        List<Paciente> lista = new ArrayList<>();
        try {
            for(int i=0;i<array.length();i++) {
                lista.add(pacienteFromJSON(array.getJSONObject(i)));
            }
        } catch (JSONException e) {

        }
        return lista;
    }
}
