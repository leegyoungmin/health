package com.Health.health.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Health.health.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.Health.health.Authentication.MainActivity;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText userET,passET,emailET;
    Button registerBtn;

    //firebase

    FirebaseAuth auth;

    //데이터베이스

    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        //초기화

        userET = findViewById(R.id.userEditText);
        passET = findViewById(R.id.passEditText);
        emailET = findViewById(R.id.emailEditText);
        registerBtn = findViewById(R.id.buttonRegister);


        // Firebase Auth
        //auth = FirebaseAuth.getInstance();

        auth = FirebaseAuth.getInstance();

        // 등록 버튼 리스너 추가

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username_text = userET.getText().toString();
                String email_text = emailET.getText().toString();
                String pass_text = passET.getText().toString();

                if(TextUtils.isEmpty(username_text)|| TextUtils.isEmpty(email_text)|| TextUtils.isEmpty(pass_text)){  // 각 editText 가 빈게 있는지 확인
                  Toast.makeText(RegisterActivity.this,"빈칸이 없게 채워주세요.",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"회원가입이 완료 되었습니다.",Toast.LENGTH_LONG).show();
                    RegisterNow(username_text,email_text,pass_text);
                }
                
            }
        });

    }

    private void RegisterNow(final String username, String email, String password){

        auth.createUserWithEmailAndPassword(email, password)  // 신규 사용자 생성 (말고도 google 이나 facebook 을 통한 로그인도 가능하다.
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();  //현재 사용자는 가져옴 (없으면 null 반환)
                            String userid = firebaseUser.getUid();  // 고유한 사용자 아이디

                            myRef = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);  //.child 는 데이터가 있을 위치의 이름을 정해주는 것

                            //HashMaps
                            //key value
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",username);
                            hashMap.put("imageURL","default");
                            hashMap.put("status","offline");

                            // 로그인이 끝나면 메인 액티비티 엶

                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);

                                        finish();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(RegisterActivity.this, "유효하지 않습니다.", Toast.LENGTH_LONG).show();
                        }

                    }
                });



    }
}