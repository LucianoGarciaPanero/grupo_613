package com.example.tp2.Partida;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tp2.Enums.EstadoEvento;
import com.example.tp2.Enums.TipoEvento;
import com.example.tp2.Inicio.IniciarSesionActivity;
import com.example.tp2.Menu.ConectorBDResultados;
import com.example.tp2.Menu.EventoPost;
import com.example.tp2.Menu.MenuActivity;
import com.example.tp2.Menu.ReceptorEvento;
import com.example.tp2.Menu.Resultado;
import com.example.tp2.Menu.ServicioPostEvento;
import com.example.tp2.R;
import com.google.gson.Gson;

public class FinPartidaActivity extends AppCompatActivity {

    private String token;
    private Resultado resultado;
    private ConectorBDResultados conn;

    // Objetos de la GUI
    private TextView puntos;
    private TextView tiempo;
    private TextView aceleracionMax;
    private TextView dificultad;
    private TextView labelResultado;
    private Button buttonVolver;

    // Atributos para la comunicacion
    public IntentFilter filtro;
    private ReceptorEvento receiver = new ReceptorEvento();
    private Intent intent;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(this.intent != null) {
            stopService(this.intent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void onResume(){
        super.onResume();
        configurarBroadcastReciever();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_partida);

        this.puntos = findViewById(R.id.puntos);
        this.tiempo = findViewById(R.id.tiempo);
        this.aceleracionMax = findViewById(R.id.aceleracionMax);
        this.dificultad = findViewById(R.id.dificultad);
        this.labelResultado = findViewById(R.id.labelResultado);
        this.buttonVolver = findViewById(R.id.buttonVolver);

        this.buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver();
            }
        });

        this.conn = new ConectorBDResultados(getSharedPreferences(ConectorBDResultados.NOMBRE_BD, Context.MODE_PRIVATE));

        //Recupero los datos del intent
        Intent intentIniciador = getIntent();
        this.token = intentIniciador.getExtras().getString("token");
        Gson json = new Gson();
        String jsonResultado = intentIniciador.getExtras().getString("json");
        this.resultado = json.fromJson(jsonResultado, Resultado.class);

        mostrarResultado();
        if(esNuevoRecord()) {
            this.labelResultado.setText("Has roto un nuevo record en " + resultado.getDificultad());
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.GREEN));

            // Guarda el resultado en la BD
            conn.guardarMejorResultado(resultado);

            // Mando un post de servicio para indiciar que hubo un nuevo record
            EventoPost eventoPost = new EventoPost(IniciarSesionActivity.ENV, TipoEvento.RECORD.toString(), EstadoEvento.ACTIVO.toString(),
                    "Nuevo record de tiempo");
            String jsonEvento = json.toJson(eventoPost);

            // Armo el intent y se lo mando al service
            this.intent = new Intent(FinPartidaActivity.this, ServicioPostEvento.class);
            this.intent.putExtra("json", jsonEvento);
            this.intent.putExtra("uri", MenuActivity.URI_EVENTO);
            this.intent.putExtra("accion", MenuActivity.ACCION_EVENTO);
            this.intent.putExtra("token", token);

            // Configuro el boradcast en el onResume()

            // Inicio servicio
            startService(this.intent);
        }
    }

    private boolean esNuevoRecord() {
        Resultado mejorResultado = conn.obtenerMejorResultado(resultado.getDificultad());
        if(mejorResultado == null) {
            return true;
        }
        if(mejorResultado.getTiempo() <= resultado.getTiempo()) {
            return true;
        }

        return false;
    }

    private void configurarBroadcastReciever() {
        this.filtro = new IntentFilter();
        this.filtro.addAction(MenuActivity.ACCION_EVENTO);
        this.filtro.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(this.receiver, this.filtro);
    }

    private void mostrarResultado() {
        this.puntos.setText(Integer.toString(this.resultado.getPuntos()));
        this.tiempo.setText(Float.toString(this.resultado.getTiempo()) + " seg");
        this.aceleracionMax.setText(Float.toString(this.resultado.getAcleracionMax()) + " m/seg2");
        this.dificultad.setText(this.resultado.getDificultad());
    }

    private void volver() {
        Intent intentV = new Intent(FinPartidaActivity.this, MenuActivity.class);
        intentV.putExtra("token", this.token);
        intentV.putExtra("origen", "fin_partida");
        startActivity(intentV);
        finish();
    }
}
