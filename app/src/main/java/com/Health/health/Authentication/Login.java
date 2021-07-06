package com.Health.health.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

public class Login extends AppCompatActivity {
    Button signin,signup,Centersignup;

    EditText mEmail,mPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signin = (Button)findViewById(R.id.signin);
        signup = (Button) findViewById(R.id.signup);
        Centersignup = (Button)findViewById(R.id.center_signup);
        mEmail = (EditText)findViewById(R.id.email);
        mPassword = (EditText)findViewById(R.id.password);
        firebaseAuth = FirebaseAuth.getInstance();

        //회원가입 클릭 시 핸드폰 인증 페이지 이동
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, auth.class);
                startActivity(intent);
            }
        });
        //로그인 버튼 클릭 시 비밀번호를 통한 로그인
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String pwd = mPassword.getText().toString().trim();
                if (email.isEmpty() || pwd.isEmpty()){
                    Toast.makeText(Login.this,"이메일 또는 비밀번호가 공백입니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(email,pwd)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Log.d("email",firebaseAuth.getCurrentUser().getEmail());
                            if (email.equals(firebaseAuth.getCurrentUser().getEmail().trim())){
                                Toast.makeText(Login.this,"비밀번호가 틀립니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else{
                                Toast.makeText(Login.this,"이메일 또는 비밀번호가 틀립니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                });
           }
        });
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