package com.example.tp2.Partida;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.Surface;
import android.widget.TextView;

import com.example.tp2.Enums.Dificultad;
import com.example.tp2.Enums.EstadoEvento;
import com.example.tp2.Enums.NombreColor;
import com.example.tp2.Enums.TipoEvento;
import com.example.tp2.Inicio.IniciarSesionActivity;
import com.example.tp2.Menu.EventoPost;
import com.example.tp2.Menu.MenuActivity;
import com.example.tp2.Menu.ReceptorEvento;
import com.example.tp2.Menu.Resultado;
import com.example.tp2.Menu.ServicioPostEvento;
import com.example.tp2.R;
import com.google.gson.Gson;

import java.text.DecimalFormat;

public class PartidaActivity extends AppCompatActivity implements SensorEventListener{

    private final String                ACTION_TIMER = "com.example.tp2.intent.action.ACTION_TIMER";
    private SensorManager               mSensorManager;
    public  IntentFilter                filtro;
    private ReceptorTimer               receiver;
    private Intent                      intent;
    private TextView                    acelerometro;
    private TextView                    giroscopio;
    private TextView                    rotada;
    private TextView                    timer;
    private TextView                    puntuacionObjetivo;
    private Integer                     rotacion; //0 vertical, 1 rotado a izquierda, 2 rotado a derecha
    private TextView                    puntuacion;
    private Double[]                    valorCoordenadaAcelerometro   = new Double[2]; //pos 0 X, pos 1 Y
    private Double[]                    valorCoordenadaGiroscopio     = new Double[2]; //pos 0 X, pos 1 Y
    private int                         puntaje;
    private String                      token;
    private String                      dificultad;
    private long                        tiempoRestante;
    private boolean                     partidaFinalizada;
    private int                         puntosParaRomperLata;
    private int                         maxAceleracionAlcanzada;

    // Para la comunicación con el servicio de post evento
    private Intent intentBackground;
    private Intent intentSensores;
    private IntentFilter filtroPostServicio;
    private ReceptorEvento receiverPostServicio = new ReceptorEvento();

    DecimalFormat dosdecimales = new DecimalFormat("###.###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);

        acelerometro    = (TextView) findViewById(R.id.acelerometro);
        giroscopio      = (TextView) findViewById(R.id.giroscopio);
        rotada          = (TextView) findViewById(R.id.rotada);
        puntuacion      = (TextView) findViewById(R.id.puntuacion);
        puntuacionObjetivo      = (TextView) findViewById(R.id.puntuacionObjetivo);
        timer           = (TextView) findViewById(R.id.timer);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotacion = 0;
        valorCoordenadaAcelerometro[0] = 0.0;
        valorCoordenadaAcelerometro[1] = 0.0;
        valorCoordenadaGiroscopio[0] = 0.0;
        valorCoordenadaGiroscopio[1] = 0.0;
        maxAceleracionAlcanzada = 0;


        partidaFinalizada=false;


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
            this.puntosParaRomperLata = 2500;
        } else if (dificultad.equals(Dificultad.MEDIO.toString())) {
            this.puntosParaRomperLata = 1500;
        } else {
            this.puntosParaRomperLata = 500;
        }

        this.puntuacionObjetivo.setText(Integer.toString(this.puntosParaRomperLata));
    }

    private void configurarBroadcastReceiver(){
        filtro = new IntentFilter();
        filtro.addAction(ACTION_TIMER);
        filtro.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver,filtro);
    }

    protected void Ini_Sensor(){
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_GAME);

        // Mando un post de servicio para indiciar que hubo un acceso de la cuenta
        EventoPost eventoPost = new EventoPost(IniciarSesionActivity.ENV, TipoEvento.SENSOR.toString(), EstadoEvento.ACTIVO.toString(),
                "Se iniciaron los snesores");
        Gson json = new Gson();
        String jsonEvento = json.toJson(eventoPost);

        // Armo el intent y se lo mando al service
        this.intent = new Intent(PartidaActivity.this, ServicioPostEvento.class);
        this.intent.putExtra("json", jsonEvento);
        this.intent.putExtra("uri", MenuActivity.URI_EVENTO);
        this.intent.putExtra("accion", MenuActivity.ACCION_EVENTO);
        this.intent.putExtra("token", token);

        // Configuro el boradcast en el onResume()

        // Inicio servicio
        startService(this.intent);
    }

    private void Parar_Sensor(){
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR));
        }


    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    @Override
    public void onSensorChanged(SensorEvent event){
        String txt = "";

        synchronized (this){
            Log.d("sensor", event.sensor.getName());

            switch (event.sensor.getType()) {
                case Sensor.TYPE_ROTATION_VECTOR:
                    txt += "";
                    int rotation = getWindowManager().getDefaultDisplay().getRotation();
                    switch (rotation) {
                        case Surface.ROTATION_90:
                            txt += "rotado a izquierda";
                            rotacion = 1;
                            break;
                        case Surface.ROTATION_270:
                            txt += "rotado a derecha";
                            rotacion = 2;
                            break;
                        default:
                            txt += "vertical";
                            rotacion = 0;
                            break;
                    }

                    rotada.setText(txt);
                    break;

                case Sensor.TYPE_ACCELEROMETER:
                    txt += "\n";
                    if (rotacion == 1 || rotacion == 2 ) {
                        txt += "x: " + dosdecimales.format(event.values[0]) + " m/seg2  ";
                        txt += "y: " + dosdecimales.format(event.values[1]) + " m/seg2  ";
                        txt += "z: " + dosdecimales.format(event.values[2]) + " m/seg2 \n";
                        if( valorCoordenadaAcelerometro[0] == 0) {
                            valorCoordenadaAcelerometro[0] = (double) event.values[0];
                        }
                        else{
                            puntaje+= ((int) Math.abs(event.values[0]-valorCoordenadaAcelerometro[0]))/6;
                            valorCoordenadaAcelerometro[0] = (double) event.values[0];

                        }

                        if(event.values[0]> maxAceleracionAlcanzada){
                            maxAceleracionAlcanzada = (int) event.values[0];
                        }


                    } else {
                        txt += "x: " + dosdecimales.format(event.values[0]) + " m/seg2\t\t";
                        txt += "y: " + dosdecimales.format(event.values[1]) + " m/seg2 \n";
                        txt += "z: " + dosdecimales.format(event.values[2]) + " m/seg2 \n";
                        if( valorCoordenadaAcelerometro[1] == 0) {
                            valorCoordenadaAcelerometro[1] = (double) event.values[1];
                        }
                        else{
                            puntaje+= ((int) Math.abs(event.values[1]-valorCoordenadaAcelerometro[1]))/5;
                            valorCoordenadaAcelerometro[1] = (double) event.values[1];
                        }

                        if(event.values[1]> maxAceleracionAlcanzada){
                            maxAceleracionAlcanzada = (int) event.values[1];
                        }

                    }

                    acelerometro.setText(txt);
                    puntuacion.setText(Integer.toString(puntaje));

                    if(puntaje>= puntosParaRomperLata){
                       terminarPartida();
                   }

                    break;

                case Sensor.TYPE_GYROSCOPE:
                    txt += "";
                    if(rotacion == 1 || rotacion ==2){
                        txt += "x: " + dosdecimales.format(event.values[0]) + " deg/s  ";
                        txt += "y: " + dosdecimales.format(event.values[1]) + " deg/s  ";
                        txt += "z: " + dosdecimales.format(event.values[2]) + " deg/s \n";
                        if( valorCoordenadaGiroscopio[1] == 0) {
                            valorCoordenadaGiroscopio[1] = (double) event.values[1];
                        }
                        else{
                            puntaje+= ((int) Math.abs(event.values[1]-valorCoordenadaGiroscopio[0]))/17;
                            valorCoordenadaGiroscopio[1] = (double) event.values[1];
                        }
                    }
                    else {
                        txt += "x: " + dosdecimales.format(event.values[0]) + " deg/s\t\t";
                        txt += "y: " + dosdecimales.format(event.values[1]) + " deg/s\n";
                        txt += "z: " + dosdecimales.format(event.values[2]) + " deg/s \n";
                        if( valorCoordenadaGiroscopio[0] == 0) {
                            valorCoordenadaGiroscopio[0] = (double) event.values[0];
                        }
                        else{
                            puntaje+= ((int) Math.abs(event.values[0]-valorCoordenadaGiroscopio[0]))/17;
                            valorCoordenadaGiroscopio[0] = (double) event.values[0];
                        }

                    }
                    giroscopio.setText(txt);
                    puntuacion.setText(Integer.toString(puntaje));

                    if(puntaje >= puntosParaRomperLata){
                        terminarPartida();
                    }

                    break;
            }
            if(puntaje >= puntosParaRomperLata){
                terminarPartida();
            }
        }
    }



    @Override
    protected void onStop()
    {
        super.onStop();

        Parar_Sensor();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("puntaje",puntaje);
        editor.putLong("timer",tiempoRestante);
        editor.putInt("maxAcel",maxAceleracionAlcanzada);

        editor.apply();


    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        Parar_Sensor();

        stopService(intent);


        if(partidaFinalizada){
            SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
        }

        if (this.intentBackground != null) {
            stopService(this.intentBackground);
        }

        if (this.intentSensores != null) {
            stopService(this.intentSensores);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        Parar_Sensor();

        unregisterReceiver(receiver);
        unregisterReceiver(this.receiverPostServicio);


    }

    @Override
    protected void onRestart()
    {
        Ini_Sensor();

        super.onRestart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        Ini_Sensor();
        configurarBroadcastReceiver();

        // Configuracion del boradcast reciever para el servicio post event
        this.filtroPostServicio = new IntentFilter();
        this.filtroPostServicio.addAction(MenuActivity.ACCION_EVENTO);
        this.filtroPostServicio.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(this.receiverPostServicio, this.filtroPostServicio);
    }

    @Override
    protected void onStart(){
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        puntaje = prefs.getInt("puntaje",0);
        tiempoRestante = prefs.getLong("timer",30000);
        maxAceleracionAlcanzada = prefs.getInt("maxAcel",maxAceleracionAlcanzada);

        receiver = new ReceptorTimer();

        intent = new Intent(PartidaActivity.this, Timer.class);
        intent.putExtra("Accion",ACTION_TIMER);
        intent.putExtra("tiempoRestante",tiempoRestante);
        startService(intent);

        // Esto es para registrar el evento de ejecución en background
        EventoPost eventoPost = new EventoPost(IniciarSesionActivity.ENV, TipoEvento.BACKGROUND.toString(), EstadoEvento.ACTIVO.toString(),
                "Se relizo ejecucion en background del timer");
        Gson json = new Gson();
        String jsonEvento = json.toJson(eventoPost);

        // Creación del intent
        this.intentBackground = new Intent(PartidaActivity.this, ServicioPostEvento.class);
        this.intentBackground.putExtra("json", jsonEvento);
        this.intentBackground.putExtra("uri", MenuActivity.URI_EVENTO);
        this.intentBackground.putExtra("accion", MenuActivity.ACCION_EVENTO);
        this.intentBackground.putExtra("token", token);

        // Inicio el servicio
        startService(this.intentBackground);

    }

    public class ReceptorTimer extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String tipo = intent.getStringExtra("tipo");
            String txt = "";
            if( tipo.equals(Timer.PASO_SEG)){
                long tiempRestante = intent.getLongExtra("tiempoRestante",0);
                txt+=(tiempRestante/1000) + " segs";
                timer.setText(txt);
                setTiempoRestante(tiempRestante);
            }
            else{
                terminarPartida();
            }
        }
    }

    public void setTiempoRestante(long tiempo){
        this.tiempoRestante = tiempo;
    }

    private void terminarPartida() {

        int puntos = puntaje;
        float tiempo = 30000-tiempoRestante;
        float aceleracionMax = maxAceleracionAlcanzada;
        this.partidaFinalizada = true;

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
