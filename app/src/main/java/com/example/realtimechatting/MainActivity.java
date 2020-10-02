package com.example.realtimechatting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView logoIm, welcomeIm;
    Button startedBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        logoIm = findViewById(R.id.logoIm);
        welcomeIm = findViewById(R.id.welcomeIm);
        startedBtn = findViewById(R.id.startedBtn);
        startedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                startedBtn.startAnimation(animation);
            }
        });


        Animation animationTop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fromtop);
        logoIm.startAnimation(animationTop);
        //welcomeTv.startAnimation(animation);
        Animation animationBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.frombottom);
        startedBtn.startAnimation(animationBottom);

        Animation animationWelcome = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.lefttoright);
        welcomeIm.startAnimation(animationWelcome);


    }
}
