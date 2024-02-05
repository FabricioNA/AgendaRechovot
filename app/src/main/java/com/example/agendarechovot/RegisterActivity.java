package com.example.agendarechovot;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Window;

import com.example.agendarechovot.ModelsAdapter.UserModel;
import com.example.agendarechovot.ModelsAdapter.Util.ConfigBD;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    Window window = RegisterActivity.this.getWindow();
    private FirebaseAuth auth;
    UserModel usuarioCad;
    private EditText nomeComp_cad, cpf_cad, senha_cad;
    private Button buttonRegister;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.BlueIsrael));
        Objects.requireNonNull(getSupportActionBar()).hide();

        nomeComp_cad = findViewById(R.id.nomeComp_cadastro);
        cpf_cad = findViewById(R.id.cpf_cadastro);
        senha_cad = findViewById(R.id.senha_cadastro);
        buttonRegister = findViewById(R.id.cadastrar_btn);
        loginRedirectText = findViewById(R.id.cadRedirectText);
        usuarioCad = new UserModel();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = nomeComp_cad.getText().toString().trim();
                String cpf = cpf_cad.getText().toString().trim();
                String senha = senha_cad.getText().toString().trim();

                if (user.isEmpty()) {
                    nomeComp_cad.setError("Nome não prenchido");
                }
                if (cpf.isEmpty()) {
                    cpf_cad.setError("CPF não prenchido");
                }
                if (senha.isEmpty()) {
                    senha_cad.setError("Senha não prenchido");
                } else {
                    usuarioCad.setNomeCompleto(user);
                    usuarioCad.setCpf(cpf);
                    usuarioCad.setSenha(senha);

                    auth = ConfigBD.FirebaseAuthentication();

                    auth.createUserWithEmailAndPassword(
                            usuarioCad.getCpf(), usuarioCad.getSenha()
                    ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Cadastro feito com sucesso!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            } else {
                                String exc;

                                try {
                                    throw Objects.requireNonNull(task.getException());
                                }catch (FirebaseAuthWeakPasswordException e){
                                    exc = "Insira uma senha mais forte!";
                                }catch (FirebaseAuthInvalidCredentialsException e){
                                    exc = "Insira um email válido!";
                                }catch (FirebaseAuthUserCollisionException e){
                                    exc = "Conta existente!";
                                }catch (Exception e){
                                    exc = "Erro no cadastro: "
                                            + Objects.requireNonNull(e.getMessage());
                                    e.printStackTrace();
                                }
                                Toast.makeText(RegisterActivity.this, exc, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}