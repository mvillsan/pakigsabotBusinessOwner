package com.capstone.pakigsabotbusinessowner.Resort;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.capstone.pakigsabotbusinessowner.Resort.PromoAndDealsResort;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Services.ServicesDentalClinic;

public class SettingUpEstablishmentResort extends AppCompatActivity {
    Button promoAndDealsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_up_establishment_resort);


        //servicesBtn.findViewById(R.id.setUpProcBtn);
       promoAndDealsBtn = findViewById(R.id.setUpPromoAndDealsBtn);

       /* servicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), ServicesResort.class);
                startActivity(in);
            }
        });*/
        promoAndDealsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), PromoAndDealsResort.class);
                startActivity(in);
            }
        });

    }


}