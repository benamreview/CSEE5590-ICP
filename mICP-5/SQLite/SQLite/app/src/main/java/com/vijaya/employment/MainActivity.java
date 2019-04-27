package com.vijaya.employment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.vijaya.employment.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.firstActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, com.vijaya.employment.EmployerActivity.class));
            }
        });

        binding.secondActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, com.vijaya.employment.EmployeeActivity.class));
            }
        });
        animate();
    }
    public void animate(){
        int duration = 2500;
        float left = -300f;
        float right = 300f;
        ObjectAnimator animation = ObjectAnimator.ofFloat(binding.firstActivityButton, "translationX", left, 0f);
        animation.setDuration(duration);
        animation.start();
        animation = ObjectAnimator.ofFloat(binding.secondActivityButton, "translationX", right, 0f);
        animation.setDuration(duration);
        animation.start();
    }
    public void onResume(){
        super.onResume();
        animate();

    }
}