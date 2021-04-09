package com.joshi.dhvaniimplementing01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button switch_login_screen_button;
    private Button switch_signup_screen_button;
    private Button skip_to_music_player_button;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switch_login_screen_button = findViewById(R.id.login_button_main_screen);
        switch_signup_screen_button = findViewById(R.id.signup_button_main_screen);
        skip_to_music_player_button = findViewById(R.id.skip_button_to_music_player_main_Screen);

        switch_signup_screen_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSignUpScreen();
            }
        });

        switch_login_screen_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLoginScreen();
            }
        });

        skip_to_music_player_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToMusicPlayer();
            }
        });




    }

    private void switchToMusicPlayer() {
        Intent skip_to_music_player_intent = new Intent(this,LoginActivity.class);
        startActivity(skip_to_music_player_intent);
    }

    private void switchToLoginScreen() {
        Intent login_screen_intent = new Intent(this, LoginActivity.class);
        startActivity(login_screen_intent);
    }

    private void switchToSignUpScreen() {

        Intent signup_screen_intent = new Intent(this, RegistrationActivity.class);
        startActivity(signup_screen_intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            //UpdateUI(currentUser)
            Intent timepass_intent = new Intent(this, HomeActivity.class);
            startActivity(timepass_intent);
            finish();
        }

    }
}