package com.joshi.dhvaniimplementing01.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.joshi.dhvaniimplementing01.R;
import com.joshi.dhvaniimplementing01.adaptors.UserAdaptorSearch;
import com.joshi.dhvaniimplementing01.models.UserSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private androidx.appcompat.widget.SearchView searchView;
    private UserAdaptorSearch userAdaptorSearch;
    private FirebaseFirestore firestore_db;
    private CollectionReference collectionReference;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ArrayList<String> user_full_name_list;
    private ArrayList<String> user_profile_pic_url_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.search_recycle_view);
        searchView = view.findViewById(R.id.search_view);

        firestore_db = FirebaseFirestore.getInstance();
        String user_id = mAuth.getUid();
        collectionReference = firestore_db.collection(user_id).document(user_id).collection("profile_pic");
        getUserDetails();

//        Query query = collectionReference.orderBy("user_uid", Query.Direction.ASCENDING);
//        FirestoreRecyclerOptions<UserSearch> options = new FirestoreRecyclerOptions.Builder<UserSearch>()
//                .setQuery(query, UserSearch.class)
//                .build();
//        userAdaptorSearch = new UserAdaptorSearch(options);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(userAdaptorSearch);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    private void getUserDetails() {
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                    Map<String, Object> result = queryDocumentSnapshot.getData();
                    UserSearch userSearch  = queryDocumentSnapshot.toObject(UserSearch.class);
                    String user_name = userSearch.getCreated_by().getUser_full_name();
                    System.out.println("name1 " + userSearch.getUser_full_name() + "name2" + userSearch.getCreated_by().getUser_full_name());
                    String user_dp_pic = userSearch.getCreated_by().getUser_profile_pic_url();
                    user_full_name_list.add(user_name);
                    user_profile_pic_url_list.add(user_dp_pic);
                }
                System.out.println("User name list " + user_full_name_list +
                         "\nUser url " + user_profile_pic_url_list);
            }
        });
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        userAdaptorSearch.stopListening();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        userAdaptorSearch.startListening();
//    }
}