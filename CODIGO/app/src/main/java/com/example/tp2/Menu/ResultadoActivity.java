package com.example.tp2.Menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tp2.R;

public class ResultadoActivity extends AppCompatActivity {

    // Objetos de la GUI
    private TextView labelTipoResultado;
    private Button buttonVolver;

    private String accion;
    private ConectorBDResultados connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        this.labelTipoResultado = findViewById(R.id.labelTipoResultado);
        this.buttonVolver = findViewById(R.id.buttonVolver);

        this.buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.accion = getIntent().getExtras().getString("accion");
        if(this.accion.equals(MenuActivity.MEJOR_RESULTADO)) {
            this.labelTipoResultado.setText("Este es el mejor resultado obtenido jamas");
        } else {
            this.labelTipoResultado.setText("Este es el ultimo resultado obtenido");
        }

        this.connector = new ConectorBDResultados(getSharedPreferences(ConectorBDResultados.NOMBRE_BD, Context.MODE_PRIVATE));
    }
}
