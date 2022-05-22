package com.capstone.pakigsabotbusinessowner.PremiumApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.capstone.pakigsabotbusinessowner.Profile.Profile;
import com.capstone.pakigsabotbusinessowner.R;

public class GoPremiumWS extends AppCompatActivity {

    Button getPremiumBtnGP, cancelPremiumBtnGP;
    ImageView backBtnGP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_premium_ws);

        //References:
        refs();

        getPremiumBtnGP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                upgradePremium();
            }
        });

        cancelPremiumBtnGP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelPremium();
            }
        });

        backBtnGP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToProfile();
            }
        });
    }

    public void refs(){

        getPremiumBtnGP = findViewById(R.id.getPremiumBtnGP);
        cancelPremiumBtnGP = findViewById(R.id.cancelBtnGP);
        backBtnGP = findViewById(R.id.backBtnProfile);
    }

    public void upgradePremium(){
        Intent intent = new Intent(getApplicationContext(), UpgradeScreen.class);
        startActivity(intent);
    }

    public void cancelPremium(){
        Intent intent = new Intent(getApplicationContext(), Profile.class);
        startActivity(intent);
    }

    public void backToProfile(){
        Intent intent = new Intent(getApplicationContext(), Profile.class);
        startActivity(intent);
    }
}