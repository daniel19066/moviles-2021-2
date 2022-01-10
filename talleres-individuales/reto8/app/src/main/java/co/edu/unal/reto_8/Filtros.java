package co.edu.unal.reto_8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class Filtros extends AppCompatActivity {

    private RecyclerView recyclerViewEmpresa;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresas);
        String nombre = getIntent().getExtras().getString("nombre");

        recyclerViewEmpresa = (RecyclerView)findViewById(R.id.recyclerEmpresas);
        recyclerViewEmpresa.setLayoutManager(new LinearLayoutManager(this));

        DevelopBD developBD = new DevelopBD(getApplicationContext());

        adapter = new RecyclerAdapter(developBD.showEmpresasNombre(nombre));
        recyclerViewEmpresa.setAdapter(adapter);
    }

}