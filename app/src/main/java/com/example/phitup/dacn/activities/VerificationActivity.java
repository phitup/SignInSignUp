package com.example.phitup.dacn.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.phitup.dacn.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class VerificationActivity extends AppCompatActivity {

    Button btnVerified;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference mDatabase;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        btnVerified = findViewById(R.id.buttonVerification);

        user = mAuth.getCurrentUser();
        if(user != null){
            user.sendEmailVerification();
        }else{
            Toast.makeText(this, "Error Verification Screen", Toast.LENGTH_SHORT).show();
        }

        btnVerified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadVerification();
            }
        });

    }

    private void reloadVerification() {
            user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        mProgress.setTitle("Registering User");
                        mProgress.setMessage("Please Wait");
                        mProgress.setCanceledOnTouchOutside(false);
                        mProgress.show();

                        if (user.isEmailVerified()) {
                            Intent intent = getIntent();
                            String display = intent.getStringExtra("display");
                            String password = intent.getStringExtra("password");
                            String uid = user.getUid();
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name", display);
                            userMap.put("password", password);
                            userMap.put("image", "default");

                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mProgress.dismiss();
                                    Intent i = new Intent(VerificationActivity.this, MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    finish();
                                    Toast.makeText(VerificationActivity.this, "Welcome to my App", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            mProgress.hide();
                            Toast.makeText(VerificationActivity.this, "Bạn Chưa Xác thực", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(VerificationActivity.this, "Check Your Email Verified Now", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
}
