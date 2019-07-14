package com.belenfernandez.clinicasalud.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.belenfernandez.clinicasalud.R;
import com.belenfernandez.clinicasalud.controlador.Sesion;

public class PacienteActivity extends AppCompatActivity {

    public static final int REQ_PEDIR_CITA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);
    }

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

    public void clickPedirCita(View v) {
        Intent i = new Intent(this,PedirCitaActivity.class);
        startActivityForResult(i,REQ_PEDIR_CITA);
    }

    public void clickVerMisCitas(View v) {
        Intent i = new Intent(this,MisCitasActivity.class);
        startActivity(i);
    }

    public void clickVerHistorial(View v) {
        Intent i = new Intent(this,HistorialPacienteActivity.class);
        startActivity(i);
    }

    public void clickVerCuadroMedico(View v) {
        Intent i = new Intent(this,CuadroMedicoActivity.class);
        startActivity(i);
    }

    public void clickEditarDatos(View v) {
        Intent i = new Intent(this,MisDatosActivity.class);
        startActivity(i);
    }

    public void clickContacto(View v) {
        Intent i = new Intent(this,ContactoActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }
}
