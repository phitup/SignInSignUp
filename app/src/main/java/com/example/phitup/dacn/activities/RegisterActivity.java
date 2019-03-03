package com.example.phitup.dacn.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.phitup.dacn.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Toolbar toolbarStart;
    EditText edtDisplay, edtEmail, edtPassword;
    Button btnRegister, btnAlready;
    FirebaseAuth mAuth;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        AnhXa();
        ActionToolbar();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterForEmail();
            }
        });
        btnAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }

    private void RegisterForEmail() {
        final String display = edtDisplay.getText().toString();
        final String email = edtEmail.getText().toString();
        final String password = edtPassword.getText().toString();

        final FirebaseUser user = mAuth.getCurrentUser();
        if(user == null && !display.isEmpty() && !email.isEmpty() && !password.isEmpty()){

            mProgress.setTitle("Registering User");
            mProgress.setMessage("Please Wait");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mProgress.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
                                intent.putExtra("display", display);
                                intent.putExtra("password", password);
                                startActivity(intent);
                            } else {
                                mProgress.hide();
                                Log.d("FirebaseAuth", "onComplete" + task.getException().getMessage());
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "Tại sao user lại có nhỉ? và có 1 ô bị rỗng", Toast.LENGTH_SHORT).show();
        }

    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarStart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarStart.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void AnhXa() {
        toolbarStart = findViewById(R.id.toolbarStart);
        btnRegister = findViewById(R.id.buttonRegister);
        btnAlready = findViewById(R.id.buttonRegisterAlreadyHaveAnAccount);
        edtDisplay = findViewById(R.id.edittextRegisterDisplayName);
        edtEmail = findViewById(R.id.edittextRegisterEmail);
        edtPassword = findViewById(R.id.edittextRegisterPassword);
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
    }
}
