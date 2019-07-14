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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.belenfernandez.clinicasalud.R;
import com.belenfernandez.clinicasalud.adaptador.CitaAdapter;
import com.belenfernandez.clinicasalud.controlador.Comunicacion;
import com.belenfernandez.clinicasalud.controlador.Sesion;
import com.belenfernandez.clinicasalud.interfaces.ManejadorRespuestaJSON;
import com.belenfernandez.clinicasalud.modelo.Cita;
import com.belenfernandez.clinicasalud.modelo.Especialista;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AgendaEspecialistaActivity extends AppCompatActivity implements ManejadorRespuestaJSON {

    public static final int REQ_CITAS_ESP = 1;
    public static final int REQ_ANULAR_CITA = 2;
    public static final int REQ_COMPLETAR_CITA = 3;

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Cita> citas;

    private String ultimoModo = "diario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_especialista);

        // obtener referencia a RecyclerView grafico
        recyclerView = (RecyclerView) findViewById(R.id.rvCitas);
        recyclerView.setHasFixedSize(true);

        // Poner una animacion estandar para cada vez que se muestre un card
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        actualizarLista();

    }

    //añade el menu en pantalla
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_solo_cerrar_sesion, menu);
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
        return true;
    }

    private void actualizarLista() {
        if(ultimoModo.equalsIgnoreCase("diario")) {
            clickVerCitasHoy(null);
        }
        else if(ultimoModo.equalsIgnoreCase("semanal")) {
            clickVerCitasSemanal(null);
        }
        else
            clickVerCitasMensual(null);
    }

    public void clickVerCitasHoy(View v) {
        ultimoModo = "diario";

        String url = getString(R.string.url_servidor);

        Especialista e = (Especialista) Sesion.getInstance().getUsuario();

        // Pedir al servidor (esto se haria realmente cuando se pulsara un boton, una opcion....)
        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "citas_especialista");
            JSONObject datos = new JSONObject();
            datos.put("id_especialista",e.getId());
            datos.put("tipo","DIARIO");
            json.put("datos", datos);

            // como he querido lista de especialistas, llamo a solicitud de array
            Comunicacion.solicitudJSONArray(this,url, json,this,REQ_CITAS_ESP);
        } catch (JSONException js) {
            Log.d("XXX",js.getMessage());
        }
    }

    public void clickVerCitasSemanal(View v) {
        ultimoModo = "semanal";

        String url = getString(R.string.url_servidor);

        Especialista e = (Especialista) Sesion.getInstance().getUsuario();

        // Pedir al servidor (esto se haria realmente cuando se pulsara un boton, una opcion....)
        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "citas_especialista");
            JSONObject datos = new JSONObject();
            datos.put("id_especialista",e.getId());
            datos.put("tipo","SEMANAL");
            json.put("datos", datos);

            // como he querido lista de especialistas, llamo a solicitud de array
            Comunicacion.solicitudJSONArray(this,url, json,this,REQ_CITAS_ESP);
        } catch (JSONException js) {
            Log.d("XXX",js.getMessage());
        }
    }

    public void clickVerCitasMensual(View v) {
        ultimoModo = "mensual";

        String url = getString(R.string.url_servidor);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        Especialista e = (Especialista) Sesion.getInstance().getUsuario();

        // Pedir al servidor (esto se haria realmente cuando se pulsara un boton, una opcion....)
        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "citas_especialista");
            JSONObject datos = new JSONObject();
            datos.put("id_especialista",e.getId());
            datos.put("tipo","MENSUAL");
            datos.put("mes",String.valueOf(cal.get(Calendar.MONTH)+1));
            json.put("datos", datos);

            // como he querido lista de especialistas, llamo a solicitud de array
            Comunicacion.solicitudJSONArray(this,url, json,this,REQ_CITAS_ESP);
        } catch (JSONException js) {
            Log.d("XXX",js.getMessage());
        }
    }

    public void clickAnadirCita(View v) {
        Intent i = new Intent(this, AnadirCitaActivity.class);
        startActivity(i);
    }

    @Override
    public void procesarRespuestaJSONObject(JSONObject response, int request) {
        if(request == REQ_ANULAR_CITA) {
            actualizarLista();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQ_COMPLETAR_CITA && resultCode==Activity.RESULT_OK) {
            actualizarLista();
        }
    }


    @Override
    public void procesarRespuestaJSONOArray(JSONArray response, int request) {
        // Aqui la respuesta me ha venido como un JSONArray
        // pero lo que necesita el adaptador es un ArrayList de especialistas
        // Debo recorrer el jsonarray y convertir cada uno de sus elementos en un especialista

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

        // con el arraylist formado, creo el adaptador y lo asocio al recyclerview
        adapter = new CitaAdapter(this,null, citas);
        recyclerView.setAdapter(adapter);
    }
}


