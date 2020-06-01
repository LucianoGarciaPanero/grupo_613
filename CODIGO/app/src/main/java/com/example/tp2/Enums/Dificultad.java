package com.example.tp2.Enums;

public enum Dificultad {
    DIFICIL("DIFICIL"),
    MEDIO("MEDIO"),
    FACIL("FACIL");

    private String texto;

    private Dificultad(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return this.texto;
    }
}
