package co.edu.unal.reto_8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editNombre,editNIT,editSupervisor,editRegion,editciiu,editMacrosector;
    Button btnAgregar, btnShow, btnBuscar, btnEditar, btnEliminar,btnLimpiar,btnFilNom,btnFilCls;
    RequestQueue requestQueue;  // This is our requests queue to process our HTTP requests.

    String baseUrl = "https://www.datos.gov.co/resource/8hn7-rpp8.json";  // This is the API base URL (GitHub API)
    String url;  // This will hold the full URL which will include the username entered in the etGitHubUser.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNombre = (EditText)findViewById(R.id.editNombre);
        editNIT = (EditText)findViewById(R.id.editNIT);
        editSupervisor = (EditText)findViewById(R.id.editSupervisor);
        editRegion = (EditText)findViewById(R.id.editRegion);
        editciiu = (EditText)findViewById(R.id.editciiu);
        editMacrosector = (EditText)findViewById(R.id.editMacrosector);
        requestQueue = Volley.newRequestQueue(this);
        btnShow = (Button)findViewById(R.id.btnShow);
        btnBuscar = (Button)findViewById(R.id.btnBuscar);
        btnLimpiar = (Button)findViewById(R.id.btnClean);
        btnFilNom = (Button)findViewById(R.id.btnFilNom);
        btnFilCls = (Button)findViewById(R.id.btnFilCls);


        final DevelopBD developBD = new DevelopBD(getApplicationContext());



        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mostrarEmpresa = new Intent(getApplicationContext(),EmpresaActivity.class);
                startActivity(mostrarEmpresa);
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String NombreEmpresa= editNombre.getText().toString();
                url = baseUrl+"?$where=razon_social%20like%20%27%25" + NombreEmpresa+"%25%27" ;
                System.out.println(url);
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
                            try {
                                // For each repo, add a new line to our repo list.
                                JSONObject jsonObj = response.getJSONObject(0);
                                String NombreJson = jsonObj.get("razon_social").toString();
                                String NitJson = jsonObj.get("nit").toString();
                                String supervisorJson = jsonObj.get("supervisor").toString();
                                String regionJson = jsonObj.get("regi_n").toString();
                                String ciiuJson = jsonObj.get("ciiu").toString();
                                String macrosectorJson = jsonObj.get("macrosector").toString();
                                System.out.println(NombreJson);
                                editNombre.setText(NombreJson);
                                editNIT.setText(NitJson);
                                editSupervisor.setText(supervisorJson);
                                editRegion.setText(regionJson);
                                editciiu.setText(ciiuJson);
                                editMacrosector.setText(macrosectorJson);

                            } catch (JSONException e) {
                                // If there is an error then output this to the logs.
                                System.out.println(e.toString());
                                System.out.println("xdxdxd verga we");
                            }
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
            }
        });



        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNombre.setText("");
                editNIT.setText("");
                editSupervisor.setText("");
                editRegion.setText("");
                editciiu.setText("");
                editMacrosector.setText("");
            }
        });

        btnFilNom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showFilter = new Intent(getApplicationContext(),Filtros.class);
                showFilter.putExtra("nombre", editNombre.getText().toString());

                startActivity(showFilter);
            }
        });

        btnFilCls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(editMacrosector.getText().toString());
                Intent filterClass = new Intent(getApplicationContext(),FiltroClass.class);
                filterClass.putExtra("clase", editSupervisor.getText().toString());
                startActivity(filterClass);
            }
        });


    }
}