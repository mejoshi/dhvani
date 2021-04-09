package com.joshi.dhvaniimplementing01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Switch;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.joshi.dhvaniimplementing01.fragments.HomeFragment;
import com.joshi.dhvaniimplementing01.fragments.MusicLibraryFragment;
import com.joshi.dhvaniimplementing01.fragments.SearchFragment;
import com.joshi.dhvaniimplementing01.fragments.UploadMusicFragment;
import com.joshi.dhvaniimplementing01.fragments.UserProfileFragment;

public class HomeActivity extends AppCompatActivity {


    private BottomNavigationView bottom_navigation_view;
    private FrameLayout frame_layout_home;
    private HomeFragment home_fragment;
    private SearchFragment search_fragment;
    private MusicLibraryFragment music_library_fragment;
    private UserProfileFragment user_profile_fragment;
    private UploadMusicFragment upload_add_music_fragment;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        frame_layout_home = findViewById(R.id.home_frame_layout);
        bottom_navigation_view = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.top_navigation_toolbar);
        setSupportActionBar(toolbar);

        home_fragment = new HomeFragment();
        upload_add_music_fragment = new UploadMusicFragment();
        music_library_fragment = new MusicLibraryFragment();
        search_fragment = new SearchFragment();
        user_profile_fragment = new UserProfileFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.home_frame_layout,home_fragment).commit();

        bottom_navigation_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.botton_nav_home :
                        setFragment(home_fragment);
                        return true;
                    case R.id.bottom_nav_add :
                        setFragment(upload_add_music_fragment);
                        return  true;
//                    case R.id.bottom_nav_search :
//                        setFragment(search_fragment);
//                        return true;
//                    case R.id.bottom_nav_library :
//                        setFragment(music_library_fragment);
//                        return true;
                    case R.id.bottom_nav_user:
                        setFragment(user_profile_fragment);
                        return true;

                    default:return false;

                }
            }
        });
    }



    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_frame_layout, fragment);
        fragmentTransaction.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser current_user = mAuth.getCurrentUser();
        if (current_user != null){
            // Do nothing
        }else {
            Intent intent_to_main_Act = new Intent(this,MainActivity.class);
            startActivity(intent_to_main_Act);
            finish();
        }

        }
}