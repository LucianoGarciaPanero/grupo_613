package com.example.tp2.Partida;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.util.Log;

import static java.lang.Thread.sleep;

public class Timer extends IntentService {

    private String               action;
    public static final String   PASO_SEG = "pasoSegundo";
    public static final String   FINALIZO_TIMER = "finalizoTimer";

    private long                 tiempoRestanteEnMilis;
    private boolean activo;

    public Timer() {
        super("Timer");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activo = false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        activo = true;
        action = intent.getExtras().getString("Accion");
        tiempoRestanteEnMilis = intent.getLongExtra("tiempoRestante",30000);
        long topeTiempo = tiempoRestanteEnMilis/1000;

        for(int j = 0 ; j <= topeTiempo && activo; j++){

            Intent i = new Intent();
            i.setAction(action);
            i.putExtra("tipo",PASO_SEG);
            i.putExtra("tiempoRestante",tiempoRestanteEnMilis);
            sendBroadcast(i);

            try {
                sleep(998);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            tiempoRestanteEnMilis-= 1000;

        }
        if (activo) {
            Intent i = new Intent();
            i.setAction(action);
            i.putExtra("tipo",FINALIZO_TIMER);
            sendBroadcast(i);
            stopSelf();
        }

    }




}
