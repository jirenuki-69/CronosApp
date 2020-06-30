package cronos.com.cronosapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

public class HomeProfesorActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String STATUS = "status";
    public static final String ID_CHOFER = "id_chofer";
    public static final String NOMBRE = "nombre";
    public static final String APELLIDOS = "apellidos";
    public static final String MATRICULA = "matricula";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_profesor);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String nombreprofesor = sharedPreferences.getString(NOMBRE,"");
        String apellidoprofesor = sharedPreferences.getString(APELLIDOS,"");

        showToolbar("Lobby de " + nombreprofesor + " " + apellidoprofesor, false);
    }

    public void lanzarCerrarSesion(View view) {
        showDialog(HomeProfesorActivity.this,"¿Realmente desea cerrar sesión?","Recuerde que tendra que iniciar sesión la proxima vez :)");
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
                Intent intent = new Intent(HomeProfesorActivity.this, LoginActivity.class);
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

    public void lanzarMaterias(View view) {
        Intent intent = new Intent(HomeProfesorActivity.this, MateriaActivity.class);
        startActivity(intent);
    }
}
