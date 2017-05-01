package com.example.demento.briskula;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.database.*;

import java.util.HashMap;

public class ClientActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private ImageButton playerClientCard1;
    private ImageButton playerClientCard2;
    private ImageButton playerClientCard3;
    private ImageButton oponentClientCard1;
    private ImageButton oponentClientCard2;
    private ImageButton oponentClientCard3;
    private ImageButton gameClientCard1;
    private ImageButton gameClientCard2;
    private ImageView backRotatedCard;
    private ImageButton briskulaClientCard;
    private TextView oponentClientScore;
    private TextView playerClientScore;
    private TextView chatMe;
    private TextView chatOp;
    private DatabaseReference ref;
    private Long flagGameTurn;
    private String gameID;
    private FirebaseBriskula fb;
    private Long WIN;
    private String m_Text = "";
    private boolean backPressedToExitOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_multy_client);

        playerClientCard1 = (ImageButton) findViewById(R.id.playerClientCard1);
        playerClientCard2 = (ImageButton) findViewById(R.id.playerClientCard2);
        playerClientCard3 = (ImageButton) findViewById(R.id.playerClientCard3);
        oponentClientCard1 = (ImageButton) findViewById(R.id.oponentClientCard1);
        oponentClientCard2 = (ImageButton) findViewById(R.id.oponentClientCard2);
        oponentClientCard3 = (ImageButton) findViewById(R.id.oponentClientCard3);
        gameClientCard1 = (ImageButton) findViewById(R.id.gameClientCard1);
        gameClientCard2 = (ImageButton) findViewById(R.id.gameClientCard2);
        briskulaClientCard = (ImageButton) findViewById(R.id.briskulaClientCard);
        oponentClientScore = (TextView) findViewById(R.id.oponentClientScore);
        playerClientScore = (TextView) findViewById(R.id.playerClientScore);
        chatMe = (TextView) findViewById(R.id.chatTextMe1);
        chatOp = (TextView) findViewById(R.id.chatTxtOp1);
        backRotatedCard = (ImageView) findViewById(R.id.backRotatedCard);

        // Create an auto-managed GoogleApiClient with access to App Invites.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppInvite.API)
                .enableAutoManage(this, this)
                .build();

        // Check for App Invite invitations and launch deep-link activity if possible.
        // Requires that an Activity is registered in AndroidManifest.xml to handle
        // deep-link URLs.
        boolean autoLaunchDeepLink = true;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(AppInviteInvitationResult result) {
                                Log.d(TAG, "getInvitation:onResult:" + result.getStatus());
                                if (result.getStatus().isSuccess()) {
                                    // Extract information from the intent
                                    Intent intent = result.getInvitationIntent();
                                    String deepLink = AppInviteReferral.getDeepLink(intent);
                                    String invitationId = AppInviteReferral.getInvitationId(intent);

                                    //gameID je posljednji dio deeplinka linka koji izvlacimo i koristimo dalje za pristup firebase-i
                                    gameID = deepLink.substring(deepLink.indexOf("https://briskula.com/") + 21, deepLink.length());

                                    ref = FirebaseDatabase.getInstance().getReference(gameID);

                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot ds) {

                                            fb = ds.getValue(FirebaseBriskula.class);

                                            if (ds.child("WIN").exists()) {
                                                WIN = (Long) ds.child("WIN").getValue();
                                                finishGame();
                                            }
                                            if (ds.child("PlayerMessage").exists()) {
                                                chatOp.setText(ds.child("PlayerMessage").getValue().toString());
                                            }
                                            if (ds.child("ClientMessage").exists()) {
                                                chatMe.setText(ds.child("ClientMessage").getValue().toString());
                                            }


                                            try {
                                                HashMap<String, Long> gc1 = (HashMap<String, Long>) fb.tableCard1;
                                                gameClientCard1.setImageResource(gc1.get("cardImage").intValue());
                                            } catch (Exception e) {
                                            }
                                            try {
                                                HashMap<String, Object> gc2 = (HashMap<String, Object>) fb.tableCard2;
                                                gameClientCard2.setImageResource(((Long) gc2.get("cardImage")).intValue());
                                            } catch (Exception ex) {
                                            }

                                            //ako su skupljene karte sa stola, tj nema ih u firebaseu onda i ukloni sa ekrana
                                            if (!ds.child("tableCard1").exists() && !ds.child("tableCard2").exists()) {
                                                gameClientCard1.setImageResource(android.R.color.transparent);
                                                gameClientCard2.setImageResource(android.R.color.transparent);
                                            }

                                            try {
                                                HashMap<String, Object> pl = (HashMap<String, Object>) fb.oponent.get(0);
                                                playerClientCard1.setImageResource(((Long) pl.get("cardImage")).intValue());
                                                playerClientCard1.setVisibility(View.VISIBLE);
                                            } catch (Exception e) {
                                                playerClientCard1.setVisibility(View.INVISIBLE);
                                            }

                                            try {
                                                HashMap<String, Object> pl1 = (HashMap<String, Object>) fb.oponent.get(1);
                                                playerClientCard2.setImageResource(((Long) pl1.get("cardImage")).intValue());
                                                playerClientCard2.setVisibility(View.VISIBLE);
                                            } catch (Exception e) {
                                                playerClientCard2.setVisibility(View.INVISIBLE);
                                            }

                                            try {
                                                HashMap<String, Object> pl2 = (HashMap<String, Object>) fb.oponent.get(2);
                                                playerClientCard3.setImageResource(((Long) pl2.get("cardImage")).intValue());
                                                playerClientCard3.setVisibility(View.VISIBLE);
                                            } catch (Exception e) {
                                                playerClientCard3.setVisibility(View.INVISIBLE);
                                            }

                                            //brisanje protivnikove karte sa stola nakon bacanja
                                            try {
                                                HashMap<String, Long> op1 = (HashMap<String, Long>) fb.player.get(0);
                                                oponentClientCard1.setVisibility(View.VISIBLE);
                                            } catch (Exception e) {
                                                oponentClientCard1.setVisibility(View.INVISIBLE);
                                            }
                                            try {
                                                HashMap<String, Long> op2 = (HashMap<String, Long>) fb.player.get(1);
                                                oponentClientCard2.setVisibility(View.VISIBLE);
                                            } catch (Exception e) {
                                                oponentClientCard2.setVisibility(View.INVISIBLE);
                                            }
                                            try {
                                                HashMap<String, Long> op3 = (HashMap<String, Long>) fb.player.get(2);
                                                oponentClientCard3.setVisibility(View.VISIBLE);
                                            } catch (Exception e) {
                                                oponentClientCard3.setVisibility(View.INVISIBLE);
                                            }

                                            try {
                                                HashMap<String, Long> bris = (HashMap<String, Long>) fb.briskulaCard;
                                                briskulaClientCard.setImageResource(bris.get("cardImage").intValue());
                                            } catch (Exception e) {
                                                briskulaClientCard.setVisibility(View.INVISIBLE);
                                                backRotatedCard.setVisibility(View.INVISIBLE);
                                            }

                                            oponentClientScore.setText(fb.playerScore.toString());
                                            playerClientScore.setText(fb.oponentScore.toString());
                                            flagGameTurn = fb.flagGameTurn;
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });

                                }
                            }
                        });
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    public void clickedPlayerClientCard1(View v) {
        if (flagGameTurn == 1 && playerClientCard1.getVisibility() == View.VISIBLE) {

            HashMap<String, Object> zero = (HashMap<String, Object>) fb.oponent.get(0);
            zero.put("indexInSpil", 0);
            ref.child("tableCard2").setValue(zero);

            ref.child("oponent").child("0").setValue(null);

            playerClientCard1.setVisibility(View.INVISIBLE);
            ref.child("flagGameTurn").setValue(0);
        }
    }

    public void clickedPlayerClientCard2(View v) {
        if (flagGameTurn == 1 && playerClientCard2.getVisibility() == View.VISIBLE) {

            HashMap<String, Object> one = (HashMap<String, Object>) fb.oponent.get(1);
            one.put("indexInSpil", 1);

            ref.child("tableCard2").setValue(one);
            ref.child("oponent").child("1").setValue(null);
            playerClientCard2.setVisibility(View.INVISIBLE);
            ref.child("flagGameTurn").setValue(0);
        }
    }

    public void clickedPlayerClientCard3(View v) {
        if (flagGameTurn == 1 && playerClientCard3.getVisibility() == View.VISIBLE) {


            HashMap<String, Object> two = (HashMap<String, Object>) fb.oponent.get(2);
            two.put("indexInSpil", 2);


            ref.child("tableCard2").setValue(two);
            ref.child("oponent").child("0").setValue(null);
            playerClientCard3.setVisibility(View.INVISIBLE);
            ref.child("flagGameTurn").setValue(0);
        }
    }

    public void finishGame() {

        //0-pobjeda servera, 1-pojeda clienta, 2-izjednacenje

        if (this.WIN == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.dialogLose)
                    .setTitle(R.string.dialogTitle);
            AlertDialog dialog = builder.create();
            if (!((Activity) this).isFinishing()) {
                dialog.show();
            }
        } else if (this.WIN == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.dialogWin)
                    .setTitle(R.string.dialogTitle);
            AlertDialog dialog = builder.create();
            if (!((Activity) this).isFinishing()) {
                dialog.show();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.dialogDeuce)
                    .setTitle(R.string.dialogTitle);
            AlertDialog dialog = builder.create();
            if (!((Activity) this).isFinishing()) {
                dialog.show();
            }
        }
        try {
            ref.child(gameID).removeValue();
        } catch (Exception e) {
            //vec je izbrisana
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void Message2(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.message);

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                ref.child("ClientMessage").setValue(m_Text);
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

