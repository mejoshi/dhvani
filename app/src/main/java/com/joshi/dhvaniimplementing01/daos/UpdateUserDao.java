package com.joshi.dhvaniimplementing01.daos;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joshi.dhvaniimplementing01.models.UserRegistrationDetails;

import java.util.Map;

public class UpdateUserDao {
    private FirebaseFirestore firestore_db;
    private FirebaseAuth mAuth;

    public void updateUserDetails(UserRegistrationDetails update_user_details){
        firestore_db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getUid();
        firestore_db.collection("users")
                .document(user_id)
                .collection("profile_info").
                document(user_id).
                set(update_user_details);
    }



}
