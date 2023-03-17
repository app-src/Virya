package com.scammer101.Virya.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.scammer101.Virya.databinding.ActivitySplashScreenBinding;

public class SplashScreen extends AppCompatActivity {

    private ActivitySplashScreenBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        startThread();

    }

    private void init()
    {
        auth = FirebaseAuth.getInstance();
    }

    private void startThread() {

        Thread thread = new Thread(){
            public void run(){

                try{
                    sleep(3000);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if(auth.getCurrentUser()!=null)
                    {
                        Intent intent = new Intent(SplashScreen.this, MainScreen.class);
//                        intent.putExtra("frag",3);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(SplashScreen.this, NumberScreen.class);
                        startActivity(intent);
                    }
                }
            }
        };
        thread.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        startThread();
    }
}