package com.example.tp2.Partida;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.example.tp2.Enums.Dificultad;
import com.example.tp2.Enums.NombreColor;
import com.example.tp2.R;
import com.google.gson.Gson;

public class PartidaActivity extends AppCompatActivity {

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);

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
        String dificultad = config.getDificultad();
        if( dificultad.toString().equals(Dificultad.DIFICIL.toString())) {
            // TODO: Aca va la logica para una dificultad dificil.
        } else if (dificultad.equals(Dificultad.MEDIO.toString())) {
            // TODO: Aca va la logica para una dificultad medio.
        } else {
            // TODO: Aca va la logica para una dificultad facil.
        }
    }
}
