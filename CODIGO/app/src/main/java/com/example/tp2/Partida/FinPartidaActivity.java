package com.example.tp2.Partida;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tp2.Menu.MenuActivity;
import com.example.tp2.Menu.Resultado;
import com.example.tp2.R;
import com.google.gson.Gson;

public class FinPartidaActivity extends AppCompatActivity {

    private String token;
    private Resultado resultado;

    // Objetos de la GUI
    private Button buttonVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_partida);

        this.buttonVolver = findViewById(R.id.buttonVolver);
        this.buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver();
            }
        });

        //Recupero los datos del intent
        Intent intentIniciador = getIntent();
        this.token = intentIniciador.getExtras().getString("token");
        Gson json = new Gson();
        String jsonResultado = intentIniciador.getExtras().getString("json");
        this.resultado = json.fromJson(jsonResultado, Resultado.class);
    }

    private void volver() {
        Intent intentV = new Intent(FinPartidaActivity.this, MenuActivity.class);
        intentV.putExtra("token", this.token);
        intentV.putExtra("origen", "fin_partida");
        startActivity(intentV);
        finish();
    }
}
