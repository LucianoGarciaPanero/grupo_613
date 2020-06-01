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

public class IniciarSesionActivity extends AppCompatActivity {

    // Constantes
    private final String URI_INICIAR_SESION = "http://so-unlam.net.ar/api/api/login";
    private final String ACCION_INICIAR_SESION = "com.example.tp2.intent.action.INICIAR_SESION";
    // Esta variable es para que podamos cambiar de entorno facilmente
    public static final String ENV = "TEST"; // ENV = ["TEST"/"DEV"]

    // Variables para la comunicacion con el service
    public IntentFilter filtro;
    private ReceptorIniciarSesion receiver = new ReceptorIniciarSesion();
    private Intent intent;

    // Objetos de la GUI
    private EditText txtEmail;
    private EditText txtContrasenia;
    private Button buttonRegistrar;
    private Button buttonIngresar;

    // Clases para validar
    private ValidadorCampos validadorCampos;
    private ValidadorConexionInternet validadorConexionInternet;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.intent != null) {
            stopService(this.intent);
        }
    }


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

    private void iniciarRegistro() {
        Intent intentR = new Intent(this, RegistrarActivity.class);
        startActivity(intentR);
        finish();
    }

    private void iniciarSesion() {
        if(!this.validadorCampos.camposCorrectos(this, this.txtContrasenia.getText().toString(), this.txtEmail.getText().toString())) {
            return;
        }

        if(!this.validadorConexionInternet.estaConectadoAInternet(IniciarSesionActivity.this)){
            return;
        }

        // Como solo se valida el email y la contrasenia, los demas campos los dejo vacío
        String email = this.txtEmail.getText().toString();
        String password = this.txtContrasenia.getText().toString();
        FormularioUsuario fu = new FormularioUsuario(this.ENV, "","",0, email, password, 0, 0);
        Gson json = new Gson();
        String jsonUsuario = json.toJson(fu);

        //Armo el intent y se lo mando al service
        this.intent = new Intent(IniciarSesionActivity.this, ServicioPostUsuario.class);
        this.intent.putExtra("json", jsonUsuario);
        this.intent.putExtra("uri", this.URI_INICIAR_SESION);
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

    public class ReceptorIniciarSesion extends BroadcastReceiver {

        public ReceptorIniciarSesion() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // Recibo lo que me llega del intent
            Gson gson = new Gson();
            RespuestaServicioPostUsuario respuestaServicioPostUsuario;
            String json = intent.getStringExtra("json");

            // Si es un error termino el método
            if(json.equals(ServicioPostUsuario.ERROR)) {
                Toast.makeText(context.getApplicationContext(), "Datos incorrectos", Toast.LENGTH_LONG).show();
                return;
            }

            // Si no es un error lo transformo para poder analizarlo
            respuestaServicioPostUsuario = gson.fromJson(json, RespuestaServicioPostUsuario.class);
            if(respuestaServicioPostUsuario.getState().equals("success")) {
                Toast.makeText(context.getApplicationContext(), "Inicio de sesión exitoso", Toast.LENGTH_LONG).show();
                Intent intentM = new Intent(IniciarSesionActivity.this, MenuActivity.class);
                intentM.putExtra("token", respuestaServicioPostUsuario.getToken());
                startActivity(intentM);
                finish();
            }
        }
    }
}

