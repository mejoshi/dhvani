package com.joshi.dhvaniimplementing01.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.joshi.dhvaniimplementing01.MainActivity;
import com.joshi.dhvaniimplementing01.R;
import com.joshi.dhvaniimplementing01.UpdateUserDetailsActivity;
import com.squareup.picasso.Picasso;



public class UserProfileFragment extends Fragment {

    private Button signout_button;
    private TextView user_name;
    private TextView user_email_id;
    private FirebaseAuth mAuth;
    private DocumentReference documentReference;
    private FirebaseFirestore firestore_db;
    private Button show_your_upload_button;
    private ImageView user_profile_photo_image_view;
    private Button edit_profile_button;

    private FirebaseStorage firebaseStorage_user_profile;
    private StorageReference storageReference_user_profile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        user_name =view.findViewById(R.id.username_profile);
        user_email_id = view.findViewById(R.id.emailid_profile);
        signout_button = view.findViewById(R.id.signout_btn);
        user_profile_photo_image_view = view.findViewById(R.id.user_profile_pic_view);
        edit_profile_button = view.findViewById(R.id.edit_profile_information_button);

        edit_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit_profile_intent = new Intent(getActivity().getApplication(), UpdateUserDetailsActivity.class);
                startActivity(edit_profile_intent);
            }
        });


        // Show your Upload Button

        show_your_upload_button = view.findViewById(R.id.show_your_uploads_button);
        show_your_upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYourUploadButton();
            }
        });

        // Document Reference initlization => for path
        firestore_db = FirebaseFirestore.getInstance();
        documentReference = firestore_db.collection("users").document(mAuth.getUid()).collection("profile_info").document(mAuth.getUid());


        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent signout_intent = new Intent(getActivity().getApplication(), MainActivity.class);
                startActivity(signout_intent);
                getActivity().finish();
            }
        });

            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        String name = documentSnapshot.getString("user_full_name");
                        String bio = documentSnapshot.getString("user_short_description");
                        String image_url = documentSnapshot.getString("user_profile_pic_url");
                        fetchDP(image_url);
                        user_name.setText(name);
                        user_email_id.setText(bio);
                    }else {
                        Toast.makeText(getActivity().getApplication(), "Failure", Toast.LENGTH_SHORT).show();
                        Intent direct_to_main_activity = new Intent(getActivity().getApplication(),MainActivity.class);
                        startActivity(direct_to_main_activity);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity().getApplication(), "Can't work", Toast.LENGTH_SHORT).show();

                }
            });

        return view;

    }

    private void fetchDP(String image_url) {
        Picasso.get().load(image_url).resize(user_profile_photo_image_view.getWidth(),user_profile_photo_image_view.getHeight()).into(user_profile_photo_image_view);
    }


    public void showYourUploadButton(){
        Intent music_library_intent = new Intent(getActivity().getApplication(), MusicLibraryFragment.class);
        startActivity(music_library_intent);
    }

}