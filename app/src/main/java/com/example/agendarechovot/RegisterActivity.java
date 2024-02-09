package com.example.agendarechovot;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    UserModel usuarioCad;
    private EditText nomeComp_cad, email_cad,  telephone_cad, cpf_cad, endereco_cad, cep_cad, senha_cad, senhaRe_cad;
    ImageView img_user;
    Button galeria, cadastrar_ft;
    private Uri imageUri;
    ProgressBar progressBar;
    public String key;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance()
            .getReference("Usuarios");
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

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
        Button buttonRegister = findViewById(R.id.cadastrar_btn);
        TextView loginRedirectText = findViewById(R.id.cadRedirectText);
        telephone_cad = findViewById(R.id.telefone_cadastro);
        endereco_cad = findViewById(R.id.endereco_cadastro);
        cep_cad = findViewById(R.id.cep_cadastro);
        usuarioCad = new UserModel();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = nomeComp_cad.getText().toString().trim();
                String email = email_cad.getText().toString().trim();
                String cpf = cpf_cad.getText().toString().trim();
                String telephone = telephone_cad.getText().toString().trim();
                String endereco = endereco_cad.getText().toString().trim();
                String cep = cep_cad.getText().toString().trim();
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
                if (endereco.isEmpty()) {
                    endereco_cad.setError("Endereço não prenchido");
                }
                if (cep.isEmpty()) {
                    cep_cad.setError("CEP não prenchido");
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
                    usuarioCad.setEndereco(endereco);
                    usuarioCad.setCep(cep);
                    usuarioCad.setSenha(senha);

                    showModalPhoto();
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

    private void showModalPhoto() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.modal_register_photo);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.transparent);

        galeria = (Button) dialog.findViewById(R.id.escolher_ft_btn);
        cadastrar_ft = (Button) dialog.findViewById(R.id.cadastrar_btn);
        img_user = (ImageView) dialog.findViewById(R.id.image_view_user);
        progressBar = (ProgressBar) dialog.findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");

                activityResultLauncher.launch(photoPicker);
            }
        });

        cadastrar_ft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri == null) {
                    Toast.makeText(RegisterActivity.this, "Insira uma imagem de usuario!", Toast.LENGTH_SHORT).show();
                }
                else {
                    createUserAuth(dialog);
                }
            }
        });
        dialog.show();
    }

    private void createUserAuth(Dialog dialog) {
        auth = ConfigBD.FirebaseAuthentication();
        auth.createUserWithEmailAndPassword(
                usuarioCad.getEmail(), usuarioCad.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    uploadToFirebase(imageUri);
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
                    dialog.dismiss();
                }
            }
        });

    }

    private void InsertUserToDB(String nomeCompleto, String email, String telephone, String cpf, String endereco, String cep, String foto) {
        HashMap<String, Object> lineHashmap = new HashMap<>();
        lineHashmap.put("USERNAME", nomeCompleto);
        lineHashmap.put("EMAIL", email);
        lineHashmap.put("TELEFONE", telephone);
        lineHashmap.put("CPF", cpf);
        lineHashmap.put("ENDERECO", endereco);
        lineHashmap.put("CEP", cep);
        lineHashmap.put("FOTO", foto);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference lineRef = database.getReference("Usuarios");

        key = lineRef.push().getKey();

        assert key != null;
        lineRef.child(key).setValue(lineHashmap).addOnCompleteListener(task -> {
            //Toast.makeText(getApplicationContext(), "Usuario adicionado!", Toast.LENGTH_SHORT).show();
            nomeComp_cad.getText().clear();
            email_cad.getText().clear();
            cpf_cad.getText().clear();
            telephone_cad.getText().clear();
            endereco_cad.getText().clear();
            cep_cad.getText().clear();
            senha_cad.getText().clear();
            senhaRe_cad.getText().clear();
        });
    }

    private void uploadToFirebase(Uri uri) {
        final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." +
                getFileExtension(uri));
        imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        InsertUserToDB(usuarioCad.getNomeCompleto(),usuarioCad.getEmail(),usuarioCad.getTelephone(),
                                usuarioCad.getCpf(), usuarioCad.getEndereco(), usuarioCad.getCep(), uri.toString());
                        progressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(RegisterActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(RegisterActivity.this, "Não foi possivel fazer o cadastro!", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setMax(Math.toIntExact(snapshot.getTotalByteCount()));
                progressBar.setProgress(Math.toIntExact(snapshot.getBytesTransferred()));
            }
        });
    }
    private String getFileExtension(Uri fileUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        imageUri = data.getData();
                        img_user.setImageURI(imageUri);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Nenhuma imagem selecionada!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


}