package com.example.trabalhofinalpdm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia_anonima);

        editTextDenuncia = findViewById(R.id.editTextDenuncia);
        checkboxIdentify = findViewById(R.id.checkbox_identify);
        buttonEnviar = findViewById(R.id.buttonEnviar);

        // Cria o banco de dados SQLite
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        verificarDenunciasLocais();

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

        boolean isOnline = isOnline();

        if (isOnline) {
            enviarDenunciaFirestore(userID, denuncia, identify);
        } else {
            salvarDenunciaLocal(userID, identify, denuncia);
        }
    }

    private void enviarDenunciaFirestore(String userID, String denuncia, boolean identify) {
        // Crie um novo documento para a denúncia dentro da coleção "usuarios"
        DocumentReference denunciaRef = firebaseFirestore.collection("usuarios")
                .document(userID)
                .collection("denuncias")
                .document();

        // Crie um mapa com os dados da denúncia
        Map<String, Object> denunciaData = new HashMap<>();
        denunciaData.put("denuncia", denuncia);
        denunciaData.put("identify", identify);

        // Salve os dados da denúncia no Firestore
        denunciaRef.set(denunciaData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(denunciaAnonima.this, "Denúncia enviada com sucesso", Toast.LENGTH_SHORT).show();
                        excluirDenunciaLocal(userID, denuncia);
                        finish(); // Feche a tela de denúncia após o envio
                    } else {
                        Toast.makeText(denunciaAnonima.this, "Falha ao enviar a denúncia", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void salvarDenunciaLocal(String userID, boolean identify, String denuncia) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.DenunciaEntry.COLUMN_USER_ID, userID);
        values.put(DBContract.DenunciaEntry.COLUMN_IDENTIFY, identify ? 1 : 0);
        values.put(DBContract.DenunciaEntry.COLUMN_DENUNCIA, denuncia);

        long newRowId = db.insert(DBContract.DenunciaEntry.TABLE_NAME, null, values);


        if (newRowId == -1) {
            // Ocorreu um erro ao inserir a denúncia no banco de dados local
            Toast.makeText(this, "Falha ao salvar a denúncia localmente", Toast.LENGTH_SHORT).show();
        } else {
            // A denúncia foi salva com sucesso no banco de dados local
            Toast.makeText(this, "Denúncia salva localmente", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void verificarDenunciasLocais() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DBContract.DenunciaEntry.COLUMN_USER_ID,
                DBContract.DenunciaEntry.COLUMN_DENUNCIA,
                DBContract.DenunciaEntry.COLUMN_IDENTIFY
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

        if (cursor.getCount() > 0) {
            // Existem denúncias salvas localmente
            Toast.makeText(this, "Existem denúncias salvas localmente", Toast.LENGTH_SHORT).show();
        }

        while (cursor.moveToNext()) {
            String userID = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.DenunciaEntry.COLUMN_USER_ID));
            String denuncia = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.DenunciaEntry.COLUMN_DENUNCIA));
            int identifyInt = cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.DenunciaEntry.COLUMN_IDENTIFY));
            boolean identify = identifyInt == 1;

            enviarDenunciaFirestore(userID, denuncia, identify);
        }

        cursor.close();
        db.close(); // Feche o banco de dados após a consulta
    }



    private void excluirDenunciaLocal(String userID, String denuncia) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DBContract.DenunciaEntry.COLUMN_USER_ID + " = ? AND " +
                DBContract.DenunciaEntry.COLUMN_DENUNCIA + " = ?";
        String[] selectionArgs = {userID, denuncia};
        db.delete(DBContract.DenunciaEntry.TABLE_NAME, selection, selectionArgs);
    }


    private String getCurrentUserID() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        }
        return null;
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
