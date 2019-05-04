package com.vijaya.speechtotext;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView mVoiceInputTv, mSubText;
    private ImageButton mSpeakBtn;
    private TextToSpeech TTSSpeaker;
    private static final String NAME = "name";
    String PREFS = "myPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSubText = (TextView) findViewById(R.id.textView);
        mVoiceInputTv = (TextView) findViewById(R.id.voiceInput);
        mSpeakBtn = (ImageButton) findViewById(R.id.btnSpeak);
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });
        hideUI();
        TTSSpeaker=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    TTSSpeaker.setLanguage(Locale.UK);
                    TTSSpeaker.speak("Hello! I'm Superman", TextToSpeech.QUEUE_FLUSH, null);
                    showUI();
                }
            }
        });

    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mVoiceInputTv.setText(result.get(0));
                    String input = result.get(0);
                    Answer(input);
                }
                break;
            }


        }
    }
    public void onPause(){
        super.onPause();
    }
    public void Answer(String Str){
        Log.d("Str" , Str.toLowerCase());
        String symptoms = "I totally understand. Please drop this class!";
        String medicine = "I think you should take a break! Programmers tend to have really serious back pains!.";
        String time;
        String thank ;
        if (Str.toLowerCase().equals("hello")){
            TTSSpeaker.speak("What is your name?", TextToSpeech.QUEUE_FLUSH, null);
        }
        else if (Str.toLowerCase().contains("name")){

            int index = Str.indexOf("name is");
            if (index != -1) {
                // it contains "name is"
                String name = Str.substring(index + 7, Str.length());
                TTSSpeaker.speak("Your name is " + name, TextToSpeech.QUEUE_FLUSH, null);
                setStoredName(name);

            }
        }
        else if (Str.toLowerCase().contains("not feeling good")){
            //extract and save name
            TTSSpeaker.speak(symptoms, TextToSpeech.QUEUE_FLUSH, null);

        }
        else if (Str.toLowerCase().contains("thank")){
            //get saved name
            String storedName = getStoredName();
            thank = "Awww! You are being overly sweet, " + storedName + "! I'm flattered!";
            TTSSpeaker.speak(thank, TextToSpeech.QUEUE_FLUSH, null);

        }
        else if (Str.toLowerCase().contains("time")){
            //extract and save name
            SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm a");//dd/MM/yyyy
            Date now = new Date();String[] strDate = sdfDate.format(now).split(":");
            if(strDate[1].contains("00")) {
                strDate[1] = "o'clock";
            }
            System.out.println("The time is " + sdfDate.format(now));
            time = "The current time is " + sdfDate.format(now);
            TTSSpeaker.speak(time, TextToSpeech.QUEUE_FLUSH, null);
        }
        else if (Str.toLowerCase().contains("medicine")){
            //extract and save name
            TTSSpeaker.speak(medicine, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    private void setStoredName(String name){
        SharedPreferences preferences;
        SharedPreferences.Editor editor;
        preferences = getSharedPreferences(PREFS,0);
        editor = preferences.edit();
        editor.putString(NAME,name).apply();
        editor.commit();
    }
    private String getStoredName(){
        //Get Preferenece
        SharedPreferences myPrefs;
        myPrefs = getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
        String StoredValue=myPrefs.getString(NAME, "");
        Log.d("Stored Value", StoredValue);
        if (!StoredValue.equals("")){
            return StoredValue;
        }
        else{
            return "user";
        }
    }
    private void hideUI(){
        mSpeakBtn.setVisibility(View.GONE);
        mSubText.setVisibility(View.GONE);
    }
    private void showUI(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSpeakBtn.setVisibility(View.VISIBLE);
                mSubText.setVisibility(View.VISIBLE);
            }
        }, 3200);

    }
}