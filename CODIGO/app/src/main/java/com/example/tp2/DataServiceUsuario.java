package com.example.tp2;

import java.net.HttpURLConnection;
import java.net.URL;

public class DataServiceUsuario {

    public DataServiceUsuario() {
    }

    public Usuario registrarUsuario(String env, String name, String lastname, int dni, String email, String password, int comission, int group) throws EnvException, PassException{
        if(!env.equals("TEST") || !env.equals("DEV")){
            throw new EnvException("El ambiente no se especifico correctamente");
        }
        if (password.length() < 8){
            throw new PassException("La contraseña debe contener al menos 8 caracteres");
        }

        URL url = new URL("http://so-unlam.net.ar/api/api/register");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");




        return new Usuario(name,lastname,dni,email,comission,group);
    }
}
