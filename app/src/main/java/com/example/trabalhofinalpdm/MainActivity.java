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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

public class MainActivity extends AppCompatActivity {


    TextInputEditText editEmail, editTextPassword;

    Button btnLogin;

    LinearLayout cadastrar;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.buttonLogin);
        editEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.senha);
        cadastrar = findViewById(R.id.layoutCadastro);

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(MainActivity.this, telaInicial.class));
            finish();
        }

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterPage.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Carregando...");
                progressDialog.show();

                String email, senha;
                email = String.valueOf(editEmail.getText());
                senha = String.valueOf(editTextPassword.getText());

                if(TextUtils.isEmpty(email)){
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Entre com um email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(senha)){
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Entre com a senha", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Login feito com sucesso!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, telaInicial.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Email ou senha incorretos!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}