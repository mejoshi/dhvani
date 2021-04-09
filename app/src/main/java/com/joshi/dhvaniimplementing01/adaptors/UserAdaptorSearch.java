package com.joshi.dhvaniimplementing01.adaptors;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.joshi.dhvaniimplementing01.R;
import com.joshi.dhvaniimplementing01.models.UserSearch;
import com.squareup.picasso.Picasso;

public class UserAdaptorSearch extends FirestoreRecyclerAdapter<UserSearch, UserAdaptorSearch.SearchHolder> {
    private SearchHolder holder;
    public UserAdaptorSearch(@NonNull FirestoreRecyclerOptions<UserSearch> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SearchHolder holder, int position, @NonNull UserSearch model) {
            holder.full_name.setText(model.getCreated_by().getUser_full_name());
            Picasso.get().load(model.getCreated_by().getUser_profile_pic_url()).into(holder.profile_pic_image);
            holder.follow_button.setVisibility(View.VISIBLE);
            holder.follow_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_users,parent,false);
        holder = new SearchHolder(view);
        return holder;
    }

    public class SearchHolder extends RecyclerView.ViewHolder{
        TextView full_name;
        Button follow_button;
        ImageView profile_pic_image;

        public SearchHolder(@NonNull View itemView) {
            super(itemView);

         full_name = itemView.findViewById(R.id.fullname_search);
         follow_button = itemView.findViewById(R.id.followbutton);
         profile_pic_image = itemView.findViewById(R.id.profile_pic_search);


        }
    }
}

