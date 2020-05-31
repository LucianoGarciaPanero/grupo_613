package com.example.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private String token;

    // Constantes
    public final static  String ULTIMO_RESULTADO = "ULTIMO_RESULTADO";
    public final static String MEJOR_RESULTADO = "MEJOR_RESULTADO";

    // Objetos de la GUI
    private Button buttonUltimosResultados;
    private Button buttonMejorResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.buttonUltimosResultados = (Button)findViewById(R.id.buttonUltimoResultado);
        this.buttonMejorResultado = (Button)findViewById(R.id.buttonMejorResultado);

        this.buttonUltimosResultados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verPuntuacion(MenuActivity.ULTIMO_RESULTADO);
            }
        });

        this.buttonMejorResultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verPuntuacion(MenuActivity.MEJOR_RESULTADO);
            }
        });

        Intent intentIniciador = getIntent();
        this.token = intentIniciador.getExtras().getString("token");
    }

    private void verPuntuacion(String accion) {
        /*
        No finalizo esta activity porque se perdería el token, y de esa forma no se podría recuperar.
         */
        Intent intentP = new Intent(MenuActivity.this, ResultadoActivity.class);
        intentP.putExtra("accion", accion);
        startActivity(intentP);
    }
}
