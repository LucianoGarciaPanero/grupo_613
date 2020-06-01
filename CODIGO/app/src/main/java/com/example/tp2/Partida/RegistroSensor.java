package com.example.tp2.Partida;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.widget.TextView;

import com.example.tp2.R;

import java.text.DecimalFormat;

public class RegistroSensor extends AppCompatActivity implements SensorEventListener{

    private final String                ACTION_TIMER = "com.example.tp2.intent.action.ACTION_TIMER";
    private SensorManager               mSensorManager;
    public  IntentFilter                filtro;
    private ReceptorTimer               receiver = new ReceptorTimer();
    private Intent                      intent;
    private TextView                    acelerometro;
    private TextView                    giroscopio;
    private TextView                    gravedad;
    private TextView                    rotada;
    private TextView                    timer;
    private Integer                     rotacion; //0 vertical, 1 rotado a izquierda, 2 rotado a derecha
    private TextView                    puntuacion;
    private Double[]                    valorCoordenada   = new Double[2]; //pos 0 X, pos 1 Y
    private int                         puntaje;


    DecimalFormat dosdecimales = new DecimalFormat("###.###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_sensor);

        acelerometro    = (TextView) findViewById(R.id.acelerometro);
        giroscopio      = (TextView) findViewById(R.id.giroscopio);
        gravedad        = (TextView) findViewById(R.id.gravedad);
        rotada          = (TextView) findViewById(R.id.rotada);
        puntuacion      = (TextView) findViewById(R.id.puntuacion);
        timer           = (TextView) findViewById(R.id.timer);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotacion = 0;
        valorCoordenada[0] = 0.0;
        valorCoordenada[1] = 0.0;
        puntaje = 0;

        intent = new Intent(RegistroSensor.this, Timer.class);
        configurarBroadcastReceiver();
        intent.putExtra("Accion",ACTION_TIMER);
        startService(intent);
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
                    txt += "Rotacion: ";
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
                    txt += "Acelerometro:\n";
                    if (rotacion == 1 || rotacion == 2 ) {
                        txt += "x: " + dosdecimales.format(event.values[0]) + " m/seg2  ";
                        txt += "y: " + dosdecimales.format(event.values[1]) + " m/seg2  ";
                        txt += "z: " + dosdecimales.format(event.values[2]) + " m/seg2 \n";
                        if( valorCoordenada[0] == 0) {
                            valorCoordenada[0] = (double) event.values[0];
                        }
                        else{
                            puntaje+= ((int) Math.abs(event.values[0]-valorCoordenada[0]))/17;
                            valorCoordenada[0] = (double) event.values[0];
                        }

                    } else {
                        txt += "x: " + dosdecimales.format(event.values[0]) + " m/seg2\t\t";
                        txt += "y: " + dosdecimales.format(event.values[1]) + " m/seg2 \n";
                        txt += "z: " + dosdecimales.format(event.values[2]) + " m/seg2 \n";
                        if( valorCoordenada[1] == 0) {
                            valorCoordenada[1] = (double) event.values[1];
                        }
                        else{
                            puntaje+= ((int) Math.abs(event.values[1]-valorCoordenada[1]))/10;
                            valorCoordenada[1] = (double) event.values[1];
                        }

                    }

                    acelerometro.setText(txt);
                    puntuacion.setText(""+puntaje);
                    break;

                case Sensor.TYPE_GYROSCOPE:
                    txt += "Giroscopio:\n";
                    if(rotacion == 1 || rotacion ==2){
                        txt += "x: " + dosdecimales.format(event.values[0]) + " deg/s  ";
                        txt += "y: " + dosdecimales.format(event.values[1]) + " deg/s  ";
                        txt += "z: " + dosdecimales.format(event.values[2]) + " deg/s \n";
                    }
                    else {
                        txt += "x: " + dosdecimales.format(event.values[0]) + " deg/s\t\t";
                        txt += "y: " + dosdecimales.format(event.values[1]) + " deg/s\n";
                        txt += "z: " + dosdecimales.format(event.values[2]) + " deg/s \n";
                    }
                    giroscopio.setText(txt);
                    break;

                case Sensor.TYPE_GRAVITY :
                    txt += "Gravedad:\n";
                    if(rotacion == 1 || rotacion ==2){
                        txt += "x: " + dosdecimales.format(event.values[0]) + "  ";
                        txt += "y: " + dosdecimales.format(event.values[1]) + "  ";
                        txt += "z: " + dosdecimales.format(event.values[2]) + "\n";
                    }
                    else {
                        txt += "x: " + dosdecimales.format(event.values[0]) + "\t\t";
                        txt += "y: " + dosdecimales.format(event.values[1]) + "\n";
                        txt += "z: " + dosdecimales.format(event.values[2]) + "\n";
                    }
                    gravedad.setText(txt);
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
        editor.apply();


    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        Parar_Sensor();

        if(intent!= null){
            stopService(intent);
        }

    }

    @Override
    protected void onPause()
    {
        super.onPause();

        Parar_Sensor();


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
    }

    protected static void actualizarTimer(long tiempoRestante){

    }

    @Override
    protected void onStart(){
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        puntaje = prefs.getInt("puntaje",0);

    }

    public class ReceptorTimer extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String tipo = intent.getStringExtra("tipo");
            String txt = "";
            if( tipo.equals(Timer.PASO_SEG)){
                txt+= "Faltan: "+intent.getLongExtra("tiempoRestante",0) + " segs";
                timer.setText(txt);
            }
            else{
                finish();
            }
        }
    }



}
