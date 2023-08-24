package com.example.project_learn;

import static com.example.project_learn.globals.mTTS;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;


public class dashboardActivity extends AppCompatActivity {

    String username, user, pwd;
    private FirebaseAuth auth;
    private CardView card1, card2, card3, card4, card5, card6, addform;
    private LinearLayout linear;
    private ImageView img;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        session = new SessionManager(dashboardActivity.this);
        linear = findViewById(R.id.showVideos);
        addform = findViewById(R.id.addForm);
        auth = FirebaseAuth.getInstance();

        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        card5 = findViewById(R.id.card5);
        card6 = findViewById(R.id.card6);
        img = findViewById(R.id.logout);

        img.setOnClickListener(view -> {
            auth.signOut();
            session.removeSession();
            Intent intent = new Intent(dashboardActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

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

        setClickListeners();
        setdoubleClickListeners();
        username = getIntent().getStringExtra("username");

        if (username.equals("admin")) {
            addform.setVisibility(View.VISIBLE);
            linear.setVisibility(View.GONE);
            EditText stdUser, stdPwd;

            stdUser = findViewById(R.id.uName);
            stdPwd = findViewById(R.id.uPwd);


            Button addStd = findViewById(R.id.addStd);
            addStd.setOnClickListener(v -> {
                Toast.makeText(this, "Hello Everyone", Toast.LENGTH_SHORT).show();
                String user = stdUser.getText().toString();
                String pwd = stdPwd.getText().toString();
                Toast.makeText(this, user + " : " + pwd, Toast.LENGTH_SHORT).show();

                if (user.equals("") || pwd.equals("")) {
                    Toast.makeText(dashboardActivity.this, "Field is empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (pwd.length() < 6)
                        Toast.makeText(dashboardActivity.this, "Password must be long", Toast.LENGTH_SHORT).show();
                    else {
                        auth.createUserWithEmailAndPassword(user, pwd).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(dashboardActivity.this, "Student Registered!!", Toast.LENGTH_SHORT).show();
                                stdPwd.setText("");
                                stdUser.setText("");
                                stdPwd.clearFocus();
                            } else {
                                Toast.makeText(dashboardActivity.this, "Student Not Registered!!", Toast.LENGTH_SHORT).show();
                                Toast.makeText(dashboardActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            });


        } else {
            addform.setVisibility(View.GONE);
            linear.setVisibility(View.VISIBLE);
        }
    }

    private void setClickListeners() {
        card1.setOnClickListener(view -> mTTS.speak("BK Shivani Motivation and long click to play video", TextToSpeech.QUEUE_FLUSH, null, null));
        card2.setOnClickListener(view -> mTTS.speak("Learn English with Us and long click to play video", TextToSpeech.QUEUE_FLUSH, null, null));
        card3.setOnClickListener(view -> mTTS.speak("How Hellen Keller learned to speak? and long click to play video", TextToSpeech.QUEUE_FLUSH, null, null));
        card4.setOnClickListener(view -> mTTS.speak("The Science of Hearing by Douglas Oliver and long click to play video", TextToSpeech.QUEUE_FLUSH, null, null));
        card5.setOnClickListener(view -> mTTS.speak("Maheshwari's how to be fearless and long click to play video", TextToSpeech.QUEUE_FLUSH, null, null));
        card6.setOnClickListener(view -> mTTS.speak("The Dyslexic Mindset: It's Not What You Think and long click to play video", TextToSpeech.QUEUE_FLUSH, null, null));
    }

    private void setdoubleClickListeners() {

        card1.setOnLongClickListener(v -> {
            transferControl("https://www.youtube.com/watch?v=nzeVRLBCZNM&pp=ygUdbW90aXZhdGUgeW91ciBzb3VsIGJrIHNoaXZhbmk%3D");
            return true;
        });

        card2.setOnLongClickListener(v -> {
            transferControl("https://www.youtube.com/watch?v=GEogN8Kd6-s&pp=ygUWbGVhcm4gZW5nbHNpc2ggd2l0aCB1cw%3D%3D");
            return true;
        });
        card3.setOnLongClickListener(v -> {
            transferControl("https://www.youtube.com/watch?v=_XSDpEY2VbU&pp=ygUgaG93IGhlbGxlbiBrZWxsZXIgbGVhcm4gdG8gc3BlYWs%3D");

            return true;
        });
        card4.setOnLongClickListener(view -> {
            transferControl("https://www.youtube.com/watch?v=LkGOGzpbrCk&pp=ygUXdGhlIHNjaWVuY2Ugb2YgaGVhcmluZyA%3D");
            return true;
        });
        card5.setOnLongClickListener(view -> {
            transferControl("https://www.youtube.com/watch?v=UiLvA7lHKp8");
            return true;
        });
        card6.setOnLongClickListener(view -> {
            transferControl("https://www.youtube.com/watch?v=V2403mG2-Gk&pp=ygUPZHlsZXhpYSBtaW5kc2V0");
            return true;
        });

    }

    private void transferControl(String url) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private void callStudent() {

    }


}
