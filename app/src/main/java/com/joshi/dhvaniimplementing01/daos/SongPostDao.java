package com.joshi.dhvaniimplementing01.daos;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.joshi.dhvaniimplementing01.models.UserPosts;
import com.joshi.dhvaniimplementing01.models.UserRegistrationDetails;
import com.joshi.dhvaniimplementing01.models.UserSongUploadDetails;


public class SongPostDao {
    private final FirebaseFirestore firestore_db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final CollectionReference collectionReference = firestore_db
            .collection("users")
            .document(mAuth.getUid())
            .collection("music_info");



    public void addSongDetails(String user_song_id,
                               String user_song_name,
                               String user_song_genre,
                               String user_song_description,
                               String user_song_upload_time_stamp,
                               String user_song_storage_url){
        String user_id = mAuth.getUid();
        UserDao userDao = new UserDao();
        Task<DocumentSnapshot> userRegistrationDetails = userDao.getUserDetails(user_id);
        userRegistrationDetails.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserRegistrationDetails userRegistrationDetails1 = documentSnapshot.toObject(UserRegistrationDetails.class);
                UserSongUploadDetails userSongUploadDetails = new UserSongUploadDetails(user_song_id,
                        user_song_name
                        ,user_song_genre
                        ,user_song_description,
                        user_song_upload_time_stamp,
                        user_song_storage_url,
                        userRegistrationDetails1);
                collectionReference.document().set(userSongUploadDetails);

            }
        });


    }
}
