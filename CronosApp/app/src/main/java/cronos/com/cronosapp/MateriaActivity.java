package cronos.com.cronosapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cronos.com.cronosapp.Adaptadores.MateriaAdapter;
import cronos.com.cronosapp.Helpers.Common;
import cronos.com.cronosapp.Helpers.Const;
import cronos.com.cronosapp.Models.Materia;

public class MateriaActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private List<Materia> MateriaList;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    //Volley Request Queue
    private RequestQueue requestQueue;

    private SwipeRefreshLayout swipeRefreshLayout;

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
        setContentView(R.layout.activity_materias);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        matriculaprofesor = sharedPreferences.getString(MATRICULA,"");

        recyclerView = (RecyclerView)findViewById(R.id.recylerMaterias);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_layout_materias);
        swipeRefreshLayout.setOnRefreshListener(this);

        //TODO INICIALIZAMOS NUESTRA LISTA DE TIPS
        MateriaList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //TODO LLAMAMOS LA METODO QUE REALIZA LA BAJADA DE DATOS Y LOS COLOCA EN EL RECYCLER VIEW
        getData();

        //TODO INICIALIZAMOS  EL ADATADOR
        adapter = new MateriaAdapter(MateriaList, this);

        //TODO AÑADIMOS EL ADATER AL RECYCLER VIEW
        recyclerView.setAdapter(adapter);
    }

    /*
     *TODO PROCESAMOS LA PETICION HTTP Y CAPTURAMOS LOS ELEMENTOS JSON RECIBIDOS
     *
     **/
    private JsonArrayRequest getDataFromServer() {
        //TODO REALIZAMOS LA LECTURA DEL JSONARRAY CON VOLLEY
        String urlGetMaterias = Const.URL_MATERIAS + matriculaprofesor;
        Log.d("JSONREQ",urlGetMaterias);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlGetMaterias,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //TODO LLAMAMOS AL METODO PARSEDATA SI HUBO CAPTURA DE DATOS

                        parseData(response);

                        //TODO DE IGUAL FORMA OCULTAMOS EL PROGRESS BAR Y EL TEXTO DE CARGA

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO SI HAY ERROR OCULTAMOS EL PROGRESS BAR Y EL TEXTO DE CARGA Y MOSTRAMOS EL CONTENEDOR QUE MARCA ERROR
                        //TODO Y EL BOTON PARA RECARGAR

                        lanzarError();
                    }
                });
        //TODO RETORNAMOS LO CAPTURADO SEA ERROR O DATOS
        return jsonArrayRequest;
    }

    //TODO ESTE METODO ES EL QUE HACE QUE BAJEMOS LOS DATOS DE LA RED
    private void getData() {
        //TODO AÑADIENDO AL  queue LLAMANDO AL METODO getDataFromServer
        MateriaList.clear();
        swipeRefreshLayout.setRefreshing(true);
        requestQueue.add(getDataFromServer());
    }

    //TODO ESTE METODO PASA LOS DATOS A SUS GET Y SET CORRESPONDIENTES PARA SU USO
    private void parseData(JSONArray array) {
        if(array.length() == 0){
            Toast.makeText(MateriaActivity.this, "NO HAY", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);

        }else{

            for (int i = 0; i < array.length(); i++) {
                //TODO CREAMOS EL OBJETO TIP
                Materia materia = new Materia();
                JSONObject json = null;
                try {
                    //TODO OBTENEMOS EL JSON
                    json = array.getJSONObject(i);

                    //TODO AÑADIMOS DATOS AL OBJETO TIP
                    materia.setNum_materia(json.getInt(Const.TAG_NUM_MATERIA));
                    materia.setNombre_materia(json.getString(Const.TAG_NOMBRE_MATERIA));

                } catch (JSONException e) {

                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }
                //TODO AÑADIMOS EL OBJETO TIP A LA LISTA TIP
                MateriaList.add(materia);

            }

            //TODO NOTIFICAMOS AL ADAPTADOR PARA QUE HAGA CAMBIOS
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        getData();
    }

    private void lanzarError(){
        Toast.makeText(getApplicationContext(), "Error al obtener materias", Toast.LENGTH_SHORT).show();
    }

    public void lanzarOpcionesARealizar(View view) {

        if(Common.currentItemMateria != null){
            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(Html.fromHtml("<font color='#FF0000'>Opciones que Puedo Realizar: </font>"));

            // add a list
            final String[] estados = {"Generar Clase", "Ver Clases", "Modificar Materia"};
            builder.setItems(estados, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0: // VALE 3 QUE ES IGUAL A EN VIAJE A RETRASADO
                            Intent intent2 = new Intent(MateriaActivity.this, GruposActivity.class);
                            intent2.putExtra("type", "generar");
                            startActivity(intent2);

                            break;
                        case 1:
                            Intent intent3 = new Intent(MateriaActivity.this, GruposActivity.class);
                            intent3.putExtra("type", "ver");
                            startActivity(intent3);

                            break;
                        case 2:
                            Intent intent = new Intent(MateriaActivity.this, ActualizarMateriaActivity.class);
                            startActivity(intent);

                            break;
                    }
                }
            });

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            Toast.makeText(MateriaActivity.this, "Primero Debe Seleccionar una Materia", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelar(View view) {
        Intent intent2 = new Intent(getApplicationContext(), HomeProfesorActivity.class);
        startActivity(intent2);
        finish();
    }
}
