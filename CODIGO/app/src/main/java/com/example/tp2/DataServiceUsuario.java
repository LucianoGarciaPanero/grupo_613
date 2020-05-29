package com.example.tp2;

import android.text.InputType;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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

        // A partir de los datos del formulario busco crear un objeto json.
        FormularioUsuario fs = new FormularioUsuario(env, name, lastname, dni, email, password, comission, group);
        Gson json = new Gson();
        String usuarioString = json.toJson(fs);

        // Establezco la conexión y le mando el json.
        URL url = null;
        HttpURLConnection con = null;
        try {
            url = new URL("http://so-unlam.net.ar/api/api/register");
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/json");
            con.connect();
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            out.write(usuarioString);
            out.close();
            int res = con.getResponseCode();
            if(res == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader( new InputStreamReader(con.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null){
                    System.out.println(line);
                }
                br.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(con != null) {
                con.disconnect();
            }
        }

        return new Usuario(name,lastname,dni,email,comission,group);
    }
}
