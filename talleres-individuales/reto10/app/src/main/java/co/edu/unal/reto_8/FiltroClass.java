package co.edu.unal.reto_8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.Request;
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

public class FiltroClass extends AppCompatActivity {

    private RecyclerView recyclerViewEmpresa;
    private RecyclerAdapter adapter;
    private static final String baseUrl = "https://www.datos.gov.co/resource/8hn7-rpp8.json";  // This is the API base URL (GitHub API)
    private String url;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresas);
        String nombre = getIntent().getExtras().getString("clase");
        requestQueue = Volley.newRequestQueue(this);

        recyclerViewEmpresa = (RecyclerView)findViewById(R.id.recyclerEmpresas);
        recyclerViewEmpresa.setLayoutManager(new LinearLayoutManager(this));

        mostradtodo(nombre);
    }

    private void mostradtodo(String nombre){
        String NombreEmpresa= nombre;
        url = baseUrl+"?supervisor=" + NombreEmpresa ;
        System.out.println(url);
        List<EmpresaModelo> empresasFil = new ArrayList<>();
        empresasFil.clear();
        // Next, we create a new JsonArrayRequest. This will use Volley to make a HTTP request
        // that expects a JSON Array Response.
        // To fully understand this, I'd recommend readng the office docs: https://developer.android.com/training/volley/index.html
        System.out.println("va  entro");
        JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>(){
            @Override

            public void onResponse(JSONArray response) {
                System.out.println(" we  entro");
                // Check the length of our response (to see if the user has any repos)
                if (response.length() > 0) {
                    // The user does have repos, so let's loop through them all.
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            // For each repo, add a new line to our repo list.
                            JSONObject jsonObj = response.getJSONObject(i);
                            String NombreJson = jsonObj.get("razon_social").toString();
                            String NitJson = jsonObj.get("nit").toString();
                            String supervisorJson = jsonObj.get("supervisor").toString();
                            String regionJson = jsonObj.get("regi_n").toString();
                            String ciiuJson = jsonObj.get("ciiu").toString();
                            String macrosectorJson = jsonObj.get("macrosector").toString();
                            System.out.println(NombreJson);
                            empresasFil.add(new EmpresaModelo(NombreJson,NitJson,supervisorJson,regionJson,ciiuJson,macrosectorJson));


                        } catch (JSONException e) {
                            // If there is an error then output this to the logs.
                            System.out.println(e.toString());
                            System.out.println("xdxdxd verga we");
                        }
                    }
                    System.out.println("tamaaaaaaaaaaaaaaaaaaaaaaano"+empresasFil.size());
                    adapter = new RecyclerAdapter(empresasFil);
                    recyclerViewEmpresa.setAdapter(adapter);

                } else {
                    // The user didn't have any repos.
                    System.out.println("fallo xd.");
                }

            }

        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // If there a HTTP error then add a note to our repo list.
                        System.out.println("Error while calling REST API");
                        System.out.println(error.toString());

                    }
                }
        );
        // Add the request we just defined to our request queue.
        // The request queue will automatically handle the request as soon as it can.
        requestQueue.add(arrReq);
        requestQueue.start();


    }
}