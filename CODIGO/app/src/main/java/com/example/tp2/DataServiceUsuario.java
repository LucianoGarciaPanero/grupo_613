package com.example.tp2;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class DataServiceUsuario {

    private static String URI_REGISTRO = "http://so-unlam.net.ar/api/api/register";
    private static String URI_LOGIN = "http://so-unlam.net.ar/api/api/login";

    public DataServiceUsuario() {
    }

    public Usuario registrarUsuario(String env, String name, String lastname, int dni, String email, String password, int comission, int group) throws EnvException, PassException, IOException{
        // Validaciones necesarias
        if(!env.equals("TEST") && !env.equals("DEV")){
            throw new EnvException("El ambiente no se especifico correctamente, espeficado: " + env);
        }
        if (password.length() < 8){
            throw new PassException("La contraseña debe contener al menos 8 caracteres");
        }

        // A partir de los datos del formulario busco crear un objeto json.
        FormularioUsuario fs = new FormularioUsuario(env, name, lastname, dni, email, password, comission, group);
        Gson json = new Gson();
        String usuarioString = json.toJson(fs);

        // Creo la conexión.
        URL url = new URL(URI_REGISTRO);

        // Configuro la conexión.
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Content-type","application/json");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestMethod("POST");

        // Realizo la conexion
        con.connect();

        // Creo el flujo de datos
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(URLEncoder.encode(usuarioString, "UTF-8"));
        out.flush();
        out.close();

        // Recibo la información
        int res = con.getResponseCode();
        if(res == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader( new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = br.readLine()) != null){
                System.out.println(line);
            }
            br.close();
        }

        // Por ultimo cierro la conexión
        if(con != null) {
            con.disconnect();
        }

        return new Usuario(name,lastname,dni,email,comission,group);
    }
}
