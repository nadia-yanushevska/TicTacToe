package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EnterNamesActivity extends AppCompatActivity {

    EditText player, player2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_names);

        player = findViewById(R.id.editTextPlayerName);
        player2 = findViewById(R.id.editTextPlayerName2);

    }

    public void OnSaveBtnClick(View view) {
        if(player2.getText().length() == 0) {
            player2.setText("Android");
        }
        if(player.getText().length() == 0) {
            if(player2.getText().toString().equals("Android"))
                player.setText("Player");
            else player.setText("Android");
        }
        Intent fromNamesIntent = new Intent(this, MainActivity.class);
        fromNamesIntent.putExtra("pl", player.getText().toString());
        fromNamesIntent.putExtra("pl2", player2.getText().toString());
        startActivity(fromNamesIntent);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("text", player.getText().toString());
        outState.putString("text2", player2.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);

        player.setText(outState.getString("text"));
        player2.setText(outState.getString("text2"));
    }

}