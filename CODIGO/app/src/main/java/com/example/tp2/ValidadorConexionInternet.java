package com.example.tp2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/*
Esta clase es para validad que exista algun tipo de conexión a internet
*/

public class ValidadorConexionInternet {

    public ValidadorConexionInternet() {
    }
    public boolean estaConectadoAInternet(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if( ni != null && ni.isConnected()) {
            return true;
        } else {
            Toast.makeText(context, "Error: no se esta conectado a internet", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
