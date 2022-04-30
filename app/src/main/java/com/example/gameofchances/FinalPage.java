package com.example.gameofchances;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.material.transition.platform.MaterialFadeThrough;

public class FinalPage extends AppCompatActivity {
    TextView finalMessage;
    Button playAgain, exit;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_page);
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());

        finalMessage = findViewById(R.id.finalMessage);
        playAgain = findViewById(R.id.playAgainButton);
        exit = findViewById(R.id.exitButton);
        videoView = findViewById(R.id.videoView);
        Uri uri;

        Intent intent = getIntent();
        String result = intent.getStringExtra("successful");
        if(result != null && result.equals("yes")){
            finalMessage.setText(R.string.successMessage);
            finalMessage.setTextColor(Color.parseColor("#FF077E0C"));
            uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.celebration);
        } else {
            finalMessage.setText(R.string.failureMessage);
            finalMessage.setTextColor(Color.parseColor("#FF9A1208"));
            uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.sad);
        }

        videoView.setVideoURI(uri);
        videoView.start();

        playAgain.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(FinalPage.this, GameScreen.class));
        });

        exit.setOnClickListener(view -> {
            finish();
        });
    }
}