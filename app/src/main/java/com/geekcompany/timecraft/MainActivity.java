package com.geekcompany.timecraft;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTimeStamp;
    private Button mReset;
    private long timeStamp;

    private final Handler aff = new Handler();

    private final static String USER_KEY_DATE = "USER_KEY_DATE";
    private final static String USER_DATE_TIMESTAMP = "USER_DATE_TIMESTAMP";

    private final static long DAY_MILIS = 86400000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialisation des variables
        mTimeStamp = findViewById(R.id.timeStamp);
        mReset = findViewById(R.id.reset);

        //recuperation du temps via les shared prefs
        //timestamp ne prend en compte que jusqu'a l'année  -> max 365 jours
        timeStamp = getSharedPreferences(USER_KEY_DATE,MODE_PRIVATE).getLong(USER_DATE_TIMESTAMP,0);

        //creation utilisation du bouton
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamp = System.currentTimeMillis();
                getSharedPreferences(USER_KEY_DATE,MODE_PRIVATE).edit().putLong(USER_DATE_TIMESTAMP,System.currentTimeMillis()).apply();
            }
        });

        //affichage date
        aff.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawDate();
                aff.postDelayed(this,100);
            }
        },0);
    }

    private void drawDate(){
        long curTimeStamp = System.currentTimeMillis() - timeStamp;
        if(curTimeStamp > DAY_MILIS) {
            //plus d'1 jour d'absence
            long dayCount = (int)(curTimeStamp/DAY_MILIS);
            if(dayCount > 1){
                //jours
                mTimeStamp.setText(dayCount + " jours");
            }else{
                //jour
                mTimeStamp.setText("1 jour");
            }
        }else{
            //moins d'un jour
            long secondes = curTimeStamp / 1000;
            long minutes = secondes / 60;
            //rectifie seconde
            secondes = secondes % 60;
            long heures = minutes / 60;
            //rectifie minutes
            minutes = minutes % 60;

            mTimeStamp.setText((heures<10?"0"+heures:heures)+":"+(minutes<10?"0"+minutes:minutes)+":"+(secondes<10?"0"+secondes:secondes));
        }
    }
}