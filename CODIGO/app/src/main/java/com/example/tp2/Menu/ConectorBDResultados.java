package com.example.tp2.Menu;

import android.content.SharedPreferences;

import com.example.tp2.Enums.Dificultad;
import com.google.gson.Gson;

/*
Los resultados que van a existir son:
    ultimoResutlado
    mejorFacil
    mejorMedio
    mejorDificil
Cada uno es un json de Resultado
 */

public class ConectorBDResultados {
    // Constante
    public static final String NOMBRE_BD = "com.example.tp2.MIS_RESULTADOS";

    private SharedPreferences preference;

    public ConectorBDResultados(SharedPreferences preference) {
        this.preference = preference;
    }

    public void guardarUltimoResultado(Resultado resultado){
        Gson json = new Gson();
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("ultimoResultado", json.toJson(resultado));
        editor.apply();
    }

    public void guardarMejorResultado(Resultado resultado){
        Gson json = new Gson();
        SharedPreferences.Editor editor = preference.edit();
        if (resultado.getDificultad().equals(Dificultad.FACIL)) {
            editor.putString("mejorFacil", json.toJson(resultado));
        } else if (resultado.getDificultad().equals(Dificultad.MEDIO)) {
            editor.putString("mejorMedio", json.toJson(resultado));
        } else {
            editor.putString("mejorDificil", json.toJson(resultado));
        }
        editor.apply();
    }

    public Resultado obtenerUltimoResultado(){
        Resultado resultado;
        String jsonResultado = preference.getString("ultimoResultado", "");
        if (jsonResultado.equals("")) {
            resultado = null;
        } else {
            Gson json = new Gson();
            resultado = json.fromJson(jsonResultado, Resultado.class);
        }
        return resultado;
    }

    public Resultado obtenerMejorResultado(String dificultad){
        Resultado resultado;
        String jsonResultado;

        if (dificultad.equals(Dificultad.FACIL)) {
            jsonResultado = preference.getString("mejorFacil", "");
        } else if (dificultad.equals(Dificultad.MEDIO)) {
            jsonResultado = preference.getString("mejorMedio", "");
        } else {
            jsonResultado = preference.getString("mejorDificil", "");
        }

        if (jsonResultado.equals("")) {
            resultado = null;
        } else {
            Gson json = new Gson();
            resultado = json.fromJson(jsonResultado, Resultado.class);
        }
        return resultado;
    }

}
