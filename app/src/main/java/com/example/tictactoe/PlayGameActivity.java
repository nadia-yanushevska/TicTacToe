package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PlayGameActivity extends AppCompatActivity {

    String player, player2, players;
    int plPoints = 0, pl2Points = 0;
    TextView playersField, pointsField, turnField;
    int turns = 0;
    int[][] game = new int[3][3];
    String Filename = "players_file.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        Intent fromMenuIntent = getIntent();
        player = fromMenuIntent.getStringExtra("pl");
        player2 = fromMenuIntent.getStringExtra("pl2");
        playersField = findViewById(R.id.gameText);
        playersField.setText(player + " vs " + player2);
        pointsField = findViewById(R.id.gamePoints);
        pointsField.setText(plPoints + " : " + pl2Points);
        turnField = findViewById(R.id.gameTurn);
        turnField.setText(player + "'s Turn");
        initializeGame();
        firstAndroidMove(turns, player);
    }

    public void initializeGame() {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                game[r][c] = 0;
    }

    public void fillEmptyCells() {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if(game[r][c] == 0)
                    game[r][c] = 3;

    }

    public void emptyTheButtons() {
        Button btn = findViewById(R.id.trBtn);
        btn.setText("");
        btn = findViewById(R.id.tcBtn);
        btn.setText("");
        btn = findViewById(R.id.tlBtn);
        btn.setText("");
        btn = findViewById(R.id.mrBtn);
        btn.setText("");
        btn = findViewById(R.id.mcBtn);
        btn.setText("");
        btn = findViewById(R.id.mlBtn);
        btn.setText("");
        btn = findViewById(R.id.brBtn);
        btn.setText("");
        btn = findViewById(R.id.bcBtn);
        btn.setText("");
        btn = findViewById(R.id.blBtn);
        btn.setText("");

    }

    public void firstAndroidMove(int turns, String player) {
        if(player.equals("Android") && turns == 0) {
            Button btn = findViewById(R.id.mcBtn);
            btn.performClick();
        }
    }

    public void OnCellBtnClick(View view) {
        Button cell = (Button) view;
        int row = dimensionInterpreter(getDimension(cell), 'r'), col = dimensionInterpreter(getDimension(cell), 'c');
        if (turns % 2 == 0) {
            if (game[row][col] == 0) {
                game[row][col] = 1;
                cell.setText("x");
                turns++;
            } else return;
        } else {
            if (game[row][col] == 0) {
                game[row][col] = 2;
                cell.setText("o");
                turns++;
            } else return;
        }
        if (turns % 2 == 0)
            turnField.setText(player + "'s Turn");
        else turnField.setText(player2 + "'s Turn");

        if (hasWon()) {
            if (turns % 2 == 0) {
                Toast.makeText(this, player2 + " won the game. Congratulations!", Toast.LENGTH_SHORT).show();
                pl2Points++;
                switchPlayers();
                fillEmptyCells();
                pointsField.setText(plPoints + " : " + pl2Points);
            } else {
                Toast.makeText(this, player + " won the game. Congratulations!", Toast.LENGTH_SHORT).show();
                plPoints++;
                fillEmptyCells();
                pointsField.setText(plPoints + " : " + pl2Points);
            }
            return;
        }

        if (turns == 9) {
            Toast.makeText(this, "No more moves left. It's a tie.", Toast.LENGTH_SHORT).show();
            switchPlayers();
            return;
        }
        // call android player that will call this method
        if ((turns % 2 == 0 && player.equals("Android")) || (turns % 2 == 1 && player2.equals("Android"))) {
            PlayerThread thread = new PlayerThread();
            thread.execute(game[0][0], game[0][1], game[0][2],
                    game[1][0], game[1][1], game[1][2],
                    game[2][0], game[2][1], game[2][2], turns%2);
        }
    }

    public void OnReBtnClick(View view) {
        turns = 0;
        initializeGame();
        emptyTheButtons();
        pointsField.setText(plPoints + " : " + pl2Points);
        turnField.setText(player + "'s Turn");
        firstAndroidMove(turns, player);
    }

    public void setButtonTexts(int[][] buttons) {
        Button btn;
        String id;
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++) {
                if(buttons[i][j] == 0)
                    continue;
                id = i + "." + j;
                btn = getButtonId(id);
                if(buttons[i][j] == 1)
                    btn.setText("x");
                else if(buttons[i][j] == 2)
                    btn.setText("o");
            }
    }

    public Button getButtonId(String index) {
        Button button = findViewById(R.id.trBtn);
        if(index.equals("0.1"))
            button = findViewById(R.id.tcBtn);
        else if(index.equals("0.2"))
            button = findViewById(R.id.tlBtn);
        else if(index.equals("1.0"))
            button = findViewById(R.id.mrBtn);
        else if(index.equals("1.1"))
            button = findViewById(R.id.mcBtn);
        else if(index.equals("1.2"))
            button = findViewById(R.id.mlBtn);
        else if(index.equals("2.0"))
            button = findViewById(R.id.brBtn);
        else if(index.equals("2.1"))
            button = findViewById(R.id.bcBtn);
        else if(index.equals("2.2"))
            button = findViewById(R.id.blBtn);
        return button;
    }

    public String getDimension(Button btn) {
        int id = btn.getId();
        switch (id) {
            case R.id.trBtn:
                return "0.0";
            case R.id.tcBtn:
                return "0.1";
            case R.id.tlBtn:
                return "0.2";
            case R.id.mrBtn:
                return "1.0";
            case R.id.mcBtn:
                return "1.1";
            case R.id.mlBtn:
                return "1.2";
            case R.id.brBtn:
                return "2.0";
            case R.id.bcBtn:
                return "2.1";
            case R.id.blBtn:
                return "2.2";
        }
        return null;
    }

    public int dimensionInterpreter(String str, char d) {
        if (d == 'r')
            return Character.getNumericValue(str.charAt(0));
        else if (d == 'c')
            return Character.getNumericValue(str.charAt(2));
        else return -1;
    }

    public void switchPlayers() {
        String pltemp;
        int ptemp;

        pltemp = player;
        player = player2;
        player2 = pltemp;

        ptemp = plPoints;
        plPoints = pl2Points;
        pl2Points = ptemp;

        playersField.setText(player + " vs " + player2);
        pointsField.setText(plPoints + " : " + pl2Points);
    }

    public boolean hasWon() {
        for (int r = 0; r < 3; r++) {
            if (game[r][0] != 0 && game[r][0] == game[r][1]
                    && game[r][0] == game[r][2])
                return true;
        }
        for (int c = 0; c < 3; c++) {
            if (game[0][c] != 0 && game[0][c] == game[1][c]
                    && game[0][c] == game[2][c])
                return true;
        }
        if (game[0][0] != 0 && game[0][0] == game[1][1] && game[0][0] == game[2][2])
            return true;
        else if (game[0][2] != 0 && game[0][2] == game[1][1] && game[0][2] == game[2][0])
            return true;
        return false;
    }

    public class PlayerThread extends AsyncTask<Integer, Void, String> {
        int xoTurn;
        int[][] game = new int[3][3];
        String cellIndex;

        @Override
        protected String doInBackground(Integer... integers) {
            game[0][0] = integers[0];
            game[0][1] = integers[1];
            game[0][2] = integers[2];
            game[1][0] = integers[3];
            game[1][1] = integers[4];
            game[1][2] = integers[5];
            game[2][0] = integers[6];
            game[2][1] = integers[7];
            game[2][2] = integers[8];
            xoTurn = integers[9];

            cellIndex = AndroidPlayer(xoTurn);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Button button = getButtonId(cellIndex);
            button.performClick();
        }

        public String AndroidPlayer(int turn) {
            String btnId;
            int count, count2;
            if(turn % 2 == 0) {
                count = 1;
                count2 = 2;
            }
            else {
                count = 2;
                count2 = 1;
            }

            btnId = CloseToWinCheck(count);
            if(btnId == null)
                btnId = CloseToWinCheck(count2);
            if(btnId == null)
                btnId = FindCell(count);

            return btnId;
        }

        public String CloseToWinCheck(int number) {
            String result = null;
            for(int r = 0; r < 3; r++) {
                if (game[r][0] == number || game[r][1] == number) {
                    if (game[r][0] == game[r][1] && game[r][2] == 0)
                        result = r + ".2";
                    else if(game[r][0] == game[r][2] && game[r][1] == 0)
                        result = r + ".1";
                    else if(game[r][1] == game[r][2] && game[r][0] == 0)
                        result = r + ".0";
                }
            }
            for(int c = 0; c < 3; c++) {
                if(game[0][c] == number || game[1][c] == number) {
                    if(game[0][c] == game[1][c] && game[2][c] == 0)
                        result = "2." + c;
                    else if(game[0][c] == game[2][c] && game[1][c] == 0)
                        result = "1." + c;
                    else if(game[1][c] == game[2][c] && game[0][c] == 0)
                        result = "0." + c;
                }
            }
            if(game[0][0] == number || game[1][1] == number) {
                if(game[0][0] == game[1][1] && game[2][2] == 0)
                    result = "2.2";
                else if(game[0][0] == game[2][2] && game[1][1] == 0)
                    result = "1.1";
                else if(game[1][1] == game[2][2] && game[0][0] == 0)
                    result = "0.0";
            }
            if(game[0][2] == number || game[1][1] == number) {
                if(game[0][2] == game[1][1] && game[2][0] == 0)
                    result = "2.0";
                else if(game[0][2] == game[2][0] && game[1][1] == 0)
                    result = "1.1";
                else if(game[1][1] == game[2][0] && game[0][2] == 0)
                    result = "0.2";
            }
            return result;
        }

        public String FindCell(int number) {
            String emptyCell = null;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (game[i][j] == 0) {
                        emptyCell = i + "." + j;
                        break;
                    }
                }
                if (emptyCell != null) break;
            }
            if (game[1][1] == 0)
                return "1.1";
            return emptyCell;
        }
    }

    private void writeFile() {
        try {
            FileOutputStream fos = openFileOutput(Filename, MODE_APPEND);
            fos.write(players.getBytes());
            fos.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("pl", player);
        outState.putString("pl2", player2);
        outState.putInt("plPoint", plPoints);
        outState.putInt("pl2Point", pl2Points);
        outState.putInt("turn", turns);
        for(int i = 0; i < 3; i++)
            outState.putIntArray("item"+i, game[i]);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);

        player = outState.getString("pl");
        player2 = outState.getString("pl2");
        plPoints = outState.getInt("plPoint");
        pl2Points = outState.getInt("pl2Point");
        turns = outState.getInt("turn");
        for(int i = 0; i < 3; i++)
            game[i] = outState.getIntArray("item"+i);

        playersField.setText(player + " vs " + player2);
        pointsField.setText(plPoints + " : " + pl2Points);
        turnField.setText(player + "'s Turn");
        setButtonTexts(game);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss", Locale.getDefault());

        if(isFinishing()) {
            if(plPoints != 0)
                players = player + " : " + plPoints + ". Played at : " + sdf.format(new Date()) + "\n";
            if(pl2Points != 0)
                players = player2 + " : " + pl2Points + ". Played at : " + sdf.format(new Date()) + "\n";
            if(players != null) {
                writeFile();
                players = null;
            }
        }
    }
}