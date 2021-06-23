package com.Health.health;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    TextView example;
    FirebaseDatabase firebaseDatabase;
    String value,currentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        example=findViewById(R.id.example);
        currentuser=FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.e("user_id",currentuser);

        example.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(currentuser.equals(snapshot.child("Users").child(currentuser).getKey())){
                            Intent intent = new Intent(MainActivity.this,membertab.class);
                            startActivity(intent);
                        }
                        if(currentuser.equals(snapshot.child("trainer").child(currentuser).getKey())){
                            Intent intent = new Intent(MainActivity.this,trainertab.class);
                            startActivity(intent);
                        }
                        Log.e("check_type", String.valueOf(snapshot.child("Users").child(currentuser).getValue()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("error", String.valueOf(error));
                    }
                });
            }
        });
    }
}