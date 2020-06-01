package com.example.tp2.Enums;

public enum Color {
    BLANCO("BLANCO"),
    ROJO("ROJO"),
    AMARILO("AMARILLO");

    private String texto;

    private Color(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return this.texto;
    }
}
