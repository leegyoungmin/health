package com.Health.health;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class membersignup extends AppCompatActivity {
    private static final String TAG="RegisterActivity";
    EditText mEmailText,mPasswordText,mPasswordcText,mName;
    Button mregiser_btn;
    int account_type;
    private FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membersignup);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //user=firebaseAuth.getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        //firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        mEmailText =(EditText) findViewById(R.id.email);
        mPasswordText = (EditText)findViewById(R.id.password);
        mPasswordcText = (EditText)findViewById(R.id.password_c);
        mName = (EditText)findViewById(R.id.name);
        mregiser_btn = (Button)findViewById(R.id.register);

        mregiser_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=mEmailText.getText().toString().trim();
                String pwd=mPasswordText.getText().toString().trim();
                String pwdc=mPasswordcText.getText().toString().trim();

                if(pwd.equals(pwdc)){
                    Log.d(TAG,"등록 버튼"+email+","+pwd);
                    final ProgressDialog mDialog = new ProgressDialog(membersignup.this);
                    mDialog.setTitle("가입 진행중입니다.");
                    mDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(membersignup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                mDialog.dismiss();

                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String email=user.getEmail();
                                String uid=user.getUid();
                                String name = mName.getText().toString().trim();

                                HashMap<Object,String> hashMap = new HashMap<>();

                                hashMap.put("uid",uid);
                                hashMap.put("email",email);
                                hashMap.put("name",name);
                                account_type=getIntent().getIntExtra("account_type",0);
//                                String user_id=firebaseAuth.getCurrentUser().getUid();
                                hashMap.put("account_type", String.valueOf(account_type));
                                if (account_type==0){
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getReference("Users");
                                    reference.child(uid).setValue(hashMap);
                                }
                                else{
                                    FirebaseDatabase database =FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getReference("trainer");
                                    reference.child(uid).setValue(hashMap);
                                }

                                Intent intent = new Intent(membersignup.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(membersignup.this,"회원가입에 성공하였습니다.",Toast.LENGTH_SHORT).show();

                            }
                            else{
                                mDialog.dismiss();
                                Toast.makeText(membersignup.this,"이미 존재하는 아이디 입니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(membersignup.this,"비밀번호가 틀렸습니다. 다시 입력해주세요.",Toast.LENGTH_SHORT).show();

                    return;
                }
            }
        });
    }
    public boolean onSupportNavigatorUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}