package com.capstone.pakigsabotbusinessowner.SignUpRequirement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.capstone.pakigsabotbusinessowner.NavBar.BottomNavigation;
import com.capstone.pakigsabotbusinessowner.R;

public class PaySubscription extends AppCompatActivity {

    Button subscribeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_subscription);

        //References::
        refs();

        subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homePage();
            }
        });
    }

    private void refs(){
        subscribeBtn = findViewById(R.id.subscribeBtn);
    }

    //Home Fragment::
    private void homePage(){
        startActivity(new Intent(getApplicationContext(), BottomNavigation.class));
    }
}