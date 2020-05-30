package com.example.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

public class RegistrarActivity extends AppCompatActivity {

    // Constantes
    private final String URI_REGISTRO = "http://so-unlam.net.ar/api/api/register";
    private final String URI_LOGIN = "http://so-unlam.net.ar/api/api/login";
    private final String ACCION_REGISTRAR = "com.example.tp2.ACCION_REGISTRAR";

    // Variables para la comunicacion con el service
    public IntentFilter filtro;
    private ReceptorOperacion receiver = new ReceptorOperacion();
    private Intent intent;

    // Objetos de la GUI
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        this.comboGrupo = (Spinner)findViewById(R.id.comboGrupo);
        ArrayAdapter<CharSequence> adapterGrupo = ArrayAdapter.createFromResource(this, R.array.grupos, android.R.layout.simple_spinner_item);
        this.comboGrupo.setAdapter(adapterGrupo);

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
            return;
        }

        // Creo el json que se manda al service
        FormularioUsuario fu = crearFormularioUsuario();
        Gson json = new Gson();
        String jsonUsuario = json.toJson(fu);

        // Armo el intent y se lo mando al usuario
        this.intent = new Intent(RegistrarActivity.this, ServicePostUsuario.class);
        this.intent.putExtra("json", jsonUsuario);
        this.intent.putExtra("uri", URI_REGISTRO);
        this.intent.putExtra("accion", ACCION_REGISTRAR);
        startService(this.intent);

        // Configuro el boradcast para poder recibir el resultado de service
        configurarBroadcastReciever();
    }

    private void configurarBroadcastReciever() {
        this.filtro = new IntentFilter(ACCION_REGISTRAR);
        this.filtro.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(this.receiver, this.filtro);
    }

    private FormularioUsuario crearFormularioUsuario() {
        // Conversión de los datos en la GUI a variables para mandarlos
        String env = this.comboEnv.getSelectedItem().toString().replaceAll("\n", "");
        String name = this.txtNombre.getText().toString().replaceAll("\n", "");
        String lastname = this.txtApellido.getText().toString().replaceAll("\n", "");
        int dni = Integer.parseInt(this.txtDni.getText().toString());
        String email = this.txtEmail.getText().toString().replaceAll("\n", "");
        String password = this.txtContrasenia.getText().toString().replaceAll("\n", "");
        int comission = Integer.parseInt(this.comboComision.getSelectedItem().toString());
        int group = Integer.parseInt(this.comboGrupo.getSelectedItem().toString());

        return new FormularioUsuario(env, name, lastname, dni, email, password, comission, group);
    }

    private boolean ningunaEntradaVacia() {
       if (this.txtNombre.getText().length() > 0 && this.txtApellido.getText().length() > 0 && this.txtDni.getText().length() > 0
            && this.txtContrasenia.getText().length() > 0 && this.txtEmail.getText().length() > 0
               && this.comboEnv.getSelectedItem() != null && this.comboGrupo.getSelectedItem() != null
               && this.comboComision.getSelectedItem() != null ) {
            return true;
       }
        return false;
    }

    private void enviarMensaje(String msj) {
        Toast toast = Toast.makeText(this, msj, Toast.LENGTH_LONG);
        toast.show();
    }

    public void onDestroy() {
        super.onDestroy();
        stopService(this.intent);
    }
}
