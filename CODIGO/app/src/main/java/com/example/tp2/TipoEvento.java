package com.example.tp2;

public enum TipoEvento {
    ACCESO("AccesoCuenta");

    private String texto;

    private TipoEvento(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return this.texto;
    }
}
