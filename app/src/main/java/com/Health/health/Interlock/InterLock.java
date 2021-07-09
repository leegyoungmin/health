package com.Health.health.Interlock;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.Health.health.Fragments.UsersFragment;
import com.Health.health.Trainer.trainertab;
import com.Health.health.request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.Health.health.Model.Interlocker;

import org.jetbrains.annotations.NotNull;
import com.Health.health.R;
import java.util.ArrayList;
import java.util.List;


public class InterLock extends AppCompatActivity{

    EditText phone_num;
    TextView output;
    Button button;
    String after_text;
    String UserCode;
    ArrayList<String> list;
    ArrayList<String> PreUpdatelist;

    //데이터베이스 전역변수
    String checkingId;
    String findedId;
    ArrayList<String> arr;
    String currentUser=FirebaseAuth.getInstance().getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();





    final Interlocker it=new Interlocker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter_lock);


        phone_num = findViewById(R.id.phone_num);
        output = findViewById(R.id.output);
        button = findViewById(R.id.button1);


        button.setOnClickListener(new View.OnClickListener() {

            final DatabaseReference reference = database.getReference().child("Users");
            final DatabaseReference Trainerreference=database.getReference();
            @Override
            public void onClick(View v) {


                output.setText(phone_num.getText());
                Log.d("test11",arr.toString());



                //1차 탐색
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot value : snapshot.getChildren()) {
                            if (after_text.equals(value.child("PhoneNumber").getValue().toString())) {
                                findedId = value.getKey();
                            }
                        }



                        UserCode=findedId;
                        Intent intent=new Intent(InterLock.this, request.class);


                        list = new ArrayList();
                        list=getPreUpdatelist();
                        list.add(UserCode);

                        //만일 데이터베이스에 동일한 uid가 있다면 덮어씌우기.
                        Trainerreference.child("Trainer").child(currentUser).child("Trainee").setValue(list);

                        intent.putExtra("ArrayTrainee",list);
                        Log.d("Id", findedId);
                        Log.d("UserCode",UserCode);
                        //데이터베이스에 동이한 uid가 없다면 그냥.
                        startActivity(intent);


                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(InterLock.this, "일치하지않습니다.", Toast.LENGTH_SHORT);
                    }
                });


            }
        });


    }

    //우선적으로 Trainer DB에서 데이터를 가져와 Stack시킨다. 즉 업데이트 되기 전 데이터 받아서 전역변수로 설정.
    @Override
    protected void onResume() {
        ArrayList tempArr=new ArrayList();
        final DatabaseReference Trainerreference=database.getReference("Trainer").child(currentUser).child("Trainee");


        Trainerreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot value : snapshot.getChildren()){
                    tempArr.add(value.getValue().toString());
                }


                /*for (DataSnapshot value : snapshot.getChildren()) {
                    tempArr.add(value.child("Trainee").getValue());
                }*/

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        //PreUpdatelist=tempArr;
        setPreUpdatelist(tempArr);
        final DatabaseReference reference = database.getReference().child("Trainer");





        arr=new ArrayList<>();
        after_text=new String();



        //텍스트 감지
        phone_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("change_num",s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //after_text=new String();
                after_text=s.toString();
                Log.d("after_text",after_text);
            }
        });

        super.onResume();
    }





    public String getAfter_text() {
        return after_text;
    }

    public void setAfter_text(String after_text) {
        this.after_text = after_text;
    }

    public ArrayList<String> getArr() {
        return arr;
    }

    public void setArr(ArrayList<String> arr) {
        this.arr = arr;
    }

    public Interlocker getIt() {
        return it;
    }
    public String getCheckingId() {
        return checkingId;
    }

    public void setCheckingId(String checkingId) {
        this.checkingId = checkingId;
    }

    public String getFindedId() {
        return findedId;
    }

    public void setFindedId(String findedId) {
        this.findedId = findedId;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public ArrayList<String> getPreUpdatelist() {
        return PreUpdatelist;
    }

    public void setPreUpdatelist(ArrayList<String> preUpdatelist) {
        PreUpdatelist = preUpdatelist;
    }
}