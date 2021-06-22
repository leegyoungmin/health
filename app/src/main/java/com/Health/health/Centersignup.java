package com.Health.health;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Centersignup extends AppCompatActivity {
    EditText code;
    Button next;
    String code_value;
    private FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centersignup);

        code=(EditText)findViewById(R.id.center_code);
        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code_value=s.toString();
                Log.e("code_value",code_value);

            }
        });


        next=(Button)findViewById(R.id.register);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference().child("fitness");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot value : snapshot.getChildren()){
                            if(code_value.equals(value.getValue().toString())){
                                Intent intent = new Intent(Centersignup.this,membersignup.class);
                                intent.putExtra("account_type",1);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(Centersignup.this,"가입되지 않은 센터입니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Log.e("value.getvalue", String.valueOf(value.getValue()));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("example_1", String.valueOf(error));
                    }
                });
            }
        });

    }
}