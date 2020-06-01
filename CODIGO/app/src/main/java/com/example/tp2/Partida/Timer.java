package com.example.tp2.Partida;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.util.Log;

import static java.lang.Thread.sleep;

public class Timer extends IntentService {

    private String         action;
    public static final String   PASO_SEG = "pasoSegundo";
    public static final String   FINALIZO_TIMER = "finalizoTimer";

    private long                 tiempoRestanteEnMilis = 30000;

    public Timer() {
        super("Timer");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        action = intent.getExtras().getString("Accion");

        for(int j = 0 ; j <=29; j++){

            Intent i = new Intent();
            i.setAction(action);
            i.putExtra("tipo",PASO_SEG);
            i.putExtra("tiempoRestante",tiempoRestanteEnMilis/1000);
            sendBroadcast(i);

            try {
                sleep(998);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            tiempoRestanteEnMilis-= 1000;


        }

        Intent i = new Intent();
        i.setAction(action);
        i.putExtra("tipo",FINALIZO_TIMER);
        sendBroadcast(i);
    }




}
