package cronos.com.cronosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.glxn.qrgen.android.QRCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cronos.com.cronosapp.Helpers.Common;
import cronos.com.cronosapp.Helpers.Const;
import cronos.com.cronosapp.Helpers.RequestHandler;

public class GenerateQRActivity extends AppCompatActivity {

    ProgressDialog pdLoading;

    SharedPreferences sharedPreferences;
    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String STATUS = "status";
    public static final String ID_CHOFER = "id_chofer";
    public static final String NOMBRE = "nombre";
    public static final String MATRICULA = "matricula";
    String matriculaprofesor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        matriculaprofesor = sharedPreferences.getString(MATRICULA,"");

        pdLoading = new ProgressDialog(GenerateQRActivity.this);

        generarCodigoQR(matriculaprofesor, "" +Common.currentItemMateria.getNum_materia(), "" +Common.currentItemGrupo.getNum_grupo());
    }

    private void generarCodigoQR(String Mmatricula, String Mmateria, String Mgrupo) {
        final String matricula = Mmatricula;
        final String materia = Mmateria;
        final String grupo = Mgrupo;

            class CreandoClase extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    //this method will be running on UI thread
                    pdLoading.setMessage("\tCreando Clase...");
                    pdLoading.setCancelable(false);
                    pdLoading.show();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("matricula_profesor", matricula);
                    params.put("num_materia", materia);
                    params.put("num_grupo", grupo);

                    //returing the response
                    return requestHandler.sendPostRequest(Const.URL_GENERAR_CLASE, params);
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
                            Bitmap bitmap = QRCode.from(obj.getString("id_clase")).bitmap();
                            // Suponiendo que tienes un ImageView con el id ivCodigoGenerado
                            ImageView imagenCodigo = findViewById(R.id.imagenGenerateQR);
                            imagenCodigo.setImageBitmap(bitmap);

                            TextView textoVen = findViewById(R.id.textoVencimientoQR);
                            textoVen.setText("" + obj.getString("hora_limite"));
                        }else{
                            Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(GenerateQRActivity.this, "Exception: " + e, Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), type + " No encontrado", Toast.LENGTH_LONG).show();
                    }
                }
            }

            CreandoClase login = new CreandoClase();
            login.execute();
    }

    public void finalizarGenerateQR(View view) {
        Common.currentItemGrupo = null;
        Common.currentItemMateria = null;
        Intent intent2 = new Intent(GenerateQRActivity.this, HomeProfesorActivity.class);
        startActivity(intent2);
        finish();
    }
}
