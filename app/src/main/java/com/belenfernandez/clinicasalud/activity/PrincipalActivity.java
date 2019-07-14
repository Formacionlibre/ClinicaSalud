package com.belenfernandez.clinicasalud.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.belenfernandez.clinicasalud.R;
import com.belenfernandez.clinicasalud.controlador.Comunicacion;
import com.belenfernandez.clinicasalud.controlador.Sesion;
import com.belenfernandez.clinicasalud.interfaces.ManejadorRespuestaJSON;
import com.belenfernandez.clinicasalud.modelo.Especialista;
import com.belenfernandez.clinicasalud.modelo.Paciente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PrincipalActivity extends AppCompatActivity implements ManejadorRespuestaJSON {

    public static final int REQ_LOGIN = 1;
    public static final int REQ_REGISTRO= 2;

    private EditText txtDni, txtPassword;
    private Switch swRecordar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        txtDni = (EditText) findViewById(R.id.txtDni);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        swRecordar = (Switch) findViewById(R.id.swRecordar);

        // miro si se recordaron las credenciales
        SharedPreferences prefs = getSharedPreferences(getString(R.string.preferencias), MODE_PRIVATE);
        String dniGuardado = prefs.getString("dni", null);
        if(dniGuardado!=null) {
            swRecordar.setChecked(true);
            txtDni.setText(dniGuardado);
            txtPassword.setText(prefs.getString("password", null));
        }

        // revisar si ya esta el login en las preferences y saltar al activity correspondiente
    }

    public void registrarse(View v) {
        Intent i = new Intent(this, DatosPersonalesActivity.class);
        i.putExtra("modo",getString(R.string.modo_registro));
        startActivityForResult(i,REQ_REGISTRO);
    }

    public void login(View v) {

        String dni = txtDni.getText().toString();
        String pwd = txtPassword.getText().toString();

        // miro si quiere recordar o no
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.preferencias), MODE_PRIVATE).edit();
        if(swRecordar.isChecked()) {
            editor.putString("dni", dni);
            editor.putString("password", pwd);
        }
        else {
            editor.remove("dni");
            editor.remove("password");
        }
        editor.apply();

        String url = getString(R.string.url_servidor);
        Log.d("XXX","Pido URL " + url);

        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "login");
            JSONObject datos = new JSONObject();
            datos.put("dni", dni);
            datos.put("password", pwd);
            json.put("datos", datos);

            Comunicacion.solicitudJSONObject(this,url, json,this,REQ_LOGIN);
        } catch (JSONException js) {
            Toast.makeText(this,"Error: " + js.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQ_REGISTRO && resultCode==Activity.RESULT_OK) {
            Paciente paciente = (Paciente) data.getSerializableExtra("paciente");
            Sesion.getInstance().setUsuario(paciente);
            Intent intent = new Intent(this, PacienteActivity.class);
            startActivity(intent);
        }
        else if(resultCode==Activity.RESULT_CANCELED){
            Toast.makeText(this,"Error de registro ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void procesarRespuestaJSONObject(JSONObject response, int request) {

        if(request==REQ_LOGIN) {
            try {
                String estado = response.getString("estado");
                if (estado.equals("OK") && response.getJSONObject("datos").getString("tipo").equals("paciente")) {
                    Intent intent = new Intent(this, PacienteActivity.class);
                    // guardo el paciente que soy
                    Paciente paciente = Paciente.pacienteFromJSON(response.getJSONObject("datos").getJSONObject("usuario"));
                    Sesion.getInstance().setUsuario(paciente);

                    startActivity(intent);
                } else if (estado.equals("OK") && response.getJSONObject("datos").getString("tipo").equals("especialista")) {
                    Intent intent = new Intent(this, EspecialistaActivity.class);
                    // guardo el especialista que soy
                    Especialista especialista = Especialista.especialistaFromJSON(response.getJSONObject("datos").getJSONObject("usuario"));
                    Sesion.getInstance().setUsuario(especialista);

                    startActivity(intent);
                } else {
                    Toast.makeText(this,"Error en las credenciales", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException js) {
                Toast.makeText(this,"Error: " + js.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void procesarRespuestaJSONOArray(JSONArray response, int request) {

    }
}
