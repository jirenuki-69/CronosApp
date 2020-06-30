package cronos.com.cronosapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import cronos.com.cronosapp.Models.Profesor;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String MATRICULA = "matricula";
    public static final String NOMBRE = "nombre";
    public static final String APELLIDOS = "apellidos";
    public static final String CONTRASEÑA = "contrasena";
    public static final String NUM_GRUPO = "num_grupo";
    private static final String STATUS = "status";
    public static final String TIPO = "";
    private boolean status;
    public String tp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showToolbar("Cronos App", false);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        status = sharedPreferences.getBoolean(STATUS, false);
        tp = sharedPreferences.getString(TIPO,"");

        if (status){
            if(tp.equals("PROFESOR")){
                finish();
                Intent intent = new Intent(MainActivity.this, HomeProfesorActivity.class);
                startActivity(intent);
                finish();
            }else if(tp.equals("ALUMNO")){
                finish();
                Intent intent = new Intent(MainActivity.this, HomeAlumnoActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    public void lanzarInicioDeSesion(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void lanzarSelectProfile(View view) {
        Intent intent = new Intent(MainActivity.this, SelectProfile.class);
        startActivity(intent);
        finish();
    }
}
