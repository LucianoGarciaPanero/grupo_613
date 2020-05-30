package com.example.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

        this.controladorUsuario = new ControladorUsuario();
    }

    public void volver(View view){
        finish();
    }

    public  void registrar(View view) {

        if( !ningunaEntradaVacia()) {
            enviarMensaje("Falta completar algún campo");
        }
        String env = comboEnv.getSelectedItem().toString();
        String name = txtNombre.getText().toString();
        String lastname = txtApellido.getText().toString();
        int dni = Integer.parseInt(txtDni.getText().toString());
        String email = txtEmail.getText().toString();
        String password = txtContrasenia.getText().toString();
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
