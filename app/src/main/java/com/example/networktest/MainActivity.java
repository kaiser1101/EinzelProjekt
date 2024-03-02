package com.example.networktest;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editTxtEingabeMatNr;
    private TextView txtViewAntwortServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        editTxtEingabeMatNr = findViewById(R.id.editTxt_eingabeMatNr);
        txtViewAntwortServer = findViewById(R.id.txtView_antwortServer);
        Button buttonAbschicken = findViewById(R.id.btn_abschicken);

        }
}
