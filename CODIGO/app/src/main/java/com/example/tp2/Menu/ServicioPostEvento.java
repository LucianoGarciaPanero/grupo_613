package com.example.tp2.Menu;

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

public class ServicioPostEvento extends IntentService {

    private final static String ERROR = "ERROR";

    public ServicioPostEvento() {
        super("ServicioPostEvento");
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
        String token = extras.getString("token");

        // Ejecuto el POST
        String resultado = post(uri, json, token);

        // Devuelvo lo que llega
        Intent intentPost = new Intent();
        intentPost.setAction(accion);
        intentPost.putExtra("json",resultado);
        sendBroadcast(intentPost);
    }

    private String post(String uri, String json, String token) {

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
            con.setRequestProperty("token", token);

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
                resultado = this.ERROR;
            }
            return resultado;
        } catch (Exception e){
            return this.ERROR;
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
