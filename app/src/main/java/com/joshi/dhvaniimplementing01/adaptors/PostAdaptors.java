package com.joshi.dhvaniimplementing01.adaptors;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joshi.dhvaniimplementing01.R;
import com.joshi.dhvaniimplementing01.models.UserSongUploadDetails;
import com.squareup.picasso.Picasso;


public class PostAdaptors extends FirestoreRecyclerAdapter<UserSongUploadDetails, PostAdaptors.PostHolder> {
    private PostHolder holder;
    public PostAdaptors(@NonNull FirestoreRecyclerOptions<UserSongUploadDetails> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostHolder holder, int position, @NonNull UserSongUploadDetails model) {

        holder.user_name.setText(model.getCreated_by().getUser_full_name());
        Picasso.get().load(model.getCreated_by().getUser_profile_pic_url()).into(holder.user_dp);
        System.out.println("Image Url " + model.getCreated_by().getUser_profile_pic_url());
        holder.user_song_name.setText(model.getUser_song_name());
        String formatted_text = "<b><i>" + model.getCreated_by().getUser_full_name() +"</i></b> " + model.getUser_short_description();
        holder.user_song_desc.setText(Html.fromHtml(formatted_text));
        holder.user_post_time.setText("Song ID : " + model.getUser_song_time_stamp());

        holder.user_post_song_seekbar.setMax(100);
        holder.user_play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mediaPlayer.isPlaying()){
                    holder.handler.removeCallbacks(runnable);
                    holder.mediaPlayer.pause();
                    holder.user_play_button.setImageResource(R.drawable.ic_baseline_play_circle_24);
                }else {
                    holder.mediaPlayer.start();
                    holder.user_play_button.setImageResource(R.drawable.ic_baseline_pause_circle_24);
                    updateSeekBar();
                }
            }
        });
        System.out.println("URL is " + model.getUser_song_storage_url());

        mediaPlayerSet(model.getUser_song_storage_url());

    }

    private void mediaPlayerSet(String url){
        System.out.println("URL is " + url);
        try {
            holder.mediaPlayer.setDataSource(url);
            holder.mediaPlayer.prepare();
            holder.song_duration_end.setText(milliSecondToTimer(holder.mediaPlayer.getDuration()));
            System.out.println("Duration is " + holder.mediaPlayer.getDuration());
        }catch (Exception e){
//            Toast.makeText(context, "Failed to load", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration = holder.mediaPlayer.getCurrentPosition();
            holder.song_duration.setText(milliSecondToTimer(currentDuration));
            System.out.println("Current Duration is :  " + currentDuration);


        }
    };

    private void updateSeekBar() {
        if (holder.mediaPlayer.isPlaying()){
            holder.user_post_song_seekbar.setProgress((int) (((float) holder.mediaPlayer.getCurrentPosition()/holder.mediaPlayer.getDuration()) *100));
            holder.handler.postDelayed(runnable,1000);
        }
    }


    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_post,parent,false);
        holder = new PostHolder(view);
        return holder;
    }

    public class PostHolder extends RecyclerView.ViewHolder{

        // For View
        public ImageView user_dp ; /* user_no_like_image_view, user_liked_image_view ,user_post_option_image_view; */
        public TextView user_name,user_post_time,user_song_name,user_song_desc, song_duration, song_duration_end;
        public SeekBar user_post_song_seekbar;
        public ImageButton user_play_button, user_pause_button;
        public FirebaseFirestore firestore_db = FirebaseFirestore.getInstance();
        public FirebaseAuth mAuth = FirebaseAuth.getInstance();
        public CollectionReference collectionReference = firestore_db.collection("users").document(mAuth.getUid()).collection("music_info");
        public MediaPlayer mediaPlayer = new MediaPlayer();
        public Handler handler = new Handler();

        public PostHolder(@NonNull View itemView) {
            super(itemView);



            // Init views
            user_dp = itemView.findViewById(R.id.user_post_profile_pic);
/*          user_no_like_image_view = itemView.findViewById(R.id.user_post_nolike);
            user_liked_image_view = itemView.findViewById(R.id.user_post_liked_song);
            user_post_option_image_view = itemView.findViewById(R.id.user_post_option); */
            song_duration = itemView.findViewById(R.id.song_duration_tv);
            user_name = itemView.findViewById(R.id.user_post_full_name);
            user_post_time = itemView.findViewById(R.id.user_post_time);
            user_song_name = itemView.findViewById(R.id.user_post_song_name);
            user_song_desc = itemView.findViewById(R.id.user_post_song_description);
            user_post_song_seekbar = itemView.findViewById(R.id.user_post_song_seek_bar);
            user_play_button = itemView.findViewById(R.id.user_post_song_play_button);
//            user_pause_button = itemView.findViewById(R.id.user_post_song_pause_button);
            song_duration_end = itemView.findViewById(R.id.song_duration_tv2);
        }

    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    private String milliSecondToTimer(long milli_second){
        String timerString = "";
        String secondString;

        int hours = (int) (milli_second / (1000 * 60 * 60));
        int minutes = (int) (milli_second % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milli_second % (1000 * 60 * 60)) % (1000 * 60) / 1000 );

        if (hours > 0){
            timerString = hours + ":";
        }
        if (seconds < 10){
            secondString = "0" + seconds;
        }else {
            secondString =  "" + seconds;
        }
        timerString = timerString + minutes + ":" + secondString;

        return timerString;
    }


}
