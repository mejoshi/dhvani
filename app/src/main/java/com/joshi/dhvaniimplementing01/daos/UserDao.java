package com.joshi.dhvaniimplementing01.daos;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joshi.dhvaniimplementing01.fragments.UploadMusicFragment;
import com.joshi.dhvaniimplementing01.models.UserPosts;
import com.joshi.dhvaniimplementing01.models.UserRegistrationDetails;

public class UserDao {


    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firestore_db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference =
            firestore_db.collection("users")
            .document(mAuth.getUid()).collection("profile_info");
     private UserRegistrationDetails userRegistrationDetails;

    public void addUserDetails(UserRegistrationDetails user_details){
        String user_id = mAuth.getUid();
        collectionReference.document(user_id).set(user_details);
    }

    public Task<DocumentSnapshot> getUserDetails(String  user_id){

        return collectionReference.document(mAuth.getUid()).get();
    }

    public UserRegistrationDetails getUserRegistrationDetails (UserRegistrationDetails userRegistrationDetails){
        return userRegistrationDetails;
    }

}
