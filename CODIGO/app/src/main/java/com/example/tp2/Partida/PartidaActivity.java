package com.example.tp2.Partida;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tp2.Enums.Dificultad;
import com.example.tp2.Enums.NombreColor;
import com.example.tp2.Menu.Resultado;
import com.example.tp2.R;
import com.google.gson.Gson;

public class PartidaActivity extends AppCompatActivity {

    private String token;
    private String dificultad;

    // Objetos de la GUI
    private Button buttonFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);

        this.buttonFin = findViewById(R.id.buttonFin);

        this.buttonFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terminarPartida();
            }
        });

        // Hago la configuración dependiendo de lo que me llego
        Gson json = new Gson();
        ConfiguracionDePartida config;

        Intent intentIniciador = getIntent();
        String jsonConfig = intentIniciador.getExtras().getString("json");
        config = json.fromJson(jsonConfig, ConfiguracionDePartida.class);

        // Recupero el token
        this.token = config.getToken();

        // Pongo el fondo de color
        String nombreColor = config.getColorFondo();
        if( nombreColor.equals(NombreColor.BLANCO.toString())) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        } else if (nombreColor.equals(NombreColor.AMARILO.toString())) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
        } else {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
        }

        // Logica para la dificultad
        this.dificultad = config.getDificultad();
        if( dificultad.toString().equals(Dificultad.DIFICIL.toString())) {
            // TODO: Aca va la logica para una dificultad dificil.
        } else if (dificultad.equals(Dificultad.MEDIO.toString())) {
            // TODO: Aca va la logica para una dificultad medio.
        } else {
            // TODO: Aca va la logica para una dificultad facil.
        }
    }

    private void terminarPartida() {
        // Hay que ver como setear estos valores
        int puntos = 0;
        float tiempo = 0;
        float aceleracionMax = 0;
        //
        Gson json = new Gson();

        Resultado resultado = new Resultado(puntos, tiempo, aceleracionMax, this.dificultad);
        String jsonResultado = json.toJson(resultado);

        // Armo el intent
        Intent intentF = new Intent(PartidaActivity.this, FinPartidaActivity.class);
        intentF.putExtra("json", jsonResultado);
        intentF.putExtra("token", this.token);

        // Inicio activity
        startActivity(intentF);

        // Cierro esta activity
        finish();
    }
}
