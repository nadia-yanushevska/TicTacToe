package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ShowStandingsActivity extends AppCompatActivity {

    String Filename = "players_file.txt";
    ListView list;
    String[] arrayList = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_standings);

        arrayList[0] = "No records were found.";
        readFile();
        list = findViewById(R.id.standingList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(adapter);
    }

    public void OnBackBtnClick(View view) {
        Intent fromStandingIntent = new Intent(this, MainActivity.class);
        startActivity(fromStandingIntent);
    }

    public boolean fileExists(String FName) {
        File file = getBaseContext().getFileStreamPath(FName);
        return file.exists();
    }

    private void readFile() {
        if (fileExists(Filename)) {

            FileInputStream fis = null;
            try {
                fis = openFileInput(Filename);
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            }

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String sLine;
            String[] arrayLines = new String[10];
            int count = 0;

            try {
                while ((sLine = br.readLine()) != null) {
                    arrayLines[count] = sLine;
                    count++;
                    if(count == arrayLines.length)
                        arrayLines = Arrays.copyOf(arrayLines, count+10);
                }
                arrayList = Arrays.copyOf(arrayLines, count);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else Toast.makeText(this, "File does not exist.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArray("array", arrayList);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);

        arrayList = outState.getStringArray("array");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(adapter);
    }
}