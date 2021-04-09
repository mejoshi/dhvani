package com.joshi.dhvaniimplementing01;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.joshi.dhvaniimplementing01.daos.UserDao;
import com.joshi.dhvaniimplementing01.models.UserRegistrationDetails;

import java.util.ArrayList;


public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button registration_button;
    private EditText email_field;
    private EditText full_name_field;
    private EditText password_field;
    ProgressDialog progressDialog;
    FirebaseFirestore firestore_db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // FireStore
        firestore_db = FirebaseFirestore.getInstance();

        // Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering....");

        registration_button = findViewById(R.id.signup_button_signup_screen);
        email_field = findViewById(R.id.emailid_field_registration);
        full_name_field = findViewById(R.id.full_name_field_registration);
        password_field = findViewById(R.id.password_field_registration);
        registration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationScreen();
            }
        });
    }

    private void RegistrationScreen() {
        progressDialog.show();
        String login_email = email_field.getText().toString();
        String fullname = full_name_field.getText().toString();
        String login_password = password_field.getText().toString();

        if (TextUtils.isEmpty(login_email) ||  TextUtils.isEmpty(login_password)){
                Toast.makeText(this, "Don't leave blank",Toast.LENGTH_LONG).show();
        }

        mAuth.createUserWithEmailAndPassword(login_email,login_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser current_user = mAuth.getCurrentUser();
                        String user_id = mAuth.getUid();
                        ArrayList<String> num_followers = new ArrayList<>();
                        num_followers.add(0,"0");
                    // User Registration upload Details
                    UserRegistrationDetails user_registration_details = new UserRegistrationDetails(user_id,fullname,login_email,"Create your own Dhun!","https://firebasestorage.googleapis.com/v0/b/dhvani-implementation01.appspot.com/o/profile-42914_12801-150x150.png?alt=media&token=a1307646-1865-4427-b780-cec811d30168", num_followers);
                    UserDao firebaseFirestoreRegistrationService = new UserDao();
                    firebaseFirestoreRegistrationService.addUserDetails(user_registration_details);

                    Toast.makeText(getApplicationContext(),"Registration Successful!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                        profile();         
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Maybe you have already registered",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // After Registration jump to setup profile information, for now it's default home activity
    private void profile() {
        Intent news_feed_intent = new Intent(this,ProfileSetupActivity.class);
        startActivity(news_feed_intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser current_user = mAuth.getCurrentUser();
        if (current_user != null){
            // Update UI
            Intent newsfeed_intent = new Intent(this,HomeActivity.class);
            startActivity(newsfeed_intent);
            finish();
        }else{

        }

    }
}