package com.capstone.pakigsabotbusinessowner.SignUpRequirement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.capstone.pakigsabotbusinessowner.MainActivity;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.SignUp;

public class PaySubscription extends AppCompatActivity {

    Button subscribeBtn,notnowBtn;
    ImageView backBtnSU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_subscription);

        //References::
        refs();

        subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        notnowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainScreen();
            }
        });

        backBtnSU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainScreen();
            }
        });
    }

    private void refs(){
        subscribeBtn = findViewById(R.id.subscribeBtn);
        notnowBtn = findViewById(R.id.notnowBtn);
        backBtnSU = findViewById(R.id.backBtnSU);
    }

    private void signUp(){
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }

    //Go back to Main Page::
    private void mainScreen(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

}