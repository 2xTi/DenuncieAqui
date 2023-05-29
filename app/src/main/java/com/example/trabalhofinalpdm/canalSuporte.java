package com.example.trabalhofinalpdm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class canalSuporte extends AppCompatActivity {


    private String userID; // Declaração da variável userID
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canal_suporte);

        ImageView imageViewHome = findViewById(R.id.imageview_home);
        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação a ser executada quando a ImageView for clicada, por exemplo:
                Intent intent = new Intent(canalSuporte.this, telaInicial.class);
                startActivity(intent);
            }
        });

        ImageView imageViewGear = findViewById(R.id.imageview_gear);
        imageViewGear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação a ser executada quando a ImageView for clicada, por exemplo:
                Intent intent = new Intent(canalSuporte.this, Config.class);
                startActivity(intent);
            }
        });

        Spinner spinnerOptions = findViewById(R.id.selectOptions);

        String[] options = {"Opção 1", "Opção 2", "Opção 3", "Opção 4", "Opção 5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOptions.setAdapter(adapter);

        spinnerOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                // Ação a ser executada quando uma opção for selecionada no Spinner
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ação a ser executada quando nenhuma opção for selecionada no Spinner
            }
        });

        Button buttonEnviar = findViewById(R.id.buttonEnviar);
        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedOption = spinnerOptions.getSelectedItem().toString();

                // Obter a referência do documento do usuário
                DocumentReference userRef = firebaseFirestore.collection("usuarios").document(userID);

                // Criar uma subcoleção chamada "suporte" dentro do documento do usuário
                CollectionReference suporteRef = userRef.collection("suporte");

                // Criar um novo documento na subcoleção "suporte"
                suporteRef.add(new SupportOption(selectedOption))
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // Ação a ser executada quando a opção é salva com sucesso
                                Toast.makeText(canalSuporte.this, "Opção enviada com sucesso!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Ação a ser executada em caso de falha no envio da opção
                                Toast.makeText(canalSuporte.this, "Falha ao enviar a opção. Tente novamente.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    public class SupportOption {
        private String option;

        public SupportOption() {
            // Construtor vazio necessário para o Firebase Firestore
        }

        public SupportOption(String option) {
            this.option = option;
        }

        public String getOption() {
            return option;
        }

        public void setOption(String option) {
            this.option = option;
        }
    }

}



