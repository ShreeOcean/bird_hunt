package com.sandipbhattacharya.birdhunt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {
    TextView tvPoints;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        int points = getIntent().getExtras().getInt("points");
        tvPoints = findViewById(R.id.tvPoints);
        points = (int)(points/20.0*100.0);
        tvPoints.setText(""+points);
    }

    public void restart(View view){
        Intent intent = new Intent(GameOver.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
