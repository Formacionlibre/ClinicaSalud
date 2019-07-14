package com.belenfernandez.clinicasalud.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.belenfernandez.clinicasalud.R;
import com.belenfernandez.clinicasalud.controlador.Comunicacion;
import com.belenfernandez.clinicasalud.interfaces.ManejadorRespuestaJSON;
import com.belenfernandez.clinicasalud.modelo.Cita;
import com.belenfernandez.clinicasalud.modelo.FichaCita;
import com.belenfernandez.clinicasalud.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CompletarCitaActivity extends AppCompatActivity implements ManejadorRespuestaJSON {

    public static final int REQ_COMPLETAR_CITA = 1;
    private TextView tvNombrePaciente, tvFechaHora, tvMutua;
    private EditText tvObservaciones, tvRecomendaciones;
    private Cita cita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar_cita);

        cita = (Cita) getIntent().getSerializableExtra("cita");

        tvNombrePaciente = (TextView) findViewById(R.id.tvNombrePaciente);
        tvNombrePaciente.setText(cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellidos());
        tvFechaHora = (TextView) findViewById(R.id.tvFechaHora);
        tvFechaHora.setText(Util.dateToString(cita.getFecha()));
        tvMutua = (TextView) findViewById(R.id.tvMutua);
        tvMutua.setText(cita.getMutua().getNombre());
        tvObservaciones = (EditText) findViewById(R.id.tvObservaciones);
        tvRecomendaciones = (EditText) findViewById(R.id.tvRecomendaciones);
    }

    public void completarCita(View v) {

        String observaciones = tvObservaciones.getText().toString();
        String recomendaciones = tvRecomendaciones.getText().toString();

        String url = getString(R.string.url_servidor);

        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "completar_cita");
            JSONObject datos = new JSONObject();
            datos.put("id_cita",this.cita.getId());
            datos.put("observaciones",observaciones);
            datos.put("recomendaciones",recomendaciones);
            json.put("datos", datos);

            Comunicacion.solicitudJSONObject(this, url, json, this, REQ_COMPLETAR_CITA);
        } catch (JSONException js) {

        }

    }

    @Override
    public void procesarRespuestaJSONObject(JSONObject response, int request) {
        if(request==REQ_COMPLETAR_CITA) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }
    }

    @Override
    public void procesarRespuestaJSONOArray(JSONArray response, int request) {

    }
}
