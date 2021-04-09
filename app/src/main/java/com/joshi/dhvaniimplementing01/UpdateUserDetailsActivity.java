package com.joshi.dhvaniimplementing01;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.joshi.dhvaniimplementing01.daos.UpdateUserDao;
import com.joshi.dhvaniimplementing01.models.UserRegistrationDetails;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

public class UpdateUserDetailsActivity extends AppCompatActivity {

    private ImageButton update_user_details_button;
    private EditText update_full_name_et, update_email_id_et, update_bio_et;
    private FirebaseFirestore firestore_db;
    private FirebaseAuth mAuth;
    private DocumentReference documentReference;
    private FloatingActionButton update_dp_fab;
    private final int IMAGE_REQUEST_CODE = 1;
    private Uri update_image_uri; // Taking from firebase
    private Uri result_uri; // taking from phone device
    private String updated_image_url; // updating String url to FireStore
    private String user_profile_image_url;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private ImageView update_dp_image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_details);
        // Retrieved data from Storage

        // Edit text box init
        update_email_id_et = findViewById(R.id.update_email_id_et);
        update_full_name_et = findViewById(R.id.update_full_name_et);
        update_bio_et = findViewById(R.id.update_bio_et);

        // Firebase service init
        firestore_db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Getting data from the firebase Database
        documentReference = firestore_db.collection("users").document(mAuth.getUid()).collection("profile_info").document(mAuth.getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String user_full_name = documentSnapshot.getString("user_full_name");
                    String user_email_id = documentSnapshot.getString("user_email_id");
                    String user_short_description = documentSnapshot.getString("user_short_description");
                    user_profile_image_url = documentSnapshot.getString("user_profile_pic_url");
                    update_email_id_et.setText(user_email_id);
                    update_full_name_et.setText(user_full_name);
                    update_bio_et.setText(user_short_description);
                    fetchDP(user_profile_image_url);  // To fetch data into imageView this function has been used
                    System.out.println("Profile Image URL 1 : " + user_profile_image_url + " URI " + updated_image_url);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateUserDetailsActivity.this, "Can't work", Toast.LENGTH_SHORT).show();

            }
        });
        // Image View Init
        update_dp_image_view = findViewById(R.id.update_dp_image_view);

//        Bitmap bitmap_user = BITMA
//        Picasso.get().load(user_profile_image_url).resize(100, 100).into(update_dp_image_view);




        String user_id = mAuth.getUid();
        update_user_details_button = findViewById(R.id.submit);
        update_user_details_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If user update fields only then the previous image will not be changed to null
//                System.out.println("Profile URL 4 " + user_profile_image_url);
//                System.out.println("Profile URL 5 " + updated_image_url);
//                System.out.println("Profile URL 6 " + result_uri);

                if (result_uri == null){
                    updated_image_url = user_profile_image_url;
                }

                if (user_id != null) {
                    ArrayList<String> num_followers = new ArrayList<>();
                    num_followers.add(0,"0");
                    UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(
                            user_id,
                            update_full_name_et.getText().toString(),
                            update_email_id_et.getText().toString(),
                            update_bio_et.getText().toString(),
                            updated_image_url,
                            num_followers);

                    UpdateUserDao updateUserDao = new UpdateUserDao();
                    updateUserDao.updateUserDetails(userRegistrationDetails);

                    Toast.makeText(UpdateUserDetailsActivity.this, "Profile Update Successfully", Toast.LENGTH_SHORT).show();
                    Intent user_profile_intent = new Intent(UpdateUserDetailsActivity.this, HomeActivity.class);
                    startActivity(user_profile_intent);
                    finish();
                } else {
                    Toast.makeText(UpdateUserDetailsActivity.this, "Failed to load data!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Update profile image
        update_dp_fab = findViewById(R.id.update_user_profile_pic_fab);
        update_dp_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_image();
            }
        });
    }


    public void fetchDP(String user_profile_image_url) {
        System.out.println("Profile Image URL 3 : " + user_profile_image_url);
        if (user_profile_image_url != null){
            Picasso.get().load(user_profile_image_url).resize(update_dp_image_view.getWidth(),update_dp_image_view.getHeight()).into(update_dp_image_view);
        }else {
            Toast.makeText(this, "Can't fetch data", Toast.LENGTH_SHORT).show();
        }
    }

    public void get_image() {
        Intent get_dp_intent = new Intent();
        get_dp_intent.setType("image/*");
        get_dp_intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(get_dp_intent, "Choose Image"), IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            update_image_uri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                result_uri = result.getUri();
                int height = update_dp_image_view.getHeight() ;
                int width = update_dp_image_view.getWidth();
                if (width == 0 || height == 0){
                    height = 100;
                    width = 100;
                }
                Picasso.get().load(result_uri).resize(width, height).into(update_dp_image_view);
                uploadPhoto();

            }
        }
    }

    private void uploadPhoto() {
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        String storage_path = String.format("songs/users/%s/profile_pic",mAuth.getUid());
        if (update_image_uri != null){
            storageReference = storageReference.child(storage_path);
            storageReference.putFile(result_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri firebase_uri_image = uriTask.getResult();
                            updated_image_url = firebase_uri_image.toString();

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
                Picasso.get().load(user_profile_image_url).resize(update_dp_image_view.getWidth(),update_dp_image_view.getHeight()).into(update_dp_image_view);
        }
    }
}
