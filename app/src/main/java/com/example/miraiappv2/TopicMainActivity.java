package com.example.miraiappv2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TopicMainActivity extends AppCompatActivity {
    private Toast customToast;
    private Handler toastHandler;
    private Runnable toastRunnable;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();

        setContentView(R.layout.activity_topic_select);

        // Array to store the drawable resource IDs
        int[] drawableIds = {R.raw.img_0,R.raw.img_1, R.raw.img_2, R.raw.img_3, R.raw.img_4,
                R.raw.img_5, R.raw.img_6, R.raw.img_7, R.raw.img_8,
                R.raw.img_9, R.raw.img_10, R.raw.img_11, R.raw.img_12,
                R.raw.img_13, R.raw.img_14, R.raw.img_15};
        int[] drawableIds1 = {R.raw.img_0, R.raw.img2, R.raw.img3, R.raw.img4,
                R.raw.img5, R.raw.img6, R.raw.img7, R.raw.img8,
                R.raw.img9, R.raw.img10, R.raw.img11, R.raw.img12,
                R.raw.img13, R.raw.img16, R.raw.img15, R.raw.img_15};

        // Array to store the buttons
        ImageButton[] buttons = {findViewById(R.id.imageButton1), findViewById(R.id.imageButton2),
                findViewById(R.id.imageButton3), findViewById(R.id.imageButton4),
                findViewById(R.id.imageButton5), findViewById(R.id.imageButton6),
                findViewById(R.id.imageButton7), findViewById(R.id.imageButton8),
                findViewById(R.id.imageButton9), findViewById(R.id.imageButton10),
                findViewById(R.id.imageButton11), findViewById(R.id.imageButton12),
                findViewById(R.id.imageButton13), findViewById(R.id.imageButton14),
                findViewById(R.id.imageButton15), findViewById(R.id.imageButton16)};
        Activity activity=this;
        // Load drawables and assign them as button backgrounds
        for (int i = 0; i < buttons.length; i++) {
            Drawable drawable = getResources().getDrawable(drawableIds[i]);
            Drawable drawable1 = getResources().getDrawable(drawableIds1[i]);
            int j=i;
            ImageButton button= buttons[i];
            if (drawable != null) {
                button.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            button.setBackground(drawable1);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            button.setBackground(drawable);
                            if(j>0 & j<15){
                                topic(activity,j);

                            }
                            if (j>14){
                                displayInfoToast();
                            }
                            if (j==0){finish();}
                        }
                        return false;
                    }
                });

            }
        }
    }

    private void topic(Activity v, int j) {
        Intent intent = new Intent(v, TopicActivity.class);
        intent.putExtra("topic", j);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void displayInfoToast() {

        // Create a custom layout for the Toast
        View toastView = getLayoutInflater().inflate(R.layout.custom_toast, null);
        toast = new Toast(getApplicationContext());
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_LONG);

        // Set the gravity to center both horizontally and vertically
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        // Create a handler and runnable to dismiss the Toast after 30 seconds
        toastHandler = new Handler();
        toastRunnable = new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        };
        toastHandler.postDelayed(toastRunnable, 30000);

        // Set a touch listener to cancel the Toast if the screen is tapped
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    toast.cancel();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Cancel the Toast and remove the Runnable when the activity is stopped
        if (toast != null) {
            toast.cancel();
        }
        if (toastHandler != null && toastRunnable != null) {
            toastHandler.removeCallbacks(toastRunnable);
        }
    }
}