package com.capstone.pakigsabotbusinessowner.SignUpRequirement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.capstone.pakigsabotbusinessowner.MainActivity;
import com.capstone.pakigsabotbusinessowner.R;

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
                paySub();
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
                welcomeScreen();
            }
        });
    }

    public void refs(){
        agreeContinueBtn = findViewById(R.id.agreeContinueBtn);
        declineBtn = findViewById(R.id.declineBtn);
        backBtnAgreement = findViewById(R.id.backBtn);
    }

    private void welcomeScreen(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void paySub(){
        Intent intent = new Intent(getApplicationContext(), PaySubscription.class);
        startActivity(intent);
    }
}