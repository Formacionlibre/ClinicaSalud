package com.belenfernandez.clinicasalud.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class AnadirCitaActivity extends AppCompatActivity implements ManejadorRespuestaJSON  {

    private static final int REQ_LISTA_PAC = 1;
    private Spinner spPaciente;
    private EditText txtDesde, txtHasta;
    private ImageView ivDesde, ivHasta;
    private DatePickerDialog datePickertDialog;
    private List<Paciente> pacientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_cita);

        spPaciente = (Spinner) findViewById(R.id.spPaciente);
        txtDesde = (EditText) findViewById(R.id.tvDesde);
        txtHasta = (EditText) findViewById(R.id.tvHasta);
        ivDesde = (ImageView) findViewById(R.id.ivDesde);
        ivHasta = (ImageView) findViewById(R.id.ivHasta);

        solicitarPacientes();

        ivDesde.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){
                Calendar calendar = Calendar.getInstance();
                datePickertDialog = new DatePickerDialog(AnadirCitaActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                datePickertDialog = new DatePickerDialog(AnadirCitaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtHasta.setText(dayOfMonth + "-" + (month+1) +  "-" + year);
                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickertDialog.show();

            }
        });
    }

    public void adelante(View v) {
        Paciente paciente = (Paciente) spPaciente.getSelectedItem();
        String fechaInicio = txtDesde.getText().toString();
        String fechaFinal = txtHasta.getText().toString();

        Intent i = new Intent(this, ElegirCitaActivity.class);
        i.putExtra("paciente",paciente);
        i.putExtra("especialista",Sesion.getInstance().getUsuario());
        i.putExtra("fechaInicio", fechaInicio);
        i.putExtra("fechaFinal",fechaFinal);
        startActivity(i);
    }

    private void solicitarPacientes() {
        String url = getString(R.string.url_servidor);
        Log.d("XXX","Pido URL " + url);

        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "lista_pacientes");
            JSONObject datos = new JSONObject();
            json.put("datos", datos);

            Comunicacion.solicitudJSONArray(this,url, json,this, REQ_LISTA_PAC);
        } catch (JSONException js) {

        }

    }

    @Override
    public void procesarRespuestaJSONObject(JSONObject response, int request) {

    }

    @Override
    public void procesarRespuestaJSONOArray(JSONArray response, int request) {
        if(request==REQ_LISTA_PAC) {

            pacientes = Paciente.listaPacienteFromJSON(response);

            ArrayAdapter<Paciente> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, pacientes);
            spPaciente.setAdapter(adapter);
        }

    }
}
