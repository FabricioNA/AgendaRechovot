package com.example.agendarechovot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.agendarechovot.ModelsAdapter.Util.ConfigBD;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MenuPrincipalActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.BlueIsrael));
        Objects.requireNonNull(getSupportActionBar()).hide();
        auth = ConfigBD.FirebaseAuthentication();

        LinearLayout todosMembros = findViewById(R.id.membros_btn);
        FloatingActionButton verQrCode = findViewById(R.id.VerQrCode);
        ImageButton verListAprov = findViewById(R.id.VerlistAprov);
        ImageButton logout = findViewById(R.id.Logout);

        verListAprov.setOnClickListener(view -> {
            Intent intent = new Intent(MenuPrincipalActivity.this, AprovVisitsActivity.class);
            startActivity(intent);
        });
        todosMembros.setOnClickListener(view -> {
            Intent intent = new Intent(MenuPrincipalActivity.this, TodosMembrosActivity.class);
            startActivity(intent);
        });
        logout.setOnClickListener(view -> {
            logout();
        });
    }
    public void logout(){
        try {
            auth.signOut();
            finish();
            Toast.makeText(MenuPrincipalActivity.this, "Logout feito com sucesso!", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}