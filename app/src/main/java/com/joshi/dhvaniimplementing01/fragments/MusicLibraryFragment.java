package com.joshi.dhvaniimplementing01.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.joshi.dhvaniimplementing01.R;

import java.util.ArrayList;
import java.util.Map;

public class MusicLibraryFragment extends AppCompatActivity {

    private FirebaseFirestore firestore_db;
    private CollectionReference collectionReference_db;
     FirebaseStorage firebaseStorage;
    private ListView retrieved_song_list_view;
    private FirebaseAuth mAuth;



    ArrayList<String> array_list_of_songs = new ArrayList<>();
    ArrayList<String> array_list_of_url = new ArrayList<>();
    ArrayAdapter<String> array_adapter_list_songs;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_music_library);
        //Set On click listner for list view




        // Firebase Initilization
        firestore_db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        retrieved_song_list_view = findViewById(R.id.song_list_view);
        mAuth = FirebaseAuth.getInstance();

        // Collection refrence
        collectionReference_db = firestore_db.collection("users").document(mAuth.getUid()).collection("music_info");
        collectionReference_db.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                            Map<String,Object> retrieve_data = queryDocumentSnapshot.getData();
                            Object object_url = retrieve_data.get("user_song_storage_url");
                            Object object_name = retrieve_data.get("user_song_name");
                            String string_object_url = String.valueOf(object_url);
                            String string_object_name = String.valueOf(object_name);
                            array_list_of_url.add(string_object_url);
                            array_list_of_songs.add(string_object_name);
                    }
                    array_adapter_list_songs = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,array_list_of_songs);
                    retrieved_song_list_view.setAdapter(array_adapter_list_songs);
            }
        });

//        retrieved_song_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });

    }

}