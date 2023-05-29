package com.example.trabalhofinalpdm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;


public class RegisterPage extends AppCompatActivity {

    TextInputEditText editEmail, editTextPassword, editNome, editCPF;

    Button btnCadastro;

    LinearLayout logIn;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    private String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        btnCadastro = findViewById(R.id.buttonCadastro);
        editEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.senha);
        logIn = findViewById(R.id.layoutLogIn);
        editNome = findViewById(R.id.nome);
        editCPF = findViewById(R.id.cpf);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterPage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(RegisterPage.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Carregando...");
                progressDialog.show();

                String email, senha;
                email = String.valueOf(editEmail.getText());
                senha = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterPage.this, "Entre com um email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(senha)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterPage.this, "Entre com a senha", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {

                                    String nome = String.valueOf(editNome.getText());
                                    String cpf = String.valueOf(editCPF.getText());

                                    // Crie um HashMap com os dados do usuário
                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("nome", nome);
                                    userData.put("email", email);
                                    userData.put("cpf", cpf);

                                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                    if (currentUser != null) {
                                        userID = currentUser.getUid();
                                    }

                                    // Salve os dados do usuário no Firestore
                                    mFirestore.collection("usuarios")
                                            .document(userID)  // Defina o ID do documento como userID
                                            .set(userData)  // Use set() em vez de add()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(RegisterPage.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(RegisterPage.this, telaInicial.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(RegisterPage.this, "Falha ao cadastrar usuário.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(RegisterPage.this, "Falha ao criar usuário.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}