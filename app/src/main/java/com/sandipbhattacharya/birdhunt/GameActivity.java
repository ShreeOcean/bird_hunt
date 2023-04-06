package com.sandipbhattacharya.birdhunt;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    MediaPlayer mp_bg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mp_bg = MediaPlayer.create(this, R.raw.bg_music);
        if(mp_bg != null){
            mp_bg.start();
            mp_bg.setLooping(true);
        }
        setContentView(new GameView(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mp_bg != null){
            mp_bg.stop();
            mp_bg.release();
        }
    }
}
