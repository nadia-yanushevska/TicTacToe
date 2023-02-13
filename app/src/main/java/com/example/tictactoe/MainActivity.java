package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String player, player2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent fromNamesIntent = getIntent();
        player = fromNamesIntent.getStringExtra("pl");
        player2 = fromNamesIntent.getStringExtra("pl2");
        if(player2 == null)
            player2 = "Android";
        if(player == null) {
            if(player2.equals("Android"))
                player = "Player";
            else player = "Android";
        }
    }

    public void OnNamesBtnClick(View view) {
        Intent toNamesIntent = new Intent(this, EnterNamesActivity.class);
        startActivity(toNamesIntent);
    }

    public void OnGameBtnClick(View view) {
        Intent toGameIntent = new Intent(this, PlayGameActivity.class);
        toGameIntent.putExtra("pl", player);
        toGameIntent.putExtra("pl2", player2);
        startActivity(toGameIntent);
    }

    public void OnStandingBtnClick(View view) {
        Intent toStandingIntent = new Intent(this, ShowStandingsActivity.class);
        startActivity(toStandingIntent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("pl", player);
        outState.putString("pl2", player2);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);

        player = outState.getString("pl");
        player2 = outState.getString("pl2");
    }
}