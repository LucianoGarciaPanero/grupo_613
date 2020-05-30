package com.example.tp2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class ReceptorRegistrar extends BroadcastReceiver {

    public ReceptorRegistrar() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Gson gson = new Gson();
        RespuestaServicioRegistrar respuesta;
        String json = intent.getStringExtra("json");
        if(json.equals(ServicePostUsuario.ERROR)) {
            Toast.makeText(context.getApplicationContext(), "Datos incorrectos", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(context.getApplicationContext(), json, Toast.LENGTH_LONG).show();
        /*
        respuesta = gson.fromJson(json, RespuestaServicioRegistrar.class);
        if(respuesta.getState().equals("success")) {
            Toast.makeText(context.getApplicationContext(), "Registro correcto", Toast.LENGTH_LONG).show();
            System.out.println(respuesta.toString());
        }*/


    }
}
