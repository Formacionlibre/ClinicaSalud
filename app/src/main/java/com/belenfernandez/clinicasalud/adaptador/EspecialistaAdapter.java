package com.belenfernandez.clinicasalud.adaptador;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.belenfernandez.clinicasalud.R;
import com.belenfernandez.clinicasalud.modelo.Especialista;

import java.util.ArrayList;

public class EspecialistaAdapter extends RecyclerView.Adapter<EspecialistaAdapter.EspecialistaViewHolder> {

    private ArrayList<Especialista> especialistas;


    public EspecialistaAdapter(ArrayList<Especialista> especialistas) {
        this.especialistas = especialistas;
    }

    public static class EspecialistaViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre;
        TextView tvEspecialidad;
        TextView tvApellidos;

        public EspecialistaViewHolder(View itemView) {
            super(itemView);
            this.tvNombre = (TextView) itemView.findViewById(R.id.tvNombre);
            this.tvEspecialidad = (TextView) itemView.findViewById(R.id.tvEspecialidad);
            this.tvApellidos = (TextView) itemView.findViewById(R.id.tvApellidos);
        }
    }

    @NonNull
    @Override
    public EspecialistaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout_especialista, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        EspecialistaViewHolder myViewHolder = new EspecialistaViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EspecialistaViewHolder holder, int position) {
        holder.tvNombre.setText(especialistas.get(position).getNombre());
        holder.tvEspecialidad.setText(especialistas.get(position).getEspecialidad());
        holder.tvApellidos.setText(especialistas.get(position).getApellidos());
    }

    @Override
    public int getItemCount() {
        return especialistas.size();
    }
}
