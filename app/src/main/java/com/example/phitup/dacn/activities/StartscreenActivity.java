package com.example.phitup.dacn.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.phitup.dacn.R;

public class StartscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startscreen);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Animation animation_slide_left = AnimationUtils.loadAnimation(StartscreenActivity.this, R.anim.slide_left);
                    Animation animation_slide_right = AnimationUtils.loadAnimation(StartscreenActivity.this, R.anim.slide_right);
                    findViewById(R.id.textviewStartScreenWelcome).startAnimation(animation_slide_left);
                    findViewById(R.id.textviewStartScreenDescription).startAnimation(animation_slide_right);
                    Thread.sleep(5000);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(StartscreenActivity.this, StartActivity.class));
                }
            }
        });
        thread.start();
    }
}
