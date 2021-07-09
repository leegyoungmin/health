package com.Health.health;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.Health.health.Fragments.UsersFragment;

import java.util.ArrayList;

public class request extends AppCompatActivity {
    UsersFragment usersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        Intent intent=getIntent();
        usersFragment=new UsersFragment();
        ArrayList<String> temp_arr=intent.getStringArrayListExtra("ArrayTrainee");


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.request_layout,usersFragment)
                .commit();

        Bundle bundle=new Bundle();
        bundle.putStringArrayList("ArrayTrainee",temp_arr);
        usersFragment.setArguments(bundle);



    }
}