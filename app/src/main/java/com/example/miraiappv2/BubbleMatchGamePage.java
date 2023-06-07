package com.example.miraiappv2;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.animation.AccelerateInterpolator;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BubbleMatchGamePage extends AppCompatActivity {
    //Retrieve the value of the button object from the BubbleMatchButton class
    private String getJSONValueForButton(BubbleMatchButton button) {
        //Create a JSON object for the button data
        JSONObject jsonObject = new JSONObject();
        try {
            //Add the data to the object
            jsonObject.put("id", button.getId());
            jsonObject.put("eword", button.getEword());
            jsonObject.put("jword", button.getJword());
            jsonObject.put("topic", button.getTopic());
            jsonObject.put("type", button.getType());
            jsonObject.put("sound", button.getSound());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Return the object as a string
        return jsonObject.toString();
    }
    //Declare a view-group for the animation
    private ViewGroup bubbleAnimation;

    //Declare media player for sounds
    MediaPlayer BubbleMatchMediaPlayer;

    //Declare count down timer
    private CountDownTimer countDownTimer;

    //Declare textview to display the countdown
    private TextView countdownTextView;

    //Declare a list of MagicTroubleQuestionItem objects to store the questions and their answers
    List<BubbleMatchButton> bubbleMatchButton;

    //Declare image button names
    ImageButton bubble_matchendbtn, bubble_matchinfobtn;

    //Declare matchesFound, matchesIncorrect, correct, wrong and score counters to zero
    int correct = 0, wrong = 0, score = 0;
    private int matchesFound = 0;
    private int matchesIncorrect = 0;

    //Declare variables to store the JSON values and buttons for each word
    String eJsonValue = null;
    String jJsonValue = null;

    //Declare variables to store the values of true/false on the first selected button and second seleted button.
    boolean isFirstSelection = false;
    boolean isSecondSelection = false;
    private int firstSelectedButtonId = -1;

    //Declare the values of the selected buttons to null
    BubbleMatchButton eButton1 = null;
    BubbleMatchButton eButton2 = null;
    BubbleMatchButton jButton1 = null;
    BubbleMatchButton jButton2 = null;

    //Declare value of isvisible to false
    private boolean isVisible = false;

    //Declare an empty list of image buttons to store all the generated buttons
    private List<ImageButton> generatedButtons = new ArrayList<>();

    //Declare an empty list of BubbleAnimations to store the bubbles
    private List<BubbleAnimation> bubbles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Set the background to the xml game file
        setContentView(R.layout.activity_bubble_match_game_page);

        //Find the id of the animation container
        bubbleAnimation = findViewById(R.id.bubble_match_animation_container);

        //Set the gridlayout and link it to xml grid
        GridLayout gridLayout = findViewById(R.id.grid_layout_bubble_match);

        //Create a new array list of Bubble Match Buttons
        bubbleMatchButton = new ArrayList<BubbleMatchButton>();

        //Retrieve the selected topics and selected type values from the previous page
        Intent intent = getIntent();
        ArrayList<String> selectedTopics = intent.getStringArrayListExtra("selected_topics");
        String selectedType = intent.getStringExtra("selected_type");

        //Save the value of the amount of selected topics
        int numberOfSelectedTopics = selectedTopics.size();

        //Set the id of the countdown text
        countdownTextView = findViewById(R.id.countdown_textview);

        //Start the coutdown method and give it the values of selectedtype and numberofselectedtopics
        startCountdown(selectedType, numberOfSelectedTopics);

        //Set generatedwordcount to zero
        int generatedWordCount = 0;

        //Check if selectedTopics is empty before accessing it
        if (selectedTopics != null) {
            //If selected topics is not null then loadjson file
            String jsonStr = loadJSONFromAsset("bubble.json");

            //Load all the json data from the file into an object
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray questions = jsonObj.getJSONArray("bubble");

                //For every object get all the data
                for (int i = 0; i < questions.length(); i++) {
                    JSONObject question = questions.getJSONObject(i);
                    String idString = question.getString("id");
                    String ewordString = question.getString("eword");
                    String jwordString = question.getString("jword");
                    String topicString = question.getString("topic");
                    String typeString = question.getString("type");
                    String soundString = question.getString("sound");

                    //Check if the question topic and type is one of the ones the user has selected
                    if (selectedTopics.contains(topicString) && selectedType.equals(typeString)) {
                        bubbleMatchButton.add(new BubbleMatchButton(
                                idString,
                                ewordString,
                                jwordString,
                                topicString,
                                typeString,
                                soundString
                        ));
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }



            //Set the id of the end button
            bubble_matchendbtn = findViewById(R.id.bubble_match_endbtn);
            //Onclick listener
            bubble_matchendbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedType.equals("romaji")) {
                        //Game over, pass the type background, score and results to the next page
                        Intent intent = new Intent(getApplicationContext(), BubbleMatchEndPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        intent.putExtra("background", R.drawable.background_bubble_match_end_romaji_phone);
                        startActivity(intent);
                        finish();
                    } else {
                        //Game over, pass the type background, score and results to the next page
                        Intent intent = new Intent(getApplicationContext(), BubbleMatchEndPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        intent.putExtra("background", R.drawable.background_bubble_match_end_kana_phone);
                        startActivity(intent);
                        finish();
                    }
                }
            });

            //Set the id of the info button
            bubble_matchinfobtn = findViewById(R.id.bubble_match_infobtn);
            //Onclick listener
            bubble_matchinfobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    //On click create a temporary pop up window
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                    //Uses the xml file for the popup
                    View popupView = inflater.inflate(R.layout.bubble_match_info_popup, null);

                    // Create the magic_popup window
                    int width;
                    int height;
                    boolean focusable = true;
                    final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, focusable);

                    // Set an onClickListener to the close button in the magic_popup layout XML file
                    Button closeButton = popupView.findViewById(R.id.infobackbtn);
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //On click remove the magic_popup window
                            popupWindow.dismiss();
                        }
                    });

                    //Set the size of the popup window based on the device orientation
                    int orientation = getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        width = getResources().getDisplayMetrics().widthPixels * 90 / 100; //Adjust Value accordingly - Set the width to 90% of the screen width
                        height = getResources().getDisplayMetrics().heightPixels * 95 / 100;//Adjust Value accordingly -Set the height to 95% of the screen height
                    } else {
                        width = getResources().getDisplayMetrics().widthPixels * 80 / 100; //Adjust Value accordingly - Set the width to 80% of the screen width
                        height = getResources().getDisplayMetrics().heightPixels * 90 / 100; //Adjust Value accordingly - Set the height to 90% of the screen height
                    }

                    //Set width and height for the magic_popup window
                    popupWindow.setWidth(width);
                    popupWindow.setHeight(height);
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                }
            });

            //Shuffle the bubblematchbutton game elements
            Collections.shuffle(bubbleMatchButton);

            //Iterate over each of the shuffled game elements and create buttons for each english word and japanese word
            for (BubbleMatchButton buttonData : bubbleMatchButton) {
                //Modify the button size
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                int buttonWidth = (int) (screenWidth * 0.43); //Adjust Value accordingly - changes the width of the bubble match buttons
                int buttonHeight = (int) (buttonWidth * 0.30); //Adjust Value accordingly - changes the height of the bubble match buttons
                int marginInPixels = (int) (buttonWidth * 0.01); //Adjust Value accordingly - changes the margin of the bubble match butttons
                float desiredTextSizeInDp = 16; //Adjust Value accordingly - desired text size as needed

                //Adjust text size depending on device type
                float density = getResources().getDisplayMetrics().density;
                float scaledTextSize = desiredTextSizeInDp * density;

                //Check if the generated word count has reached the desired limit
                if (generatedWordCount >= 20 * numberOfSelectedTopics ) { //Adjust Value accordingly - changes the amount of buttons displayed
                    break; // Exit the loop if the limit is reached
                }

                //For each bubble match item create a english word container that contains the button and text
                RelativeLayout ebuttonContainer = new RelativeLayout(this);
                ImageButton ewordButton = new ImageButton(this);

                //Set the background of the button to default
                ewordButton.setBackgroundResource(R.drawable.button_bubble_match_default);

                //Add the word to the button
                generatedButtons.add(ewordButton);

                //Set the button size using the values above
                RelativeLayout.LayoutParams ebuttonLayoutParams = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
                ebuttonLayoutParams.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);
                ewordButton.setLayoutParams(ebuttonLayoutParams);

                //Create a text view and set the text as the english word
                TextView ewordText = new TextView(this);
                ewordText.setText(buttonData.getEword());

                //Set the colour, size and outline for the text
                ewordText.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledTextSize);
                ewordText.setTextColor(Color.WHITE);//Adjust Values accordingly
                ewordText.setShadowLayer(10, 0, 0, Color.BLUE);//Adjust Values accordingly

                //If statement to change the font based on what topic was selected
                if (selectedType.equals("romaji")) {
                    //Load the comicsans font from assets
                    Typeface typeface = Typeface.createFromAsset(getAssets(), "comicsans.ttf");
                    //Apply the font to the eword TextView
                    ewordText.setTypeface(typeface);
                } else {
                    //Load the kana font from assets
                    Typeface typeface = Typeface.createFromAsset(getAssets(), "kyo.ttc");
                    //Apply the font to the eword TextView
                    ewordText.setTypeface(typeface);
                }

                //Set the text view size to wrap the content
                RelativeLayout.LayoutParams etextLayoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                //Center the word in the container
                etextLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

                //Add the layout set above to the text
                ewordText.setLayoutParams(etextLayoutParams);

                //Add the button and the text to the container and add the container to the grid
                ebuttonContainer.addView(ewordButton);
                ebuttonContainer.addView(ewordText);
                gridLayout.addView(ebuttonContainer);

                //Add to the generated word count
                generatedWordCount++;



                //For each bubble match item create a Japanese word container that contains the button
                RelativeLayout jbuttonContainer = new RelativeLayout(this);
                ImageButton jwordButton = new ImageButton(this);

                //Set the background of the button to default
                jwordButton.setBackgroundResource(R.drawable.button_bubble_match_default);

                //Add the word to the button
                generatedButtons.add(jwordButton);

                //Set the button size using the values above
                RelativeLayout.LayoutParams jbuttonLayoutParams = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
                jbuttonLayoutParams.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);
                jwordButton.setLayoutParams(jbuttonLayoutParams);

                //Create a text view and set the text as the Japanese word
                TextView jwordText = new TextView(this);
                jwordText.setText(buttonData.getJword());

                //Set the colour, size and outline for the text
                jwordText.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledTextSize);
                jwordText.setTextColor(Color.WHITE);//Adjust Value accordingly
                jwordText.setShadowLayer(10, 0, 0, Color.BLUE);//Adjust Value accordingly

                //If statement to change the font based on what topic was selected
                if (selectedType.equals("romaji")) {
                    //Load the comicsans font from assets
                    Typeface typeface = Typeface.createFromAsset(getAssets(), "comicsans.ttf");
                    //Apply the font to the eword TextView
                    jwordText.setTypeface(typeface);
                } else {
                    //Load the kana font from assets
                    Typeface typeface = Typeface.createFromAsset(getAssets(), "kyo.ttc");
                    //Apply the font to the eword TextView
                    jwordText.setTypeface(typeface);
                }

                //Set the text view size to wrap the content
                RelativeLayout.LayoutParams jtextLayoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                //Center the word in the container
                jtextLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

                //Add the layout set above to the text
                jwordText.setLayoutParams(jtextLayoutParams);

                //Add the button and the text to the container and add the container to the grid
                jbuttonContainer.addView(jwordButton);
                jbuttonContainer.addView(jwordText);
                gridLayout.addView(jbuttonContainer);

                //Add to the generated word count
                generatedWordCount++;

                String jsonValue = getJSONValueForButton(buttonData);
                //Test log - This was used to see if the button was able to be clicked.
                //Remove the double slash below to have it show in the console while the emulator is running
                //Log.d("Button Clicked", jsonValue);

                //Retrieve the sound name from the button data
                String soundName = buttonData.getSound();

                //OnClickListener for eButton
                ewordButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Change the background of the button when selected
                        ewordButton.setBackgroundResource(R.drawable.button_bubble_match_selected);

                        //Save the JSON value of the button
                        String jsonValue = getJSONValueForButton(buttonData);
                        //Test log - This was used to see if the jsonvalue was pulling information
                        //Remove the double slash below to have it show in the console while the emulator is running
                        //Log.d("Button Clicked", jsonValue);

                        //If statement to check if its the first selected button
                        if (isFirstSelection == false) {
                            //On selection change background to selected
                            ewordButton.setBackgroundResource(R.drawable.button_bubble_match_selected);
                            //Store the jsonvalue in the eJsonValue
                            eJsonValue = jsonValue;
                            //Store the button data in ebutton1
                            eButton1 = buttonData;
                            //Set isfirstselection to true
                            isFirstSelection = true;
                            firstSelectedButtonId = view.getId();
                            //Test log - This was used to see if the first button selection was working
                            //Remove the double slash below to have it show in the console while the emulator is running
                            //Log.d("Button Clicked", "First Selection");
                        } else {
                            //On second button selection change background to selected
                            ewordButton.setBackgroundResource(R.drawable.button_bubble_match_selected);
                            //Store the button data in ebutton2
                            eButton2 = buttonData;
                            //Set issecondselection to true
                            isSecondSelection = true;

                            //Test log - This was used to see if all the values were matching before performing the next step
                            //Remove the double slashes below to have it show in the console while the emulator is running
                            //Log.d("Button Clicked", "Second selection");
                            //Log.d("Button Clicked", "eButton2: " + eButton2);
                            //Log.d("Button Clicked", "jButton1: " + jButton1);
                            //Log.d("Button Clicked", "eButton1: " + eButton1);
                            //Log.d("Button Clicked", "jButton2: " + jButton2);
                            //Log.d("Button Clicked", "eJsonValue: " + eJsonValue);
                            //Log.d("Button Clicked", "jJsonValue: " + jJsonValue);

                            //If statement to check if they are both english words
                            if(eButton1 != null && eButton2 !=null){
                                //Test log - This was used to check if the bubbles were both english words
                                //Remove the double slashes below to have it show in the console while the emulator is running
                                //Log.d("Button Clicked", "They cant match!");

                                //Add 1 to the wrong count
                                wrong +=1; //Adjust as required

                                //Run the resetButtonBackgrounds method
                                resetButtonBackgrounds();

                                //Reset all of the stored values
                                eJsonValue = null;
                                jJsonValue = null;
                                eButton1 = null;
                                jButton1 = null;
                                eButton2 = null;
                                jButton2 = null;
                                isFirstSelection = false;
                                isSecondSelection = false;
                                firstSelectedButtonId = -1;
                            }
                            //If statement to check if they are matching and remove them off the screen
                            if (eButton2 != null && jButton1 != null) {
                                //If statement to check the json values of the first button are equal to the second button
                                if (getJSONValueForButton(eButton2).equals(getJSONValueForButton(jButton1))) {
                                    //Test log - This was used to check if the bubbles matched
                                    //Remove the double slashes below to have it show in the console while the emulator is running
                                    //Log.d("Button Clicked", "They match!");
                                    jwordButton.setBackgroundResource(R.drawable.button_bubble_match_correct);
                                    ewordButton.setBackgroundResource(R.drawable.button_bubble_match_correct);

                                    //Add points to the score and results
                                    correct +=1;//Adjust Value accordingly
                                    score +=100;//Adjust Value accordingly

                                    //Update the score on the screen
                                    TextView bubbleMatchScore = findViewById(R.id.bubble_match_score_textview);
                                    bubbleMatchScore.setText("" + score);

                                    //Load media player and play sound based on JSON file
                                    //If it doesnt load it will display a error log with the file that it failed on
                                    MediaPlayer mediaPlayer = loadAudio(soundName);
                                    if (mediaPlayer != null) {
                                        mediaPlayer.start();
                                        //Remove the MediaPlayer after 10 seconds
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mediaPlayer.release();
                                            }
                                        }, 10000);//Adjust Value accordingly - Delay for 10 seconds (10000 milliseconds)
                                    } else {
                                        //Test log - This was used to check if the sound file wasnt working. It would display the json value given
                                        //Remove the double slashes below to have it show in the console while the emulator is running
                                        //Log.e(TAG, "Error creating MediaPlayer object");
                                        //Log.d(TAG, "Sound name: " + soundName);
                                    }

                                    //Delay the removal of the matched buttons
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Remove the matched buttons from the screen
                                            gridLayout.removeView(ebuttonContainer);
                                            gridLayout.removeView(jbuttonContainer);

                                            //Increment the matchesFound count
                                            matchesFound++;

                                            //Check to see if all matches have been found
                                            if (matchesFound == 20 * numberOfSelectedTopics) {//Adjust Value accordingly
                                                if (selectedType.equals("romaji")) {
                                                    Intent intent = new Intent(getApplicationContext(), BubbleMatchEndPage.class);
                                                    intent.putExtra("correct", correct);
                                                    intent.putExtra("wrong", wrong);
                                                    intent.putExtra("score", score);
                                                    intent.putExtra("background", R.drawable.background_bubble_match_end_romaji_phone);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Intent intent = new Intent(getApplicationContext(), BubbleMatchEndPage.class);
                                                    intent.putExtra("correct", correct);
                                                    intent.putExtra("wrong", wrong);
                                                    intent.putExtra("score", score);
                                                    intent.putExtra("background", R.drawable.background_bubble_match_end_kana_phone);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }
                                    }, 2000); //Adjust Value accordingly - Delay for 2 seconds (2000 milliseconds)
                                } else {
                                    //Test log - This was used to check if the buttons didnt match
                                    //Remove the double slashes below to have it show in the console while the emulator is running
                                    //Log.d("Button Clicked", "They don't match!");

                                    //Set the background of the button to incorrect
                                    ewordButton.setBackgroundResource(R.drawable.button_bubble_match_incorrect);

                                    //Add 1 to the wrong result
                                    wrong +=1; //Adjust Value accordingly

                                    //Delay the removal of the matched buttons
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Reset the backgrounds of all buttons
                                            resetButtonBackgrounds();
                                            //Increment the matchesincorrect count
                                            matchesIncorrect++;

                                            //Resets the background on both buttons to default
                                            if (firstSelectedButtonId != -1) {
                                                Button firstSelectedButton = findViewById(firstSelectedButtonId);
                                                if (firstSelectedButton != null) {
                                                    firstSelectedButton.setBackgroundResource(R.drawable.button_bubble_match_default);
                                                }
                                            }
                                        }
                                    }, 2000); //Adjust Value accordingly - Delay for 2 seconds (2000 milliseconds)

                                }
                                //Reset all of the stored values
                                eJsonValue = null;
                                jJsonValue = null;
                                eButton1 = null;
                                jButton1 = null;
                                eButton2 = null;
                                jButton2 = null;
                                isFirstSelection = false;
                                isSecondSelection = false;
                                firstSelectedButtonId = -1;
                            }
                        }
                    }
                });

                //OnClickListener for jButton
                jwordButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Change the background of the button when selected
                        jwordButton.setBackgroundResource(R.drawable.button_bubble_match_selected);
                        //Save the JSON value of the button
                        String jsonValue = getJSONValueForButton(buttonData);
                        //Test log - This was used to see if the jsonvalue was pulling information
                        //Remove the double slash below to have it show in the console while the emulator is running
                        //Log.d("Button Clicked", jsonValue);

                        //If statement to check if its the first selected button
                        if (isFirstSelection == false) {
                            //On selection change background to selected
                            jwordButton.setBackgroundResource(R.drawable.button_bubble_match_selected);

                            //Store the jsonvalue in the eJsonValue
                            jJsonValue = jsonValue;

                            //Store the button data in jbutton1
                            jButton1 = buttonData;

                            //Set isfirstselection to true
                            isFirstSelection = true;
                            firstSelectedButtonId = view.getId();

                            //Test log - This was used to see if the first button selection was working
                            //Remove the double slash below to have it show in the console while the emulator is running
                            //Log.d("Button Clicked", "First Selection");

                        } else {
                            //On second button selection change background to selected
                            jwordButton.setBackgroundResource(R.drawable.button_bubble_match_selected);

                            //Store the button data in jbutton2
                            jButton2 = buttonData;

                            //Set issecondselection to true
                            isSecondSelection = true;

                            //Test log - This was used to see if all the values were matching before performing the next step
                            //Remove the double slashes below to have it show in the console while the emulator is running
                            //Log.d("Button Clicked", "Second selection");
                            //Log.d("Button Clicked", "eButton2: " + eButton2);
                            //Log.d("Button Clicked", "jButton1: " + jButton1);
                            //Log.d("Button Clicked", "eButton1: " + eButton1);
                            //Log.d("Button Clicked", "jButton2: " + jButton2);
                            //Log.d("Button Clicked", "eJsonValue: " + eJsonValue);
                            //Log.d("Button Clicked", "jJsonValue: " + jJsonValue);

                            //If statement to check if they are both japanese words
                            if(jButton1 != null && jButton2 !=null){
                                //Test log - This was used to check if the bubbles were both japanese words
                                //Remove the double slashes below to have it show in the console while the emulator is running
                                //Log.d("Button Clicked", "They cant match!");

                                //Add 1 to the wrong count
                                wrong +=1; //Adjust as required

                                //Run the resetButtonBackgrounds method
                                resetButtonBackgrounds();

                                //Reset all of the stored values
                                eJsonValue = null;
                                jJsonValue = null;
                                eButton1 = null;
                                jButton1 = null;
                                eButton2 = null;
                                jButton2 = null;
                                isFirstSelection = false;
                                isSecondSelection = false;
                                firstSelectedButtonId = -1;
                            }
                            //If statement to check if they are matching and remove them off the screen
                            if (eButton1 != null && jButton2 != null) {
                                //If statement to check the json values of the first button are equal to the second button
                                if (getJSONValueForButton(eButton1).equals(getJSONValueForButton(jButton2))) {
                                    //Test log - This was used to check if the bubbles matched
                                    //Remove the double slashes below to have it show in the console while the emulator is running
                                    //Log.d("Button Clicked", "They match!");

                                    //Change background to correct
                                    jwordButton.setBackgroundResource(R.drawable.button_bubble_match_correct);
                                    ewordButton.setBackgroundResource(R.drawable.button_bubble_match_correct);

                                    //Add points to the score and results
                                    correct +=1;//Adjust Value accordingly
                                    score +=100;//Adjust Value accordingly

                                    //Update the score on the screen
                                    TextView bubbleMatchScore = findViewById(R.id.bubble_match_score_textview);
                                    bubbleMatchScore.setText("" + score);

                                    //Load media player and play sound based on JSON file
                                    //If it doesnt load it will display a error log with the file that it failed on
                                    MediaPlayer mediaPlayer = loadAudio(soundName);
                                    if (mediaPlayer != null) {
                                        mediaPlayer.start();
                                        //Remove the MediaPlayer after 10 seconds
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mediaPlayer.release();
                                            }
                                        }, 10000);//Adjust Value accordingly - Delay for 10 seconds (10000 milliseconds)
                                    } else {
                                        //Test log - This was used to check if the sound file wasnt working. It would display the json value given
                                        //Remove the double slashes below to have it show in the console while the emulator is running
                                        //Log.e(TAG, "Error creating MediaPlayer object");
                                        //Log.d(TAG, "Sound name: " + soundName);
                                    }

                                    //Delay the removal of the matched buttons
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Remove the matched buttons from the screen
                                            gridLayout.removeView(ebuttonContainer);
                                            gridLayout.removeView(jbuttonContainer);

                                            //Increment the matchesFound count
                                            matchesFound++;

                                            //Check to see if all matches have been found
                                            if (matchesFound == 20 * numberOfSelectedTopics) {//Adjust Value accordingly
                                                if (selectedType.equals("romaji")) {
                                                    // game over for romaji, pass the score and results
                                                    Intent intent = new Intent(getApplicationContext(), BubbleMatchEndPage.class);
                                                    intent.putExtra("correct", correct);
                                                    intent.putExtra("wrong", wrong);
                                                    intent.putExtra("score", score);
                                                    intent.putExtra("background", R.drawable.background_bubble_match_end_romaji_phone);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    // game over for other type, pass the score and results
                                                    Intent intent = new Intent(getApplicationContext(), BubbleMatchEndPage.class);
                                                    intent.putExtra("correct", correct);
                                                    intent.putExtra("wrong", wrong);
                                                    intent.putExtra("score", score);
                                                    intent.putExtra("background", R.drawable.background_bubble_match_end_kana_phone);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }
                                    }, 2000); //Adjust Value accordingly - Delay for 2 seconds (2000 milliseconds)
                                } else {
                                    //Test log - This was used to check if the buttons didnt match
                                    //Remove the double slashes below to have it show in the console while the emulator is running
                                    //Log.d("Button Clicked", "They don't match!");

                                    //Set the background of the button to incorrect
                                    jwordButton.setBackgroundResource(R.drawable.button_bubble_match_incorrect);

                                    //Add 1 to the wrong result
                                    wrong +=1; //Adjust Value accordingly

                                    //Delay the removal of the matched buttons
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Reset the backgrounds of all buttons
                                            resetButtonBackgrounds();
                                            //Increment the matchesincorrect count
                                            matchesIncorrect++;
                                            //Resets the background on both buttons to default
                                            if (firstSelectedButtonId != -1) {
                                                Button firstSelectedButton = findViewById(firstSelectedButtonId);
                                                if (firstSelectedButton != null) {
                                                    firstSelectedButton.setBackgroundResource(R.drawable.button_bubble_match_default);
                                                }
                                            }
                                        }
                                    }, 2000); //Adjust Value accordingly - Delay for 2 seconds (2000 milliseconds)
                                }
                                //Reset all of the stored values
                                eJsonValue = null;
                                jJsonValue = null;
                                eButton1 = null;
                                jButton1 = null;
                                eButton2 = null;
                                jButton2 = null;
                                isFirstSelection = false;
                                isSecondSelection = false;
                                firstSelectedButtonId = -1;
                            }
                        }
                    }
                });
            }
        }
        //Shuffle all of the buttons within the grid layout
        List<View> allButtons = new ArrayList<>();
        //Get all the buttons within the grid layout and add them to the array list
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            allButtons.add(child);
        }
        //Shuffle the order of buttons in the list
        Collections.shuffle(allButtons);

        //Remove anything on the grid layout to prepare for rearranging the buttons
        gridLayout.removeAllViews();

        //Add the buttons back to the grid in the shuffled order
        for (View button : allButtons) {
            gridLayout.addView(button);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            addBubble();
        }
    }

    //Start the animation for the bubble
    private void startBubbleAnimation(ImageView bubble) {
        bubble.post(new Runnable() {
            @Override
            public void run() {
                animateBubble(bubble);
            }
        });
    }

    //Add bubbles to the container
    private void addBubble() {
        //Set the limit of the number of bubbles
        int minBubbles = 45;//Adjust Value accordingly - Min amount of bubbles
        int maxBubbles = 60;//Adjust Value accordingly - Max amount of bubbles

        //Randomises the amount of bubbles
        int numBubbles = (int) (Math.random() * (maxBubbles - minBubbles + 1)) + minBubbles;

        //Set the positions of the bubbles to random
        int minYPosition = bubbleAnimation.getHeight() / 1;
        int maxYPosition = bubbleAnimation.getHeight() - getResources().getDimensionPixelSize(R.dimen.bubble_animation_size);

        //For each bubble created
        for (int i = 0; i < numBubbles; i++) {
            //Create a new image view and set the background as the bubble
            final ImageView bubble = new ImageView(this);
            bubble.setImageDrawable(getResources().getDrawable(R.drawable.bubble_image));

            //Set the size of the bubble
            int size = getResources().getDimensionPixelSize(R.dimen.bubble_animation_size); //Adjust Value accordingly
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);

            //Sets random position for bubble
            params.leftMargin = getRandomXPosition();
            params.topMargin = getRandomYPosition(minYPosition, maxYPosition);
            bubble.setLayoutParams(params);

            //Adds the bubble to the view
            bubbleAnimation.addView(bubble);

            //Onclick listener
            bubble.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //If the player clicks the button add 10 to the score
                    score += 10; //Adjust Value accordingly
                    //Update the score text
                    TextView bubbleMatchScore = findViewById(R.id.bubble_match_score_textview);
                    bubbleMatchScore.setText("" + score);

                    //Remove the clicked bubble from the container
                    bubbleAnimation.removeView(bubble);
                }
            });
            bubble.post(new Runnable() {
                @Override
                public void run() {
                    animateBubble(bubble);
                }
            });
        }
    }

    // Get a random y position
    private int getRandomYPosition(int minY, int maxY) {
        return (int) (Math.random() * (maxY - minY + 1)) + minY;
    }

    //Get a random x position
    private int getRandomXPosition() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        return (int) (Math.random() * (screenWidth - getResources().getDimensionPixelSize(R.dimen.bubble_animation_size)));
    }

    //Animate the bubble
    private void animateBubble(final ImageView bubble) {
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int minDuration = 5000; //Adjust Value accordingly
        int maxDuration = 15000; //Adjust Value accordingly
        int duration = (int) (Math.random() * (maxDuration - minDuration + 1)) + minDuration;
        ObjectAnimator animator = ObjectAnimator.ofFloat(bubble, "translationY", -screenHeight);
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                bubbleAnimation.removeView(bubble);
            }
        });
        animator.start();
    }

    //Method to reset the backgrounds of all buttons
    private void resetButtonBackgrounds() {
        Log.d("ResetButtonBackgrounds", "Running!");
        // Iterate through all buttons in the generatedButtons list and set their backgrounds to default
        for (ImageButton button : generatedButtons) {
            button.setBackgroundResource(R.drawable.button_bubble_match_default);
        }
    }

    //Method to start the coutdown
    private void startCountdown(String selectedType, int numberOfSelectedTopics) {
        countDownTimer = new CountDownTimer(60000 * numberOfSelectedTopics, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                countdownTextView.setTextColor(Color.WHITE);
                countdownTextView.setText("" + secondsRemaining);
            }

            public void onFinish() {

                if (selectedType.equals("romaji")) {
                    // game over for romaji, pass the score and results
                    Intent intent = new Intent(getApplicationContext(), BubbleMatchEndPage.class);
                    intent.putExtra("correct", correct);
                    intent.putExtra("wrong", wrong);
                    intent.putExtra("score", score);
                    intent.putExtra("background", R.drawable.background_bubble_match_end_romaji_phone);
                    startActivity(intent);
                    finish();
                } else {
                    // game over for other type, pass the score and results
                    Intent intent = new Intent(getApplicationContext(), BubbleMatchEndPage.class);
                    intent.putExtra("correct", correct);
                    intent.putExtra("wrong", wrong);
                    intent.putExtra("score", score);
                    intent.putExtra("background", R.drawable.background_bubble_match_end_kana_phone);
                    startActivity(intent);
                    finish();
                }
            }
        }.start();
    }

    private void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCountdown();
    }

    //
    private MediaPlayer loadAudio(String soundName) {
        int resID = getResources().getIdentifier(soundName, "raw", getPackageName());

        if (resID != 0) {
            return MediaPlayer.create(this, resID);
        } else {
            // Handle error getting resource ID
            Log.e(TAG, "Error getting resource ID");
            return null;
        }
    }

    //Load json file from folder
    private String loadJSONFromAsset(String file){
        String json = "";
        try{
            InputStream is = getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }catch (IOException e){
            e.printStackTrace();
        }
        return json;
    }
}