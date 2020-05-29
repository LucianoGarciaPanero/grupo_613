package com.example.tp2;

public class DataServiceUsuario {
    public Usuario registrarUsuario(String env, String name, String lastname,int dni, String email, String password, int comission, int group) throws EnvException, PassException{
        if(!env.equals("TEST") || !env.equals("DEV")){
            throw new EnvException("El ambiente no se especifico correctamente");
        }
        if (password.length() < 8){
            throw new PassException("La contraseña debe contener al menos 8 caracteres");
        }
        return new Usuario(name,lastname,dni,email,comission,group);
    }
}
