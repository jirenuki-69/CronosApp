package cronos.com.cronosapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cronos.com.cronosapp.Helpers.Const;
import cronos.com.cronosapp.Helpers.RequestHandler;

public class RegistroAlumnoActivity extends AppCompatActivity {

    //COSAS PARA EL SPINEER
    private Spinner spinner ;
    ArrayList<String> grupos;

    EditText EDTMatricula, EDTNombre, EDTApellidos, EDTPassword, EDTPassword2;

    String grupo;

    ProgressDialog pdLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_alumno);

        showToolbar("Registro de Alumnos", false);

        pdLoading = new ProgressDialog(RegistroAlumnoActivity.this);

        EDTMatricula = (EditText) findViewById(R.id.edtMatriculaA);

        EDTNombre = (EditText) findViewById(R.id.edtNombreA);

        EDTApellidos = (EditText) findViewById(R.id.edtApellidosA);

        EDTPassword = (EditText) findViewById(R.id.edtPasswordA);

        EDTPassword2 = (EditText) findViewById(R.id.edtPassword2A);

        grupos = new ArrayList<>();

        spinner = (Spinner) findViewById(R.id.spinnerGruposA);

        listar();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                grupo =   spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                //Toast.makeText(getApplicationContext(),grupo,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    public void listar(){
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Const.URL_GRUPOS_REGISTRO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("grupos");
                            grupos.add("Seleccione un grupo");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                String carrera =jsonObject1.getString("carrera");
                                String num_grupo =jsonObject1.getString("num_grupo");
                                String semestre =jsonObject1.getString("semestre");
                                grupos.add(num_grupo + " - " + carrera + " semestre: " + semestre);
                            }
                            spinner.setAdapter(new ArrayAdapter<String>(RegistroAlumnoActivity.this, android.R.layout.simple_spinner_dropdown_item, grupos));
                        }catch (JSONException e){e.printStackTrace();}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    public void registroDeAlumnos(View view) {
        final String matricula = EDTMatricula.getText().toString();
        final String nombre = EDTNombre.getText().toString();
        final String apellidos = EDTApellidos.getText().toString();
        final String password = EDTPassword.getText().toString();
        final String password2 = EDTPassword2.getText().toString();
        String subnumgrup = grupo;
        final String type = "ALUMNO";
        final String nombremateria = "ninguna";

        if(matricula.isEmpty()|| nombre.isEmpty() || apellidos.isEmpty()|| password.isEmpty() || password2.isEmpty()){
            Toast.makeText(this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
        }else if(!password.equals(password2)){
            Toast.makeText(this, "Las contraseñas no son iguales.", Toast.LENGTH_SHORT).show();
        }else if(subnumgrup.equals("Seleccione un grupo")){
            Toast.makeText(this, "Debes Seleccionar un Grupo.", Toast.LENGTH_SHORT).show();
        }else {
            String string = subnumgrup;
            String[] parts = string.split("-");
            final String grupooficial = parts[0]; // 19
            class Login extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    //this method will be running on UI thread
                    pdLoading.setMessage("\tRegistrando Alumno...");
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
                    params.put("numgrupo", grupooficial);
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
                            Intent intent = new Intent(RegistroAlumnoActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RegistroAlumnoActivity.this, "Alumno ya existente. ", Toast.LENGTH_LONG).show();
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
