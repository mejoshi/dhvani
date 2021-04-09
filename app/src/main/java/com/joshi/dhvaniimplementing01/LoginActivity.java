package com.joshi.dhvaniimplementing01;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button login_button;
    private EditText emailid_login_field;
    private EditText password_login_field;
    ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        login_button = findViewById(R.id.login_button_login_screen);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginScreen();
            }
        });

    }

    public void loginScreen() {
        progressDialog.show();
        emailid_login_field = findViewById(R.id.emailid_login_field);
        password_login_field = findViewById(R.id.password_login_field);
        String login_email = emailid_login_field.getText().toString();
        String login_pass = password_login_field.getText().toString();

        if (TextUtils.isEmpty(login_email) || TextUtils.isEmpty(login_pass)){
                Toast.makeText(this, "Format is required", Toast.LENGTH_SHORT).show();
        }

        mAuth.signInWithEmailAndPassword(login_email, login_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    newsfeed();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser current_user = mAuth.getCurrentUser();

        if (current_user != null){
            // Update Activity
            Intent news_feed = new Intent(this,HomeActivity.class);
            startActivity(news_feed);
            finish();
        }
    }

    public void newsfeed(){
        // Update Activity
        Intent news_feed_intent = new Intent(this,HomeActivity.class);
        startActivity(news_feed_intent);
        finish();
    }
}