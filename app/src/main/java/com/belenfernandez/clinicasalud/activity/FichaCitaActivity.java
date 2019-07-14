package com.belenfernandez.clinicasalud.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.belenfernandez.clinicasalud.R;
import com.belenfernandez.clinicasalud.modelo.FichaCita;

public class FichaCitaActivity extends AppCompatActivity {

    private TextView tvObservaciones, tvRecomendaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_cita);

        FichaCita ficha = (FichaCita) getIntent().getSerializableExtra("ficha");

        this.tvObservaciones = (TextView) findViewById(R.id.tvObservaciones);
        this.tvRecomendaciones = (TextView) findViewById(R.id.tvRecomendaciones);
        this.tvObservaciones.setText(ficha.getObservaciones());
        this.tvRecomendaciones.setText(ficha.getRecomendaciones());
    }

    public void aceptar(View v) {
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        finish();
    }
}
