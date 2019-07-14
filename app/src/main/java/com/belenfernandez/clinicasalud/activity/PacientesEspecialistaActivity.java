package com.belenfernandez.clinicasalud.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.belenfernandez.clinicasalud.R;
import com.belenfernandez.clinicasalud.controlador.Comunicacion;
import com.belenfernandez.clinicasalud.controlador.Sesion;
import com.belenfernandez.clinicasalud.interfaces.ManejadorRespuestaJSON;
import com.belenfernandez.clinicasalud.modelo.Paciente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PacientesEspecialistaActivity extends AppCompatActivity implements ManejadorRespuestaJSON {

    public static final int REQ_LISTA_PACIENTES = 1;
    public static final int REQ_REGISTRO= 2;

    private Spinner spPacientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacientes_especialista);

        spPacientes = (Spinner) findViewById(R.id.spPacientes);

        solicitarPacientes();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pacientes_especialista_activity, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.cerrar_sesion) {
            Sesion.getInstance().setUsuario(null);
            Intent i = new Intent(this, PrincipalActivity.class);
            // no permitir volver atras con el boton
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else if(id==R.id.anadir_paciente) {
            Intent i = new Intent(this, DatosPersonalesActivity.class);
            i.putExtra("modo",getString(R.string.modo_registro));
            startActivityForResult(i,REQ_REGISTRO);
        }
        return true;
    }

    private void solicitarPacientes() {
        String url = getString(R.string.url_servidor);

        // Pedir al servidor (esto se haria realmente cuando se pulsara un boton, una opcion....)
        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "lista_pacientes");
            JSONObject datos = new JSONObject();
            json.put("datos", datos);

            // como he querido lista de pacientes, llamo a solicitud de array
            Comunicacion.solicitudJSONArray(this,url, json,this, REQ_LISTA_PACIENTES);
        } catch (JSONException js) {
            Log.d("XXX",js.getMessage());
        }
    }
    public void verHistorialPaciente(View v) {
        Paciente seleccionado = (Paciente) spPacientes.getSelectedItem();
        Intent i = new Intent(this, HistorialPacienteActivity.class);
        i.putExtra("paciente",seleccionado);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQ_REGISTRO && resultCode==Activity.RESULT_OK) {
            Paciente paciente = (Paciente) data.getSerializableExtra("paciente");
            Toast.makeText(this,"Paciente " + paciente.getNombre() + " registrado", Toast.LENGTH_LONG).show();
            solicitarPacientes();
        }
        else if(resultCode==Activity.RESULT_CANCELED){
            Toast.makeText(this,"Error de registro ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void procesarRespuestaJSONObject(JSONObject response, int request) {

    }
    @Override
    public void procesarRespuestaJSONOArray(JSONArray response, int request) {
        if(request==REQ_LISTA_PACIENTES) {
            List<Paciente> pacientes = new ArrayList<>();

            for(int i=0;i<response.length();i++) {
                try {
                    JSONObject o = response.getJSONObject(i);
                    // Para cada objeto del jsonarray, lo convierto en un Paciente y lo anado al arraylist
                    // de pacientes
                    pacientes.add(Paciente.pacienteFromJSON(o));
                } catch (JSONException js) {
                    Log.d("XXX",js.getMessage());
                }
            }
            ArrayAdapter<Paciente> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,pacientes);
            this.spPacientes.setAdapter(adapter);
        }
    }
}
