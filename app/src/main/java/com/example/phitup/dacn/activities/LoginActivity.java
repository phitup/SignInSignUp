package com.example.phitup.dacn.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phitup.dacn.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Toolbar toolbarLogin;
    TextView txtLogin;
    EditText edtEmail, edtPassword;
    Button btnLogin;
    private ProgressDialog mProgress;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        AnhXa();
        ActionBar();
        LoginEvent();

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void LoginEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                if(!email.isEmpty() && !password.isEmpty()){

                    mProgress.setTitle("Login User");
                    mProgress.setMessage("Please Wait");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        mProgress.dismiss();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    } else {
                                        mProgress.hide();
                                        Toast.makeText(LoginActivity.this, "Email Or Password incorrectly", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    if(email.isEmpty()) {
                        edtEmail.setError("Email không được bỏ trống");
                    }else edtPassword.setError("Password không được bỏ trống");
                }
            }
        });
    }

    private void ActionBar() {
        setSupportActionBar(toolbarLogin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarLogin.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void AnhXa() {
        toolbarLogin = findViewById(R.id.toolbarLogin);
        txtLogin = findViewById(R.id.textviewLogin);
        edtEmail = findViewById(R.id.edittextLoginEmail);
        edtPassword = findViewById(R.id.edittextLoginPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
    }
}
