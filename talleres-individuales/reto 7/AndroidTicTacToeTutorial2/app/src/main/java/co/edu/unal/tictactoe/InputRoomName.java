package co.edu.unal.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputRoomName extends AppCompatActivity {

    private Button aceptar;
    private Button buttonCancel;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_room_name);

        aceptar = findViewById(R.id.buttonAccept);
        buttonCancel = findViewById(R.id.buttonCancel);
        input = findViewById(R.id.NAME);
    }

    public void aceptar(View view) {
        Intent intent = new Intent();
        intent.putExtra("name",input.getText().toString());
        setResult(-2,intent);
        finish();
    }


    public void cancelar(View view) {
        setResult(-1);
        finish();
    }
}