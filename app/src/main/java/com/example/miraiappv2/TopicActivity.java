package com.example.miraiappv2;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TopicActivity extends AppCompatActivity {

    private ImageView slideImageView;
    private Button nextButton;
    private Button backButton;
    private Button endButton;
    private Button audio;
    private List<String> slidePaths;
    private List<String> slideaudio;
    private int currentSlideIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //new stuff
        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();

        setContentView(R.layout.activity_topic);
        int topic = getIntent().getIntExtra("topic", 1);
        List<String> topics = Arrays.asList("Essential", "Greetings", "Colours", "General", "Number", "School",
                "Adjectives", "Verbs", "Animals", "Body", "Days", "Family", "Food", "Seasons");
        String current=topics.get(topic-1);
        String path="App - Study Cards/";
        String path1="Vocab Pronunciation Files_Flashcards/";

        // Get the AssetManager instance
        AssetManager assetManager = getAssets();

        try {
            // List all files and directories in the assets folder

            String[] files = assetManager.list("App - Study Cards/");
            String[] files1 = assetManager.list("Vocab Pronunciation Files_Flashcards/");

            if (files != null) {

                // Iterate over the files and check for a folder name similar to current
                for (String fileName : files) {

                    if (fileName.toLowerCase().contains(current.toLowerCase())) {
                        // Found a folder with a name similar to "Greetings"
                        String relativePath = fileName;
                        Log.d("TAG", "Relative Path: " + relativePath);
                        path=path+relativePath+"/";
                        break;
                    }
                }
            }
            if (files1 != null) {

                // Iterate over the files and check for a folder name similar to current
                for (String fileName : files1) {

                    if (fileName.toLowerCase().contains(current.toLowerCase())) {
                        // Found a folder with a name similar to "Greetings"
                        String relativePath = fileName;
                        Log.d("TAGg", "Relative Path: " + relativePath);
                        path1=path1+relativePath+"/";
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }


        slideImageView = findViewById(R.id.slideImageView);
        nextButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.backButton);
        endButton = findViewById(R.id.endButton);
        audio=findViewById(R.id.play);

        // Load slide paths from assets.slides folder
        slidePaths = getSlidePaths(path);
        slideaudio= getSlidePaths(path1);


        // Set initial slide
        currentSlideIndex = 0;
        showSlide(currentSlideIndex);

        Log.d("Audio",path1);
        // Set click listeners for buttons
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSlideIndex < slidePaths.size() - 1) {
                    currentSlideIndex++;
                    showSlide(currentSlideIndex);

                }
            }
        });
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSlideIndex < slidePaths.size() - 1) {
                    playAu(slideaudio,currentSlideIndex);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSlideIndex > 0) {
                    currentSlideIndex--;
                    showSlide(currentSlideIndex);
                }
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void playAu(List<String> slideaudio, int currentSlideIndex) {
        Log.d(TAG, "test " + currentSlideIndex);
        Log.d(TAG, "test 2 " + currentSlideIndex);

        playAudio(slideaudio.get(currentSlideIndex));
    }
    private static final String TAG = "AudioPlayer";
    private static MediaPlayer mediaPlayer;

    public void playAudio(String relativePath) {
        // Stop any audio currently playing
        stopAudio();

        try {
            // Create a new MediaPlayer instance
            mediaPlayer = new MediaPlayer();

            // Get the AssetFileDescriptor for the audio file
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(relativePath);

            // Set the data source of the MediaPlayer
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());

            // Prepare the MediaPlayer
            mediaPlayer.prepare();

            // Start playing the audio
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e(TAG, "Error playing audio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void stopAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    private List<String> getSlidePaths(String path) {
        List<String> paths = new ArrayList<>();
        AssetManager assetManager = getAssets();

        try {
            String[] files = assetManager.list(path);
            if (files != null) {
                for (String file : files) {
                    paths.add(path + file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return paths;
    }

    private void showSlide(int slideIndex) {
        String slidePath = slidePaths.get(slideIndex);

        try {
            InputStream inputStream = getAssets().open(slidePath);
            Drawable slideDrawable = Drawable.createFromStream(inputStream, null);
            slideImageView.setImageDrawable(slideDrawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

