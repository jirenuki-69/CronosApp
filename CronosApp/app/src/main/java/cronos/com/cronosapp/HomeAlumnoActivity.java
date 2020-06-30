package cronos.com.cronosapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cronos.com.cronosapp.Helpers.Const;
import cronos.com.cronosapp.Helpers.RequestHandler;

public class HomeAlumnoActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String NOMBRE = "nombre";
    public static final String APELLIDOS = "apellidos";

    public static final String MATRICULA = "matricula";

    String matriculaAlumno = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_alumno);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String nombreprofesor = sharedPreferences.getString(NOMBRE,"");
        String apellidoprofesor = sharedPreferences.getString(APELLIDOS,"");
        matriculaAlumno = sharedPreferences.getString(MATRICULA,"");
        showToolbar("Lobby de " + nombreprofesor + " " + apellidoprofesor, false);
    }

    public void lanzarCerrarSesion(View view) {
        showDialog(HomeAlumnoActivity.this,"¿Realmente desea cerrar sesión?","Recuerde que tendra que iniciar sesión la proxima vez :)");
    }

    public void lanzarEscaneo(View view) {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result =   IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this,"Cancelado",Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                solicitarAsistencia(matriculaAlumno,result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void solicitarAsistencia(String matriculaAlumno, String idClase) {
        class EscannerQr extends AsyncTask<Void, Void, String> {
            ProgressDialog pdLoading = new ProgressDialog(HomeAlumnoActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //this method will be running on UI thread
                pdLoading.setMessage("\tSolicitando Asistencia...");
                pdLoading.setCancelable(false);
                pdLoading.show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("matricula_alumno", "" +matriculaAlumno);
                params.put("idclase", ""  +idClase);

                //returing the response
                return requestHandler.sendPostRequest(Const.URL_SOLICITAR_ASISTENCIA, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pdLoading.dismiss();
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Exception =>" + e, Toast.LENGTH_LONG).show();
                }
            }
        }

        EscannerQr qrcode = new EscannerQr();
        qrcode.execute();
    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    public void showDialog(Activity activity, String title, CharSequence message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(Html.fromHtml("<font color='#FF0000'>" + title + "</font>"));
        builder.setMessage(Html.fromHtml("<font color='#222222'>" + message + "</font>"));
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                finish();
                Intent intent = new Intent(HomeAlumnoActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
