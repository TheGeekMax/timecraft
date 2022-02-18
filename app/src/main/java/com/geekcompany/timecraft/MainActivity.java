package com.geekcompany.timecraft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mTimeStamp;
    private Button mReset;
    private RecyclerView mDataShow;
    private long timeStamp;
    ItemAdapter adapter;

    private final Handler aff = new Handler();

    int frame = 0;

    private ArrayList<FarmData> fData;
    private long curTimeStamp;

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
        mDataShow = findViewById(R.id.farm_data);
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

        //definition du Recycler view
        fData = (new FileData("nolife.txt")).getDatas();

        adapter = new ItemAdapter(fData);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mDataShow.setLayoutManager(layoutManager);
        mDataShow.setItemAnimator(new DefaultItemAnimator());
        mDataShow.setAdapter(adapter);

        Log.i("TEMP",getFilesDir().toString());
        //affichage date
        aff.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawDate();
                aff.postDelayed(this,100);
            }
        },0);
    }

    public long getCurTimeStamp() {
        return curTimeStamp;
    }

    private void drawDate(){
        curTimeStamp = System.currentTimeMillis() - timeStamp;

        //update de l'affiche proincipal
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

        //update des quantités
        for (int i = 0; i < fData.size(); i++) {
            FarmData cur = fData.get(i);
            cur.setCurCount(cur.getCountPerSeconds() * curTimeStamp / 1000);
            fData.set(i, cur);
            adapter.notifyItemChanged(i,false);

        }
    }
}