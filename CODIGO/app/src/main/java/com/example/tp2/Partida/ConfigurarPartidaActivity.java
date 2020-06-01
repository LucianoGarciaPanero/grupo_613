package com.example.tp2.Partida;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import com.example.tp2.R;

public class ConfigurarPartidaActivity extends AppCompatActivity {

    // Objetos de la GUI
    private Spinner comboDificultad;
    private Spinner comboFondo;
    private Button buttonIniciarPartida;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_partida);

        this.buttonIniciarPartida = findViewById(R.id.buttonIniciarPartida);
        this.comboDificultad = findViewById(R.id.comboDificultad);
        this.comboFondo = findViewById(R.id.comboFondo);


    }
}
