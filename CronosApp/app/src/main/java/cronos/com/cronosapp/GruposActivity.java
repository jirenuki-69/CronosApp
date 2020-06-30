package cronos.com.cronosapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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

import cronos.com.cronosapp.Adaptadores.GrupoAdapter;
import cronos.com.cronosapp.Adaptadores.MateriaAdapter;
import cronos.com.cronosapp.Helpers.Common;
import cronos.com.cronosapp.Helpers.Const;
import cronos.com.cronosapp.Models.Grupo;
import cronos.com.cronosapp.Models.Materia;

public class GruposActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private List<Grupo> GrupoList;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    //Volley Request Queue
    private RequestQueue requestQueue;

    private SwipeRefreshLayout swipeRefreshLayout;

    String adondevamos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);

        Intent intent = getIntent();
        adondevamos = intent.getStringExtra("type");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerGrupos);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_layout_grupos);
        swipeRefreshLayout.setOnRefreshListener(this);

        //TODO INICIALIZAMOS NUESTRA LISTA DE TIPS
        GrupoList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //TODO LLAMAMOS LA METODO QUE REALIZA LA BAJADA DE DATOS Y LOS COLOCA EN EL RECYCLER VIEW
        getData();

        //TODO INICIALIZAMOS  EL ADATADOR
        adapter = new GrupoAdapter(GrupoList, this);

        //TODO AÑADIMOS EL ADATER AL RECYCLER VIEW
        recyclerView.setAdapter(adapter);
    }

    public void lanzarSolicitudFinal(View view) {
        switch (adondevamos) {
            case "generar": // VALE 3 QUE ES IGUAL A EN VIAJE A RETRASADO
                if(Common.currentItemGrupo != null){
                    Intent intent2 = new Intent(GruposActivity.this, GenerateQRActivity.class);
                    startActivity(intent2);
                    finish();
                }else{
                    Toast.makeText(this, "Debe de Seleccionar un Grupo", Toast.LENGTH_SHORT).show();
                }
                break;
            case "ver":
                if(Common.currentItemGrupo != null){
                    Intent intent5 = new Intent(GruposActivity.this, ClasesActivity.class);
                    startActivity(intent5);

                }else{
                    Toast.makeText(this, "Debe de Seleccionar un Grupo", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void cancelar(View view) {
        Intent intent2 = new Intent(getApplicationContext(), HomeProfesorActivity.class);
        startActivity(intent2);
        finish();
    }

    private JsonArrayRequest getDataFromServer() {
        //TODO REALIZAMOS LA LECTURA DEL JSONARRAY CON VOLLEY
        String urlGetMaterias = Const.URL_GRUPOS;
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
        GrupoList.clear();
        swipeRefreshLayout.setRefreshing(true);
        requestQueue.add(getDataFromServer());
    }

    //TODO ESTE METODO PASA LOS DATOS A SUS GET Y SET CORRESPONDIENTES PARA SU USO
    private void parseData(JSONArray array) {
        if(array.length() == 0){
            Toast.makeText(GruposActivity.this, "NO HAY", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);

        }else{

            for (int i = 0; i < array.length(); i++) {
                //TODO CREAMOS EL OBJETO TIP
                Grupo materia = new Grupo();
                JSONObject json = null;
                try {
                    //TODO OBTENEMOS EL JSON
                    json = array.getJSONObject(i);

                    //TODO AÑADIMOS DATOS AL OBJETO TIP
                    materia.setNum_grupo(json.getInt(Const.TAG_NUM_GRUPO));
                    materia.setCarrera(json.getString(Const.TAG_CARRERA));
                    materia.setSemestre(json.getString(Const.TAG_SEMESTRE));

                } catch (JSONException e) {

                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }
                //TODO AÑADIMOS EL OBJETO TIP A LA LISTA TIP
                GrupoList.add(materia);

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
}
