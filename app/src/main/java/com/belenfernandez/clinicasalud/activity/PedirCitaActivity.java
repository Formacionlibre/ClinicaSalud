package com.belenfernandez.clinicasalud.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.belenfernandez.clinicasalud.R;
import com.belenfernandez.clinicasalud.controlador.Comunicacion;
import com.belenfernandez.clinicasalud.controlador.Sesion;
import com.belenfernandez.clinicasalud.interfaces.ManejadorRespuestaJSON;
import com.belenfernandez.clinicasalud.modelo.Especialista;
import com.belenfernandez.clinicasalud.modelo.Paciente;
import com.belenfernandez.clinicasalud.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class PedirCitaActivity extends AppCompatActivity implements ManejadorRespuestaJSON {

    private static final int REQ_LISTA_ESP = 1;
    private static final int REQ_VER_DISPONIBILIDAD = 2;

    private Spinner spEspecialista, spEspecialidad;
    private List<Especialista> especialistas;
    private EditText txtDesde, txtHasta;
    private ImageView ivDesde, ivHasta;

    private DatePickerDialog datePickertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedir_cita);

        // nada mas entrar, precargar spinners
        spEspecialidad = (Spinner) findViewById(R.id.spEspecialidad);
        spEspecialista = (Spinner) findViewById(R.id.spEspecialista);
        txtDesde = (EditText) findViewById(R.id.txtDesde);
        txtHasta = (EditText) findViewById(R.id.txtHasta);
        ivDesde = (ImageView) findViewById(R.id.ivDesde);
        ivHasta = (ImageView) findViewById(R.id.ivHasta);

        ivDesde.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){
                Calendar calendar = Calendar.getInstance();
                datePickertDialog = new DatePickerDialog(PedirCitaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtDesde.setText(dayOfMonth + "-" + (month+1) +  "-" + year);
                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickertDialog.show();

            }
        });

        ivHasta.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){
                Calendar calendar = Calendar.getInstance();
                datePickertDialog = new DatePickerDialog(PedirCitaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtHasta.setText(dayOfMonth + "-" + (month+1) +  "-" + year);
                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickertDialog.show();

            }
        });

        solicitarEspecialistas();
    }

    private void rellenarEspecialistasPorEspecialidad(String especialidad) {

        List<Especialista> especialistasEspecialidad = new ArrayList<>();
        for(Especialista e : especialistas) {
            if(e.getEspecialidad().equalsIgnoreCase(especialidad))
                especialistasEspecialidad.add(e);
        }

        ArrayAdapter<Especialista> adapterEspecialistas = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,especialistasEspecialidad);
        spEspecialista.setAdapter(adapterEspecialistas);
    }

    public void verDisponibilidad(View v) {

        if(txtDesde.getText().toString().isEmpty() || txtHasta.getText().toString().isEmpty())
        {
            Toast.makeText(this,"Rellena las fechas",Toast.LENGTH_LONG).show();
            return;
        }

        Especialista especialista = (Especialista) spEspecialista.getSelectedItem();
        String fechaInicio = txtDesde.getText().toString();
        String fechaFinal = txtHasta.getText().toString();


        Intent i = new Intent(this, ElegirCitaActivity.class);
        i.putExtra("especialista",especialista);
        i.putExtra("paciente",Sesion.getInstance().getUsuario());
        i.putExtra("fechaInicio", fechaInicio);
        i.putExtra("fechaFinal",fechaFinal);
        startActivityForResult(i,REQ_VER_DISPONIBILIDAD);
    }

    public void solicitarEspecialistas() {

        String url = getString(R.string.url_servidor);
        Log.d("XXX","Pido URL " + url);

        try {
            //creo el JSON que voy a pedir
            JSONObject json = new JSONObject();
            json.put("operacion", "lista_especialistas");
            JSONObject datos = new JSONObject();
            json.put("datos", datos);
        //ejecuto la peticion y a la vez le digo que una vez tenga la respuesta tiene que manejar la respuesta el cuarto parametro que es this
            Comunicacion.solicitudJSONArray(this,url, json,this, REQ_LISTA_ESP);
        } catch (JSONException js) {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQ_VER_DISPONIBILIDAD) {
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

        if(request==REQ_LISTA_ESP) {

            especialistas = Especialista.listaEspecialistaFromJSON(response);
            Collections.sort(especialistas);

            // cargar datos en ambos
            List<String> especialidades = Util.obtenerEspecialidadesDiferentes(especialistas);
            ArrayAdapter<String> adapterEspecialidades = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, especialidades);
            spEspecialidad.setAdapter(adapterEspecialidades);
            spEspecialidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (especialistas != null) {
                        String elegida = (String) parent.getItemAtPosition(position);
                        rellenarEspecialistasPorEspecialidad(elegida);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

    }
}
