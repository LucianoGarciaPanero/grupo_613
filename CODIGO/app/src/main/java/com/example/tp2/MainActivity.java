package com.example.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    // Constantes
    private final String URI_LOGIN = "http://so-unlam.net.ar/api/api/login";
    private final String ACCION_INICIAR_SESION = "com.example.tp2.intent.action.INICIAR_SESION";
    // Esta variable es para que podamos cambiar de entorno facilmente
    public static final String ENV = "TEST"; // ENV = ["TEST"/"DEV"]

    // Clase para validar 
    private ValidadorCampos validadorCampos;
    private ValidadorConexionInternet validadorConexionInternet;

    // Objetos de la GUI
    private Button buttonRegistrar;
    private Button buttonIngresar;
    private EditText txtEmail;
    private EditText txtContrasenia;

    // Variables para la comunicacion con el service
    public IntentFilter filtro;
    private ReceptorIniciarSesion receiver = new ReceptorIniciarSesion();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.buttonRegistrar = (Button)findViewById(R.id.buttonRegistrar);
        this.buttonIngresar = (Button)findViewById(R.id.buttonIngresar);
        this.txtEmail = findViewById(R.id.txtEmail);
        this.txtContrasenia = findViewById(R.id.txtContrasenia);

        this.buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarRegistro();
            }
        });
        this.buttonIngresar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });

        this.validadorCampos = new ValidadorCampos();
        this.validadorConexionInternet = new ValidadorConexionInternet();

    }

    private void iniciarSesion() {
        if(!this.validadorCampos.camposCorrectos(this, this.txtContrasenia.getText().toString(), this.txtEmail.getText().toString())) {
            return;
        }

        if(!this.validadorConexionInternet.validarConexionInternet(MainActivity.this)){
            return;
        }

        // Como solo se valida el email y la contrasenia, los demas campos los dejo vacío
        String email = this.txtEmail.getText().toString();
        String password = this.txtContrasenia.getText().toString();
        FormularioUsuario fu = new FormularioUsuario(this.ENV, "","",0, email, password, 0, 0);
        Gson json = new Gson();
        String jsonUsuario = json.toJson(fu);

        //Armo el intent y se lo mando al service
        this.intent = new Intent(MainActivity.this, ServicePostUsuario.class);
        this.intent.putExtra("json", jsonUsuario);
        this.intent.putExtra("uri", this.URI_LOGIN);
        this.intent.putExtra("accion", this.ACCION_INICIAR_SESION);

        // Configuro el boradcast para poder recibir el resultado de service
        configurarBroadcastReciever();

        // Inicio servicio
        startService(this.intent);


    }

    private void configurarBroadcastReciever() {
        this.filtro = new IntentFilter();
        this.filtro.addAction(ACCION_INICIAR_SESION);
        this.filtro.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(this.receiver, this.filtro);
    }

    private void iniciarRegistro() {
        Intent intent = new Intent(this, RegistrarActivity.class);
        startActivity(intent);
    }

    public void onDestroy() {
        super.onDestroy();
        stopService(this.intent);
    }

    public class ReceptorIniciarSesion extends BroadcastReceiver {

        public ReceptorIniciarSesion() {
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
                Toast.makeText(context.getApplicationContext(), "Inicio de sesión exitoso", Toast.LENGTH_LONG).show();
                // TODO: crea una nueva activity y le paso el json.
            }
        }
    }
}

