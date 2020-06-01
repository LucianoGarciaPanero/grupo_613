package com.example.tp2.Partida;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tp2.Enums.Color;
import com.example.tp2.Enums.Dificultad;
import com.example.tp2.R;
import com.google.gson.Gson;

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

        this.buttonIniciarPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarPartida();
            }
        });

        // Recupero el token
        Intent intentIniciador = getIntent();
        this.token = intentIniciador.getExtras().getString("token");
        Toast.makeText(this,token, Toast.LENGTH_LONG);

    }

    private void iniciarPartida() {
        String colorFondo;
        String dificultad;

        // Lógica para seleccionar la dificultad y el color de fondo

        if( this.comboDificultad.getSelectedItem().toString().equals(Dificultad.DIFICIL.toString())) {
            dificultad = Dificultad.DIFICIL.toString();
        } else if (this.comboDificultad.getSelectedItem().toString().equals(Dificultad.MEDIO.toString())) {
            dificultad = Dificultad.MEDIO.toString();
        } else {
            dificultad = Dificultad.FACIL.toString();
        }

        if( this.comboFondo.getSelectedItem().toString().equals(Color.BLANCO.toString())) {
            colorFondo = Color.BLANCO.toString();
        } else if (this.comboFondo.getSelectedItem().toString().equals(Color.AMARILO.toString())) {
            colorFondo = Color.AMARILO.toString();
        } else {
            colorFondo = Color.ROJO.toString();
        }

        ConfiguracionDePartida config = new ConfiguracionDePartida(dificultad, colorFondo, this.token);

        //Conversion a json
        Gson json = new Gson();
        String jsonPartida = json.toJson(config);

        //Creo el intent y lo mando
        Intent intentP = new Intent(ConfigurarPartidaActivity.this, PartidaActivity.class);
        intentP.putExtra("json", jsonPartida);
        startActivity(intentP);
        finish();
    }
}
