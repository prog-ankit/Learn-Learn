package com.example.project_learn;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private EditText input_user, input_pwd;
    String username = "", password = "";
    private FirebaseAuth auth;
    private TextToSpeech mTTS;
    private TextView txt;
    private static final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

        input_user = findViewById(R.id.username);
        input_pwd = findViewById(R.id.password);
        txt = findViewById(R.id.title);
        ImageView userMic = findViewById(R.id.userMic);
        ImageView pwdMic = findViewById(R.id.pwdMic);

        Button login = findViewById(R.id.btn_login);

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

        userMic.setOnClickListener(view -> {
            mTTS.speak("Say Username as Username is myUsername", TextToSpeech.QUEUE_FLUSH, null, null);
            startVoiceInput();
        });
        pwdMic.setOnClickListener(view -> {
            mTTS.speak("Say Password as Password is myPassword", TextToSpeech.QUEUE_FLUSH, null, null);
            startVoiceInput();
        });

        login.setOnClickListener(view -> login());

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
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

        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String username = result.get(0);


                String[] splited = username.split("\\s+");
                String type = splited[0];
                if (splited.length > 3)
                    mTTS.speak("Incorrect Way to Speak", TextToSpeech.QUEUE_FLUSH, null, null);
                else {
                    type = type.toLowerCase(Locale.ROOT);
                    if (type.equals("username")) {
                        username = splited[2];
                        input_user.setText(username);
                    } else if (type.equals("password")) {
                        password = splited[2];
                        input_pwd.setText(password);
                    } else
                        mTTS.speak("Incorrect Way to Speak", TextToSpeech.QUEUE_FLUSH, null, null);
//                    Log.d("length", password);
                }


            }
        }
    }

    public void login() {
        username = input_user.getText().toString();
        password = input_pwd.getText().toString();

//        mTTS.speak("Please Enter Username!!",TextToSpeech.QUEUE_FLUSH,null,null);


        if (username.isEmpty() || password.isEmpty()) {
            if (username.isEmpty()) {
                input_user.setError("Please Enter Username!!");
            }
            if (password.isEmpty()) {

                input_pwd.setError("Please Enter Password!!");
            }

        } else {
            if (username.equals("admin") && password.equals("myAdmin")) {
                moveToMainActivity(dashboardActivity.class);
            } else {
                validate();
            }

        }

    }

    private void checkSession() {
        SessionManager sessionManagement = new SessionManager(MainActivity.this);
        String userId = sessionManagement.getSession(MainActivity.this);

        if (!userId.equals("cancel")) {
            alreadyMoved(dashboardActivity.class, userId);
        }

    }


    private void alreadyMoved(Class aclass, String UserID) {
        Intent intent = new Intent(MainActivity.this, aclass);
        intent.putExtra("username", UserID);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void moveToMainActivity(Class aclass) {
        Intent intent = new Intent(MainActivity.this, aclass);
        intent.putExtra("username", username);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void validate() {

        auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SessionManager sessionManagement = new SessionManager(MainActivity.this);
                sessionManagement.saveSession(username);

                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();
                moveToMainActivity(dashboardActivity.class);
            } else {
                Toast.makeText(getApplicationContext(), "Login Failed!! "+task.getException(), Toast.LENGTH_LONG).show();
            }
        });


    }
//        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
//            if(response.equals("Login Success")){
//                User user = new User(username,password);
//                SessionManager sessionManagement = new SessionManager(MainActivity.this);
//                sessionManagement.saveSession(user);
//
//                Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_LONG).show();
//                /*    StyleableToast.makeText(this, "Hello World!", R.style.exampleToast).show();*/
//                if(username.length()==12){
//                    moveToMainActivity(std_dash.class);
//                }
//                else{
//                    if(username.equals("9999")){
//
//                        moveToMainActivity(hod_dash.class);
//                    }else{
//
//                        moveToMainActivity(faculty_dash.class);
//                    }
//                }
//            }
//            else{
//                Toast.makeText(getApplicationContext(),"Invalid User",Toast.LENGTH_LONG).show();
//
//            }
//        }, error -> Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show()){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> param = new HashMap<>();
//                param.put("id",username);
//                param.put("password",password);
//                return param;
//            }
//        };
//
//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//        queue.add(request);
//    }
}