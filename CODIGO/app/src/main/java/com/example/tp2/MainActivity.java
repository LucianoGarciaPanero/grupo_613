package com.example.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    // Esta variable es para que podamos cambiar de entorno facilmente
    public static final String ENV = "TEST"; // ENV = ["TEST"/"DEV"]

    // Clase para validar entrada
    private ValidadorCampos vc;

    // Objetos de la GUI
    private Button buttonRegistrar;
    private Button buttonIngresar;
    private EditText txtEmail;
    private EditText txtContrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRegistrar = (Button)findViewById(R.id.buttonRegistrar);
        buttonIngresar = (Button)findViewById(R.id.buttonIngresar);
        txtEmail = findViewById(R.id.txtEmail);
        txtContrasenia = findViewById(R.id.txtContrasenia);

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarRegistro();
            }
        });
        buttonIngresar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });

        vc = new ValidadorCampos();

    }

    private void iniciarSesion() {
        if(!vc.camposCorrectos(this, txtContrasenia.getText().toString(), txtEmail.getText().toString())) {
            return;
        }

    }

    private void iniciarRegistro() {
        Intent intent = new Intent(this, RegistrarActivity.class);
        startActivity(intent);
    }
}

