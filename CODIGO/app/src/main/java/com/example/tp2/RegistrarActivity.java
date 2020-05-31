package com.example.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
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

public class RegistrarActivity extends AppCompatActivity {

    // Constantes
    private final String URI_REGISTRO = "http://so-unlam.net.ar/api/api/register";
    private final String ACCION_REGISTRAR = "com.example.tp2.intent.action.ACCION_REGISTRAR";

    // Variables para la comunicacion con el service
    public IntentFilter filtro;
    private ReceptorRegistrar receiver = new ReceptorRegistrar();
    private Intent intent;

    // Objetos de la GUI
    private Spinner comboGrupo;
    private Spinner comboComision;
    private EditText txtNombre;
    private EditText txtApellido;
    private EditText txtDni;
    private EditText txtContrasenia;
    private EditText txtEmail;
    private Button buttonVovler;
    private Button buttonRegistrar;

    // Clase para validar
    private ValidadorCampos validadorCampos;
    private ValidadorConexionInternet validadorConexionInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        this.comboGrupo = (Spinner)findViewById(R.id.comboGrupo);
        ArrayAdapter<CharSequence> adapterGrupo = ArrayAdapter.createFromResource(this, R.array.grupos, android.R.layout.simple_spinner_item);
        this.comboGrupo.setAdapter(adapterGrupo);

        this.comboComision = (Spinner)findViewById(R.id.comboComision);
        ArrayAdapter<CharSequence> adapterComision = ArrayAdapter.createFromResource(this, R.array.comisiones, android.R.layout.simple_spinner_item);
        this.comboComision.setAdapter(adapterComision);

        this.txtNombre = findViewById(R.id.txtNombre);
        this.txtApellido = findViewById(R.id.txtApellido);
        this.txtDni = findViewById(R.id.numberDni);
        this.txtContrasenia = findViewById(R.id.txtContrasenia);
        this.txtEmail = findViewById(R.id.txtEmail);
        this.buttonRegistrar = (Button)findViewById(R.id.buttonRegistrar);
        this.buttonVovler = (Button)findViewById(R.id.buttonVolver);

        this.buttonVovler.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                volver();
            }
        });

        this.buttonRegistrar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                registrar();
            }
        });

        this.validadorCampos = new ValidadorCampos();
        this.validadorConexionInternet = new ValidadorConexionInternet();
    }

    private void volver(){
        finish();
    }

    private void registrar() {

        if( !ningunaEntradaVacia()) {
            return;
        }

        if(!validadorCampos.camposCorrectos(this, txtContrasenia.getText().toString(), txtEmail.getText().toString())){
            return;
        }

        if(!this.validadorConexionInternet.validarConexionInternet(RegistrarActivity.this)){
            return;
        }

        // Creo el json que se manda al service
        FormularioUsuario fu = crearFormularioUsuario();
        Gson json = new Gson();
        String jsonUsuario = json.toJson(fu);

        // Armo el intent y se lo mando al service
        this.intent = new Intent(RegistrarActivity.this, ServicePostUsuario.class);
        this.intent.putExtra("json", jsonUsuario);
        this.intent.putExtra("uri", URI_REGISTRO);
        this.intent.putExtra("accion", ACCION_REGISTRAR);

        // Configuro el boradcast para poder recibir el resultado de service
        configurarBroadcastReciever();

        // Inicio servicio
        startService(this.intent);
    }

    private void configurarBroadcastReciever() {
        this.filtro = new IntentFilter();
        this.filtro.addAction(ACCION_REGISTRAR);
        this.filtro.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(this.receiver, this.filtro);
    }

    private FormularioUsuario crearFormularioUsuario() {
        // Conversión de los datos en la GUI a variables para mandarlos
        String name = this.txtNombre.getText().toString().replaceAll("\n", "");
        String lastname = this.txtApellido.getText().toString().replaceAll("\n", "");
        int dni = Integer.parseInt(this.txtDni.getText().toString());
        String email = this.txtEmail.getText().toString().replaceAll("\n", "");
        String password = this.txtContrasenia.getText().toString().replaceAll("\n", "");
        int comission = Integer.parseInt(this.comboComision.getSelectedItem().toString());
        int group = Integer.parseInt(this.comboGrupo.getSelectedItem().toString());

        return new FormularioUsuario(MainActivity.ENV, name, lastname, dni, email, password, comission, group);
    }

    /*
    Solo valida que no haya campos vacios
     */

    private boolean ningunaEntradaVacia() {
       if (this.txtNombre.getText().length() > 0 && this.txtApellido.getText().length() > 0 && this.txtDni.getText().length() > 0
            && this.txtContrasenia.getText().length() > 0 && this.txtEmail.getText().length() > 0
               && this.comboGrupo.getSelectedItem() != null && this.comboComision.getSelectedItem() != null ) {
            return true;
       }
        enviarMensaje("Falta completar algún campo");
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

    public class ReceptorRegistrar extends BroadcastReceiver {

        public ReceptorRegistrar() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // Recibo lo que me llega del intent
            Gson gson = new Gson();
            RespuestaServicioRegistrar respuesta;
            String json = intent.getStringExtra("json");

            // Si es un error termino el método
            if(json.equals(ServicePostUsuario.ERROR)) {
                Toast.makeText(context.getApplicationContext(), "Datos incorrectos", Toast.LENGTH_LONG).show();
                return;
            }

            // Si no es un error lo transformo para poder analizarlo
            respuesta = gson.fromJson(json, RespuestaServicioRegistrar.class);
            if(respuesta.getState().equals("success")) {
                Toast.makeText(context.getApplicationContext(), "Registro correcto", Toast.LENGTH_LONG).show();
                // TODO: crea una nueva activity y le paso el json.
            }
        }
    }
}
