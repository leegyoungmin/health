package com.Health.health.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Health.health.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class signup extends AppCompatActivity {
    private static final String TAG="RegisterActivity";
    EditText mEmailText,mPasswordText,mPasswordcText,mName;
    Button mregiser_btn;
    int account_type;
    private FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;
    String PhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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

                Log.e("email",email);
                Log.e("pwd",pwd);
                if(pwd.equals(pwdc)){
                    Log.d(TAG,"등록 버튼"+email+","+pwd);
                    if (email.isEmpty()==true || pwd.isEmpty()==true){
                        Toast.makeText(signup.this,"공백이 존재합니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final ProgressDialog mDialog = new ProgressDialog(signup.this);
                    mDialog.setTitle("가입 진행중입니다.");
                    mDialog.show();

                    //회원과 트레이너 판단 후 데이터 베이스 저장
                    firebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
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
                                PhoneNumber = getIntent().getStringExtra("PhoneNumber");
                                Log.e("PhoneNumber",PhoneNumber);
//                                String user_id=firebaseAuth.getCurrentUser().getUid();
                                Log.e("account_type", String.valueOf(account_type));
                                hashMap.put("PhoneNumber",PhoneNumber);
                                hashMap.put("AccountType", String.valueOf(account_type));
                                if (account_type==0){
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getReference("Users");
                                    reference.child(uid).setValue(hashMap);
                                }
                                else{
                                    FirebaseDatabase database =FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getReference("Trainer");
                                    reference.child(uid).setValue(hashMap);
                                }

                                Intent intent = new Intent(signup.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(signup.this,"회원가입에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                mDialog.dismiss();
                                Toast.makeText(signup.this,"이미 존재하는 아이디 입니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(signup.this,"비밀번호가 틀렸습니다. 다시 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
            }


        });

    }

    public boolean onSupportNavigatorUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
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