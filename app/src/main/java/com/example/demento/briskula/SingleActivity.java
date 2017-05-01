package com.example.demento.briskula;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class SingleActivity extends AppCompatActivity {

    private ImageButton playerCard1;
    private ImageButton playerCard2;
    private ImageButton playerCard3;
    private ImageButton computerCard1;
    private ImageButton computerCard2;
    private ImageButton computerCard3;
    private ImageButton gameCard1;
    private ImageButton gameCard2;
    private ImageButton briskulaCard;
    private TextView androidScore;
    private TextView playerScore;
    private SinglePlayer game;
    private Card currentCompCard;
    private Card currentPlayerCard;
    private int[] gameCounter; //broji odigrane partije

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*remove status bar clock and battery info*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_single);

        game = new SinglePlayer();
        playerCard1 = (ImageButton) findViewById(R.id.playerCard1);
        playerCard2 = (ImageButton) findViewById(R.id.playerCard2);
        playerCard3 = (ImageButton) findViewById(R.id.playerCard3);
        computerCard1 = (ImageButton) findViewById(R.id.computerCard1);
        computerCard2 = (ImageButton) findViewById(R.id.computerCard2);
        computerCard3 = (ImageButton) findViewById(R.id.computerCard3);
        gameCard1 = (ImageButton) findViewById(R.id.gameCard1);
        gameCard2 = (ImageButton) findViewById(R.id.gameCard2);
        briskulaCard = (ImageButton) findViewById(R.id.briskulaCard);
        androidScore = (TextView) findViewById(R.id.androidScore);
        playerScore = (TextView) findViewById(R.id.playerScore);

        playerCard1.setImageResource(game.getMe(0).cardImage);
        playerCard2.setImageResource(game.getMe(1).cardImage);
        playerCard3.setImageResource(game.getMe(2).cardImage);
        computerCard1.setImageResource(game.getBackCard().cardImage);
        computerCard2.setImageResource(game.getBackCard().cardImage);
        computerCard3.setImageResource(game.getBackCard().cardImage);
        briskulaCard.setImageResource(game.getGameCard().cardImage);
        androidScore.setText("0");
        playerScore.setText("0");
    }

    public void clickedPlayerCard1(View v){
        if (gameCard1.getVisibility() == View.INVISIBLE) {
            if (gameCard2.getVisibility() == View.INVISIBLE) { //ako comp nije igrao

                currentPlayerCard = game.getMeCard(0);
                gameCard1.setImageResource(currentPlayerCard.cardImage);
                gameCard1.setVisibility(View.VISIBLE);
                playerCard1.setVisibility(View.INVISIBLE);
                compPlaySecond();
            } else {
                currentPlayerCard = game.getMeCard(0);
                gameCard1.setImageResource(currentPlayerCard.cardImage);
                gameCard1.setVisibility(View.VISIBLE);
                playerCard1.setVisibility(View.INVISIBLE);
            }
        }
    }
    public void clickedPlayerCard2(View v){

        if (gameCard1.getVisibility() == View.INVISIBLE) {

            if (gameCard2.getVisibility() == View.INVISIBLE) { //ako comp nije igrao
                currentPlayerCard = game.getMeCard(1);
                gameCard1.setImageResource(currentPlayerCard.cardImage);
                gameCard1.setVisibility(View.VISIBLE);
                playerCard2.setVisibility(View.INVISIBLE);
                compPlaySecond();
            } else {
                currentPlayerCard = game.getMeCard(1);
                gameCard1.setImageResource(currentPlayerCard.cardImage);
                gameCard1.setVisibility(View.VISIBLE);
                playerCard2.setVisibility(View.INVISIBLE);
            }
        }
    }
    public void clickedPlayerCard3(View v){
        if (gameCard1.getVisibility() == View.INVISIBLE){
            if (gameCard2.getVisibility() == View.INVISIBLE) { //ako comp nije igrao
                currentPlayerCard = game.getMeCard(2);
                gameCard1.setImageResource(currentPlayerCard.cardImage);
                gameCard1.setVisibility(View.VISIBLE);
                playerCard3.setVisibility(View.INVISIBLE);
                compPlaySecond();
            } else {
                currentPlayerCard = game.getMeCard(2);
                gameCard1.setImageResource(currentPlayerCard.cardImage);
                gameCard1.setVisibility(View.VISIBLE);
                playerCard3.setVisibility(View.INVISIBLE);
            }
        }
    }
    public void compPlayFirst() { //prvi igra comp
        currentCompCard = game.bestCompCard(); //karta koja sluzi za igru od srane comp-a
        gameCard2.setImageResource(currentCompCard.cardImage);
        gameCard2.setVisibility(View.VISIBLE);
    }
    public void compPlaySecond() { //ako je prije player igra
        currentCompCard = game.getCompCard(currentPlayerCard); //karta koja sluzi za igru od srane comp-a
        gameCard2.setImageResource(currentCompCard.cardImage);
        gameCard2.setVisibility(View.VISIBLE);
    }
    public void pick1(View v){

        if (gameCard1.getVisibility() == View.VISIBLE && gameCard2.getVisibility() == View.VISIBLE){

            game.hand(currentPlayerCard, currentCompCard);

            androidScore.setText(Integer.toString(game.compScore()));
            playerScore.setText(Integer.toString(game.playerScore()));

            gameCard1.setVisibility(View.INVISIBLE);
            gameCard2.setVisibility(View.INVISIBLE);

            if (briskulaCard.getVisibility() == View.VISIBLE){

                if (playerCard1.getVisibility() == View.INVISIBLE) {
                    playerCard1.setImageResource(game.getMe(0).cardImage);
                    playerCard1.setVisibility(View.VISIBLE);
                } else if (playerCard2.getVisibility() == View.INVISIBLE) {
                    playerCard2.setImageResource(game.getMe(1).cardImage);
                    playerCard2.setVisibility(View.VISIBLE);
                } else if (playerCard3.getVisibility() == View.INVISIBLE) {
                    playerCard3.setImageResource(game.getMe(2).cardImage);
                    playerCard3.setVisibility(View.VISIBLE);
                }
            }
            if (game.checkUltima()) {
                    briskulaCard.setVisibility(View.INVISIBLE);
            }

            //kraj igre
            if (game.cardSpilSize() == 0 && game.getCompCards().isEmpty()) {
/*
                if (game.playerScore() > game.compScore()) {
                    gameCounter[0]++; //pobjeda playera
                } else if (game.playerScore() < game.compScore()) {
                    gameCounter[1]++;//pobjeda compa
                } else {
                    gameCounter[2]++; //nerijeseno
                }
                gameCounter[3]++; //odigrane partije

                */
                //izbrisi sve sa ekrana
                computerCard1.setVisibility(View.INVISIBLE);
                computerCard2.setVisibility(View.INVISIBLE);
                computerCard3.setVisibility(View.INVISIBLE);


                if (game.compScore() > game.playerScore()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SingleActivity.this);
                    builder.setMessage(R.string.dialogLose)
                            .setTitle(R.string.dialogTitle);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (game.compScore() < game.playerScore()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SingleActivity.this);
                    builder.setMessage(R.string.dialogWin)
                            .setTitle(R.string.dialogTitle);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SingleActivity.this);
                    builder.setMessage(R.string.dialogDeuce)
                            .setTitle(R.string.dialogTitle);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            } else if (!game.checkFlag()) { //ako je flag false znaci da je comp-ov red na igru
                compPlayFirst();
            }
        }
    }

}
