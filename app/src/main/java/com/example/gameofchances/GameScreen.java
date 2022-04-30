package com.example.gameofchances;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.accessibilityservice.AccessibilityService;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class GameScreen extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<TextView> allTextViews; // all tile number views
    private ArrayList<CardView> allCardViews; // all tile holders
    private ArrayList<Integer> numbers;       // a random array of 9 integers
    private int numberToFind = -1;            // the chosen element to guess among numbers
    private TextView attemptsTextView, targetTextView;
    private EditText targetEditText;
    private int numberOfAttempts = 3;         // number of attempts left
    private ImageButton submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade()); // layout transition animations

        targetEditText = findViewById(R.id.targetEditText);
        targetTextView = findViewById(R.id.targetTextView);
        attemptsTextView = findViewById(R.id.attemptsText);
        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
        findAllTextViews(); // fill the values in tiles and set click listeners
    }

    private void initializeNumbers() {
        numbers = new ArrayList<>();
        numbers.add(numberToFind);
        for(int i=0;i<8;i++){
            int newVal = (int) (Math.random()*100 + 1);
            if(numbers.contains(newVal)) { // duplicate element do not include
                i--;
                continue;
            }
            numbers.add(newVal);
        }
        Collections.shuffle(numbers);
    }

    private void findAllTextViews() {
        allTextViews = new ArrayList<>();
        allCardViews = new ArrayList<>();
        for(int i=1;i<=9;i++){
            TextView textView = findViewById(getResources().getIdentifier("tile"+i+"Text",
                    "id", getPackageName()));
            allTextViews.add(textView);
            CardView cardView = findViewById(getResources().getIdentifier("tile"+i,
                    "id", getPackageName()));
            allCardViews.add(cardView);
            cardView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){ // call isCorrect function with tile number clicked
            case R.id.tile1: isCorrect(0); break;
            case R.id.tile2: isCorrect(1); break;
            case R.id.tile3: isCorrect(2); break;
            case R.id.tile4: isCorrect(3); break;
            case R.id.tile5: isCorrect(4); break;
            case R.id.tile6: isCorrect(5); break;
            case R.id.tile7: isCorrect(6); break;
            case R.id.tile8: isCorrect(7); break;
            case R.id.tile9: isCorrect(8); break;
            case R.id.submitButton: processInput(); break;
        }
    }

    private void processInput() {
        String inputText = targetEditText.getText().toString();
        if(inputText == null || inputText.isEmpty()) {
            Toast.makeText(this, "Number Can't be empty, please enter a valid value",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            numberToFind = Integer.valueOf(inputText);
            initializeNumbers(); // generate 9 random numbers
            targetEditText.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);
            targetTextView.setText(String.valueOf(numberToFind));
            targetTextView.setVisibility(View.VISIBLE);
            InputMethodManager manager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(submitButton.getWindowToken(), 0);
        }
    }

    private void isCorrect(int index) { // check if chosen tile is correct
        if(numberToFind == -1){
            Toast.makeText(this, "Please enter a number before starting the play !",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        allTextViews.get(index).setText(numbers.get(index).toString());
        if(numbers.get(index) == numberToFind){
            animateCard(true, index);
        } else {
            numberOfAttempts--; // wrong answer
            animateCard(false, index);
        }
    }

    private void animateCard(boolean correct, int index) {
        TextView currentTextView = allTextViews.get(index);
        CardView currentCardView = allCardViews.get(index);
        if(correct) {
            currentTextView.setTextColor(Color.parseColor("#FF6464FF"));
            currentTextView.setBackgroundResource(R.drawable.success_card);
        } else {
            currentTextView.setBackgroundResource(R.drawable.failure_card);
        }

        int cx = currentCardView.getWidth()/2;
        int cy = currentCardView.getHeight()/2;

        float finalRadius = (float) Math.min(cx, cy);

        Animator animator = ViewAnimationUtils.createCircularReveal(currentCardView,
                cx, cy, 0f, finalRadius);
        animator.setDuration(400);  //  setting upon reveal animation

        animator.addListener(new AnimatorListenerAdapter() { // when animation ends update attempts left
            @Override                                        // and make clickable true
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                StringBuilder message = new StringBuilder(getResources().getString(R.string.attemptsLeft));
                message.setCharAt(message.length()-1, (char)('0' + numberOfAttempts));
                attemptsTextView.setText(message.toString());

                if(numberOfAttempts == 0 || correct){
                    finish(); // if attempts exhausted or correct tile is chosen game is over
                    Intent intent = new Intent(GameScreen.this, FinalPage.class);
                    if(correct) intent.putExtra("successful", "yes");
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                            GameScreen.this).toBundle());
                }
                toggleClikable(1);
                currentCardView.setClickable(false);
            }
        });
        toggleClikable(0);
        animator.start();
    }

    private void toggleClikable(int val) {
        for(CardView cardView : allCardViews){
            cardView.setClickable(val == 1);
            cardView.setAlpha((float) (val==1?1:0.3));
        }
    }
}