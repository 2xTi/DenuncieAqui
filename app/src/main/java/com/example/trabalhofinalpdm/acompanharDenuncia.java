package com.example.trabalhofinalpdm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class acompanharDenuncia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acompanhar_denuncia);

        ImageView imageViewHome = findViewById(R.id.imageview_home);
        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação a ser executada quando a ImageView for clicada, por exemplo:
                Intent intent = new Intent(acompanharDenuncia.this, telaInicial.class);
                startActivity(intent);
            }
        });

        ImageView imageViewGear = findViewById(R.id.imageview_gear);
        imageViewGear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação a ser executada quando a ImageView for clicada, por exemplo:
                Intent intent = new Intent(acompanharDenuncia.this, Config.class);
                startActivity(intent);
            }
        });
    }
}