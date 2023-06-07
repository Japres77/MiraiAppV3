package com.example.miraiappv2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class BubbleMatchEndPage extends AppCompatActivity {
    //Declare text view objects
    TextView tv_bubble_correct, tv_bubble_incorrect, tv_bubble_score;
    //Declare image buttons
    ImageButton buttonRestart, buttonMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Set the background of the page to the xml file
        setContentView(R.layout.activity_bubble_match_end_page);

        //Retrieve the background image (kana/romaji) from the previous page, if nothing is given the romaji background is the default
        int background = getIntent().getIntExtra("background", 0);

        //Set the background image for the root ConstraintLayout
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ConstraintLayout rootLayout = findViewById(R.id.bubble_match_layout);
        rootLayout.setBackgroundResource(background);

        //Finds the id of the score text view in the xml
        tv_bubble_score = findViewById(R.id.score);
        int score = getIntent().getIntExtra("score", 0);
        //Sets the text as the score
        tv_bubble_score.setText("" + score);

        //Finds the id of the correct guesses text view in the xml
        tv_bubble_correct = findViewById(R.id.result1);
        //Gets the value of the correct guesses from the previous page
        int correct = getIntent().getIntExtra("correct", 0);
        //Sets the text as the amount of correct guesses
        tv_bubble_correct.setText("" + correct);

        //Finds the id of the incorrect guesses text view in the xml
        tv_bubble_incorrect = findViewById(R.id.result2);
        //Gets the value of the incorrect guesses from the previous page
        int wrong = getIntent().getIntExtra("wrong",0);
        //Sets the text as the amount of incorrect guesses
        tv_bubble_incorrect.setText("" + wrong);

        //Finds the id of the main menu button in the xml
        buttonMenu = findViewById(R.id.button_main_menu);
        //Set an onclick listener
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            //On click run the ReturnToMain method below
            public void onClick(View view) {
                ReturnToMain();
            }
        });

        //Finds the id of the main menu button in the xml
        buttonRestart = findViewById(R.id.button_restart_magic);
        //Set an onclick listener
        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            //On click run the RestartBubble method below
            public void onClick(View view) {
                RestartBubble();
            }
        });
    }
    //Method that returns to the main menu
    public void ReturnToMain(){
        //Intent to go to the main menu page
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    //Method to return back to the bubble match selection page
    public void RestartBubble(){
        //Intent to go to the bubble match selection page
        Intent intent = new Intent(this, BubbleMatchSelectionPage.class);
        startActivity(intent);
    }
}