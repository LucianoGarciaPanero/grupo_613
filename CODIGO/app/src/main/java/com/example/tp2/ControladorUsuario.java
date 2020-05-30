package com.example.tp2;

import java.io.IOException;

public class ControladorUsuario {
    private DataServiceUsuario dataServiceUsuario;
    private Usuario usuario;

    public ControladorUsuario() {
        this.dataServiceUsuario = new DataServiceUsuario();
    }

    public void registrarUsuario(String env, String name, String lastname, int dni, String email, String password, int comission, int group) throws EnvException, PassException, IOException {
        this.usuario = dataServiceUsuario.registrarUsuario(new FormularioUsuario(env, name, lastname, dni, email, password, comission, group));
    }
}
