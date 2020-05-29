package com.example.tp2;

import java.io.IOException;

public class ControladorUsuario {
    private DataServiceUsuario dataServiceUsuario;

    public ControladorUsuario() {
        this.dataServiceUsuario = new DataServiceUsuario();
    }

    public Usuario registrarUsuario(String env, String name, String lastname, int dni, String email, String password, int comission, int group) throws EnvException, PassException, IOException {
        return dataServiceUsuario.registrarUsuario(env, name, lastname, dni, email, password, comission, group);
    }
}
