package com.Health.health.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.Health.health.Member.membertab;
import com.Health.health.R;
import com.Health.health.Trainer.trainertab;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    TextView example;
    FirebaseDatabase firebaseDatabase;
    String value, currentuser;
    private CustomProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.e("user_id", currentuser);
        customProgressDialog = new CustomProgressDialog(MainActivity.this);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customProgressDialog.show();

        //회원과 트레이너 판단 후 트레이너 or 회원 화면 이동
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Log.e("Userin", String.valueOf(snapshot.child("Users").child(currentuser).child("AccountType").getValue()));
                if (snapshot.child("Users").child(currentuser).child("AccountType").getValue() == null) {
                    Intent intent = new Intent(MainActivity.this, trainertab.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, membertab.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.e("error", String.valueOf(error));
            }
        });
        customProgressDialog.dismiss();
    }

    public class CustomProgressDialog extends Dialog{
        public CustomProgressDialog(Context context){
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_main);
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