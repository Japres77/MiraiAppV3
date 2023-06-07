package com.example.miraiappv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class MagicTroubleEndPage extends AppCompatActivity {

    TextView tv_bubble_correct, tv_bubble_incorrect, tv_bubble_score;
    ImageButton buttonRestart, buttonMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();

        setContentView(R.layout.activity_magic_trouble_end_page);

        // Retrieve the background image resource identifier from the intent
        int background = getIntent().getIntExtra("background", 0);

        // Set the background image for the root ConstraintLayout
        ConstraintLayout rootLayout = findViewById(R.id.magic_trouble_layout);
        rootLayout.setBackgroundResource(background);

        tv_bubble_score = findViewById(R.id.score);
        int score = getIntent().getIntExtra("score", 0);
        tv_bubble_score.setText("" + score);

        tv_bubble_correct = findViewById(R.id.result1);
        int correct = getIntent().getIntExtra("correct", 0);
        tv_bubble_correct.setText("" + correct);

        tv_bubble_incorrect = findViewById(R.id.result2);
        int wrong = getIntent().getIntExtra("wrong",0);
        tv_bubble_incorrect.setText("" + wrong);


        buttonMenu = findViewById(R.id.button_main_menu);
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReturnToMain();
            }
        });
        buttonRestart = findViewById(R.id.button_restart_magic);
        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestartMagic();
            }
        });
    }
    public void ReturnToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void RestartMagic(){
        Intent intent = new Intent(this, MagicTroubleSelectionPage.class);
        startActivity(intent);
    }
}