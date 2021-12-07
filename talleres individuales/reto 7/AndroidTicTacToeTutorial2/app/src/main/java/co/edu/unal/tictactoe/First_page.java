package co.edu.unal.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class First_page extends AppCompatActivity {
    private boolean singleUser = false;
    private  Button button = (Button) findViewById(R.id.button11);
    private  Button button1 = (Button) findViewById(R.id.button12);
    private Intent intent = new Intent(this, MainActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                singleUser = true;
                intent.putExtra("singleUser",singleUser);
                startActivity(intent);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                intent.putExtra("singleUser",singleUser);
                startActivity(intent);
            }
        });

    }


}