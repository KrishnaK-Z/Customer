package com.example.mini.stupro;

import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class forgetPass extends AppCompatActivity {

    TextView txtv1,closeb;
    Button btnreset;
    private FirebaseAuth auth;
    TextToSpeech textToSpeech;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        txtv1=(TextView)findViewById(R.id.editText);
        btnreset=(Button)findViewById(R.id.reser);
        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }

            }
        });

        txtv1=(TextView)findViewById(R.id.editText);
        closeb=(TextView)findViewById(R.id.closebtn);

        closeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetPass.super.onBackPressed();
            }
        });
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetpass(txtv1.getText().toString());
            }
        });
        auth = FirebaseAuth.getInstance();
    }

    private void resetpass(final String email)
    {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(forgetPass.this, "Reset mail sent to "+email, Toast.LENGTH_SHORT).show();
                    speakToast("Reset mail has sent");

                }
                else
                {
                    Toast.makeText(forgetPass.this, "Wrong Email", Toast.LENGTH_SHORT).show();
                    speakToast("wrong email");
                }
            }
        });
    }
    public void speakToast(String text)
    {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        if(textToSpeech!=null)
        {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
