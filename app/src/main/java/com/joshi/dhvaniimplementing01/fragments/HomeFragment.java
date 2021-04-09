package com.joshi.dhvaniimplementing01.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.joshi.dhvaniimplementing01.PredictionActivity;
import com.joshi.dhvaniimplementing01.R;
import com.joshi.dhvaniimplementing01.adaptors.PostAdaptors;
import com.joshi.dhvaniimplementing01.models.UserPosts;
import com.joshi.dhvaniimplementing01.models.UserSongUploadDetails;


public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostAdaptors postAdaptors;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore_db;
    private CollectionReference postRef;
    private Button music_genre_classification;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        firestore_db = FirebaseFirestore.getInstance();
        postRef = firestore_db.collection("users").document(mAuth.getUid()).collection("music_info");
        Query query = postRef.orderBy("user_song_name", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<UserSongUploadDetails> options = new FirestoreRecyclerOptions.Builder<UserSongUploadDetails>()
                .setQuery(query,UserSongUploadDetails.class)
                .build();
        postAdaptors = new PostAdaptors(options);
        recyclerView = view.findViewById(R.id.post_items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(postAdaptors);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                postAdaptors.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);




        music_genre_classification = view.findViewById(R.id.music_genre_classification_app);
        music_genre_classification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PredictionActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        postAdaptors.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        postAdaptors.stopListening();
    }
}