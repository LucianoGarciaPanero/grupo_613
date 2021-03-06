package com.example.tp2.Inicio;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

/*
Este servicio se registra en segundo plano, es el que se comunica con el WebService para registrar al usuario.
 */

public class ServicioPostUsuario extends IntentService {

    public final static String ERROR = "ERROR";

    public ServicioPostUsuario() {
        super("ServicioPostUsuario");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        // Saco los datos del intent
        Bundle extras = intent.getExtras();
        String accion = extras.getString("accion");
        String json = extras.getString("json");
        String uri = extras.getString("uri");

        // Ejecuto el POST
        String resultado = post(uri,json);

        // Devuelvo lo que llega
        Intent intentPost = new Intent();
        intentPost.setAction(accion);
        intentPost.putExtra("json",resultado);
        sendBroadcast(intentPost);
    }

    private String post(String uri, String json) {

        // Creo variables necesarias
        String resultado;
        HttpURLConnection con = null;
        URL url;

        try {
            // Creo la conexión.
            url = new URL(uri);

            // Configuro la conexión.
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "application/json; charset = UTF-8");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");

            // Creo el flujo de datos
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.write(json.getBytes("UTF-8"));
            out.flush();
            out.close();

            // Realizo la conexion
            con.connect();

            // Recibo el coidgo de respuesta
            int codR = con.getResponseCode();

            // Evaluo según el código de respuesta
            if (codR == HttpURLConnection.HTTP_OK || codR == HttpURLConnection.HTTP_CREATED) {
                resultado = convertirInputStreamToString(new InputStreamReader(con.getInputStream()));
            } else {
                resultado = ERROR;
            }
            return resultado;
        } catch (Exception e){
            return ERROR;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    private String convertirInputStreamToString(InputStreamReader in) throws IOException {
        BufferedReader reader = new BufferedReader(in);
        StringBuffer stringB = new StringBuffer();
        String linea;
        while ((linea = reader.readLine()) != null){
            stringB.append(linea);
        }
        return stringB.toString();
    }
}
