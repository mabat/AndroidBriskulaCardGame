package com.example.demento.briskula;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*remove status bar clock and battery info*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

    }
    //SINGLEPLAYER
    public void Action (View v){
        Intent i = new Intent(MainActivity.this, SingleActivity.class);
        startActivity(i);
    }

    //MULTIPLAYER
    public void Action2 (View v){
        Intent i = new Intent(MainActivity.this, ServerActivity.class);
        startActivity(i);
    }

}
