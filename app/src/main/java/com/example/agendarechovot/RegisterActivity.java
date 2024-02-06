package com.example.agendarechovot;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendarechovot.ModelsAdapter.UserModel;
import com.example.agendarechovot.ModelsAdapter.Util.ConfigBD;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    UserModel usuarioCad;
    private EditText nomeComp_cad, email_cad,  telephone_cad, cpf_cad, senha_cad, senhaRe_cad;
    private Button buttonRegister;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.BlueIsrael));
        Objects.requireNonNull(getSupportActionBar()).hide();

        nomeComp_cad = findViewById(R.id.nomeComp_cadastro);
        email_cad = findViewById(R.id.email_cadastro);
        cpf_cad = findViewById(R.id.cpf_cadastro);
        telephone_cad = findViewById(R.id.telefone_cadastro);
        senha_cad = findViewById(R.id.senha_cadastro);
        senhaRe_cad = findViewById(R.id.senhaDeNovo_cadastro);
        buttonRegister = findViewById(R.id.cadastrar_btn);
        loginRedirectText = findViewById(R.id.cadRedirectText);
        telephone_cad = findViewById(R.id.telefone_cadastro);
        usuarioCad = new UserModel();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = nomeComp_cad.getText().toString().trim();
                String email = email_cad.getText().toString().trim();
                String cpf = cpf_cad.getText().toString().trim();
                String telephone = telephone_cad.getText().toString().trim();
                String senha = senha_cad.getText().toString().trim();
                String senhaRe = senhaRe_cad.getText().toString().trim();

                if (user.isEmpty()) {
                    nomeComp_cad.setError("Nome não prenchido");
                }
                if (email.isEmpty()) {
                    email_cad.setError("Email não prenchido");
                }
                if (cpf.isEmpty()) {
                    cpf_cad.setError("CPF não prenchido");
                }
                if (telephone.isEmpty()) {
                    telephone_cad.setError("Telefone não prenchido");
                }
                if (senha.isEmpty()) {
                    senha_cad.setError("Senha não prenchido");
                }
                if (senhaRe.isEmpty()) {
                    senhaRe_cad.setError("Confirmação de senha não preenchido");
                }
                if (!senha.equals(senhaRe)) {
                    nomeComp_cad.setError("Os campos de senha não são equivalentes");
                } else {
                    usuarioCad.setNomeCompleto(user);
                    usuarioCad.setEmail(email);
                    usuarioCad.setCpf(cpf);
                    usuarioCad.setTelephone(telephone);
                    usuarioCad.setSenha(senha);

                    auth = ConfigBD.FirebaseAuthentication();

                    auth.createUserWithEmailAndPassword(
                            usuarioCad.getEmail(), usuarioCad.getSenha()
                    ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                InsertUserToDB(usuarioCad.getNomeCompleto(),usuarioCad.getEmail(),usuarioCad.getTelephone(),usuarioCad.getCpf());
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
    private void InsertUserToDB(String nomeCompleto, String email, String telephone, String cpf) {
        HashMap<String, Object> lineHashmap = new HashMap<>();
        lineHashmap.put("USERNAME", nomeCompleto);
        lineHashmap.put("EMAIL", email);
        lineHashmap.put("TELEFONE", telephone);
        lineHashmap.put("CPF", cpf);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference lineRef = database.getReference("Usuarios");

        String key = lineRef.push().getKey();
        lineHashmap.put("key", key);

        assert key != null;
        lineRef.child(key).setValue(lineHashmap).addOnCompleteListener(task -> {
            //Toast.makeText(getApplicationContext(), "Usuario adicionado!", Toast.LENGTH_SHORT).show();
            nomeComp_cad.getText().clear();
            email_cad.getText().clear();
            cpf_cad.getText().clear();
            telephone_cad.getText().clear();
            senha_cad.getText().clear();
            senhaRe_cad.getText().clear();
        });
    }
}