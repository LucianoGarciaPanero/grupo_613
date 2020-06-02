package com.example.tp2.Menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.tp2.Inicio.ServicioPostUsuario;
import com.google.gson.Gson;

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
                    "Se ha registrado un evento" + "\nTipo evento: " +  respuestaServicioPostEvento.getEvent().getType_events()
                            +"\nNro grupo: " + respuestaServicioPostEvento.getEvent().getGroup(), Toast.LENGTH_SHORT).show();

        }
    }
}
