package com.example.tp2.Enums;

public enum TipoEvento {
    ACCESO("AccesoCuenta"),
    BACKGROUND("BACKGROUND"),
    RECORD("RECORD");

    private String texto;

    private TipoEvento(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return this.texto;
    }
}
