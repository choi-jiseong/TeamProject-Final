package com.example.teamproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfessorStatusAdapter extends RecyclerView.Adapter<ProfessorStatusAdapter.ViewHolder> {

    Context context;
    List<ProfessorStatusModel> status_lilst;

    public ProfessorStatusAdapter(Context context, List<ProfessorStatusModel> status_lilst) {
        this.context = context;
        this.status_lilst = status_lilst;
    }

    @NonNull
    @Override
    public ProfessorStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_professor_status_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfessorStatusAdapter.ViewHolder holder, int position) {
        if (status_lilst != null && status_lilst.size() > 0 ) {
            ProfessorStatusModel model = status_lilst.get(position);
            holder.id_tv.setText(model.getId());
            holder.name_tv.setText(model.getName());
            holder.status_tv.setText(model.getStatus());

        } else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return status_lilst.size();
    }



    public class ViewHolder extends  RecyclerView.ViewHolder {
        TextView id_tv, name_tv, status_tv;
        public  ViewHolder(@NonNull View itemView) {
            super(itemView);

            id_tv = itemView.findViewById(R.id.id_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
            status_tv = itemView.findViewById(R.id.status_tv);
        }
    }
}


