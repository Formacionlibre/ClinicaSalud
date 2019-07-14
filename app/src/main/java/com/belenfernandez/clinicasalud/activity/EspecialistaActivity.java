package com.belenfernandez.clinicasalud.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.belenfernandez.clinicasalud.R;
import com.belenfernandez.clinicasalud.controlador.Sesion;

public class EspecialistaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_especialista);
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

    public void clickVerCitas(View v) {
        Intent i = new Intent(this, AgendaEspecialistaActivity.class);
        startActivity(i);
    }

    public void clickVerPacientes(View v) {
        Intent i = new Intent(this, PacientesEspecialistaActivity.class);
        startActivity(i);
    }
}
