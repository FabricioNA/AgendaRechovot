package com.example.agendarechovot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.agendarechovot.ModelsAdapter.MainAdapter;
import com.example.agendarechovot.ModelsAdapter.VisitModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AprovVisits extends AppCompatActivity {
    RecyclerView recAprovVisitas;
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprov_visits);
        Objects.requireNonNull(getSupportActionBar()).hide();
        recAprovVisitas = findViewById(R.id.listAprov);
        recAprovVisitas.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<VisitModel> options =
                new FirebaseRecyclerOptions.Builder<VisitModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Visitas"), VisitModel.class)
                        .build();

        mainAdapter = new MainAdapter(options);
        recAprovVisitas.setAdapter(mainAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.startListening();
    }

}
