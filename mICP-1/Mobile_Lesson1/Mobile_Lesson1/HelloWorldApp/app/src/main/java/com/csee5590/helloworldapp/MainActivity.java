package com.csee5590.helloworldapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    boolean clicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Button logOutBtn = (Button) findViewById(R.id.button);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clicked = !clicked;
//                if (clicked){
//                    view.setScaleY((float) 1.2);
//                    view.setScaleX((float) 1.2);
//                }
//                else {
//                    view.setScaleY((float) 1);
//                    view.setScaleX((float) 1);
//                }
//            }
//
//        });
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        view.setScaleY((float) 1.2);
                        view.setScaleX((float) 1.2);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        view.setScaleY((float) 1);
                        view.setScaleX((float) 1);
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        logOutBtn.setOnTouchListener(new View.OnTouchListener() {
            private long start = 0;
            private long end = 0;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        //Start counting the pressing duration using start and end
                        start = System.currentTimeMillis();
                        view.setScaleY((float) 1.2);
                        view.setScaleX((float) 1.2);
                        view.setBackgroundColor(Color.BLUE);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        end = System.currentTimeMillis();
                        view.setScaleY((float) 1);
                        view.setScaleX((float) 1);
                        view.setBackgroundColor(Color.RED);
                        //If the button is held/pressed for more than 3 seconds, the screen will not log out to prevent loss of information.
                        //Holding a button for more than three seconds can mean that the user is hesitating leaving the site.
                        if (end - start <=3000){
                            LogOut(view);
                        }
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
    }
    public void LogOut(View v){
        Intent redirect = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(redirect);
    }

}
