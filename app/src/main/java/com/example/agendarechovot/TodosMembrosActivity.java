package com.example.agendarechovot;

import android.app.SearchableInfo;
import android.content.ClipData;
import android.media.RouteListingPreference;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendarechovot.ModelsAdapter.AdapterTodosMembros;
import com.example.agendarechovot.ModelsAdapter.MemberModel;
import com.example.agendarechovot.ModelsAdapter.VisitModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TodosMembrosActivity extends AppCompatActivity {

    RecyclerView recAllMember;
    SearchView searchView;
    AdapterTodosMembros adapterTodosMembros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_member);
        Objects.requireNonNull(getSupportActionBar()).hide();

        searchView = findViewById(R.id.search_member);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        recAllMember = findViewById(R.id.all_members);
        recAllMember.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<MemberModel> options =
                new FirebaseRecyclerOptions.Builder<MemberModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Usuarios"), MemberModel.class)
                        .build();

        adapterTodosMembros = new AdapterTodosMembros(options);
        recAllMember.setAdapter(adapterTodosMembros);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterTodosMembros.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterTodosMembros.startListening();
    }
}