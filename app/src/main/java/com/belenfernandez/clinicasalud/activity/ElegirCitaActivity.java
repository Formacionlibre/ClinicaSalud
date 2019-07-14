package com.belenfernandez.clinicasalud.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.belenfernandez.clinicasalud.R;
import com.belenfernandez.clinicasalud.adaptador.CitaAdapter;
import com.belenfernandez.clinicasalud.controlador.Comunicacion;
import com.belenfernandez.clinicasalud.interfaces.ManejadorRespuestaJSON;
import com.belenfernandez.clinicasalud.modelo.Cita;
import com.belenfernandez.clinicasalud.modelo.Especialista;
import com.belenfernandez.clinicasalud.modelo.Paciente;
import com.belenfernandez.clinicasalud.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ElegirCitaActivity extends AppCompatActivity implements ManejadorRespuestaJSON {

    private static final int REQ_CITAS_ESP = 1;
    private static final int REQ_CONFIRMAR_CITA= 2;
    private List<String> huecos;
    private ListView lvListaHuecos;

    private Especialista especialista;
    private Paciente paciente;
    private String fechaInicio;
    private String fechaFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_cita);

        this.especialista = (Especialista) getIntent().getSerializableExtra("especialista");
        this.paciente = (Paciente) getIntent().getSerializableExtra("paciente");
        this.fechaInicio = getIntent().getStringExtra("fechaInicio");
        this.fechaFinal = getIntent().getStringExtra("fechaFinal");

        this.lvListaHuecos = (ListView) findViewById(R.id.lvListaHuecos);

        solicitarCitasEspecialista(especialista);

    }

    private void solicitarCitasEspecialista(Especialista especialista) {

        String url = getString(R.string.url_servidor);
        Log.d("XXX","Pido URL " + url);

        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "citas_especialista");
            JSONObject datos = new JSONObject();
            datos.put("id_especialista",especialista.getId());
            datos.put("tipo","TODAS");
            json.put("datos", datos);

            Log.d("XXX","Pido JSON " + json.toString());

            Comunicacion.solicitudJSONArray(this,url, json,this, REQ_CITAS_ESP);
        } catch (JSONException js) {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQ_CONFIRMAR_CITA) {
            Intent returnIntent = new Intent();
            setResult(resultCode, returnIntent);
            finish();
        }
    }

    @Override
    public void procesarRespuestaJSONObject(JSONObject response, int request) {

    }

    @Override
    public void procesarRespuestaJSONOArray(JSONArray response, int request) {
        if(request==REQ_CITAS_ESP) {

            List<Cita> citasEspecialista = new ArrayList<>();

            for(int i=0;i<response.length();i++) {
                try {
                    JSONObject o = response.getJSONObject(i);
                    // Para cada objeto del jsonarray, lo convierto en un especialista y lo anado al arraylist
                    // de especialistas
                    citasEspecialista.add(Cita.citaFromJSON(o));
                } catch (JSONException js) {
                    Log.d("XXX",js.getMessage());
                }
            }

            huecos = Util.huecosDisponibles(this.fechaInicio, this.fechaFinal, citasEspecialista);


            // con el arraylist formado, creo el adaptador y lo asocio al recyclerview

            //uso el array por defecto que ofrece android
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, huecos);
            lvListaHuecos.setAdapter(adapter);
            lvListaHuecos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String huecoElegido = (String) parent.getItemAtPosition(position);
                    Intent i = new Intent(ElegirCitaActivity.this, ConfirmarCitaActivity.class);
                    i.putExtra("especialista",especialista);
                    i.putExtra("paciente",paciente);
                    i.putExtra("hueco",huecoElegido);
                    startActivityForResult(i,REQ_CONFIRMAR_CITA);
                }
            });

          }
    }
}
