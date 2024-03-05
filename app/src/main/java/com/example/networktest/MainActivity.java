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
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private EditText txtInput;
    private TextView txtAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtInput = findViewById(R.id.editTxt_eingabeMatNr);
        txtAnswer = findViewById(R.id.txtView_antwortServer);
        Button btnSend = findViewById(R.id.btn_abschicken);
        Button btnCalc = findViewById(R.id.btn_berechnen);

        // OnClickListener für den Button
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMatrikelnummer(txtInput.getText().toString());
            }
        });

        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String transformed = calcMatNr(txtInput.getText().toString());
                txtAnswer.setText(transformed);
            }
        });

    }

    private void sendMatrikelnummer(String matNr) {
        new Thread(() -> {
            try (Socket socket = new Socket("se2-submission.aau.at", 20080);
                 //BufferedWriter nicht möglich
                 //BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                 PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // senden MatNr
                out.println(matNr);
                out.flush();    // damit alle Daten gesendet sind

                // Lesen der Antwort vom Server
                String response = in.readLine();

                runOnUiThread(() -> txtAnswer.setText(response));

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> txtAnswer.setText("Fehler: " + e.getMessage()));
            }
        }).start();
    }


    private String calcMatNr(String number) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < number.length(); i++) {
            if ((i + 1) % 2 == 0) {
                int digit = Character.getNumericValue(number.charAt(i));
                char letter = (char) ('a' + digit);
                sb.append(letter);
            } else {
                sb.append(Character.getNumericValue(number.charAt(i)));
            }
        }
        return sb.toString();
    }
}

