package co.edu.unal.reto_8;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DevelopBD extends SQLiteOpenHelper {
    private static final String NOMBRE_BD = "sql_rene_BD_reto8";
    private static final int VERSION_BD = 1;
    private static final String baseUrl = "https://www.datos.gov.co/resource/8hn7-rpp8.json";  // This is the API base URL (GitHub API)
    private String url;
    private RequestQueue requestQueue;
    private static final String TABALA_EMPRESAS = "CREATE TABLE EMPRESAS(NOMBRE TEXT PRIMARY KEY, URLWEB TEXT, TELEFONO TEXT, CORREO TEXT,PRODSERV TEXT, CLASIFICACION TEXT)";

    public DevelopBD(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
        requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABALA_EMPRESAS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABALA_EMPRESAS);
        db.execSQL(TABALA_EMPRESAS);
    }

    public void agregarEmpresa(String nombre, String url ,String telefono, String email, String produc,String clasificacion){
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null){
            db.execSQL("INSERT INTO EMPRESAS VALUES('"+nombre+"','"+url+"','"+telefono+"','"+email+"','"+produc+"','"+clasificacion+"')");
            db.close();
        }
    }

    public List<EmpresaModelo> showEmpresas(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM EMPRESAS",null);
        List<EmpresaModelo> empresas = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                empresas.add(new EmpresaModelo(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)));
            }while(cursor.moveToNext());
        }
        return empresas;
    }

    public void buscarEmpresa(EmpresaModelo modelo, String nombreSearch){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM EMPRESAS WHERE NOMBRE='"+nombreSearch+"'",null);
        if(cursor.moveToFirst()){
            do{
                modelo.setUrlweb(cursor.getString(1));
                modelo.setTelefono(cursor.getString(2));
                modelo.setEmail(cursor.getString(3));
                modelo.setProduServ(cursor.getString(4));
                modelo.setClasifica(cursor.getString(5));
            }while(cursor.moveToNext());
        }
    }

    public void editarEmpresa(String nombre, String url ,String telefono, String email, String produc,String clasificacion){
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null){
            db.execSQL("UPDATE EMPRESAS SET URLWEB='"+url+"',TELEFONO='"+telefono+"',CORREO='"+email+"',PRODSERV='"+produc+"',CLASIFICACION='"+clasificacion+"' WHERE NOMBRE='"+nombre+"'");
            db.close();
        }
    }

    public void eliminarEmpresa(String nombre){
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null){
            db.execSQL("DELETE FROM EMPRESAS WHERE NOMBRE='"+nombre+"'");
            db.close();
        }
    }


    public List<EmpresaModelo> showEmpresasNombre(String nombre){
        String NombreEmpresa= nombre;
        url = baseUrl+"?$where=razon_social%20like%20%27%25" + NombreEmpresa+"%25%27" ;
        System.out.println(url);
        List<EmpresaModelo> empresasFil = new ArrayList<>();
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
                            JSONObject jsonObj = response.getJSONObject(0);
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
        System.out.println("tamaaaaaaaaaaaaaaaaaaaaaaano"+empresasFil.size());
        return empresasFil;
    }

    public List<EmpresaModelo> showEmpresasClase(String clase){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM EMPRESAS WHERE CLASIFICACION='"+clase+"'",null);
        List<EmpresaModelo> filClase = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                filClase.add(new EmpresaModelo(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)));
            }while(cursor.moveToNext());
        }
        return filClase;
    }
}
