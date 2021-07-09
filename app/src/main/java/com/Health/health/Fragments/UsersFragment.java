package com.Health.health.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.Health.health.Adapter.UserAdapter;
import com.Health.health.Interlock.InterLock;
import com.Health.health.Model.Users;
import com.Health.health.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ArrayList<Users> mUsers;
    private ArrayList<String> Trainee;
    private ArrayList<String> tempArr;


    private ArrayList<String> Bundledusercode;








    public UsersFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Log.e(this.getClass().getName(),"onActivityView");
        View view = inflater.inflate(R.layout.fragment_users,container,false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();
        ReadUsers();




        Button plusbtn = (Button) view.findViewById(R.id.Plusbtn);
        plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), InterLock.class);
                startActivity(intent);


            }
        });



        return view;
    }





    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(this.getClass().getName(),"onActivityCreated");

    }




    @Override
    public void onResume() {
        super.onResume();
        Log.e(this.getClass().getName(),"onActivityResume");


        Bundle bundle=getArguments();
        if(bundle!=null){
            Bundledusercode=getArguments().getStringArrayList("ArrayTrainee");

            if(Bundledusercode==null){
                //it is empty
            }
            else{
                for(int i=0; i< Bundledusercode.size();i++){
                    Log.d("iiii",Bundledusercode.get(i));
                }
                setBundledusercode(Bundledusercode);
                //it is not empty
            }
        }

    }





    private void ReadUsers(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser=FirebaseAuth.getInstance().getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Trainee = new ArrayList<>();

        DatabaseReference Traineereference=FirebaseDatabase
                .getInstance()
                .getReference("Trainer")
                .child(currentUser);


        Traineereference.child("Trainee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot3) {

                for(DataSnapshot value: snapshot3.getChildren()){
                    Log.d("snapshot1", value.getValue().toString().trim());
                    Trainee.add(value.getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        for(int i=0; i< Trainee.size(); i++){
            int finalI = i;
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUsers.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Users user = snapshot.getValue(Users.class);
                        assert user != null;
                        if(Trainee.get(finalI).equals(firebaseUser.getUid())){
                            mUsers.add(user);
                        }

                        userAdapter = new UserAdapter(getContext(),mUsers,false);
                        recyclerView.setAdapter(userAdapter);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }




    }
    public ArrayList<String> getBundledusercode() {
        return Bundledusercode;
    }

    public void setBundledusercode(ArrayList<String> bundledusercode) {
        Bundledusercode = bundledusercode;
    }

    public ArrayList<String> getTempArr() {
        return tempArr;
    }

    public void setTempArr(ArrayList<String> tempArr) {
        this.tempArr = tempArr;
    }
}