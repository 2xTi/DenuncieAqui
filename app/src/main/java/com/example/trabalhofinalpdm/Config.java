package com.example.trabalhofinalpdm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class Config extends AppCompatActivity {

    Button sair;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        mAuth = FirebaseAuth.getInstance();
        sair = findViewById(R.id.buttonSair);

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(Config.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        ImageView imageViewHome = findViewById(R.id.imageview_home);
        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação a ser executada quando a ImageView for clicada, por exemplo:
                Intent intent = new Intent(Config.this, telaInicial.class);
                startActivity(intent);
            }
        });

        ImageView imageViewGear = findViewById(R.id.imageview_gear);
        imageViewGear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação a ser executada quando a ImageView for clicada, por exemplo:
                Intent intent = new Intent(Config.this, Config.class);
                startActivity(intent);
            }
        });
    }
}