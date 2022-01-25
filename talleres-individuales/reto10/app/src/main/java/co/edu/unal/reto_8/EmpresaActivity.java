package co.edu.unal.reto_8;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EmpresaActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEmpresa;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresas);

        recyclerViewEmpresa = (RecyclerView)findViewById(R.id.recyclerEmpresas);
        recyclerViewEmpresa.setLayoutManager(new LinearLayoutManager(this));

        DevelopBD developBD = new DevelopBD(getApplicationContext());

        adapter = new RecyclerAdapter(developBD.showEmpresas());
        recyclerViewEmpresa.setAdapter(adapter);
    }

}