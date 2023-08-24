package com.example.project_learn;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    CardView mainCard;
    TextToSpeech mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        String speech = "Hello, our User We are pleased to have you in our learn learn app to promote education to every group of disabled people. To move to the next screen, click on any part of your screen. Click on username space to say your username and identical for password.";

        mainCard =  findViewById(R.id.clickScreen);
        mainCard.setOnLongClickListener(view ->{
            mTTS.stop();
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        });
        mTTS = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = mTTS.setLanguage(Locale.ENGLISH);

                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported");
                }
            } else {
                Log.e("TTS", "Initialization failed");
            }
        });

        mainCard.setOnClickListener(v -> mTTS.speak(speech,TextToSpeech.QUEUE_FLUSH,null,null));


    }




}