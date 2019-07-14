package com.belenfernandez.clinicasalud.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.belenfernandez.clinicasalud.R;
import com.belenfernandez.clinicasalud.controlador.Comunicacion;
import com.belenfernandez.clinicasalud.controlador.Sesion;
import com.belenfernandez.clinicasalud.interfaces.ManejadorRespuestaJSON;
import com.belenfernandez.clinicasalud.modelo.Paciente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MisDatosActivity extends AppCompatActivity implements ManejadorRespuestaJSON {

    private static final int REQ_ELIMINAR_PACIENTE = 1;
    private static final int REQ_MODIFICAR_DATOS = 2;
    private static final int REQ_CAMBIAR_CONTRASENA = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_datos);
    }

    public void modificarDatos(View v) {
        Intent i = new Intent(this,DatosPersonalesActivity.class);
        i.putExtra("modo",getString(R.string.modo_editar));
        startActivityForResult(i,REQ_MODIFICAR_DATOS);
    }

    public void cambiarContrasena(View v) {

        Intent i = new Intent(this, CambiarContrasenaActivity.class);
        startActivityForResult(i,REQ_CAMBIAR_CONTRASENA);

    }

    public void eliminarCuenta(View v) {
        solicitarEliminarPaciente();
    }

    private void solicitarEliminarPaciente() {

        String url = getString(R.string.url_servidor);

        int id_paciente = ((Paciente) Sesion.getInstance().getUsuario()).getId();

        // Pedir al servidor (esto se haria realmente cuando se pulsara un boton, una opcion....)
        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "eliminar_paciente");
            JSONObject datos = new JSONObject();
            datos.put("id",id_paciente);
            json.put("datos", datos);

            Comunicacion.solicitudJSONObject(this,url, json,this, REQ_ELIMINAR_PACIENTE);
        } catch (JSONException js) {
            Log.d("XXX",js.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQ_MODIFICAR_DATOS && resultCode==Activity.RESULT_OK) {
            Paciente paciente = (Paciente) data.getSerializableExtra("paciente");
            Toast.makeText(this,"Paciente " + paciente.getNombre() + " editado", Toast.LENGTH_LONG).show();
            Sesion.getInstance().setUsuario(paciente);
        }
        else if(requestCode==REQ_CAMBIAR_CONTRASENA && resultCode==Activity.RESULT_OK){
            Toast.makeText(this,"Contraseña cambiada", Toast.LENGTH_LONG).show();
        }
        else if(requestCode==REQ_MODIFICAR_DATOS && resultCode==Activity.RESULT_CANCELED){
            Toast.makeText(this,"Error de edicion ", Toast.LENGTH_LONG).show();
        }
        else if(requestCode==REQ_CAMBIAR_CONTRASENA && resultCode==Activity.RESULT_CANCELED){
            Toast.makeText(this,"Error modificando contraseña", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void procesarRespuestaJSONObject(JSONObject response, int request) {
        if(request==REQ_ELIMINAR_PACIENTE) {
            try {
                String estado = response.getString("estado");
                if(estado.equalsIgnoreCase("OK")) {
                    Sesion.getInstance().setUsuario(null); // quitar sesion actual
                    //quitar el recuendo de password
                    SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.preferencias), MODE_PRIVATE).edit();
                    editor.remove("dni");
                    editor.remove("password");
                    editor.apply();

                    Toast.makeText(this,"Se ha eliminado tu cuenta", Toast.LENGTH_LONG).show();
                    // vuelta al login
                    Intent i = new Intent(this,PrincipalActivity.class);
                    startActivity(i);
                }
            } catch (JSONException e) {
                Log.d("XXX","Error: " + e.getMessage());
            }
        }
    }

    @Override
    public void procesarRespuestaJSONOArray(JSONArray response, int request) {

    }
}
