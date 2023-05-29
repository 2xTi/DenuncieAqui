package com.example.trabalhofinalpdm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class telaInicial extends AppCompatActivity {

    TextView txtNome, txtEmail;
    private String userID;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        txtNome = findViewById(R.id.textInfo);
        txtEmail = findViewById(R.id.textInfoEmail);

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            userID = currentUser.getUid();
            String email = currentUser.getEmail();
            DocumentReference userRef = firebaseFirestore.collection("usuarios").document(userID);

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String nome = document.getString("nome");
                            txtNome.setText("Nome: " + nome);
                            txtEmail.setText("Email: " + email);
                        } else {
                            Toast.makeText(telaInicial.this, "Documento do usuário não encontrado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(telaInicial.this, "Falha ao obter dados do usuário", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // O usuário não está autenticado, redirecione para a tela de login
            startActivity(new Intent(telaInicial.this, MainActivity.class));
            finish();
        }

        ImageView imageViewHome = findViewById(R.id.imageview_home);
        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação a ser executada quando a ImageView for clicada, por exemplo:
                Intent intent = new Intent(telaInicial.this, telaInicial.class);
                startActivity(intent);
            }
        });

        LinearLayout containerD = findViewById(R.id.containerDenuncia);
        containerD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação a ser executada quando o container for clicado, por exemplo:
                Intent intent = new Intent(telaInicial.this, denunciaAnonima.class);
                startActivity(intent);
            }
        });

        LinearLayout containerA = findViewById(R.id.containerAcompanhamento);
        containerA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação a ser executada quando o container for clicado, por exemplo:
                Intent intent = new Intent(telaInicial.this, acompanharDenuncia.class);
                startActivity(intent);
            }
        });

        LinearLayout containerS = findViewById(R.id.containerSuporte);
        containerS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação a ser executada quando o container for clicado, por exemplo:
                Intent intent = new Intent(telaInicial.this, canalSuporte.class);
                startActivity(intent);
            }
        });

        ImageView imageViewGear = findViewById(R.id.imageview_gear);
        imageViewGear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação a ser executada quando a ImageView for clicada, por exemplo:
                Intent intent = new Intent(telaInicial.this, Config.class);
                startActivity(intent);
            }
        });
    }
}