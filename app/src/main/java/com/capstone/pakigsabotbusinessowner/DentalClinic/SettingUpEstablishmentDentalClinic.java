package com.capstone.pakigsabotbusinessowner.DentalClinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.capstone.pakigsabotbusinessowner.DentalClinic.PromoAndDeals.PromoAndDealsDentalClinic;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.ServiceHours.ServiceHours;
import com.capstone.pakigsabotbusinessowner.Services.ServicesDentalClinic;

public class SettingUpEstablishmentDentalClinic extends AppCompatActivity {
    Button addTimeBtn, servicesBtn, promoAndDealsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_up_establishment_dental_clinic);

        //References
        servicesBtn = findViewById(R.id.setUpProcBtn);
        promoAndDealsBtn = findViewById(R.id.setUpPromoAndDealsBtn);


        servicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), ServicesDentalClinic.class);
                startActivity(in);
            }
        });
        promoAndDealsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), PromoAndDealsDentalClinic.class);
                startActivity(in);
            }
        });

    }
}