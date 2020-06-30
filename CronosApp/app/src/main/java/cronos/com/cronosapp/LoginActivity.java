package cronos.com.cronosapp;

import android.app.Activity;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cronos.com.cronosapp.Helpers.Const;
import cronos.com.cronosapp.Helpers.RequestHandler;
import cronos.com.cronosapp.R;

import static android.os.Build.ID;

public class LoginActivity extends AppCompatActivity {


    EditText EdTMatricula, EdTPassword;

    ProgressDialog pdLoading;

    Spinner mySpinner;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        showToolbar("Inicio de Sesion", false);

        pdLoading = new ProgressDialog(LoginActivity.this);

        EdTMatricula = (EditText) findViewById(R.id.matricula);

        EdTPassword = (EditText) findViewById(R.id.password);

        mySpinner = (Spinner) findViewById(R.id.typeusers);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        status = sharedPreferences.getBoolean(STATUS, false);
        tp = sharedPreferences.getString(TIPO,"");

        if (status){
            if(tp.equals("PROFESOR")){
                Intent intent = new Intent(LoginActivity.this, HomeProfesorActivity.class);
                startActivity(intent);
                finish();
            }else if(tp.equals("ALUMNO")){
                //finish();
                Intent intent = new Intent(LoginActivity.this, HomeAlumnoActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }

    public void lanzarSelectProfile(View view) {
        Intent intent = new Intent(LoginActivity.this, SelectProfile.class);
        startActivity(intent);
    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    public void lanzarLogin(View view) {
        final String email = EdTMatricula.getText().toString();
        final String password = EdTPassword.getText().toString();
        final String type = mySpinner.getSelectedItem().toString();
        String URL_USADA = "";

        if(type.equals("PROFESOR")){
            URL_USADA = Const.URL_LOGIN_PROFESOR;
        }else if(type.equals("ALUMNO")){
            URL_USADA = Const.URL_LOGIN_ALUMNO;
        }else{
            URL_USADA = "";
        }

        if(email.isEmpty()|| password.isEmpty()){
            Toast.makeText(this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
        }else if(type.equals("Seleccione")){
            Toast.makeText(this, "Debe de seleccionar un rol", Toast.LENGTH_SHORT).show();
        }else {
            final String FINAL_URL_USADA = URL_USADA;
            class Login extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    //this method will be running on UI thread
                    pdLoading.setMessage("\tIniciando Sesión...");
                    pdLoading.setCancelable(false);
                    pdLoading.show();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("matricula", email);
                    params.put("password", password);
                    params.put("type", type);

                    //returing the response
                    return requestHandler.sendPostRequest(FINAL_URL_USADA, params);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    pdLoading.dismiss();
                    Log.w("CRONOSJSON", String.valueOf(s));
                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);

                        if (!obj.getBoolean("error")) {
                            if(type.equals("PROFESOR")){

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(MATRICULA, obj.getString(MATRICULA));
                                editor.putString(NOMBRE, obj.getString(NOMBRE));
                                editor.putString(APELLIDOS, obj.getString(APELLIDOS));
                                editor.putString(TIPO, type);
                                editor.putBoolean(STATUS, true);
                                editor.apply();
                                finish();

                                Intent intent = new Intent(LoginActivity.this, HomeProfesorActivity.class);
                                startActivity(intent);
                            }else if(type.equals("ALUMNO")){

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(MATRICULA, obj.getString(MATRICULA));
                                editor.putString(NOMBRE, obj.getString(NOMBRE));
                                editor.putString(APELLIDOS, obj.getString(APELLIDOS));
                                editor.putString(NUM_GRUPO, obj.getString(NUM_GRUPO));
                                editor.putString(TIPO, type);
                                editor.putBoolean(STATUS, true);
                                editor.apply();
                                finish();

                                Intent intent = new Intent(LoginActivity.this, HomeAlumnoActivity.class);
                                startActivity(intent);
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Toast.makeText(LoginActivity.this, "Exception: " + e, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), type + " No encontrado", Toast.LENGTH_LONG).show();
                    }
                }
            }

            Login login = new Login();
            login.execute();
        }
    }
}
