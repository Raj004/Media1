package com.example.rajshekhar.my_media_7dec;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener  {

    private SeekBar seekBar;
    private Button startMedia;
    private Button stopMedia;
    private MediaPlayer mp;
    private TextView tv_duration;


    private double timeElapsed = 0, finalTime = 0;
    private int forwardTime = 2000, backwardTime = 2000;
    private Handler durationHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
            seekBar = (SeekBar) findViewById(R.id.seekBar1);
            startMedia = (Button) findViewById(R.id.button1);
            stopMedia = (Button) findViewById(R.id.button2);

            tv_duration = (TextView) findViewById(R.id.duration);
            tv_duration.setText("Source : music.mp3");

            startMedia.setOnClickListener(this);
            stopMedia.setOnClickListener(this);
            seekBar.setOnSeekBarChangeListener(this);
            seekBar.setEnabled(false);



    }

    @Override
    public void onClick(View v) {
        if(v.equals(startMedia)){
            if(mp==null){
                mp = MediaPlayer.create(getApplicationContext(), R.raw.formula_1_daniel_simon);
                 seekBar.setEnabled(true);
            }
            if(mp.isPlaying()){
                mp.pause();
                startMedia.setText("play");

            }else {
                mp.start();
                startMedia.setText("pause");
                seekBar.setMax(mp.getDuration());
                timeElapsed=mp.getCurrentPosition();
                seekBar.setProgress((int)timeElapsed);
                durationHandler.postDelayed(updateSeekBarTime, 100);

            }
        }
        if (v.equals(stopMedia) && mp != null) {
            if (mp.isPlaying() ||mp.getDuration()>0) {
                mp.stop();
                mp = null;
                startMedia.setText("play");
                seekBar.setProgress(0);
            }
        }
    }
    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            //get current position
            try {
                    if (mp == null) {
                        mp.reset();
                    }

                    timeElapsed = mp.getCurrentPosition();

                    //set seekbar progress
                    seekBar.setProgress((int) timeElapsed);
                    //set time remaing
                    double timeRemaining = finalTime - timeElapsed;
                    tv_duration.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));

                    //repeat yourself that again in 100 miliseconds
                    durationHandler.postDelayed(this, 100);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    };

    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        seekBar.setVisibility(View.VISIBLE);
        int x=(int)Math.ceil(progress/100f);

        try {
            if (mp.isPlaying() || mp != null) {
                if (fromUser)
                    mp.seekTo(progress);
            } else if (mp == null) {
                Toast.makeText(getApplicationContext(), "Media is not running",
                        Toast.LENGTH_SHORT).show();
                seekBar.setProgress(0);
            }
        } catch (Exception e) {
            Log.e("seek bar", "" + e);
            seekBar.setEnabled(false);

        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
