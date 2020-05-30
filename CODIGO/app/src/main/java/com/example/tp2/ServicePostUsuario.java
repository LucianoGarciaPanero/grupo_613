package com.example.tp2;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

/*
Este servicio se registra en segundo plano, es el que se comunica con el WebService para registrar al usuario.
 */

public class ServicePostUsuario extends IntentService {

    public ServicePostUsuario(String name) {
        super("ServicePostUsuario");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
