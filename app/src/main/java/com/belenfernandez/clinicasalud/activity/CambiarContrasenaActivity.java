package com.belenfernandez.clinicasalud.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.belenfernandez.clinicasalud.R;
import com.belenfernandez.clinicasalud.controlador.Comunicacion;
import com.belenfernandez.clinicasalud.controlador.Sesion;
import com.belenfernandez.clinicasalud.interfaces.ManejadorRespuestaJSON;
import com.belenfernandez.clinicasalud.modelo.Paciente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CambiarContrasenaActivity extends AppCompatActivity implements ManejadorRespuestaJSON {

    private static final int REQ_MODIFICAR_CONTRASENA = 1;
    private EditText tvNuevaContrasena, tvRepiteNuevaContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);

        this.tvNuevaContrasena = (EditText) findViewById(R.id.tvNuevaContrasena);
        this.tvRepiteNuevaContrasena = (EditText) findViewById(R.id.tvRepiteNuevaContrasena);
    }

    public void cambiarContrasena(View v) {
        String pass = this.tvNuevaContrasena.getText().toString();
        String rePass = this.tvRepiteNuevaContrasena.getText().toString();

        if(pass.isEmpty() || rePass.isEmpty()) {
            Toast.makeText(this,"No puedes dejar campos vacios", Toast.LENGTH_LONG).show();
            return;
        }
        else if(!pass.equals(rePass)) {
            Toast.makeText(this,"Las contraseñas deben coincidir", Toast.LENGTH_LONG).show();
            return;
        }

        String url = getString(R.string.url_servidor);

        int id_paciente = ((Paciente) Sesion.getInstance().getUsuario()).getId();

        // Pedir al servidor (esto se haria realmente cuando se pulsara un boton, una opcion....)
        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "modificar_contrasena");
            JSONObject datos = new JSONObject();
            datos.put("id_paciente", id_paciente);
            datos.put("nueva_contrasena", pass);
            json.put("datos", datos);

           Comunicacion.solicitudJSONObject(this,url, json,this, REQ_MODIFICAR_CONTRASENA);
        } catch (JSONException js) {
            Log.d("XXX",js.getMessage());
        }

    }

    @Override
    public void procesarRespuestaJSONObject(JSONObject response, int request) {
        Intent returnIntent = new Intent();
        try {
            String estado = response.getString("estado");
            if(estado.equalsIgnoreCase("OK")) {
                setResult(Activity.RESULT_OK,returnIntent);
            }
            else {
                setResult(Activity.RESULT_CANCELED,returnIntent);
            }
        } catch (JSONException e) {
            setResult(Activity.RESULT_CANCELED,returnIntent);
        }

        finish();
    }

    @Override
    public void procesarRespuestaJSONOArray(JSONArray response, int request) {

    }
}
