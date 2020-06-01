package com.example.tp2;

public enum TipoEvento {
    ACTIVO("ACTIVO"),
    INACTIVO("INACTIVO");

    private String texto;

    private TipoEvento(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return "TipoEvento{" +
                "texto='" + texto + '\'' +
                '}';
    }
}
