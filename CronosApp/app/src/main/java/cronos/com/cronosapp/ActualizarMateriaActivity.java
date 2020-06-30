package cronos.com.cronosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cronos.com.cronosapp.Helpers.Common;
import cronos.com.cronosapp.Helpers.Const;
import cronos.com.cronosapp.Helpers.RequestHandler;

public class ActualizarMateriaActivity extends AppCompatActivity {

    EditText campoMateria;
    ProgressDialog pdLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_materia);

        pdLoading = new ProgressDialog(ActualizarMateriaActivity.this);

        campoMateria = (EditText) findViewById(R.id.edtMateriaEditar);

        campoMateria.setText("" + Common.currentItemMateria.getNombre_materia());
    }

    public void lanzarActualizarMateria(View view) {
        final String materia = campoMateria.getText().toString();

        if(materia.isEmpty()){
            Toast.makeText(this, "No pueden haber campos vacios", Toast.LENGTH_SHORT).show();
        }else {
            class ActualizarMaterias extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    //this method will be running on UI thread
                    pdLoading.setMessage("\tActualizando...");
                    pdLoading.setCancelable(false);
                    pdLoading.show();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("idmateria", "" + Common.currentItemMateria.getNum_materia());
                    params.put("nuevonombremateria", materia);
                    //returing the response
                    return requestHandler.sendPostRequest(Const.URL_ACTUALIZAR_MATERIA, params);
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
                            Intent intent = new Intent(ActualizarMateriaActivity.this, HomeProfesorActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ActualizarMateriaActivity.this, "Exception: " + e, Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), " No encontrado", Toast.LENGTH_LONG).show();
                    }
                }
            }

            ActualizarMaterias login = new ActualizarMaterias();
            login.execute();
        }
    }

    public void cancelar(View view) {
        Intent intent2 = new Intent(getApplicationContext(), HomeProfesorActivity.class);
        startActivity(intent2);
        finish();
    }
}
