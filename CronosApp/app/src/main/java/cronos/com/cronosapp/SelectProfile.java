package cronos.com.cronosapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

public class SelectProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_profile);

        showToolbar("Seleccion de Rol", false);
    }

    public void lanzarLogin(View view) {
        Intent intent = new Intent(SelectProfile.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void cancelar(View view) {
        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent2);
        finish();
    }

    public void lanzarRegistroProfesor(View view) {
        Intent intent = new Intent(SelectProfile.this, RegistroProfesorActivity.class);
        startActivity(intent);
        finish();
    }

    public void lanzarRegistroAlumno(View view) {
        Intent intent = new Intent(SelectProfile.this, RegistroAlumnoActivity.class);
        startActivity(intent);
        finish();
    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
