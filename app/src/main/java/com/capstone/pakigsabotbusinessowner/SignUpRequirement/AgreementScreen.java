package com.capstone.pakigsabotbusinessowner.SignUpRequirement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.MainActivity;
import com.capstone.pakigsabotbusinessowner.NavBar.BottomNavigation;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.SignUp;

public class AgreementScreen extends AppCompatActivity {

    Button agreeContinueBtn, declineBtn;
    ImageView backBtnAgreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_screen);

        refs();

        agreeContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeScreen();
            }
        });

        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                welcomeScreen();
            }
        });

        backBtnAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpScreen();
            }
        });
    }

    public void refs(){
        agreeContinueBtn = findViewById(R.id.agreeContinueBtn);
        declineBtn = findViewById(R.id.declineBtn);
        backBtnAgreement = findViewById(R.id.backBtnAgreement);
    }

    private void homeScreen(){
        Toast.makeText(AgreementScreen.this, R.string.signUp_success, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), BottomNavigation.class);
        startActivity(intent);
    }

    private void welcomeScreen(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void signUpScreen(){
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }
}