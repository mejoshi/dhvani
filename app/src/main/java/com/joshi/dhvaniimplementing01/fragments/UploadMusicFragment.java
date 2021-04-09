package com.joshi.dhvaniimplementing01.fragments;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.joshi.dhvaniimplementing01.MainActivity;
import com.joshi.dhvaniimplementing01.R;
import com.joshi.dhvaniimplementing01.daos.SongPostDao;
import com.joshi.dhvaniimplementing01.models.UserRegistrationDetails;
import com.joshi.dhvaniimplementing01.models.UserSongUploadDetails;

import java.util.Random;

public class UploadMusicFragment extends Fragment {

    // Storage
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = firebaseStorage.getReference();

    // Authentication and Database
    private FirebaseAuth mAuth;
        // Firestore
        private FirebaseFirestore firestore_db;

    // Views : Buttons and Edit text fields and Text View
    private Button choose_file_button;
    private EditText song_name_field;
    private EditText song_genre_name_field;
    private EditText song_description_field;
    private ImageButton upload_image_button;

    // Request Code
    private final int AUDIO = 0 ;

    // for Song URI and URL and Song Unique ID
    private Uri song_firebase_uri;
    private String song_name_url;
    private String unique_song_id;

    // Dialog
    ProgressDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_music, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Uploading...");
        // Upload and choose file Button
        choose_file_button = view.findViewById(R.id.choose_file_button_upload_fragment);

        // Edit text fields
        song_name_field = view.findViewById(R.id.song_name_upload_fragment_field);
        song_genre_name_field = view.findViewById(R.id.song_artist_upload_fragment_field);
        song_description_field = view.findViewById(R.id.song_description_upload_fragment_field);

        // FireStore initililization
        firestore_db = FirebaseFirestore.getInstance();

        // To get current user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        // Image Button to upload song file
        upload_image_button = view.findViewById(R.id.upload_image_button);
        upload_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadSong();
            }
        });

        if (firebaseUser != null){
                // Choose File
                choose_file_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        file_chooser_button();


                    }
                });


            }
            else {
                Toast.makeText(getActivity().getApplication(),"Error caught, try again later",Toast.LENGTH_SHORT).show();
                Intent session_expired_intent = new Intent(getActivity().getApplication(), MainActivity.class);
                startActivity(session_expired_intent);
                getActivity().finish();
            }

        return view;
    }


    public void uploadSong(){

        // Generate random numbers upto 10
        Random rand = new Random();
        int random_song_id = rand.nextInt(10);
        // Unique song ID and Storage path
        unique_song_id = String.format("upload_%s",random_song_id);
        String storage_path = String.format("songs/users/%s/%s",mAuth.getUid(), unique_song_id);
        // Upload File
        if (song_firebase_uri != null){
            // Will show progress Dialog
            progressDialog.show();
            StorageReference user_song_storage_refrence = storageReference.child(storage_path);
            user_song_storage_refrence.putFile(song_firebase_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // get song url from storage
                    Task<Uri> uriTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri uri_song = uriTask.getResult();
                            song_name_url = uri_song.toString();
                            // Upload meta data of song to FireStore
                            UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails();
                            UserSongUploadDetails user_song_upload_details = new UserSongUploadDetails();

//                            firestore_db.collection("users").document(mAuth.getUid()).collection("music_info").document(unique_song_id).set(user_song_upload_details.userSongUploadInformation());
                            SongPostDao songPostDao = new SongPostDao();
                            songPostDao.addSongDetails(unique_song_id,
                                    song_name_field.getText().toString(),
                                    song_genre_name_field.getText().toString(),
                                    song_description_field.getText().toString(),
                                    String.valueOf(System.currentTimeMillis()),
                                    song_name_url);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity().getApplication(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(getActivity().getApplication(), "Upload Successfully", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity().getApplication(), "Uploading fails", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
//
        }
        else {
            Toast.makeText(getActivity().getApplication(), "Filepath maybe null, try again later", Toast.LENGTH_SHORT).show();
        }
    }

    private void file_chooser_button() {
        Intent audio_upload_intent = new Intent();
        audio_upload_intent.setType("audio/*");
        audio_upload_intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(audio_upload_intent,"Select Audio File"),AUDIO);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUDIO && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null){
                song_firebase_uri = data.getData();

        }
    }
}