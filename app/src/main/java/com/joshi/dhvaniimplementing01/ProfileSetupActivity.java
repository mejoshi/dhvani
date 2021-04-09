package com.joshi.dhvaniimplementing01;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileSetupActivity extends AppCompatActivity {
        private Button skip_to_main_button,submit_profile_setup_details_button;
        private EditText bio_detail_et;
        private FirebaseAuth mAuth;
        private FirebaseFirestore firestore_db;
        private FloatingActionButton add_profile_pic_fab;
        private ImageView profile_pic_image_view;
        private Uri profile_pic_uri;
        private final int  IMAGE_REQUEST_CODE = 1;
        private DocumentReference documentReference;
        private String user_profile_image_url;
        private FirebaseStorage firebaseStorage;
        private StorageReference storageReference;
        private Uri result_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        mAuth = FirebaseAuth.getInstance();
        firestore_db = FirebaseFirestore.getInstance();
        String user_id = mAuth.getUid();

        // Skip to Home Activity
        skip_to_main_button = findViewById(R.id.skip_button_to_main_activity_btn);
        skip_to_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main_activity_intent = new Intent(ProfileSetupActivity.this,HomeActivity.class);
                startActivity(main_activity_intent);
                finish();
            }
        });
        bio_detail_et = findViewById(R.id.setupup_profile_bio_et);
        Map<String, Object> bio_details = new HashMap<>();
        bio_details.put("user-short-description",bio_detail_et.getText().toString());

        // If submit image will upload on the server
        submit_profile_setup_details_button = findViewById(R.id.submit_profile_setup_btn);
        submit_profile_setup_details_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bio_detail_et.getText().toString() == null){
                    firestore_db.collection("users")
                            .document(user_id)
                            .collection("profile_info").
                            document(user_id).
                            update("user_short_description","Create your own dhun");
                }else {
                    firestore_db.collection("users")
                            .document(user_id)
                            .collection("profile_info").
                            document(user_id).
                            update("user_short_description",bio_detail_et.getText().toString());
                }
                System.out.println("Url is : " + user_profile_image_url);
                fetchDP(user_profile_image_url);
                firestore_db.collection("users")
                        .document(user_id)
                        .collection("profile_info").
                        document(user_id).
                        update("user-profile-image",user_profile_image_url);
                
                Intent intent = new Intent(ProfileSetupActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Image view Init
        profile_pic_image_view = findViewById(R.id.setup_image_profile);

        // Floating action button to add profile photo
        add_profile_pic_fab = findViewById(R.id.floating_setup_dp_button);
        add_profile_pic_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_profile_pic();
            }
        });
    }

    private void fetchDP(String user_profile_image_url) {
        if (user_profile_image_url != null){
            Picasso.get().load(user_profile_image_url).resize(profile_pic_image_view.getWidth(),profile_pic_image_view.getHeight()).into(profile_pic_image_view);
        }else {
            Toast.makeText(this, "Can't fetch data", Toast.LENGTH_SHORT).show();
        }
    }

    private void get_profile_pic() {
        Intent add_image_intent = new Intent();
        add_image_intent.setType("image/*");
        add_image_intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(add_image_intent,"Upload Profile Pic"), IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            profile_pic_uri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                result_uri = result.getUri();
                Picasso.get().load(result_uri).resize(profile_pic_image_view.getWidth(), profile_pic_image_view.getHeight()).into(profile_pic_image_view);
                uploadPhoto();
            }
        }
    }

    private void uploadPhoto() {
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        String storage_path = String.format("songs/users/%s/profile_pic",mAuth.getUid());
        if (profile_pic_uri != null){
            storageReference = storageReference.child(storage_path);
            storageReference.putFile(result_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri firebase_uri_image = uriTask.getResult();
                            user_profile_image_url = firebase_uri_image.toString();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }else {
            Picasso.get().load(user_profile_image_url).resize(profile_pic_image_view.getWidth(),profile_pic_image_view.getHeight()).into(profile_pic_image_view);
        }
    }
    }