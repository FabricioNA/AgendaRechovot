package com.example.agendarechovot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendarechovot.ModelsAdapter.UserModel;
import com.example.agendarechovot.ModelsAdapter.Util.ConfigBD;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText cpf_login, senha_login;
    private Button buttonLogin;
    private TextView loginRedirectText;
    UserModel usuarioLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.BlueIsrael));
        Objects.requireNonNull(getSupportActionBar()).hide();

        auth = ConfigBD.FirebaseAuthentication();
        cpf_login = findViewById(R.id.cpf_login);
        senha_login = findViewById(R.id.senha_login);
        buttonLogin = findViewById(R.id.login_btn);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        usuarioLogin = new UserModel();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cpf = cpf_login.getText().toString().trim();
                String senha = senha_login.getText().toString().trim();

                if (!cpf.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(cpf).matches()) {
                    if (!senha.isEmpty()) {
                        usuarioLogin.setCpf(cpf);
                        usuarioLogin.setSenha(senha);

                        auth = ConfigBD.FirebaseAuthentication();

                        auth.signInWithEmailAndPassword(
                                usuarioLogin.getCpf(), usuarioLogin.getSenha()
                        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Login feito com sucesso!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MenuPrincipal.class));
                                } else {
                                    String exc;

                                    try {
                                        throw Objects.requireNonNull(task.getException());
                                    } catch (FirebaseAuthInvalidUserException e) {
                                        exc = "Usuário não cadastrado!";
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        exc = "Email ou senha incorreta!";
                                    } catch (Exception e) {
                                        exc = "Erro no login: "
                                                + Objects.requireNonNull(e.getMessage());
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(LoginActivity.this, exc, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        senha_login.setError("Senha não preenchida!");
                    }
                } else if (cpf.isEmpty()) {
                    cpf_login.setError("CPF não preenchido!");
                } else {
                    cpf_login.setError("Insira CPF valido!!");
                }
            }
        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
    /*@Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAuth = auth.getCurrentUser();
        if (usuarioAuth != null){
            startActivity(new Intent(LoginActivity.this, LoginActivity.class));
        }
    }*/
}
