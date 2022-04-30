package com.example.gameofchances;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;

import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout layout;
    private TextView welcomeText, appNameText;
    private MaterialButton startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setExitTransition(new Fade()); // set layout transition animation

        layout = findViewById(R.id.animatedLayout);         // find
        welcomeText = findViewById(R.id.welcomeTextView);   // ui
        appNameText = findViewById(R.id.appName);           // components
        startButton = findViewById(R.id.startButton);       // for later use

        FlingAnimation startAnimation = new FlingAnimation(layout, DynamicAnimation.TRANSLATION_Y)
                .setStartVelocity(-4000)
                .setFriction(1.1f)
                .addEndListener((animation, canceled, value, velocity) -> {
                    animateRest(); // animate the texts ( fade in ) when logo
                                   // animation completes
                });
        startAnimation.start(); // animation for logo created and started

        startButton.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(MainActivity.this, GameScreen.class),
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }); // setting on click listener for Start button
    }

    void animateRest(){ // this function applies the fade in animation for texts and button
        ObjectAnimator fadeIn1 = ObjectAnimator.ofFloat(welcomeText,
                "alpha", 0f, 1f);
        ObjectAnimator fadeIn2 = ObjectAnimator.ofFloat(appNameText,
                "alpha", 0f, 1f);
        ObjectAnimator fadeIn3 = ObjectAnimator.ofFloat(startButton,
                "alpha", 0f, 1f);

        fadeIn1.start();
        fadeIn2.start();
        fadeIn3.start();
    }
}