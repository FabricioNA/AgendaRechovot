package com.example.agendarechovot.ModelsAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendarechovot.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

public class AdapterTodosMembros extends FirebaseRecyclerAdapter<MemberModel, AdapterTodosMembros.myViewHolder> {

    public AdapterTodosMembros(@NonNull FirebaseRecyclerOptions<MemberModel> options) {
        super(options);
    }

    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MemberModel model) {
        holder.NomeCompleto.setText(model.getUSERNAME());
        holder.Data.setText(model.getSINCEDATE());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_all_members,parent,false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        TextView NomeCompleto, Data;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            NomeCompleto = itemView.findViewById(R.id.Nome_Completo);
            Data = itemView.findViewById(R.id.Data);
        }
    }
}