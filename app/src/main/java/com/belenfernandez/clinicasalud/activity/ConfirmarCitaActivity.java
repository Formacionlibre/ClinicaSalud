package com.belenfernandez.clinicasalud.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.belenfernandez.clinicasalud.R;
import com.belenfernandez.clinicasalud.controlador.Comunicacion;
import com.belenfernandez.clinicasalud.controlador.Sesion;
import com.belenfernandez.clinicasalud.interfaces.ManejadorRespuestaJSON;
import com.belenfernandez.clinicasalud.modelo.Cita;
import com.belenfernandez.clinicasalud.modelo.Especialista;
import com.belenfernandez.clinicasalud.modelo.Paciente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmarCitaActivity extends AppCompatActivity implements ManejadorRespuestaJSON {

    private static final int REQ_CONFIRMAR_CITA = 1;

    private TextView tvEspecialista, tvFecha, tvHora;

    private Especialista especialista;
    private Paciente paciente;
    private String hueco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_cita);

        this.hueco = (String) getIntent().getStringExtra("hueco");
        this.especialista = (Especialista) getIntent().getSerializableExtra("especialista");
        this.paciente = (Paciente) getIntent().getSerializableExtra("paciente");

        this.tvEspecialista = (TextView) findViewById(R.id.tvEspecialista);
        this.tvEspecialista.setText(this.especialista.getNombre()+" "+this.especialista.getApellidos());
        this.tvFecha = (TextView) findViewById(R.id.tvFecha);
        this.tvFecha.setText(hueco.split(" ")[0]);
        this.tvHora = (TextView) findViewById(R.id.tvHora);
        this.tvHora.setText(hueco.split(" ")[1]);
    }

    private void confirmarCita(String fecha, String hora, Paciente paciente, Especialista especialista) {

        String url = getString(R.string.url_servidor);
        Log.d("XXX", "Pido URL " + url);

        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "confirmar_cita");
            JSONObject datos = new JSONObject();
            datos.put("id_especialista", especialista.getId());
            datos.put("id_paciente", paciente.getId());
            datos.put("fecha", fecha);
            datos.put("hora", hora);
            datos.put("id_mutua", paciente.getMutua().getId());
            json.put("datos", datos);

            Log.d("XXX", "Envio a servidor: " + json.toString());

            Comunicacion.solicitudJSONObject(this, url, json, this, REQ_CONFIRMAR_CITA);
        } catch (JSONException js) {

        }

    }

    public void confirmar(View v) {

        String[] datos = hueco.split(" ");

        confirmarCita(datos[0], datos[1], paciente, especialista);

    }

    @Override
    public void procesarRespuestaJSONObject(JSONObject response, int request) {
        // volver o algo??
        Log.d("XXX", "Cita realizada: " + response.toString());
        String estado = "ERR";

        try {
            estado = response.getString("estado");
        } catch (JSONException js) {
            Log.d("XXX", "Error json confirmar cita: " + response.toString());
        }

        if (estado.equalsIgnoreCase("OK")) {
            try {
                Cita c = Cita.citaFromJSON(response.getJSONObject("datos"));
                String mensaje = c.toString() + "\n\n" + " Se ha enviado un email con los detalles de la cita";
                mostrarDialogo("Cita confirmada", mensaje);
            } catch (JSONException js) {
                Log.d("XXX", "Error json confirmar cita: " + response.toString());
            }
        }
        else {
            mostrarDialogo("Error", "Hubo un error al confirmar la cita");
        }
    }

    @Override
    public void procesarRespuestaJSONOArray(JSONArray response, int request) {

    }

    public void mostrarDialogo(String titulo, String mensaje) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);

        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
