package com.example.trabalhofinalpdm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class acompanharDenuncia extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    private TextView textViewDenuncias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acompanhar_denuncia);

        dbHelper = new DBHelper(this);
        database = dbHelper.getReadableDatabase();

        textViewDenuncias = findViewById(R.id.textViewDenuncias);

        ImageView imageViewHome = findViewById(R.id.imageview_home);
        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(acompanharDenuncia.this, telaInicial.class);
                startActivity(intent);
            }
        });

        ImageView imageViewGear = findViewById(R.id.imageview_gear);
        imageViewGear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(acompanharDenuncia.this, Config.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isOnline = isOnline();

        if (isOnline) {
            exibirDenunciasOnline();
        } else {
            exibirDenunciasOffline();
        }
    }

    private void exibirDenunciasOnline() {
        // Realize as operações necessárias para exibir as denúncias online

        // Exemplo de exibição de denúncias:
        textViewDenuncias.setText("Todas as denúncias foram enviadas com sucesso!");
    }

    private void exibirDenunciasOffline() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DBContract.DenunciaEntry.COLUMN_DENUNCIA
        };

        Cursor cursor = db.query(
                DBContract.DenunciaEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        StringBuilder denunciasBuilder = new StringBuilder();

        while (cursor.moveToNext()) {
            String denuncia = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.DenunciaEntry.COLUMN_DENUNCIA));
            denunciasBuilder.append(denuncia).append("\n");
        }

        cursor.close();
        db.close();

        String denuncias = denunciasBuilder.toString();

        if (!denuncias.isEmpty()) {
            // Existem denúncias salvas localmente
            textViewDenuncias.setText("Denúncias offline:\n" + denuncias);
        } else {
            // Não existem denúncias salvas localmente
            textViewDenuncias.setText("Não existem denúncias offline");
        }
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Fecha o banco de dados SQLite quando a activity é destruída
        if (database != null) {
            database.close();
        }
    }
}