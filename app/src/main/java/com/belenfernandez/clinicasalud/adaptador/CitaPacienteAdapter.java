package com.belenfernandez.clinicasalud.adaptador;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.belenfernandez.clinicasalud.R;
import com.belenfernandez.clinicasalud.activity.FichaCitaActivity;
import com.belenfernandez.clinicasalud.activity.HistorialPacienteActivity;
import com.belenfernandez.clinicasalud.activity.MisCitasActivity;
import com.belenfernandez.clinicasalud.controlador.Comunicacion;
import com.belenfernandez.clinicasalud.modelo.Cita;
import com.belenfernandez.clinicasalud.modelo.FichaCita;
import com.belenfernandez.clinicasalud.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CitaPacienteAdapter extends RecyclerView.Adapter<CitaPacienteAdapter.CitaPacienteViewHolder> {

    private MisCitasActivity citasActivity;
    private HistorialPacienteActivity historialPacienteActivity;
    private AppCompatActivity context;
    private ArrayList<Cita> citas;

    public CitaPacienteAdapter(MisCitasActivity citasActivity, HistorialPacienteActivity historialPacienteActivity, ArrayList<Cita> citas) {
        this.citasActivity = citasActivity;
        this.historialPacienteActivity = historialPacienteActivity;
        this.context = this.citasActivity!=null ? citasActivity : historialPacienteActivity;
        this.citas = citas;
    }

    public static class CitaPacienteViewHolder extends RecyclerView.ViewHolder {

        //adaptar
        TextView tvEstado, tvEspecialidad, tvDoctor, tvFecha;
        ImageView ivCancelar;

        public CitaPacienteViewHolder(View itemView) {
            super(itemView);
            this.tvEstado = (TextView) itemView.findViewById(R.id.tvEstado);
            this.tvEspecialidad = (TextView) itemView.findViewById(R.id.tvEspecialidad);
            this.tvDoctor = (TextView) itemView.findViewById(R.id.tvDoctor);
            this.tvFecha = (TextView) itemView.findViewById(R.id.tvFecha);
            this.ivCancelar = (ImageView) itemView.findViewById(R.id.ivCancelar);
        }
    }

    @NonNull
    @Override
    public CitaPacienteAdapter.CitaPacienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout_cita_paciente, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        CitaPacienteAdapter.CitaPacienteViewHolder myViewHolder = new CitaPacienteAdapter.CitaPacienteViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CitaPacienteAdapter.CitaPacienteViewHolder holder, final int position) {
        String fecha = Util.dateToString(citas.get(position).getFecha());
        String especialidad = citas.get(position).getEspecialista().getEspecialidad();
        String doctor = citas.get(position).getEspecialista().getNombre() + " " + citas.get(position).getEspecialista().getApellidos();
        String estado = citas.get(position).getEstado();

        holder.tvEspecialidad.setText(especialidad);
        holder.tvDoctor.setText(doctor);
        holder.tvFecha.setText(fecha);
        holder.tvEstado.setText(estado);

        Log.d("XXX","Posicion: " + position + " Estado: " + estado);

        Drawable imgAnular = context.getDrawable(R.drawable.cancelar);
        Drawable imgVer = context.getDrawable(R.drawable.ver);


        if(estado.equalsIgnoreCase("COMPLETADA")) {
            Log.d("XXX","Posicion: " + position + " Estado: " + estado + " y pongo imagen completada");
            holder.ivCancelar.setImageDrawable(imgVer);
            holder.ivCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // se invoca la accion de eliminar cita...
                    verInformeCita(citas.get(position).getFicha());
                }
            });
        }
        else if(estado.equalsIgnoreCase("PENDIENTE")){
            Log.d("XXX","Posicion: " + position + " Estado: " + estado + " y pongo imagen cancelar");

            holder.ivCancelar.setImageDrawable(imgAnular);
            holder.ivCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // se invoca la accion de eliminar cita...
                    anularCita(citas.get(position).getId());
                }
            });
        }
        else {
            Log.d("XXX","Posicion: " + position + " Estado: " + estado + " y anulo");
            holder.ivCancelar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return citas.size();
    }


    private void anularCita(int idCita) {

        String url = citasActivity.getString(R.string.url_servidor);

        // Creo JSON que voy a pedir al servidor (esto se haria realmente cuando se pulsara un boton, una opcion....)
        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "anular_cita");
            JSONObject datos = new JSONObject();
            datos.put("id_cita",idCita);
            json.put("datos", datos);

            // como he querido lista de especialistas, llamo a solicitudJSONObject
            //ejecuto la peticion y le digo en el cuarto argumento quien tiene que gestionar la respuesta en este caso citasActivity
            Comunicacion.solicitudJSONObject(citasActivity ,url, json,citasActivity, MisCitasActivity.REQ_ANULAR_CITA);
        } catch (JSONException js) {
            Log.d("XXX",js.getMessage());
        }

    }

    private void verInformeCita(FichaCita ficha) {

        Intent i = new Intent(context, FichaCitaActivity.class);
        i.putExtra("ficha", ficha);
        context.startActivityForResult(i,HistorialPacienteActivity.REQ_START_ACTIVITY_FICHA);

    }

}
