package com.example.tp2.Enums;

public enum NombreColor {
    BLANCO("BLANCO"),
    ROJO("ROJO"),
    AMARILO("AMARILLO");

    private String texto;

    private NombreColor(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return this.texto;
    }
}
