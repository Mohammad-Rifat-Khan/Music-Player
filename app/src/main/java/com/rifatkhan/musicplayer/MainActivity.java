package com.rifatkhan.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TextView  time_left,song_tittle;
    Button play,forward,backward,pause;

    SeekBar seekBar;

    MediaPlayer mediaPlayer;

    Handler handler=new Handler();

    double startTime=0;
    double endTime=0;
    int forwardTime=1000;
    int backwardTime=1000;
    static int playOnly=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play= findViewById(R.id.play_btn);
        pause= findViewById(R.id.pause_btn);
        forward= findViewById(R.id.forward_btn);
        backward= findViewById(R.id.backward_btn);
        song_tittle= findViewById(R.id.textView2);
        time_left= findViewById(R.id.time_leftText);
        seekBar= findViewById(R.id.seekBar);


        mediaPlayer = MediaPlayer.create(this, R.raw.brown_rang);

        song_tittle.setText(getResources().getIdentifier("brownn_rang","raw",
                getPackageName()));
        seekBar.setClickable(false);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();

            }
        });


        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;
                if ((temp + forwardTime) <= endTime){
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);

                }else{
                    Toast.makeText(MainActivity.this,
                            "Can't Jump Forward!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;

                if ((temp - backwardTime) > 0){
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }else{
                    Toast.makeText(MainActivity.this,
                            "Can't Go Back!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void PlayMusic() {
         mediaPlayer.start();

         endTime= mediaPlayer.getDuration();
         startTime= mediaPlayer.getCurrentPosition();

         if (playOnly==0){
             seekBar.setMax((int) endTime);
             playOnly=1;
         }
         time_left.setText(String.format(
             "%d min, %d sec",
                 TimeUnit.MILLISECONDS.toMinutes((long)endTime),
                 TimeUnit.MILLISECONDS.toSeconds((long)endTime),
                 TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)endTime))
         ));

         seekBar.setProgress((int) startTime);
         handler.postDelayed(UpdateSongTime, 100);

    }
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            time_left.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
            ));


            seekBar.setProgress((int)startTime);
            handler.postDelayed(this, 100);
        }
    };
}
