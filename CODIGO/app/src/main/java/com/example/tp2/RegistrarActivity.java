package com.example.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

public class RegistrarActivity extends AppCompatActivity {

    private Spinner comboGrupo;
    private Spinner comboComision;
    private Spinner comboEnv;
    private EditText txtNombre;
    private EditText txtApellido;
    private EditText txtDni;
    private EditText txtContrasenia;
    private EditText txtEmail;
    private Button buttonVovler;
    private Button buttonRegistrar;
    private ControladorUsuario controladorUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        comboGrupo = (Spinner)findViewById(R.id.comboGrupo);
        ArrayAdapter<CharSequence> adapterGrupo = ArrayAdapter.createFromResource(this, R.array.grupos, android.R.layout.simple_spinner_item);
        comboGrupo.setAdapter(adapterGrupo);

        comboComision = (Spinner)findViewById(R.id.comboComision);
        ArrayAdapter<CharSequence> adapterComision = ArrayAdapter.createFromResource(this, R.array.comisiones, android.R.layout.simple_spinner_item);
        comboComision.setAdapter(adapterComision);

        comboEnv = (Spinner)findViewById(R.id.comboEnv);
        ArrayAdapter<CharSequence> adapterEnv = ArrayAdapter.createFromResource(this, R.array.envs, android.R.layout.simple_spinner_item);
        comboEnv.setAdapter(adapterEnv);

        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtDni = findViewById(R.id.numberDni);
        txtContrasenia = findViewById(R.id.txtContrasenia);
        txtEmail = findViewById(R.id.txtEmail);
        buttonRegistrar = (Button)findViewById(R.id.buttonRegistrar);
        buttonVovler = (Button)findViewById(R.id.buttonVolver);

        this.controladorUsuario = new ControladorUsuario();

        buttonVovler.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                volver();
            }
        });

        buttonRegistrar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                registrar();
            }
        });
    }

    public void volver(){
        finish();
    }

    public  void registrar() {

        if( !ningunaEntradaVacia()) {
            enviarMensaje("Falta completar algún campo");
        }

        // Conversión de los datos en la GUI a variables para mandarlos
        String env = comboEnv.getSelectedItem().toString().replaceAll("\n", "");
        String name = txtNombre.getText().toString().replaceAll("\n", "");
        String lastname = txtApellido.getText().toString().replaceAll("\n", "");
        int dni = Integer.parseInt(txtDni.getText().toString());
        String email = txtEmail.getText().toString().replaceAll("\n", "");
        String password = txtContrasenia.getText().toString().replaceAll("\n", "");
        int comission = Integer.parseInt(comboComision.getSelectedItem().toString());
        int group = Integer.parseInt(comboGrupo.getSelectedItem().toString());

        try {
            controladorUsuario.registrarUsuario(env, name, lastname, dni, email, password, comission, group);
        } catch (EnvException e) {
            enviarMensaje("Error de enviroment: " + e.getMessage());
        } catch (PassException e) {
            enviarMensaje("Error de contraseña : " + e.getMessage());
        } catch (IOException e) {
            enviarMensaje("Error: " + e.getMessage());
        }
    }

    private boolean ningunaEntradaVacia() {
       if (txtNombre.getText().length() > 0 && txtApellido.getText().length() > 0 && txtDni.getText().length() > 0
            && txtContrasenia.getText().length() > 0 && txtEmail.getText().length() > 0
               && comboEnv.getSelectedItem() != null && comboGrupo.getSelectedItem() != null
               && comboComision.getSelectedItem() != null ) {
            return true;
       }
        return false;
    }

    private void enviarMensaje(String msj) {
        Toast toast = Toast.makeText(this, msj, Toast.LENGTH_LONG);
        toast.show();
    }
}
