package com.example.phitup.dacn.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phitup.dacn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        txt = findViewById(R.id.textviewMain);
//
//
//        Intent intent = getIntent();
//        String email = intent.getStringExtra("user");
//        if(email != null){
//            txt.setText(email);
//        }else{
//            Toast.makeText(this, "email null", Toast.LENGTH_SHORT).show();
//        }



    }
}
