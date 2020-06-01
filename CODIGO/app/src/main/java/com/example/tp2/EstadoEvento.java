package com.example.tp2;

public enum EstadoEvento {
    ACTIVO("ACTIVO"),
    INACTIVO("INACTIVO");

    private String texto;

    private EstadoEvento(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return this.texto;
    }
}
