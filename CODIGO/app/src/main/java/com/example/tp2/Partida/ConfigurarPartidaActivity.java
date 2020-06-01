package com.example.tp2.Partida;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tp2.Enums.Dificultad;
import com.example.tp2.R;

public class ConfigurarPartidaActivity extends AppCompatActivity {

    // Objetos de la GUI
    private Spinner comboDificultad;
    private Spinner comboFondo;
    private Button buttonIniciarPartida;
    private Button buttonVolver;

    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_partida);

        this.buttonIniciarPartida = findViewById(R.id.buttonIniciarPartida);
        this.buttonVolver = findViewById(R.id.buttonVolver);

        this.comboDificultad = findViewById(R.id.comboDificultad);
        ArrayAdapter<CharSequence> adapterDificultad = ArrayAdapter.createFromResource(this, R.array.dificultades, android.R.layout.simple_spinner_item);
        this.comboDificultad.setAdapter(adapterDificultad);

        this.comboFondo = findViewById(R.id.comboFondo);
        ArrayAdapter<CharSequence> adapterFondo = ArrayAdapter.createFromResource(this, R.array.colores, android.R.layout.simple_spinner_item);
        this.comboFondo.setAdapter(adapterFondo);

        this.buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Recupero el token
        Intent intentIniciador = getIntent();
        this.token = intentIniciador.getExtras().getString("token");
        Toast.makeText(this,token, Toast.LENGTH_LONG);

    }
}
