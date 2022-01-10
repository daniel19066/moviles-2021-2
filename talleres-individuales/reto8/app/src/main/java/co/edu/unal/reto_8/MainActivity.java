package co.edu.unal.reto_8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editNombre,editUrl,editTelefono,editEmail,editProd,editClasificacion;
    Button btnAgregar, btnShow, btnBuscar, btnEditar, btnEliminar,btnLimpiar,btnFilNom,btnFilCls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNombre = (EditText)findViewById(R.id.editNombre);
        editUrl = (EditText)findViewById(R.id.editURL);
        editTelefono = (EditText)findViewById(R.id.editTelefono);
        editEmail = (EditText)findViewById(R.id.editEmail);
        editProd = (EditText)findViewById(R.id.editProductos);
        editClasificacion = (EditText)findViewById(R.id.editClasificacion);

        btnAgregar = (Button)findViewById(R.id.btnAgregar);
        btnShow = (Button)findViewById(R.id.btnShow);
        btnBuscar = (Button)findViewById(R.id.btnBuscar);
        btnEditar = (Button)findViewById(R.id.btnEdit);
        btnEliminar = (Button)findViewById(R.id.btnEliminar);
        btnLimpiar = (Button)findViewById(R.id.btnClean);
        btnFilNom = (Button)findViewById(R.id.btnFilNom);
        btnFilCls = (Button)findViewById(R.id.btnFilCls);


        final DevelopBD developBD = new DevelopBD(getApplicationContext());

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                developBD.agregarEmpresa(editNombre.getText().toString(),editUrl.getText().toString(),editTelefono.getText().toString(),editEmail.getText().toString(),editProd.getText().toString(),editClasificacion.getText().toString());
                Toast.makeText(getApplicationContext(),"AGREGADA CORRECTAMENTE",Toast.LENGTH_SHORT).show();
            }
        });

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
                EmpresaModelo empresa =  new EmpresaModelo();
                developBD.buscarEmpresa(empresa,editNombre.getText().toString());
                editUrl.setText(empresa.getUrlweb());
                editTelefono.setText(empresa.getTelefono());
                editEmail.setText(empresa.getEmail());
                editProd.setText(empresa.getProduServ());
                editClasificacion.setText(empresa.getClasifica());
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                developBD.editarEmpresa(editNombre.getText().toString(),editUrl.getText().toString(),editTelefono.getText().toString(),editEmail.getText().toString(),editProd.getText().toString(),editClasificacion.getText().toString());
                Toast.makeText(getApplicationContext(),"EDITADO CORRECTAMENTE",Toast.LENGTH_SHORT).show();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {    //falta agregar el confirmation system y sale
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                dialog.setTitle("Eliminar Empresa");
                dialog.setMessage("Esta Por borrar una empresa de la lista ¿Esta seguro de esta acción?");
                dialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.setButton(Dialog.BUTTON_POSITIVE, "Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        developBD.eliminarEmpresa(editNombre.getText().toString());
                        Toast.makeText(getApplicationContext(),"ELIMINADO CORRECTAMENTE",Toast.LENGTH_SHORT).show();
                        dialog.getClass();
                    }
                });
                dialog.show();
            }
        });

        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNombre.setText("");
                editUrl.setText("");
                editTelefono.setText("");
                editEmail.setText("");
                editProd.setText("");
                editClasificacion.setText("");
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
                System.out.println(editClasificacion.getText().toString());
                Intent filterClass = new Intent(getApplicationContext(),FiltroClass.class);
                filterClass.putExtra("clase", editClasificacion.getText().toString());
                startActivity(filterClass);
            }
        });


    }
}