package com.belenfernandez.clinicasalud.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.belenfernandez.clinicasalud.R;
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

public class MisCitasActivity extends AppCompatActivity implements ManejadorRespuestaJSON {

    public static final int REQ_LISTA_CITAS = 1;
    public static final int REQ_ANULAR_CITA = 2;

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Cita> citas;

    private Paciente paciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_citas);

        this.paciente = (Paciente) Sesion.getInstance().getUsuario();

        // obtener referencia a RecyclerView grafico
        recyclerView = (RecyclerView) findViewById(R.id.rvCitasPaciente);
        recyclerView.setHasFixedSize(true);

        // Poner una animacion estandar para cada vez que se muestre un card
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        solicitarMisCitas();
    }

    private void solicitarMisCitas() {
        String url = getString(R.string.url_servidor);

        // Pedir al servidor (esto se haria realmente cuando se pulsara un boton, una opcion....)
        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "citas_paciente");
            JSONObject datos = new JSONObject();
            datos.put("id_paciente",this.paciente.getId());
            datos.put("estado","TODAS");
            json.put("datos", datos);

            // como he querido lista de especialistas, llamo a solicitud de array
            Comunicacion.solicitudJSONArray(this,url, json,this, REQ_LISTA_CITAS);
        } catch (JSONException js) {
            Log.d("XXX",js.getMessage());
        }
    }

    @Override
    public void procesarRespuestaJSONObject(JSONObject response, int request) {
        if(request==REQ_ANULAR_CITA) {
            String estado = "ERR";
            try {
                estado = response.getString("estado");
            } catch (JSONException je) {
                estado = "ERR";
            }

            if(estado.equals("OK")) {
                // recargar lista
                solicitarMisCitas();
            }
        }
    }

    @Override
    public void procesarRespuestaJSONOArray(JSONArray response, int request) {
        if(request==REQ_LISTA_CITAS) {
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

            Log.d("XXX","Lista de citas: " + citas.size());
            // con el arraylist formado, creo el adaptador y lo asocio al recyclerview
            adapter = new CitaPacienteAdapter(this, null, citas);
            recyclerView.setAdapter(adapter);
        }
    }
}
