package com.Health.health;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.Inet4Address;
import java.util.concurrent.TimeUnit;

public class auth extends AppCompatActivity {
    EditText code,phone,auth_code;
    Button next,send,auth_c;
    String code_value,mPhone,replace_Phone,mAuth_code,PhoneNumber;

    private static final String TAG = "PhoneAuthActivity";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        next=(Button)findViewById(R.id.register);
        mAuth = FirebaseAuth.getInstance();
        send = (Button)findViewById(R.id.send);
        auth_c = (Button)findViewById(R.id.auth_check);
        phone=(EditText)findViewById(R.id.phone);
        auth_code=(EditText)findViewById(R.id.auth_code);
        code=(EditText)findViewById(R.id.center_code);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                auth_c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("auth_code",auth_code.getText().toString());
                        if (auth_code.getText().toString().equals(credential.getSmsCode())){
                            Log.e(TAG, "onVerificationCompleted:" + credential.getSmsCode());
                            Log.e(TAG,"auth_code"+auth_code.getText().toString());
                            phone.setClickable(false);
                            phone.setFocusable(false);
                            phone.setTextColor(Color.GRAY);
                            phone.setBackgroundColor(Color.GRAY);
                            phone.isEnabled();
                            send.setClickable(false);
                            send.setTextColor(Color.GRAY);
                            send.setBackgroundColor(Color.GRAY);
                            send.isEnabled();
                            auth_code.setClickable(false);
                            auth_code.isEnabled();
                            auth_code.setFocusable(false);
                            auth_code.setBackgroundColor(Color.GRAY);
                            auth_c.setBackgroundColor(Color.GRAY);
                            signInWithPhoneAuthCredential(credential);

                        }
                        else{
                            Toast.makeText(auth.this,"잘못된 입력입니다.",Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onVerificationCompleted:" + credential.getSmsCode());
                            Log.e(TAG,"auth_code"+auth_code.getText().toString());
                            auth_code.setTextColor(Color.RED);
                            return;
                        }

                    }
                });

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.e(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                // Save verification ID and resending token so we can use them later
                Toast.makeText(auth.this,"인증번호가 전송되었습니다. 1분안에 완료해주세요.",Toast.LENGTH_SHORT).show();
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mPhone=null;
                send.setClickable(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPhone = phone.getText().toString();
                Log.e("phone",mPhone);

            }

            @Override
            public void afterTextChanged(Editable s) {
                send.setClickable(true);
            }
        });



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone.getText().toString().length()==0||phone.getText().toString().length()<11){
                    Toast.makeText(auth.this,"잘못된 전화번호 입니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    replace_Phone="+82 "+mPhone.substring(1,11);
                    Log.e("replace_phone",replace_Phone);
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth.getInstance()).setPhoneNumber(replace_Phone)
                            .setTimeout(60L,TimeUnit.SECONDS)
                            .setActivity(auth.this)
                            .setCallbacks(mCallbacks)
                            .build();

                    PhoneAuthProvider.verifyPhoneNumber(options);
                    Log.e(TAG, String.valueOf(mCallbacks));
                }

            }
        });

        auth_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAuth_code = auth_code.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                String PhoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                //Log.e("auth_phonenumber",PhoneNumber);

                if (phone.getText().toString().length()==0){
                    Toast.makeText(auth.this,"핸드폰 인증을 완료해주세요.",Toast.LENGTH_SHORT).show();

                }
                else{
                    if(code.getText().toString().isEmpty()||code.getText().toString().length()==0){

                        Toast.makeText(auth.this,"회원으로 가입을 시작합니다.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(auth.this, signup.class);
                        intent.putExtra("PhoneNumber",phone.getText().toString());
                        intent.putExtra("account_type",0);
                        startActivity(intent);
                    }
                    else {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference().child("fitness");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot value : snapshot.getChildren()){

                                    if(code_value.equals(value.getValue().toString())){
                                        Toast.makeText(auth.this,"트레이너로 가입을 시작합니다.",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(auth.this, signup.class);
                                        intent.putExtra("PhoneNumber",phone.getText().toString());
                                        intent.putExtra("account_type",1);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(auth.this,"가입되지 않은 센터입니다.",Toast.LENGTH_SHORT).show();
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
                }


            }
        });


    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            Log.e("Users", String.valueOf(user));
                            Log.e("phoneNumber",user.getPhoneNumber());

                            Toast.makeText(auth.this,"인증 완료되었습니다.",Toast.LENGTH_SHORT).show();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                auth_code.setTextColor(Color.RED);
                                Toast.makeText(auth.this,"잘못된 인증 번호 입니다. 다시 입력해주세요.",Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }
}
