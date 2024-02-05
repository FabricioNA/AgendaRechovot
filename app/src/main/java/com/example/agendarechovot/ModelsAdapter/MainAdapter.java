package com.example.agendarechovot.ModelsAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendarechovot.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<VisitModel,MainAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<VisitModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull VisitModel model) {
        holder.CPF.setText(model.getCPF());
        holder.DATA.setText(model.getDATA());
        holder.SEAPROVADO.setText(model.getSEAPROVADO());

        holder.dislikeBtn.setOnClickListener(view -> {
            //ListsDatabaseList theRemovedItem = list.get(position);
        });

        /*Glide.with(holder.img.getContext())
                .load(model.getUrl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark)
                .into(holder.img);*/
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customlistview,parent,false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView CPF, DATA, SEAPROVADO;
        ImageButton likeBtn, dislikeBtn;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            CPF = itemView.findViewById(R.id.cpf);
            DATA = itemView.findViewById(R.id.data);
            SEAPROVADO = itemView.findViewById(R.id.seAprovado);
            likeBtn = itemView.findViewById(R.id.likebtn);
            dislikeBtn = itemView.findViewById(R.id.dislikebtn);
        }

    }
}