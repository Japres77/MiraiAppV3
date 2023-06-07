package com.example.miraiappv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class BubbleMatchSelectionPage extends AppCompatActivity {

    //Declare image button names
    ImageButton buttonBubbleMatchSelectionRomaji, buttonBubbleMatchSelectionKana, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Removes the default title bar from the top of the application for screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Set content view to show the xml file
        setContentView(R.layout.activity_bubble_match_selection_page);


        //Use button name to match id in xml file
        buttonBubbleMatchSelectionRomaji = findViewById(R.id.button_bubble_match_selection_romaji);
        //Onclick listener
        buttonBubbleMatchSelectionRomaji.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                //Set string value
                String selectedType = "romaji";
                //Start new intent to go to the topic page
                Intent intent = new Intent(BubbleMatchSelectionPage.this, BubbleMatchTopicPage.class);
                //Push selected_type in the intent
                intent.putExtra("selected_type", selectedType);
                //Start intent
                startActivity(intent);
            }
        });

        //Use button name to match id in xml file
        buttonBubbleMatchSelectionKana = findViewById(R.id.button_bubble_match_selection_kana);
        //Onclick listener
        buttonBubbleMatchSelectionKana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set string value
                String selectedType = "kana";
                //Start new intent to go to the topic page
                Intent intent = new Intent(BubbleMatchSelectionPage.this, BubbleMatchTopicPage.class);
                //Push selected_type in the intent
                intent.putExtra("selected_type", selectedType);
                //Start intent
                startActivity(intent);
            }
        });

        //Use button name to match id in xml file
        buttonBack = findViewById(R.id.button_bubble_match_returnbtn);
        //Onclick listener
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            //On click it will run the ReturnToMain method below
            public void onClick(View view) {
                ReturnToMain();
            }
        });
    }
    //Created a method to return to the main page
    public void ReturnToMain(){
        //Start new intent to go to main menu
        Intent intent = new Intent(this, MainActivity.class);
        //Start intent
        startActivity(intent);
    }
}