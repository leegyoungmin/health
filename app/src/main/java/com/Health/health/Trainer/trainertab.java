package com.Health.health.Trainer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.Health.health.Fragments.ChatsFragment;
import com.Health.health.Fragments.UsersFragment;
import com.Health.health.Model.Chat;
import com.Health.health.R;
import com.google.android.material.tabs.TabLayout;
import com.Health.health.Interlock.InterLock;



public class trainertab extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabs;
    View view;
    Button btn;
    Fragment selected;
    String Usercode;

    Fragment chatsFragment;
    Fragment UsersFragment;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainertab);



        chatsFragment=new ChatsFragment();
        UsersFragment=new UsersFragment();

        //getSupportFragmentManager().beginTransaction().add(R.id.TrainerFrameLayout).commit();




        if(savedInstanceState !=null){
            onResume();
        }

        changeViewTrainer(0);



        TabLayout tabLayout = (TabLayout)findViewById(R.id.Trainertabhost);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("tab_position", String.valueOf(tab.getPosition()));
                changeViewTrainer(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



    @SuppressLint("ResourceType")
    private void changeViewTrainer(int index){


        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        FrameLayout frameLayout=(FrameLayout)findViewById(R.id.TrainerFrameLayout);
        if(frameLayout.getChildCount()>0){
            frameLayout.removeViewAt(0);
        }


        view=null;
        selected=null;


        switch(index){


            case 0:

                view=inflater.inflate(R.layout.fragment_users,frameLayout,true);

                if(UsersFragment.isAdded()){
                    fragmentTransaction.remove(UsersFragment);
                    UsersFragment=new UsersFragment();
                }


                selected=UsersFragment;

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.TrainerFrameLayout,selected)
                        .commit();
                break;


            case 1:
                view=inflater.inflate(R.layout.fragment_chats,frameLayout,true);

                if(chatsFragment.isAdded()){
                    fragmentTransaction.remove(chatsFragment);
                    chatsFragment=new ChatsFragment();
                }
                selected=chatsFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.TrainerFrameLayout,selected).commit();
                    break;

            case 2:
                break;
            case 3:
                break;
        }

    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.e(this.getClass().getName(),"onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(this.getClass().getName(),"onResume");


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent=getIntent();
        if(resultCode==RESULT_OK){
            Usercode=intent.getStringExtra("UserCode");
            Log.d("Usercode",Usercode);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e(this.getClass().getName(),"onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(this.getClass().getName(),"onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(this.getClass().getName(),"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(this.getClass().getName(),"onDestroy");
    }

}