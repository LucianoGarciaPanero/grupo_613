package com.example.tp2.Menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tp2.R;

public class ResultadoActivity extends AppCompatActivity {

    // Objetos de la GUI
    private TextView labelTipoResultado;
    private TextView labelSeleccioneDificultad;
    private TextView puntos;
    private TextView tiempo;
    private TextView aceleracionMaxima;
    private TextView labelDificultad;
    private TextView dificultad;
    private TextView labelError;
    private Spinner comboDif;
    private Button buttonVolver;

    private String accion;
    private ConectorBDResultados connector;
    private Resultado resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        this.labelTipoResultado = findViewById(R.id.labelTipoResultado);
        this.labelSeleccioneDificultad = findViewById(R.id.labelSeleccioneDificultad);
        this.puntos = findViewById(R.id.puntos);
        this.tiempo = findViewById(R.id.tiempo);
        this.aceleracionMaxima = findViewById(R.id.aceleracionMaxima);
        this.labelDificultad = findViewById(R.id.labelDificultad);
        this.dificultad = findViewById(R.id.dificultad);
        this.labelError = findViewById(R.id.labelError);

        this.comboDif = findViewById(R.id.comboDif);
        ArrayAdapter<CharSequence> adapterDificultad = ArrayAdapter.createFromResource(this, R.array.dificultades, android.R.layout.simple_spinner_item);
        this.comboDif.setAdapter(adapterDificultad);

        this.buttonVolver = findViewById(R.id.buttonVolver);

        this.buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.comboDif.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resultado = connector.obtenerMejorResultado(comboDif.getItemAtPosition(position).toString());
                mostrarResultado(resultado,"Ops, parece que aun no has jugado una partida en esta dificultad");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        this.connector = new ConectorBDResultados(getSharedPreferences(ConectorBDResultados.NOMBRE_BD, Context.MODE_PRIVATE));
        this.accion = getIntent().getExtras().getString("accion");

        if(this.accion.equals(MenuActivity.MEJOR_RESULTADO)) {
            this.labelTipoResultado.setText("Este es el mejor resultado obtenido jamas");
            this.labelDificultad.setVisibility(View.GONE);
            this.dificultad.setVisibility(View.GONE);
        } else {
            this.labelTipoResultado.setText("Este es el ultimo resultado obtenido");
            this.labelSeleccioneDificultad.setVisibility(View.GONE);
            this.comboDif.setVisibility(View.GONE);
            this.resultado = connector.obtenerUltimoResultado();
            mostrarResultado(this.resultado,"Ops, parece que aun no has jugado una partida");
        }
    }

    private void mostrarResultado(Resultado resultado, String msjEror) {
        // Seteamos todos los label en blanco
        this.puntos.setText("");
        this.tiempo.setText("");
        this.aceleracionMaxima.setText("");
        this.dificultad.setText("");
        this.labelError.setText("");


        if (resultado != null) {
            this.puntos.setText(Integer.toString(resultado.getPuntos()));
            this.tiempo.setText(Float.toString(resultado.getTiempo()/1000) + " seg");
            this.aceleracionMaxima.setText(Float.toString(resultado.getAcleracionMax()) + " m/seg2");
            this.dificultad.setText(resultado.getDificultad());
        } else {
            this.labelError.setText(msjEror);
        }

    }

}
