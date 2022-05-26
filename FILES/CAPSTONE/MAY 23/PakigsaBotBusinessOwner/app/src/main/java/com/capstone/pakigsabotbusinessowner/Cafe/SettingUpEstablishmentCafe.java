package com.capstone.pakigsabotbusinessowner.Cafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.capstone.pakigsabotbusinessowner.Cafe.PromoAndDeals.PromoAndDealsCafe;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Restaurant.MenuRestaurant;
import com.capstone.pakigsabotbusinessowner.Restaurant.PromoAndDeals.PromoAndDealsRestaurant;

public class SettingUpEstablishmentCafe extends AppCompatActivity {
    Button menuItemsBtn, promoDealsBtn, rulesBtn, cancelPolBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_up_establishment_cafe);

        //References
        refs();

        menuItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), MenuCafe.class);
                startActivity(in);
            }
        });
        promoDealsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), PromoAndDealsCafe.class);
                startActivity(in);
            }
        });
    }

    private void refs() {
        menuItemsBtn = findViewById(R.id.setUpFoodItemBtnCafe);
        promoDealsBtn = findViewById(R.id.setUpPromoAndDealsBtnCafe);
        rulesBtn = findViewById(R.id.setUpRulesBtnCafe);
        cancelPolBtn = findViewById(R.id.setUpCancelPolicyBtnCafe);
    }
}