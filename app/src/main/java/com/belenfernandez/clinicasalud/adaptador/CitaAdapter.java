package com.belenfernandez.clinicasalud.adaptador;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.belenfernandez.clinicasalud.R;
import com.belenfernandez.clinicasalud.activity.AgendaEspecialistaActivity;
import com.belenfernandez.clinicasalud.activity.CitaActivity;
import com.belenfernandez.clinicasalud.activity.CompletarCitaActivity;
import com.belenfernandez.clinicasalud.activity.FichaCitaActivity;
import com.belenfernandez.clinicasalud.activity.HistorialPacienteActivity;
import com.belenfernandez.clinicasalud.controlador.Comunicacion;
import com.belenfernandez.clinicasalud.interfaces.ManejadorRespuestaJSON;
import com.belenfernandez.clinicasalud.modelo.Cita;
import com.belenfernandez.clinicasalud.modelo.FichaCita;
import com.belenfernandez.clinicasalud.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CitaAdapter extends RecyclerView.Adapter<CitaAdapter.CitaViewHolder> {
    private AgendaEspecialistaActivity context;
    private HistorialPacienteActivity context2;
    private ArrayList<Cita> citas;

    public CitaAdapter(AgendaEspecialistaActivity context, HistorialPacienteActivity context2, ArrayList<Cita> citas) {

        this.citas = citas;
        this.context = context;
        this.context2 = context2;
    }

    public static class CitaViewHolder extends RecyclerView.ViewHolder {

        TextView tvFechaNombre;
        ImageView btnVerCita, btnEliminarCita, btnEditarCita;

        public CitaViewHolder(View itemView) {
            super(itemView);
            this.tvFechaNombre = (TextView) itemView.findViewById(R.id.tvFechaNombre);
            this.btnVerCita = (ImageView) itemView.findViewById(R.id.btnVerCita);
            this.btnEditarCita = (ImageView) itemView.findViewById(R.id.btnEditarCita);
            this.btnEliminarCita = (ImageView) itemView.findViewById(R.id.btnEliminarCita);
        }
    }

    @NonNull
    @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout_cita, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        CitaViewHolder myViewHolder = new CitaViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CitaViewHolder holder, final int position ) {
        String fecha = Util.dateToString(citas.get(position).getFecha());
        String nombre = citas.get(position).getPaciente().getNombre();
        String apellidos = citas.get(position).getPaciente().getApellidos();

        String cadena = nombre + " " + apellidos + " " +fecha;

        holder.tvFechaNombre.setText(cadena);

        if(citas.get(position).getEstado().equalsIgnoreCase("COMPLETADA")) {
            holder.btnVerCita.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verInformeCita(citas.get(position).getFicha());
                }
            });
        }
        else
            holder.btnVerCita.setVisibility(View.INVISIBLE);

        if(citas.get(position).getEstado().equalsIgnoreCase("PENDIENTE")) {
            holder.btnEditarCita.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(context!=null) {
                        Intent intent = new Intent(context, CompletarCitaActivity.class);
                        intent.putExtra("cita", citas.get(position));
                        context.startActivityForResult(intent, AgendaEspecialistaActivity.REQ_COMPLETAR_CITA);
                    }
                    else {
                        Intent intent = new Intent(context2, CompletarCitaActivity.class);
                        intent.putExtra("cita", citas.get(position));
                        context2.startActivityForResult(intent, AgendaEspecialistaActivity.REQ_COMPLETAR_CITA);
                    }
                }
            });
        }
        else
            holder.btnEditarCita.setVisibility(View.INVISIBLE);

        if(citas.get(position).getEstado().equalsIgnoreCase("PENDIENTE")) {
            holder.btnEliminarCita.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    anularCita(citas.get(position).getId());
                }
            });
        }
        else
            holder.btnEliminarCita.setVisibility(View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return citas.size();
    }

    private void anularCita(int idCita) {

        String url = context.getString(R.string.url_servidor);

        // Pedir al servidor (esto se haria realmente cuando se pulsara un boton, una opcion....)
        try {
            JSONObject json = new JSONObject();
            json.put("operacion", "anular_cita");
            JSONObject datos = new JSONObject();
            datos.put("id_cita",idCita);
            json.put("datos", datos);


            if(context!=null)
                Comunicacion.solicitudJSONObject(context ,url, json, context, AgendaEspecialistaActivity.REQ_ANULAR_CITA);
            else
                Comunicacion.solicitudJSONObject(context2 ,url, json, context2, HistorialPacienteActivity.REQ_ANULAR_CITA);
        } catch (JSONException js) {
            Log.d("XXX",js.getMessage());
        }

    }

    private void verInformeCita(FichaCita ficha) {
        Intent i = null;
        if(context!=null)
            i = new Intent(context, FichaCitaActivity.class);
        else
            i = new Intent(context2, FichaCitaActivity.class);
        i.putExtra("ficha", ficha);

        if(context!=null)
            context.startActivityForResult(i,HistorialPacienteActivity.REQ_START_ACTIVITY_FICHA);
        else
            context2.startActivityForResult(i,HistorialPacienteActivity.REQ_START_ACTIVITY_FICHA);

    }
}
