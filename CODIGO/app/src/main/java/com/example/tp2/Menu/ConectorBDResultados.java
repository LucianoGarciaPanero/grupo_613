package com.example.tp2.Menu;

import android.content.SharedPreferences;

public class ConectorBDResultados {
    // Constante
    public static final String NOMBRE_BD = "MisResultados";

    private SharedPreferences preference;

    public ConectorBDResultados(SharedPreferences preference) {
        this.preference = preference;
    }

    public void guardarResultado(Resultado resultado){

    }

    public Resultado obtenerUltimoResultado(){
        return null;
    }

    public Resultado obtenerMejorResultado(){
        return null;
    }


}
