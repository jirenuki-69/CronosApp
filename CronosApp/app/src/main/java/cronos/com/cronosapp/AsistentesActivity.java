package cronos.com.cronosapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
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

import cronos.com.cronosapp.Adaptadores.AsistenteAdapter;
import cronos.com.cronosapp.Adaptadores.ClaseAdapter;
import cronos.com.cronosapp.Helpers.Common;
import cronos.com.cronosapp.Helpers.Const;
import cronos.com.cronosapp.Models.Asistente;
import cronos.com.cronosapp.Models.Clase;

public class AsistentesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private List<Asistente> AsistenteList;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    //Volley Request Queue
    private RequestQueue requestQueue;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistentes);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerAsistentes);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_layout_asistentes);
        swipeRefreshLayout.setOnRefreshListener(this);

        //TODO INICIALIZAMOS NUESTRA LISTA DE TIPS
        AsistenteList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //TODO LLAMAMOS LA METODO QUE REALIZA LA BAJADA DE DATOS Y LOS COLOCA EN EL RECYCLER VIEW
        getData();

        //TODO INICIALIZAMOS  EL ADATADOR
        adapter = new AsistenteAdapter(AsistenteList, this);

        //TODO AÑADIMOS EL ADATER AL RECYCLER VIEW
        recyclerView.setAdapter(adapter);
    }

    public void lanzarFinalizarTodo(View view) {
        Common.currentItemGrupo = null;
        Common.currentItemMateria = null;
        Common.currentItemClase = null;
        Intent intent2 = new Intent(AsistentesActivity.this, HomeProfesorActivity.class);
        startActivity(intent2);
        finish();
    }

    public void cancelar(View view) {
        Intent intent2 = new Intent(getApplicationContext(), HomeProfesorActivity.class);
        startActivity(intent2);
        finish();
    }

    private JsonArrayRequest getDataFromServer() {
        //TODO REALIZAMOS LA LECTURA DEL JSONARRAY CON VOLLEY
        String urlGetMaterias = Const.URL_ASISTENTES + Common.currentItemClase.getId_clase();
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
        AsistenteList.clear();
        swipeRefreshLayout.setRefreshing(true);
        requestQueue.add(getDataFromServer());
    }

    //TODO ESTE METODO PASA LOS DATOS A SUS GET Y SET CORRESPONDIENTES PARA SU USO
    private void parseData(JSONArray array) {
        if(array.length() == 0){
            Toast.makeText(AsistentesActivity.this, "NO HAY", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);

        }else{

            for (int i = 0; i < array.length(); i++) {
                //TODO CREAMOS EL OBJETO TIP
                Asistente materia = new Asistente();
                JSONObject json = null;
                try {
                    //TODO OBTENEMOS EL JSON
                    json = array.getJSONObject(i);

                    //TODO AÑADIMOS DATOS AL OBJETO TIP
                    materia.setMatricula(json.getString(Const.TAG_MATRICULA_ASISTENTE));
                    materia.setNombre(json.getString(Const.TAG_NOMBRE_ASISTENTE));
                    materia.setApellidos(json.getString(Const.TAG_APELLIDO_ASISTENTE));
                    materia.setContraseña(json.getString(Const.TAG_CONTRASEÑA_ASISTENTE));
                    materia.setNum_grupo(json.getString(Const.TAG_NUM_GRUPO_ASISTENTE));

                } catch (JSONException e) {

                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }
                //TODO AÑADIMOS EL OBJETO TIP A LA LISTA TIP
                AsistenteList.add(materia);

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
        Toast.makeText(getApplicationContext(), "No Hay Asistentes", Toast.LENGTH_SHORT).show();
    }
}
