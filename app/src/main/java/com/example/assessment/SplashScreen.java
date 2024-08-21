package com.example.assessment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        img = findViewById(R.id.logo);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        img.setAnimation(anim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent inext = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(inext);
                finish();
            }
        }, 4000);

    }
}
