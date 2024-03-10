package com.example.networktest;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private TextView txtAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText txtInput = findViewById(R.id.editTxt_eingabeMatNr);
        txtAnswer = findViewById(R.id.txtView_antwortServer);
        Button btnSend = findViewById(R.id.btn_abschicken);
        Button btnCalc = findViewById(R.id.btn_berechnen);

        // Abschicken
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMatrikelnummer(txtInput.getText().toString());
            }
        });

        // Berechnen
        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String transformed = calcMatNr(txtInput.getText().toString());
                txtAnswer.setText(transformed);
            }
        });
    }

    private void sendMatrikelnummer(String matNr){
        new Thread(() -> {
            try {
                Socket socket = new Socket("se2-submission.aau.at", 20080);

                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.write(matNr + "\n");
                out.flush();    //damit alle Daten gesendet werden

                String response = in.readLine(); //wartet auf eine Nachricht und blockiert bis eine empfangen wird

                runOnUiThread(() -> txtAnswer.setText(response));

                out.close();
                in.close();
                socket.close();

            } catch (Exception e) {
                runOnUiThread(() -> txtAnswer.setText("Fehler: " + e.getMessage()));
            }
        }).start();
    }


    private String calcMatNr(String number) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < number.length(); i++) {
            int digit = Character.getNumericValue(number.charAt(i));
            if ((i + 1) % 2 == 0) {
                char letter = (char) ('a' + digit);
                sb.append(letter);
            } else {
                sb.append(digit);
            }
        }
        return sb.toString();
    }
}

