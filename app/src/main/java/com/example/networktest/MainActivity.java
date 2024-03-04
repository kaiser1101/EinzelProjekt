package com.example.networktest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

        buttonAbschicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMatrikelnummer();
            }
        });
    }
        private void sendMatrikelnummer() {
            new Thread(() -> {
                try (Socket socket = new Socket("se2-submission.aau.at", 20080);
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    // MatNr an Server senden
                    String matNummer = editTxtEingabeMatNr.getText().toString();
                    out.println(matNummer); // Dies sollte außerhalb von runOnUiThread geschehen

                    // Serverantwort lesen
                    String response = in.readLine();

                    // Antwort anzeigen
                    runOnUiThread(() -> txtViewAntwortServer.setText(response));

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> txtViewAntwortServer.setText("Fehler: " + e.getMessage()));
                }
            }).start();
        }
}
