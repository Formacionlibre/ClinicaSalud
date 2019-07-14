package com.belenfernandez.clinicasalud.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.belenfernandez.clinicasalud.R;
import com.belenfernandez.clinicasalud.controlador.Comunicacion;
import com.belenfernandez.clinicasalud.controlador.Sesion;
import com.belenfernandez.clinicasalud.interfaces.ManejadorRespuestaJSON;
import com.belenfernandez.clinicasalud.modelo.Mutua;
import com.belenfernandez.clinicasalud.modelo.Paciente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatosPersonalesActivity extends AppCompatActivity implements ManejadorRespuestaJSON {

    public static final int REQ_LISTA_MUTUAS = 1;
    public static final int REQ_EDITAR_PACIENTE = 2;
    public static final int REQ_REGISTRAR_PACIENTE = 3;

    // patron para comprobar email
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private Spinner spTipoDocumento, spMutua;
    private EditText tvNombre, tvApellidos, tvValorDocumento, tvEmail, tvTelefono, tvPassword;
    private Button btnAceptar;

    private String modo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_personales);

        this.modo = getIntent().getStringExtra("modo");

        this.spTipoDocumento = (Spinner) findViewById(R.id.spTipoDocumento);
        rellenarTipoDocumento();
        this.spMutua = (Spinner) findViewById(R.id.spMutua);
        solicitarMutuas();

        this.tvNombre = (EditText) findViewById(R.id.tvNombre);
        this.tvApellidos = (EditText) findViewById(R.id.tvApellidos);
        this.tvValorDocumento = (EditText) findViewById(R.id.tvValorDocumento);
        this.tvEmail = (EditText) findViewById(R.id.tvEmail);
        this.tvTelefono = (EditText) findViewById(R.id.tvTelefono);
        this.tvPassword = (EditText) findViewById(R.id.tvPassword);
        if(this.modo.equalsIgnoreCase(getString(R.string.modo_editar)))
            this.tvPassword.setVisibility(View.INVISIBLE);

        this.btnAceptar = (Button) findViewById(R.id.btnAceptar);
    }

    public void aceptar(View v) {
        if(!validar())
        {
            Toast.makeText(this,"Rellena los campos correctamente. DNI tiene 8 digitos y un caracter en MAYUSCULA", Toast.LENGTH_LONG).show();
            return;
        }

        Paciente p = leerFormulario();
        //en el caso de editar, el paciente es el actual de la sesion
        if(this.modo.equalsIgnoreCase(getString(R.string.modo_editar)))
            editar(p);
        else if(this.modo.equalsIgnoreCase(getString(R.string.modo_registro)))
            registro(p);
    }

    private boolean validar() {
        boolean correcto = true;

        if(tvNombre.getText().toString().isEmpty())
            return false;
        if(tvApellidos.getText().toString().isEmpty())
            return false;
        if(tvEmail.getText().toString().isEmpty())
            return false;
        if(tvValorDocumento.getText().toString().isEmpty())
            return false;
        if(tvTelefono.getText().toString().isEmpty())
            return false;
        if(tvPassword.getText().toString().isEmpty())
            return false;

        String tipoDocumento = (String) spTipoDocumento.getSelectedItem();
        String valorDocumento = tvValorDocumento.getText().toString();
        String email = tvEmail.getText().toString();

        // validar dni
        if(tipoDocumento.equalsIgnoreCase("DNI")) {
            if(valorDocumento.length()!=9)
                correcto= false;

            char letra = valorDocumento.charAt(valorDocumento.length()-1);

            if(letra<'A'||letra>'Z')
                correcto = false;
        }

        //validar email
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        if(!matcher.find())
            correcto = false;


        return correcto;
    }

    private void rellenarCampos(Paciente p) {
        this.tvNombre.setText(p.getNombre());
        this.tvApellidos.setText(p.getApellidos());

        this.spTipoDocumento.setSelection(getPosicionCadenaTipoDocumento(p.getTipoDocumento()));
        this.spMutua.setSelection(getPosicionMutuaSpinner(p.getMutua()));

        this.tvValorDocumento.setText(p.getValorDocumento());
        this.tvEmail.setText(p.getEmail());
        this.tvTelefono.setText(p.getTelefono());
        this.tvPassword.setText(p.getPassword());
    }

    private int getPosicionCadenaTipoDocumento(String s) {
        int pos = -1;
        for(int i=0;i<spTipoDocumento.getAdapter().getCount();i++) {
            if(((String)spTipoDocumento.getAdapter().getItem(i)).equalsIgnoreCase(s))
                pos = i;
        }
        return pos;
    }

    private int getPosicionMutuaSpinner(Mutua m) {
        int pos = -1;
        for(int i=0;i<spMutua.getAdapter().getCount();i++) {
            if(((Mutua)spMutua.getAdapter().getItem(i)).getId()==m.getId())
                pos = i;
        }
        return pos;
    }

    private void editar(Paciente p) {

        String url = getString(R.string.url_servidor);

        int id_paciente = ((Paciente) Sesion.getInstance().getUsuario()).getId();

        // Pedir al servidor (esto se hace cuando se pulsa un boton, una opcion....)
        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "editar_paciente");
            JSONObject datos = new JSONObject();

            datos.put("id",id_paciente);
            datos.put("nombre",p.getNombre());
            datos.put("apellidos",p.getApellidos());
            datos.put("tipo_documento",p.getTipoDocumento());
            datos.put("valor_documento",p.getValorDocumento());
            datos.put("telefono",p.getTelefono());
            datos.put("email",p.getEmail());
            datos.put("sociedad_medica",p.getMutua().getId());
            datos.put("password",p.getPassword());

            json.put("datos", datos);

            // como he querido lista de especialistas, llamo a solicitud de array
            Comunicacion.solicitudJSONObject(this,url, json,this, REQ_EDITAR_PACIENTE);
        } catch (JSONException js) {
            Log.d("XXX",js.getMessage());
        }
    }

    private void registro(Paciente p ) {
        String url = getString(R.string.url_servidor);

        // Pedir al servidor (esto se haria realmente cuando se pulsara un boton, una opcion....)
        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "registro_paciente");
            JSONObject datos = new JSONObject();

            datos.put("id",p.getId());
            datos.put("nombre",p.getNombre());
            datos.put("apellidos",p.getApellidos());
            datos.put("tipo_documento",p.getTipoDocumento());
            datos.put("valor_documento",p.getValorDocumento());
            datos.put("telefono",p.getTelefono());
            datos.put("email",p.getEmail());
            datos.put("sociedad_medica",p.getMutua().getId());
            datos.put("password",p.getPassword());

            json.put("datos", datos);

            // como he querido lista de especialistas, llamo a solicitud de array
            Comunicacion.solicitudJSONObject(this,url, json,this, REQ_REGISTRAR_PACIENTE);
        } catch (JSONException js) {
            Log.d("XXX",js.getMessage());
        }
    }

    private Paciente leerFormulario() {
        String nombre = tvNombre.getText().toString();
        String apellidos = tvApellidos.getText().toString();

        String tipoDocumento = (String) spTipoDocumento.getSelectedItem();
        Mutua mutua = (Mutua) spMutua.getSelectedItem();

        String valorDocumento = tvValorDocumento.getText().toString();
        String email = tvEmail.getText().toString();
        String telefono = tvTelefono.getText().toString();
        String password = tvPassword.getText().toString();

        Paciente p = new Paciente(-1, nombre,apellidos,tipoDocumento,valorDocumento,telefono,email,mutua,password);
        return p;
    }

    public void solicitarMutuas() {
        String url = getString(R.string.url_servidor);

        // Pedir al servidor (esto se haria realmente cuando se pulsara un boton, una opcion....)
        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "lista_mutuas");
            JSONObject datos = new JSONObject();
            json.put("datos", datos);

            // como he querido lista de especialistas, llamo a solicitud de array
            Comunicacion.solicitudJSONArray(this,url, json,this, REQ_LISTA_MUTUAS);
        } catch (JSONException js) {
            Log.d("XXX",js.getMessage());
        }
    }


    private void rellenarTipoDocumento() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,getResources().getStringArray(R.array.tipos_documento));
        this.spTipoDocumento.setAdapter(adapter);
    }

    @Override
    public void procesarRespuestaJSONObject(JSONObject response, int request) {
        Intent returnIntent = new Intent();
        String estado = "ERR";
        Paciente paciente = null;
        try {
            estado = response.getString("estado");
            paciente = Paciente.pacienteFromJSON(response.getJSONObject("datos").getJSONObject("usuario"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if((request==REQ_EDITAR_PACIENTE || request==REQ_REGISTRAR_PACIENTE) && estado.equalsIgnoreCase("OK")) {
            returnIntent.putExtra("paciente",paciente);
            setResult(Activity.RESULT_OK,returnIntent);
        }
        else
        {
            returnIntent.putExtra("paciente", (Bundle) null);
            setResult(Activity.RESULT_CANCELED,returnIntent);
        }

        finish();
    }

    @Override
    public void procesarRespuestaJSONOArray(JSONArray response, int request) {
        if(request==REQ_LISTA_MUTUAS) {
            List<Mutua> mutuas = new ArrayList<>();

            for(int i=0;i<response.length();i++) {
                try {
                    JSONObject o = response.getJSONObject(i);
                    mutuas.add(Mutua.mutuaFromJSON(o));
                } catch (JSONException js) {
                    Log.d("XXX",js.getMessage());
                }
            }

            ArrayAdapter<Mutua> adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line, mutuas);
            this.spMutua.setAdapter(adapter);

            // si vengo para editar tengo que precargar ccampos
            if(this.modo.equalsIgnoreCase(getString(R.string.modo_editar))) {
                rellenarCampos((Paciente) Sesion.getInstance().getUsuario());
                btnAceptar.setText("MODIFICAR");
            }
        }
    }


}
