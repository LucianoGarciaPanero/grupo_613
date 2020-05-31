package com.example.tp2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import static androidx.core.content.ContextCompat.getSystemService;

public class ValidadorConexionInternet {

    public ValidadorConexionInternet() {
    }
    public boolean validarConexionInternet(Context context){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if( ni != null && ni.isConnected()) {
            return true;
        } else {
            Toast.makeText(context, "No se esta conectado a internet", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
