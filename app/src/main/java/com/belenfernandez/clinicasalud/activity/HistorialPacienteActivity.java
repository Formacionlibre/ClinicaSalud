package com.belenfernandez.clinicasalud.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.belenfernandez.clinicasalud.R;
import com.belenfernandez.clinicasalud.adaptador.CitaAdapter;
import com.belenfernandez.clinicasalud.adaptador.CitaPacienteAdapter;
import com.belenfernandez.clinicasalud.controlador.Comunicacion;
import com.belenfernandez.clinicasalud.controlador.Sesion;
import com.belenfernandez.clinicasalud.interfaces.ManejadorRespuestaJSON;
import com.belenfernandez.clinicasalud.modelo.Cita;
import com.belenfernandez.clinicasalud.modelo.Paciente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistorialPacienteActivity extends AppCompatActivity implements ManejadorRespuestaJSON {

    public static final int REQ_START_ACTIVITY_FICHA = 1;
    public static final int REQ_HISTORIAL = 2;
    public static final int REQ_ANULAR_CITA = 3;

    private Paciente paciente;

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Cita> citas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_paciente);

        // si hay un extra llamado paciente, vengo por la parte del especialista
        if(getIntent().hasExtra("paciente")) {
            this.paciente = (Paciente) getIntent().getSerializableExtra("paciente");
        }
        else {
            this.paciente = (Paciente) Sesion.getInstance().getUsuario();
        }

        // obtener referencia a RecyclerView grafico
        recyclerView = (RecyclerView) findViewById(R.id.rvHistorial);
        recyclerView.setHasFixedSize(true);

        // Poner una animacion estandar para cada vez que se muestre un card
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        solicitarHistorial();
    }

    public void solicitarHistorial() {
        String url = getString(R.string.url_servidor);
        Log.d("XXX","Pido URL " + url);

        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "citas_paciente");
            JSONObject datos = new JSONObject();
            datos.put("id_paciente",this.paciente.getId());
            if(Sesion.getInstance().getUsuario() instanceof Paciente)
                datos.put("estado","COMPLETADA");
            else
                datos.put("estado","TODAS");
            json.put("datos", datos);

            Comunicacion.solicitudJSONArray(this,url, json,this, REQ_HISTORIAL);
        } catch (JSONException js) {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==Activity.RESULT_OK) {
            solicitarHistorial();
        }
    }

    @Override
    public void procesarRespuestaJSONObject(JSONObject response, int request) {

    }

    @Override
    public void procesarRespuestaJSONOArray(JSONArray response, int request) {
        if(request==REQ_HISTORIAL) {
            citas = new ArrayList<>();

            for(int i=0;i<response.length();i++) {
                try {
                    JSONObject o = response.getJSONObject(i);
                    // Para cada objeto del jsonarray, lo convierto en un especialista y lo anado al arraylist
                    // de especialistas
                    citas.add(Cita.citaFromJSON(o));
                } catch (JSONException js) {
                    Log.d("XXX",js.getMessage());
                }
            }

            // Dependiendo de si entro como especialista o como paciente, hay un tipo u otro de cardview
            if(Sesion.getInstance().getUsuario() instanceof Paciente) {
                adapter = new CitaPacienteAdapter(null, this, citas);
            }
            else {
                adapter = new CitaAdapter(null, this, citas);
            }
            recyclerView.setAdapter(adapter);
        }
    }
}
