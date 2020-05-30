package com.example.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class RegistrarActivity extends AppCompatActivity {

    private Spinner comboGrupo;
    private Spinner comboComision;
    private Spinner comboEnv;
    private EditText txtNombre;
    private EditText txtApellido;
    private EditText txtDni;
    private EditText txtContrasenia;
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
        txtDni = findViewById(R.id.txtDni);
        txtContrasenia = findViewById(R.id.txtContrasenia);

        this.controladorUsuario = new ControladorUsuario();
    }

    public void volver(View view){
        finish();
    }

    public  void registrar(View view) {

        if( !ningunaEntradaVacia())

        controladorUsuario.registrarUsuario();
    }

    private boolean ningunaEntradaVacia() {
        if(txtNombre.getText().length() > 0 && txtApellido.getText().length() > 0 && txtDni.getText().length() > 0
                && txtContrasenia.getText().length() > 0)
    }
}
