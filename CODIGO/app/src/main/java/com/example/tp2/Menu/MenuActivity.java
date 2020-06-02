package com.example.tp2.Menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tp2.Enums.EstadoEvento;
import com.example.tp2.Enums.TipoEvento;
import com.example.tp2.Inicio.IniciarSesionActivity;
import com.example.tp2.Inicio.ServicioPostUsuario;
import com.example.tp2.Partida.ConfigurarPartidaActivity;
import com.example.tp2.R;
import com.google.gson.Gson;

public class MenuActivity extends AppCompatActivity {

    private String token;

    // Constantes
    public final static  String ULTIMO_RESULTADO = "ULTIMO_RESULTADO";
    public final static String MEJOR_RESULTADO = "MEJOR_RESULTADO";
    public final static String URI_EVENTO = "http://so-unlam.net.ar/api/api/event";
    public final static String ACCION_EVENTO = "com.example.tp2.intent.action.ACCION_EVENTO";

    // Objetos de la GUI
    private Button buttonUltimosResultados;
    private Button buttonMejorResultado;
    private Button buttonJugar;

    // Variables para la comunicacion con el service
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
        setContentView(R.layout.activity_menu);

        this.buttonUltimosResultados = findViewById(R.id.buttonUltimoResultado);
        this.buttonMejorResultado = findViewById(R.id.buttonMejorResultado);
        this.buttonJugar = findViewById(R.id.buttonJugar);

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

        this.buttonJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configurarPartida();
            }
        });

        // Rescato el token
        Intent intentIniciador = getIntent();
        this.token = intentIniciador.getExtras().getString("token");
        String origen = intentIniciador.getExtras().getString("origen");

        // Solo si se llega desde la pantalla de inicio se va a realizar el post de acceso
        if (origen.equals("inicio")) {
            postAcceso();
        }

    }

    private void postAcceso() {
        // Mando un post de servicio para indiciar que hubo un acceso de la cuenta
        EventoPost eventoPost = new EventoPost(IniciarSesionActivity.ENV, TipoEvento.ACCESO.toString(), EstadoEvento.ACTIVO.toString(),
                "Se relizo un acceso a la cuenta");
        Gson json = new Gson();
        String jsonEvento = json.toJson(eventoPost);

        // Armo el intent y se lo mando al service
        this.intent = new Intent(MenuActivity.this, ServicioPostEvento.class);
        this.intent.putExtra("json", jsonEvento);
        this.intent.putExtra("uri", this.URI_EVENTO);
        this.intent.putExtra("accion", this.ACCION_EVENTO);
        this.intent.putExtra("token", token);

        // Configuro el boradcast en el onResume()

        // Inicio servicio
        startService(this.intent);
    }

    private void configurarBroadcastReciever() {
        this.filtro = new IntentFilter();
        this.filtro.addAction(this.ACCION_EVENTO);
        this.filtro.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(this.receiver, this.filtro);
    }

    private void configurarPartida() {
        Intent intentC = new Intent(MenuActivity.this, ConfigurarPartidaActivity.class);
        intentC.putExtra("token", token);
        startActivity(intentC);
        finish();
    }

    private void verPuntuacion(String accion) {
        /*
        No finalizo esta activity para poder llegar con la flecha para atras
         */
        Intent intentP = new Intent(MenuActivity.this, ResultadoActivity.class);
        intentP.putExtra("accion", accion);
        startActivity(intentP);
    }
}
