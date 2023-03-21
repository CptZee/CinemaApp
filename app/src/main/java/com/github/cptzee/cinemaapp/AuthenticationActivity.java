package com.github.cptzee.cinemaapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        LogInFragment logInFragment = new LogInFragment();
        SignUpFragment signUpFragment = new SignUpFragment();

        int button_flag = getIntent().getIntExtra("Value", 0);
        if (button_flag == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.authentication_fragment_container, logInFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.authentication_fragment_container, signUpFragment).commit();
        }
    }
}