package com.example.trabalhofinalpdm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class denunciaAnonima extends AppCompatActivity {

    private EditText editTextDenuncia;
    private CheckBox checkboxIdentify;
    private Button buttonEnviar;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia_anonima);

        editTextDenuncia = findViewById(R.id.editTextDenuncia);
        checkboxIdentify = findViewById(R.id.checkbox_identify);
        buttonEnviar = findViewById(R.id.buttonEnviar);

        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDenuncia();
            }
        });

        ImageView imageViewHome = findViewById(R.id.imageview_home);
        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(denunciaAnonima.this, telaInicial.class);
                startActivity(intent);
            }
        });

        ImageView imageViewGear = findViewById(R.id.imageview_gear);
        imageViewGear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(denunciaAnonima.this, Config.class);
                startActivity(intent);
            }
        });
    }

    private void enviarDenuncia() {
        String userID = getCurrentUserID();
        String denuncia = editTextDenuncia.getText().toString().trim();
        boolean identify = checkboxIdentify.isChecked();

        // Verifique se a denúncia não está vazia
        if (denuncia.isEmpty()) {
            Toast.makeText(this, "Por favor, digite a denúncia", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crie um novo documento para a denúncia dentro da coleção "usuarios"
        DocumentReference denunciaRef = firebaseFirestore.collection("usuarios")
                .document(userID)
                .collection("denuncias")
                .document();
        //certo agora

        // Crie um mapa com os dados da denúncia
        Map<String, Object> denunciaData = new HashMap<>();
        denunciaData.put("denuncia", denuncia);
        denunciaData.put("identify", identify);

        // Salve os dados da denúncia no Firestore
        denunciaRef.set(denunciaData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(denunciaAnonima.this, "Denúncia enviada com sucesso", Toast.LENGTH_SHORT).show();
                            finish(); // Feche a tela de denúncia após o envio
                        } else {
                            Toast.makeText(denunciaAnonima.this, "Falha ao enviar a denúncia", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String getCurrentUserID() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        }
        return null;
    }
}
