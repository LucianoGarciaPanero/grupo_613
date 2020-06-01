package com.example.tp2.Menu;

/*
Existe un record unicamente por los puntos, pero habra uno por cada categoría
*/

public class Resultado {
    private int puntos;
    private float tiempo; //Tiempo en que rompes la lata
    private float acleracionMax;
    private String dificultad;

    public Resultado(int puntos, float tiempo, float acleracionMax, String dificultad) {
        this.puntos = puntos;
        this.tiempo = tiempo;
        this.acleracionMax = acleracionMax;
        this.dificultad = dificultad;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public float getTiempo() {
        return tiempo;
    }

    public void setTiempo(float tiempo) {
        this.tiempo = tiempo;
    }

    public float getAcleracionMax() {
        return acleracionMax;
    }

    public void setAcleracionMax(float acleracionMax) {
        this.acleracionMax = acleracionMax;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }
}
