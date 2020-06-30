package cronos.com.cronosapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cronos.com.cronosapp.Helpers.Const;
import cronos.com.cronosapp.Helpers.RequestHandler;

public class RegistroProfesorActivity extends AppCompatActivity {

    EditText EDTMatricula, EDTNombre, EDTApellidos, EDTPassword, EDTPassword2, EDTMateria;

    ProgressDialog pdLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_profesor);

        showToolbar("Registro de Profesores", false);

        pdLoading = new ProgressDialog(RegistroProfesorActivity.this);

        EDTMatricula = (EditText) findViewById(R.id.edtMatricula);

        EDTNombre = (EditText) findViewById(R.id.edtNombre);

        EDTApellidos = (EditText) findViewById(R.id.edtApellidos);

        EDTPassword = (EditText) findViewById(R.id.edtPassword);

        EDTPassword2 = (EditText) findViewById(R.id.edtPassword2);

        EDTMateria = (EditText) findViewById(R.id.edtMateria);
    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    public void realizarRegistroProfe(View view) {
        final String matricula = EDTMatricula.getText().toString();
        final String nombre = EDTNombre.getText().toString();
        final String apellidos = EDTApellidos.getText().toString();
        final String password = EDTPassword.getText().toString();
        final String password2 = EDTPassword2.getText().toString();
        final String numgrupo = "0";
        final String type = "PROFESOR";
        final String nombremateria = EDTMateria.getText().toString();


        if(matricula.isEmpty()|| nombre.isEmpty() || apellidos.isEmpty()|| password.isEmpty() || password2.isEmpty()|| nombremateria.isEmpty()){
            Toast.makeText(this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
        }else if(!password.equals(password2)){
            Toast.makeText(this, "Las contraseñas no son iguales.", Toast.LENGTH_SHORT).show();
        }else {
            class Login extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    //this method will be running on UI thread
                    pdLoading.setMessage("\tRegistrando Profesor...");
                    pdLoading.setCancelable(false);
                    pdLoading.show();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("matricula", matricula);
                    params.put("nombre", nombre);
                    params.put("apellidos", apellidos);
                    params.put("password", password);
                    params.put("numgrupo", numgrupo);
                    params.put("type", type);
                    params.put("nombremateria", nombremateria);

                    //returing the response
                    return requestHandler.sendPostRequest(Const.URL_REGISTRO, params);
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
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegistroProfesorActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                        }else{
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RegistroProfesorActivity.this, "Profesor ya existente. ", Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), type + " No encontrado", Toast.LENGTH_LONG).show();
                    }
                }
            }

            Login login = new Login();
            login.execute();
        }
    }

    public void cancelar(View view) {
        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent2);
        finish();
    }
}
