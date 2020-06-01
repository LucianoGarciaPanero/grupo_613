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
import com.example.tp2.R;
import com.google.gson.Gson;

public class MenuActivity extends AppCompatActivity {

    private String token;

    // Constantes
    public final static  String ULTIMO_RESULTADO = "ULTIMO_RESULTADO";
    public final static String MEJOR_RESULTADO = "MEJOR_RESULTADO";
    private final static String URI_EVENTO = "http://so-unlam.net.ar/api/api/event";
    private final static String ACCION_EVENTO = "com.example.tp2.intent.action.ACCION_EVENTO";

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

        Intent intentIniciador = getIntent();
        this.token = intentIniciador.getExtras().getString("token");

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

        // Configuro el boradcast para poder recibir el resultado de service
        configurarBroadcastReciever();

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
    }

    private void verPuntuacion(String accion) {
        /*
        No finalizo esta activity porque se perdería el token, y de esa forma no se podría recuperar.
         */
        Intent intentP = new Intent(MenuActivity.this, ResultadoActivity.class);
        intentP.putExtra("accion", accion);
        startActivity(intentP);
    }

    public class ReceptorEvento extends BroadcastReceiver {

        public ReceptorEvento() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // Recibo lo que me llega del intent
            Gson gson = new Gson();
            RespuestaServicioPostEvento respuestaServicioPostEvento;
            String json = intent.getStringExtra("json");

            // Si es un error termino el método
            if(json.equals(ServicioPostUsuario.ERROR)) {
                Toast.makeText(context.getApplicationContext(), "No se pudo registrar el evento", Toast.LENGTH_LONG).show();
                return;
            }

            // Si no es un error lo transformo para poder analizarlo
            respuestaServicioPostEvento = gson.fromJson(json, RespuestaServicioPostEvento.class);
            if(respuestaServicioPostEvento.getState().equals("success")) {
                Toast.makeText(context.getApplicationContext(),
                        "Tipo evento: " +  respuestaServicioPostEvento.getEvent().getType_events()
                                +"\nNro grupo: " + respuestaServicioPostEvento.getEvent().getGroup(), Toast.LENGTH_LONG).show();

            }
        }
    }
}
