package com.example.mini.stupro;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class join extends AppCompatActivity {
EditText t1,t2;
private TextView forbtn;
    private FirebaseAuth fauth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference mDatabase;
    TextToSpeech textToSpeech;
    private static CheckBox show_hide_password;
Button bt1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        t1=(EditText)findViewById(R.id.user);
        t2=(EditText)findViewById(R.id.pass);
        forbtn=(TextView)findViewById(R.id.forgbtn);
        forbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(join.this, forgetPass.class);
                startActivity(intent);
            }
        });
        bt1=(Button)findViewById(R.id.logbtn);
        fauth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }

            }
        });

        show_hide_password = (CheckBox)findViewById(R.id.shp);
        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {

                if (isChecked) {

                    show_hide_password.setText(R.string.hide_pwd);
                    t2.setInputType(InputType.TYPE_CLASS_TEXT);
                    t2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    show_hide_password.setText(R.string.show_pwd);
                    t2.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    t2.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());

                }

            }
        });

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(join.this, "PROCESSING….", Toast.LENGTH_LONG).show();
                String email = t1.getText().toString().trim();
                String password = t2.getText().toString().trim();
                if (!TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password)){
                    fauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                checkUserExistence();
                            }else {
                                Toast.makeText(join.this, "Username/Password is incorrecct", Toast.LENGTH_SHORT).show();
                                speakToast("Username or Password is incorrecct");
                            } } });
                }else {
                    Toast.makeText(join.this, "Complete all fields", Toast.LENGTH_SHORT).show();
                    speakToast("Please Complete all Fields");
                } }

        });






    }

    public void checkUserExistence(){
        final String user_id = fauth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)){
                    startActivity(new Intent(join.this, MainPageActivity.class));
                }else {
                    Toast.makeText(join.this, "User not registered!", Toast.LENGTH_SHORT).show();
                    speakToast("User not registered");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
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


