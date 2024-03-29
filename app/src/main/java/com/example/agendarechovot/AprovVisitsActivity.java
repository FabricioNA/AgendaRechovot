package com.example.agendarechovot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agendarechovot.ModelsAdapter.AdapterAprovVisits;
import com.example.agendarechovot.ModelsAdapter.Util.ConfigBD;
import com.example.agendarechovot.ModelsAdapter.VisitModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class AprovVisitsActivity extends AppCompatActivity {
    RecyclerView recAprovVisitas;
    AdapterAprovVisits adapterAprovVisits;
    private FirebaseAuth auth;
    private EditText editTextDate, editTextCPF;
    String seaprovado = "Reprovado";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprov_visits);
        Objects.requireNonNull(getSupportActionBar()).hide();

        auth = ConfigBD.FirebaseAuthentication();
        Button agendeVisita = findViewById(R.id.AgendaVisita);
        editTextDate = findViewById(R.id.editTextDate);
        editTextCPF = findViewById(R.id.editTextCPF);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Visitas");

        populateCamps();

        editTextDate.setOnClickListener(view -> showDatePickerDialog());
        agendeVisita.setOnClickListener(view -> {
                    String cpfText = editTextCPF.getText().toString();
                    String dataText = editTextDate.getText().toString();

                    if (cpfText.isEmpty() || dataText.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "É necessário ambos campos preenchidos"
                                , Toast.LENGTH_SHORT).show();
                    }

                    InsertLineToDB(cpfText, dataText, seaprovado);
                    populateCamps();
        });

        recAprovVisitas = findViewById(R.id.listAprov);
        recAprovVisitas.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<VisitModel> options =
                new FirebaseRecyclerOptions.Builder<VisitModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Visitas"), VisitModel.class)
                        .build();

        adapterAprovVisits = new AdapterAprovVisits(options);
        recAprovVisitas.setAdapter(adapterAprovVisits);
    };

        private void populateCamps() {
            //DataAtual no EditTextDate
            editTextDate.setText(generateActualDate());
            //NumeroRandom no EditTextCPF
            editTextCPF.setText(String.valueOf(generateRandom11DigitNumber()));
        }

        private void InsertLineToDB(String cpfText, String dataText, String seaprovado) {
            HashMap<String, Object> lineHashmap = new HashMap<>();
            lineHashmap.put("CPF", cpfText);
            lineHashmap.put("DATA", dataText);
            lineHashmap.put("SEAPROVADO", seaprovado);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference lineRef = database.getReference("Visitas");

            String key = lineRef.push().getKey();
            lineHashmap.put("key", key);

            assert key != null;
            lineRef.child(key).setValue(lineHashmap).addOnCompleteListener(task -> {
                Toast.makeText(getApplicationContext(), "Visita adicionada!", Toast.LENGTH_SHORT).show();
                editTextDate.getText().clear();
                editTextCPF.getText().clear();
            });
        }

        private String generateActualDate() {
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            }
            String formattedDate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                formattedDate = dateFormat.format(currentDate);
            }
            return formattedDate;
        }

        private long generateRandom11DigitNumber() {
            // Crie um objeto Random
            Random random = new Random();

            // Gere um número aleatório de 11 dígitos
            long randomNumber = 10000000000L + (long) (random.nextFloat() * 90000000000L);

            randomNumber = (int) randomNumber;

            if (randomNumber < 0) {
                randomNumber *= -1;
            }

            return randomNumber;
        }

        private void showDatePickerDialog() {
            // Get the current date
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create a DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                        // Update the EditText with the selected date
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        editTextDate.setText(selectedDate);
                    },
                    year, month, day);

            // Show the DatePickerDialog
            datePickerDialog.show();
        }

    @Override
    protected void onStart() {
        super.onStart();
        adapterAprovVisits.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterAprovVisits.startListening();
    }

}
