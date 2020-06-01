package com.example.tp2.Partida;

/*
Clase para serializar la configuracion de partida
 */

public class ConfiguracionDePartida {
    private String dificultad;
    private String colorFondo;
    private String token;

    public ConfiguracionDePartida(String dificultad, String colorFondo, String token) {
        this.dificultad = dificultad;
        this.colorFondo = colorFondo;
        this.token = token;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getColorFondo() {
        return colorFondo;
    }

    public void setColorFondo(String colorFondo) {
        this.colorFondo = colorFondo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
