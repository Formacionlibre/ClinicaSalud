package com.belenfernandez.clinicasalud.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.belenfernandez.clinicasalud.R;
import com.belenfernandez.clinicasalud.adaptador.EspecialistaAdapter;
import com.belenfernandez.clinicasalud.controlador.Comunicacion;
import com.belenfernandez.clinicasalud.interfaces.ManejadorRespuestaJSON;
import com.belenfernandez.clinicasalud.modelo.Especialista;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CardViewActivity extends AppCompatActivity implements ManejadorRespuestaJSON {

    private static final int REQ_LISTA_ESP = 1;

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Especialista> especialistas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        // obtener referencia a RecyclerView grafico
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);

        // Poner una animacion estandar para cada vez que se muestre un card
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String url = getString(R.string.url_servidor);

        // Pedir al servidor (esto se haria realmente cuando se pulsara un boton, una opcion....)
        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "lista_especialistas");
            JSONObject datos = new JSONObject();
            json.put("datos", datos);

            // como he querido lista de especialistas, llamo a solicitud de array
            Comunicacion.solicitudJSONArray(this,url, json,this, REQ_LISTA_ESP);
        } catch (JSONException js) {
            Log.d("XXX",js.getMessage());
        }

    }

    @Override
    public void procesarRespuestaJSONObject(JSONObject response, int request) {

    }

    @Override
    public void procesarRespuestaJSONOArray(JSONArray response, int request) {
        // Aqui la respuesta me ha venido como un JSONArray
        // pero lo que necesita el adaptador es un ArrayList de especialistas
        // Debo recorrer el jsonarray y convertir cada uno de sus elementos en un especialista

        especialistas = new ArrayList<>();

        for(int i=0;i<response.length();i++) {
            try {
                JSONObject o = response.getJSONObject(i);
                // Para cada objeto del jsonarray, lo convierto en un especialista y lo anado al arraylist
                // de especialistas
                especialistas.add(Especialista.especialistaFromJSON(o));
            } catch (JSONException js) {
                Log.d("XXX",js.getMessage());
            }
        }

        // con el arraylist formado, creo el adaptador y lo asocio al recyclerview
        adapter = new EspecialistaAdapter(especialistas);
        recyclerView.setAdapter(adapter);
    }
}
