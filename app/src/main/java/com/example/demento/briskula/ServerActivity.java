package com.example.demento.briskula;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Locale;

public class ServerActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_INVITE = 0;
    private GoogleApiClient mGoogleApiClient;
    private ImageButton playerServerCard1;
    private ImageButton playerServerCard2;
    private ImageButton playerServerCard3;
    private ImageButton oponentServerCard1;
    private ImageButton oponentServerCard2;
    private ImageButton oponentServerCard3;
    private ImageButton gameServerCard1;
    private ImageButton gameServerCard2;
    private ImageButton briskulaServerCard;
    private ImageView backRotatedCard;
    private TextView oponentServerScore;
    private TextView playerServerScore;
    private TextView chatMe;
    private TextView chatOp;
    private MultiPlayer MPgame;
    private DatabaseReference ref = null;
    private Long flagGameTurn;
    private Long WIN;
    private String m_Text = "";
    private FirebaseBriskula fb = null;
    private boolean backPressedToExitOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                /*remove status bar clock and battery info*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_multy);

        playerServerCard1 = (ImageButton) findViewById(R.id.playerServerCard1);
        playerServerCard2 = (ImageButton) findViewById(R.id.playerServerCard2);
        playerServerCard3 = (ImageButton) findViewById(R.id.playerServerCard3);
        oponentServerCard1 = (ImageButton) findViewById(R.id.oponentServerCard1);
        oponentServerCard2 = (ImageButton) findViewById(R.id.oponentServerCard2);
        oponentServerCard3 = (ImageButton) findViewById(R.id.oponentServerCard3);
        gameServerCard1 = (ImageButton) findViewById(R.id.gameServerCard1);
        gameServerCard2 = (ImageButton) findViewById(R.id.gameServerCard2);
        briskulaServerCard = (ImageButton) findViewById(R.id.briskulaServerCard);
        oponentServerScore = (TextView) findViewById(R.id.oponentServerScore);
        playerServerScore = (TextView) findViewById(R.id.playerServerScore);
        chatMe = (TextView) findViewById(R.id.chatTextme);
        chatOp = (TextView) findViewById(R.id.chatTxtop);
        this.flagGameTurn = 0L;
        backRotatedCard = (ImageView) findViewById(R.id.backRotatedCard1);


        this.MPgame = new MultiPlayer();

        /*pozovi prijatelja*/

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, this.createDeepLink());
        startActivity(Intent.createChooser(sharingIntent, "How do you want to share?"));


        ref = FirebaseDatabase.getInstance().getReference(this.MPgame.getGameID());


        final ValueEventListener valueEventListener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {

                fb = ds.getValue(FirebaseBriskula.class);

                //provjera jel u firebase upisana pobjeda
                if (ds.child("WIN").exists()) {
                    WIN = (Long) ds.child("WIN").getValue();
                    finishGame();
                }
                //primanje poruka
                if (ds.child("PlayerMessage").exists()) {
                    chatMe.setText(ds.child("PlayerMessage").getValue().toString());
                }
                if (ds.child("ClientMessage").exists()) {
                    chatOp.setText(ds.child("ClientMessage").getValue().toString());
                }

                //bacene karte na stol,
                if (ds.child("tableCard1").exists()) {
                    HashMap<String, Long> gameCard1 = (HashMap<String, Long>) fb.tableCard1;
                    gameServerCard1.setImageResource(gameCard1.get("cardImage").intValue());
                }

                //primamo protivnikovu kartu i obradjujemo je u MPgame
                if (ds.child("tableCard2").exists()) {
                    HashMap<String, Object> gameCard2 = (HashMap<String, Object>) fb.tableCard2;

                    gameServerCard2.setImageResource(((Long) gameCard2.get("cardImage")).intValue());

                    MPgame.setOponentCurrentCard(gameCard2);
                    //MPgame.test = true;

                }

                //ako su skupljene karte sa stola, tj nema ih u firebaseu onda i ukloni sa ekrana
                if (!ds.child("tableCard1").exists() && !ds.child("tableCard2").exists()) {
                    gameServerCard1.setImageResource(android.R.color.transparent);
                    gameServerCard2.setImageResource(android.R.color.transparent);
                    MPgame.test = true;
                }

                // prva, druga i treca karta za igru, doivamo ih iz liste player
                try {
                    HashMap<String, Long> pl = (HashMap<String, Long>) fb.player.get(0);
                    playerServerCard1.setImageResource(pl.get("cardImage").intValue());
                    playerServerCard1.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    playerServerCard1.setVisibility(View.INVISIBLE);
                }
                try {
                    HashMap<String, Long> pl1 = (HashMap<String, Long>) fb.player.get(1);
                    playerServerCard2.setImageResource(pl1.get("cardImage").intValue());
                    playerServerCard2.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    playerServerCard2.setVisibility(View.INVISIBLE);
                }
                try {
                    HashMap<String, Long> pl2 = (HashMap<String, Long>) fb.player.get(2);
                    playerServerCard3.setImageResource(pl2.get("cardImage").intValue());
                    playerServerCard3.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    playerServerCard3.setVisibility(View.INVISIBLE);
                }

                //brisanje protivnikove karte sa stola nakon bacanja
                try {
                    HashMap<String, Long> op1 = (HashMap<String, Long>) fb.oponent.get(0);
                    oponentServerCard1.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    oponentServerCard1.setVisibility(View.INVISIBLE);
                }
                try {
                    HashMap<String, Long> op2 = (HashMap<String, Long>) fb.oponent.get(1);
                    oponentServerCard2.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    oponentServerCard2.setVisibility(View.INVISIBLE);
                }
                try {
                    HashMap<String, Long> op3 = (HashMap<String, Long>) fb.oponent.get(2);
                    oponentServerCard3.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    oponentServerCard3.setVisibility(View.INVISIBLE);
                }

                //karta od igre
                try {
                    HashMap<String, Long> bris = (HashMap<String, Long>) fb.briskulaCard;
                    briskulaServerCard.setImageResource(bris.get("cardImage").intValue());
                } catch (Exception ex) {
                    briskulaServerCard.setVisibility(View.INVISIBLE);
                    backRotatedCard.setVisibility(View.INVISIBLE);
                }
                //bodovi i red na igru
                oponentServerScore.setText(String.format(Locale.getDefault(), "%d", fb.oponentScore));
                playerServerScore.setText(String.format(Locale.getDefault(), "%d", fb.playerScore));
                flagGameTurn = fb.flagGameTurn;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (backPressedToExitOnce) {
            super.onBackPressed();
        } else {
            this.backPressedToExitOnce = true;
            Toast.makeText(this, R.string.backButton, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressedToExitOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //bacanje karte
    public void clickedPlayerServerCard1(View v) {
        if (flagGameTurn == 0 && playerServerCard1.getVisibility() == View.VISIBLE) {
            playerServerCard1.setVisibility(View.INVISIBLE);
            this.MPgame.playerThrowedCard(0);
            this.MPgame.oponentPlaySecond();
        }
    }

    public void clickedPlayerServerCard2(View v) {
        if (flagGameTurn == 0 && playerServerCard2.getVisibility() == View.VISIBLE) {
            playerServerCard2.setVisibility(View.INVISIBLE);
            this.MPgame.playerThrowedCard(1);
            this.MPgame.oponentPlaySecond();
        }
    }

    public void clickedPlayerServerCard3(View v) {
        if (flagGameTurn == 0 && playerServerCard3.getVisibility() == View.VISIBLE) {
            playerServerCard3.setVisibility(View.INVISIBLE);
            this.MPgame.playerThrowedCard(2);
            this.MPgame.oponentPlaySecond();
        }
    }

    //kada je gotova igra, brisemo sve sa ekrana i pokrecemo dijalog sa obavijesti o rezultatu
    private void finishGame() {

        //0-pobjeda servera, 1-pojeda clienta, 2-izjednacenje

        if (this.WIN == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ServerActivity.this);
            builder.setMessage(R.string.dialogLose)
                    .setTitle(R.string.dialogTitle);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (this.WIN == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ServerActivity.this);
            builder.setMessage(R.string.dialogWin)
                    .setTitle(R.string.dialogTitle);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ServerActivity.this);
            builder.setMessage(R.string.dialogDeuce)
                    .setTitle(R.string.dialogTitle);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    //deeplink za slanje poziva prijatelju
    public String createDeepLink() {

        return "https://esa9r.app.goo.gl/?link=https://briskula.com/" + this.MPgame.getGameID() + "&apn=com.example.demento.briskula";
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //slanje poruke klikom na dugme Message
    public void Message(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.message);

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                ref.child("PlayerMessage").setValue(m_Text);
            }
        });
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}