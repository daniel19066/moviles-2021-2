package co.edu.unal.reto_8;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DevelopBD extends SQLiteOpenHelper {
    private static final String NOMBRE_BD = "sql_rene_BD_reto8";
    private static final int VERSION_BD = 1;
    private static final String TABALA_EMPRESAS = "CREATE TABLE EMPRESAS(NOMBRE TEXT PRIMARY KEY, URLWEB TEXT, TELEFONO TEXT, CORREO TEXT,PRODSERV TEXT, CLASIFICACION TEXT)";

    public DevelopBD(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
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
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM EMPRESAS WHERE NOMBRE='"+nombre+"'",null);
        List<EmpresaModelo> empresasFil = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                empresasFil.add(new EmpresaModelo(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)));
            }while(cursor.moveToNext());
        }
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
