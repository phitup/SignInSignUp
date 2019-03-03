package com.example.phitup.dacn.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.phitup.dacn.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignInWithPhoneActivity extends AppCompatActivity {

    EditText edtNumberPhone, edtCode;
    Button btnSignInPhone, btnVerifyCode;
    LinearLayout layoutSignIn, layoutVerify;

    FirebaseAuth mAuth;
    Toolbar toolbar;

    String codeSent;
    ProgressDialog mProgress;
//    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_with_phone);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        AnhXa();
        ActionBar();
        mAuth = FirebaseAuth.getInstance();



        btnSignInPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode();
                layoutSignIn.setVisibility(View.INVISIBLE);
                layoutVerify.setVisibility(View.VISIBLE);

            }
        });

        btnVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setMessage("Please Wait");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();
                VerifySignInCode();
            }
        });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;

        }
    };

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void VerifySignInCode() {
        String Code = edtCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, Code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mProgress.dismiss();
                            startActivity(new Intent(SignInWithPhoneActivity.this, MainActivity.class));
                        }else{
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                mProgress.hide();
                                Toast.makeText(SignInWithPhoneActivity.this, "Incorrect Verification Code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void sendVerificationCode() {

        String phone = edtNumberPhone.getText().toString();

        if(phone.isEmpty()){
            edtNumberPhone.setError("Phone number is required");
            edtNumberPhone.requestFocus();
            return;
        }
        if(phone.length() < 10){
            edtNumberPhone.setError("Please enter a valid phone");
            edtNumberPhone.requestFocus();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60 , TimeUnit.SECONDS, this, mCallback);
    }

    private void AnhXa() {
        edtNumberPhone = findViewById(R.id.edittextNumberPhone);
        btnSignInPhone = findViewById(R.id.buttonSignInWithPhone);
        mProgress = new ProgressDialog(this);
        toolbar = findViewById(R.id.toolbarSignInWithPHone);
        layoutSignIn = findViewById(R.id.LayoutSignInWithPhone);
        layoutVerify = findViewById(R.id.LayoutVerifyWithPhone);
        edtCode = findViewById(R.id.edittextVerifyCode);
        btnVerifyCode = findViewById(R.id.buttonVerifyWithPhone);
        layoutVerify.setVisibility(View.INVISIBLE);
    }
}
