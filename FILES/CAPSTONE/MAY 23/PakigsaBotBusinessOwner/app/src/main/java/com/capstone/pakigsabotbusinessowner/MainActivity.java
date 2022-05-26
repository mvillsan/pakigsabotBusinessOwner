package com.capstone.pakigsabotbusinessowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.capstone.pakigsabotbusinessowner.SignUpRequirement.AgreementScreen;

public class MainActivity extends AppCompatActivity {

    Button signin, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refs();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signinScreen();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agreementScreen();
            }
        });
    }

    public void refs(){
        signin = findViewById(R.id.signInBtn);
        signup = findViewById(R.id.signUpBtn);
    }

    public void signinScreen(){
        Intent intent = new Intent(getApplicationContext(), SignIn.class);
        startActivity(intent);
    }

    public void agreementScreen(){
        Intent intent = new Intent(getApplicationContext(), AgreementScreen.class);
        startActivity(intent);
    }
}