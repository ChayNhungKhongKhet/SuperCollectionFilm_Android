package com.phamtantb24.superfilmcollection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.phamtantb24.superfilmcollection.databinding.ActivityProfileBinding;


public class ProfileActivity extends DrawerBaseActivity {

    ActivityProfileBinding activityProfileBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());

        setContentView(activityProfileBinding.getRoot());
        allocateActivityTitle("My Profile");
    }
}