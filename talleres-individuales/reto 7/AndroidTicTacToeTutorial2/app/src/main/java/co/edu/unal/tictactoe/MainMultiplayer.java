package co.edu.unal.tictactoe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.unal.tictactoe.adapter.RoomsAdapter;

public class MainMultiplayer extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore db ;
    private List<String> nombres;
    private RoomsAdapter roomsAdapter;
    private Context context;
    private Button create;
    private Map<String, Object> juegoOnline ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_multiplayer);

        recyclerView = findViewById(R.id.recyclerview);
        db = FirebaseFirestore.getInstance();

        nombres = new ArrayList<>();
        context = this;

        create = findViewById(R.id.buttonCreate);
        juegoOnline = new HashMap<>();
        getAllData();
    }

    private void getAllData(){
        db.collection("board")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                nombres.add(document.getId());
                                System.out.println(document.getId() + " => " + document.getData());
                            }

                            roomsAdapter = new RoomsAdapter(nombres,context);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            recyclerView.setAdapter(roomsAdapter);
                            recyclerView.setVisibility(View.VISIBLE);

                        } else {
                                System.out.println( "Error getting documents: ");
                        }
                    }
                });
    }


    public void createRoom(View view) {
        Intent intent = new Intent(this,InputRoomName.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==-2){
            String name = data.getStringExtra("name");

            juegoOnline.put("0", " ");
            juegoOnline.put("1", " ");
            juegoOnline.put("2", " ");
            juegoOnline.put("3", " ");
            juegoOnline.put("4", " ");
            juegoOnline.put("5", " ");
            juegoOnline.put("6", " ");
            juegoOnline.put("7", " ");
            juegoOnline.put("8", " ");
            juegoOnline.put("turno1", "");
            juegoOnline.put("turno2", "");
            juegoOnline.put("turnoactual", "1");
            juegoOnline.put("name",name);
            db.collection("board").document(name)
                    .set(juegoOnline)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            System.out.println("funciono escribir");
                            Intent intent = new Intent(context,MainActivity.class);
                            intent.putExtra("name",name);
                            intent.putExtra("singleUser",false);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("no funciono escribir");
                        }
                    });

        }

    }
}