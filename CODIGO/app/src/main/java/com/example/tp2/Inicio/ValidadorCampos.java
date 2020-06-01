package com.example.tp2.Inicio;

import android.content.Context;
import android.widget.Toast;

/*
Esta clase es para validar los campos de email y contrasenia. Esto es para evitar duplicar código en las activitys.
 */

public class ValidadorCampos {

    public ValidadorCampos() {
    }

    /*
    Solo revisa de que la contraseña y el email esten correctamente completados.
     */

    public boolean camposCorrectos(Context context, String contraseñia, String email) {
        if(contraseñia.length() <= 7){
            Toast.makeText(context, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!email.contains("@") || (!email.contains(".com") && !email.contains(".ar"))){
            Toast.makeText(context, "Se debe especificar un email", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
