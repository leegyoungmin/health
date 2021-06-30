package com.Health.health;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, auth.class);
                startActivity(intent);
            }
        });

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
                            Intent intent = new Intent(Login.this,MainActivity.class);
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
}