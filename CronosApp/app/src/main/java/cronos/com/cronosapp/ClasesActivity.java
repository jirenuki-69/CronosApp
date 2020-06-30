package cronos.com.cronosapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import cronos.com.cronosapp.Adaptadores.ClaseAdapter;
import cronos.com.cronosapp.Adaptadores.GrupoAdapter;
import cronos.com.cronosapp.Helpers.Common;
import cronos.com.cronosapp.Helpers.Const;
import cronos.com.cronosapp.Models.Clase;

public class ClasesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private List<Clase> ClaseList;

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
        setContentView(R.layout.activity_clases);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        matriculaprofesor = sharedPreferences.getString(MATRICULA,"");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerClases);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_layout_clases);
        swipeRefreshLayout.setOnRefreshListener(this);

        //TODO INICIALIZAMOS NUESTRA LISTA DE TIPS
        ClaseList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //TODO LLAMAMOS LA METODO QUE REALIZA LA BAJADA DE DATOS Y LOS COLOCA EN EL RECYCLER VIEW
        getData();

        //TODO INICIALIZAMOS  EL ADATADOR
        adapter = new ClaseAdapter(ClaseList, this);

        //TODO AÑADIMOS EL ADATER AL RECYCLER VIEW
        recyclerView.setAdapter(adapter);
    }

    public void lanzarAsistentes(View view) {
        if(Common.currentItemClase != null){
            Intent intent2 = new Intent(ClasesActivity.this, AsistentesActivity.class);
            startActivity(intent2);

        }else{
            Toast.makeText(this, "Debes de seleccionar una Clase", Toast.LENGTH_SHORT).show();
        }
    }

    private JsonArrayRequest getDataFromServer() {
        //TODO REALIZAMOS LA LECTURA DEL JSONARRAY CON VOLLEY
        String urlGetMaterias = Const.URL_CLASES + "matriculaprofesor/" + matriculaprofesor + "/numgrupo/" + Common.currentItemGrupo.getNum_grupo() + "/nummateria/" + Common.currentItemMateria.getNum_materia();
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
        ClaseList.clear();
        swipeRefreshLayout.setRefreshing(true);
        requestQueue.add(getDataFromServer());
    }

    //TODO ESTE METODO PASA LOS DATOS A SUS GET Y SET CORRESPONDIENTES PARA SU USO
    private void parseData(JSONArray array) {
        if(array.length() == 0){
            Toast.makeText(ClasesActivity.this, "NO HAY", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);

        }else{

            for (int i = 0; i < array.length(); i++) {
                //TODO CREAMOS EL OBJETO TIP
                Clase materia = new Clase();
                JSONObject json = null;
                try {
                    //TODO OBTENEMOS EL JSON
                    json = array.getJSONObject(i);

                    //TODO AÑADIMOS DATOS AL OBJETO TIP
                    materia.setId_clase(json.getInt(Const.TAG_ID_CLASE));
                    materia.setMatricula_profesor(json.getString(Const.TAG_MATRICULA_PROFESOR_CLASE));
                    materia.setNum_materia(json.getString(Const.TAG_NUM_MATERIA_CLASE));
                    materia.setNum_grupo(json.getString(Const.TAG_NUM_GRUPO_CLASE));
                    materia.setHora_generadaqr(json.getString(Const.TAG_HORA_GENERADA_CLASE));
                    materia.setHora_limiteqr(json.getString(Const.TAG_HORA_LIMITE_CLASE));

                } catch (JSONException e) {

                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }
                //TODO AÑADIMOS EL OBJETO TIP A LA LISTA TIP
                ClaseList.add(materia);

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
        Toast.makeText(getApplicationContext(), "No Hay Clases", Toast.LENGTH_SHORT).show();
    }

    public void cancelar(View view) {
        Intent intent2 = new Intent(getApplicationContext(), HomeProfesorActivity.class);
        startActivity(intent2);
        finish();
    }
}
