package co.edu.unal.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import co.edu.unal.tictactoe.adapter.RoomsAdapter;


public class First_page extends AppCompatActivity {
    private boolean singleUser = false;
    private  Button button ;
    private  Button button1 ;
    private Intent intent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent= new Intent(this, MainMultiplayer.class);
        button = (Button) findViewById(R.id.button11);
        button1 = (Button) findViewById(R.id.button12);

        setContentView(R.layout.activity_first_page);



    }
    public void cambiar_multiplayer(View view) {
        // Do something in response to button click

                singleUser = true;
                intent.putExtra("singleUser",singleUser);
                startActivity(intent);

    }
    public void cambiar_multiplayer2(View view) {
        // Do something in response to button click


                intent.putExtra("singleUser",singleUser);
                startActivity(intent);


    }



}